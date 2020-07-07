package io.outofbox.cronbot.service.mq.impl.rabbitmq;

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

@Slf4j
@Configuration
public class RabbitMQConfig {

    private String queueName;
    private  String host;
    private String username;
    private String password;
    private String virtualHost;
    private int port;

    public RabbitMQConfig(@Value("${mq.queuename}") String queueName,
                          @Value("${mq.host}") String host,
                          @Value("${mq.username:guest}") String username,
                          @Value("${mq.password:guest}") String password,
                          @Value("${mq.virtualhost:/}") String virtualHost,
                          @Value("${mq.port:5672}") int port){
        this.queueName = queueName;
        this.host = host;
        this.virtualHost =virtualHost;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    @Bean
    public Queue queue(){
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
    public MessageConverter messageConverter(){
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


}
