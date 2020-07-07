package io.outofbox.cronbot.service.job;

import io.outofbox.cronbot.model.job.Job;
import io.outofbox.cronbot.model.job.JobDetails;
import io.outofbox.cronbot.service.common.IGenericCRUDService;

public interface IJobService extends IGenericCRUDService<Job, JobDetails> {
}
