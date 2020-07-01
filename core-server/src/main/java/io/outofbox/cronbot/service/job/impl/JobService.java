package io.outofbox.cronbot.service.job.impl;

import io.outofbox.cronbot.error.MQException;
import io.outofbox.cronbot.model.Job;
import io.outofbox.cronbot.model.event.EventType;
import io.outofbox.cronbot.model.event.JobEvent;
import io.outofbox.cronbot.service.job.IJobService;
import io.outofbox.cronbot.service.mq.IMessageQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService implements IJobService {

    @Autowired
    IMessageQueueService mqService;

    @Override
    public Job createJob(Job job) {
        try {
            JobEvent jobEvent =JobEvent.builder().type(EventType.CREATE).object(job).build();
            mqService.send(job);
        } catch (MQException e) {
            e.printStackTrace();
        }
        return job;
    }
}
