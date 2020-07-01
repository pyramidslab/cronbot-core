package io.outofbox.cronbot.service.mq;

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

    private String mqImpl;
    private ApplicationContext context;

    public MessageQueueFactory(@Value("${mq.implementation:rabbitMQ}") String mqImpl,
                               @Autowired ApplicationContext context){
        this.mqImpl = mqImpl;
        this.context = context;
    }

    @Bean
    @Primary
    public IMessageQueueService getService(){
       try {
           return  (IMessageQueueService) context.getBean(mqImpl);
       }catch (BeansException ex){
           log.error("Error initializing bean {}", mqImpl);
       }
       return null;
    }
}
