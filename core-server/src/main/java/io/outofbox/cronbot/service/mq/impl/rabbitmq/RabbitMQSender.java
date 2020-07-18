package io.outofbox.cronbot.service.mq.impl.rabbitmq;

import io.outofbox.cronbot.error.MQException;
import io.outofbox.cronbot.model.plugin.PluginConfiguration;
import io.outofbox.cronbot.service.mq.IMessageQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service("rabbitMQ")
public class RabbitMQSender  implements IMessageQueueService {

    private AmqpTemplate rabbitTemplate;
    private Queue queue;

    @Autowired
    public RabbitMQSender(AmqpTemplate rabbitTemplate, Queue queue){
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    public <T> void send(T obj) throws MQException {
        try {
            rabbitTemplate.convertAndSend(queue.getName(), obj);
            log.info("Message {} sent to queue {}", obj, queue.getName());
        }catch (AmqpException ex){
            log.error("Failed to send message to queue {}",queue.getName());
            throw new MQException(ex);
        }

    }

}
