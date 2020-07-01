package io.outofbox.cronbot.service.job;

import io.outofbox.cronbot.model.Job;

public interface IJobService {
    Job createJob(Job job);
}
