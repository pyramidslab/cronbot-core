package io.outofbox.cronbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.outofbox.cronbot.error.MQException;
import io.outofbox.cronbot.model.event.EventType;
import io.outofbox.cronbot.model.event.JobEvent;
import io.outofbox.cronbot.model.job.Job;
import io.outofbox.cronbot.service.mq.IMessageQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * The entry point of the application
 *
 * @author ahelmy
 */
@SpringBootApplication
public class CronbotApplication{

    public static void main(String[] args) {
        SpringApplication.run(CronbotApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

}
