package io.outofbox.cronbot.service.mq.impl.rabbitmq;

import io.outofbox.cronbot.model.MQConfiguration;
import io.outofbox.cronbot.service.mq.IMQAdminConfiguration;
import io.outofbox.cronbot.utils.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Configuration("rabbitMQConfig")
public class RabbitMQConfig implements IMQAdminConfiguration {

    private final int RAND_PASS_LENGTH = 20;
    private final int RAND_USERNAME_LENGTH = 10;
    private final String IN_QUEUE = "in_queue";
    private final String OUT_QUEUE = "out_queue";

    private String queueName;
    private String host;
    private String username;
    private String password;
    private String virtualHost;
    private int port;
    private int apiPort;
    private RestTemplate restTemplate;
    private String apiURL;
    private RandomUtils randomUtils;

    public RabbitMQConfig(@Value("${mq.queuename}") String queueName,
                          @Value("${mq.host}") String host,
                          @Value("${mq.username:guest}") String username,
                          @Value("${mq.password:guest}") String password,
                          @Value("${mq.virtualhost:/}") String virtualHost,
                          @Value("${mq.port:5672}") int port,
                          @Value("${mq.api-port:15672}") int apiPort,
                          RestTemplate restTemplate,
                          RandomUtils randomUtils) {
        this.queueName = queueName;
        this.host = host;
        this.virtualHost = virtualHost;
        this.username = username;
        this.password = password;
        this.port = port;
        this.apiPort = apiPort;
        this.restTemplate = restTemplate;
        this.randomUtils = randomUtils;
        this.apiURL = buildAPIURL();

    }

    @Bean
    public Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(queueName);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    private String buildAPIURL() {
        String url = "http://" + this.host + ":" + this.apiPort + "/api/";
        log.debug("MQ API URL = {}", url);
        return url;
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String userPass64 = Base64.getEncoder().encodeToString(new String(this.username + ":" + this.password).getBytes());
        httpHeaders.add("Authorization", "Basic " + userPass64);
        log.trace("URL Headers = {}", httpHeaders.toSingleValueMap());
        return httpHeaders;
    }

    /**
     * Create Virtual Host in MQ
     *
     * @param vHost VHost name
     * @return True if vHost created, otherwise return false
     */
    private boolean createVHost(String vHost) {
        // Add VHost
        boolean isCreated = false;
        log.info("Create VHost = {} ...", vHost);
        try {
            HttpEntity<?> request = new HttpEntity<>("", buildHeaders());

            ResponseEntity<String> response = restTemplate.exchange(apiURL + "vhosts/" + vHost, HttpMethod.PUT, request, String.class);

            isCreated = response.getStatusCode().equals(HttpStatus.CREATED);

            if (isCreated) {
                log.info("VHost = {} created.", vHost);
            } else {
                log.error("Failed to create VHost = {}, Reason = {}", vHost, response.getBody());
            }
        } catch (RestClientException ex) {
            log.error("Failed to create VHost = " + vHost, ex);
        }
        return isCreated;
    }

    /**
     * Create user with username and password
     *
     * @param username Username
     * @param password Password
     * @return True if created, otherwise return False
     */
    private boolean createUser(String username, String password) {
        // Add Username
        boolean isCreated = false;
        log.info("Create User = {} with Password = **** ...", username);
        try {
            HttpEntity<?> request = new HttpEntity<>("", buildHeaders());

            String addUserReqBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\",\"tags\":\"\"}";

            request = new HttpEntity<String>(addUserReqBody, buildHeaders());

            ResponseEntity<String> response = restTemplate.exchange(apiURL + "users/" + username, HttpMethod.PUT, request, String.class);

            isCreated = response.getStatusCode().equals(HttpStatus.CREATED);

            if (isCreated) {
                log.info("Username = {} created.", username);
            } else {
                log.error("Failed to create Username = {}, Reason = {}", username, response.getBody());
            }
        } catch (RestClientException ex) {
            log.error("Failed to create Username = " + username, ex);
        }
        return isCreated;
    }

