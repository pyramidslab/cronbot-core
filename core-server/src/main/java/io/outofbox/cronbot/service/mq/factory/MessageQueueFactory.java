package io.outofbox.cronbot.service.mq.factory;

import io.outofbox.cronbot.service.mq.IMQAdminConfiguration;
import io.outofbox.cronbot.service.mq.IMessageQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
public class MessageQueueFactory {

    private final String CONFIG_POSTFIX = "Config";
    private String mqImpl;
    private ApplicationContext context;

    public MessageQueueFactory(@Value("${mq.implementation:rabbitMQ}") String mqImpl,
                               @Autowired ApplicationContext context) {
        this.mqImpl = mqImpl;
        this.context = context;
    }

    @Bean
    @Primary
    public IMessageQueueService getService() {
        try {
            return (IMessageQueueService) context.getBean(mqImpl);
        } catch (BeansException ex) {
            log.error("Error initializing bean {}", mqImpl);
        }
        return null;
    }

    @Bean
    @Primary
    public IMQAdminConfiguration getAdminService() {
        try {
            return (IMQAdminConfiguration) context.getBean(mqImpl + CONFIG_POSTFIX);
        } catch (BeansException ex) {
            log.error("Error initializing bean {}", mqImpl + CONFIG_POSTFIX);
        }
        return null;
    }
}
