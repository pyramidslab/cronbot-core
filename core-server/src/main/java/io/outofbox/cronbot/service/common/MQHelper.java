package io.outofbox.cronbot.service.common;

import io.outofbox.cronbot.error.MQException;
import io.outofbox.cronbot.model.event.Event;
import io.outofbox.cronbot.model.event.EventType;
import io.outofbox.cronbot.model.event.JobEvent;
import io.outofbox.cronbot.model.job.Job;
import io.outofbox.cronbot.service.mq.IMessageQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class MQHelper {

    private IMessageQueueService mqService;

    @Autowired
    public MQHelper(IMessageQueueService mqService){
        this.mqService = mqService;
    }

    public void sendCreateJobEvent(Job job) throws MQException {
        JobEvent jobEvent = new JobEvent();
        jobEvent.setEvent(EventType.CREATE);
        jobEvent.setObject(job);
        sendToMQ(jobEvent);
    }

    public void sendUpdateJobEvent(Job job) throws MQException {
        JobEvent jobEvent = new JobEvent();
        jobEvent.setEvent(EventType.UPDATE);
        jobEvent.setObject(job);
        sendToMQ(jobEvent);
    }

    public void sendDeleteJobEvent(Job job) throws MQException {
        JobEvent jobEvent = new JobEvent();
        jobEvent.setEvent(EventType.DELETE);
        jobEvent.setObject(job);
        sendToMQ(jobEvent);
    }

    private void sendToMQ(JobEvent jobEvent) {
        Executors.newCachedThreadPool().submit(() -> {
            try {
                mqService.send(jobEvent);
            } catch (MQException e) {
                log.error("Error in sending message to MQ", e);
            }
            return null;
        });

    }
}