    /**
     * Assign user to virtual host
     *
     * @param username Username
     * @param vHost    VHost name
     * @return True if assigned, otherwise return False
     */
    private boolean assignUserToVHost(String username, String vHost) {
        boolean isCreated = false;
        try {
            // Assign user to vHost
            String assignReq = "{\"username\":\"" + username + "\",\"vhost\":\"" + vHost + "\",\"configure\":\".*\",\"write\":\".*\",\"read\":\".*\"}";

            HttpEntity<?> request = new HttpEntity<String>(assignReq, buildHeaders());

            ResponseEntity<String> response = restTemplate.exchange(apiURL + "permissions/" + URLEncoder.encode(vHost) + "/" + username, HttpMethod.PUT, request, String.class);

            isCreated = response.getStatusCode().equals(HttpStatus.CREATED);
            if (isCreated) {
                log.info("Username = {} assigned to VHost = {}", username, vHost);
            } else {
                log.error("Failed to assign Username = {}, to VHost = {}, Reason = {}", username, vHost, response.getBody());
            }
        } catch (RestClientException ex) {
            log.error("Failed to assign  Username = " + username + " to VHost = " + vHost, ex);
        }
        return isCreated;
    }

    /**
     * Create queue under vhost
     *
     * @param vHost     Virtual host
     * @param queueName Queue name
     * @return True if created, otherwise return False
     */
    private boolean createQueue(String vHost, String queueName) {
        boolean isCreated = false;
        try {
            // Assign user to vHost
            String createQueueRequest = "{\"vhost\":\"" + vHost + "\",\"name\":\"" + queueName + "\",\"durable\":\"true\",\"auto_delete\":\"false\",\"arguments\":{\"x-queue-type\":\"classic\"}}";

            HttpEntity<?> request = new HttpEntity<String>(createQueueRequest, buildHeaders());

            ResponseEntity<String> response = restTemplate.exchange(apiURL + "queues/" + URLEncoder.encode(vHost) + "/" + queueName, HttpMethod.PUT, request, String.class);

            isCreated = response.getStatusCode().equals(HttpStatus.CREATED);
            if (isCreated) {
                log.info("Queue = {} created under VHost = {}", queueName, vHost);
            } else {
                log.error("Failed to create Queue = {} to VHost = {}, Reason = {}", queueName, vHost, response.getBody());
            }
        } catch (RestClientException ex) {
            log.error("Failed to create queue = " + queueName + " to VHost = " + vHost, ex);
        }
        return isCreated;
    }

    @Override
    public Optional<MQConfiguration> createMQConfig(String vHost) {
        MQConfiguration mqConfiguration = new MQConfiguration();
        mqConfiguration.setHost(this.host);
        mqConfiguration.setPort(this.port);

        HttpEntity<?> request = new HttpEntity<>("", buildHeaders());
        // Add VHost
        boolean isVHostCreated = createVHost(vHost);
        if (isVHostCreated) {
            // Add User for executor
            String randPass = randomUtils.random(20);
            String randUsername = randomUtils.random(10);
            // Add User
            boolean isUserCreated = createUser(randUsername, randPass);
            if (isUserCreated) {
                // Assign user to vhost
                boolean isUserAssignedToVHost = assignUserToVHost(randUsername, vHost);
                if (isUserAssignedToVHost) {
                    // Create in queue
                    boolean isInQueueCreated = createQueue(vHost, IN_QUEUE);
                    if (isInQueueCreated) {
                        boolean isOutQueueCreated = createQueue(vHost, OUT_QUEUE);
                        if (isOutQueueCreated) {
                            mqConfiguration.setInQueue(IN_QUEUE);
                            mqConfiguration.setOutQueue(OUT_QUEUE);
                            mqConfiguration.setVirtualHost(vHost);
                            mqConfiguration.setUsername(randUsername);
                            mqConfiguration.setPassword(randPass);
                            return Optional.of(mqConfiguration);
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

}
