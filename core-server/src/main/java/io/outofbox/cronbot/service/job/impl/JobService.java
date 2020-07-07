package io.outofbox.cronbot.service.job.impl;

import com.mongodb.MongoException;
import io.outofbox.cronbot.error.ConflictExcpetion;
import io.outofbox.cronbot.error.MQException;
import io.outofbox.cronbot.error.NotFoundException;
import io.outofbox.cronbot.error.OperationFailureException;
import io.outofbox.cronbot.model.MonitoringObj;
import io.outofbox.cronbot.model.job.Job;
import io.outofbox.cronbot.model.job.JobDetails;
import io.outofbox.cronbot.repository.job.JobRepository;
import io.outofbox.cronbot.service.common.MonitoringObjHelper;
import io.outofbox.cronbot.service.job.IJobService;
import io.outofbox.cronbot.service.common.MQHelper;
import io.outofbox.cronbot.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService implements IJobService {

    private JobRepository jobRepository;
    private ObjectUtils objectUtils;
    private MonitoringObjHelper monitoringObjHelper;
    private MQHelper mqHelper;

    @Autowired
    public JobService(JobRepository jobRepository, ObjectUtils objectUtils,
                      MonitoringObjHelper monitoringObjHelper, MQHelper mqHelper){
        this.jobRepository = jobRepository;
        this.objectUtils = objectUtils;
        this.monitoringObjHelper = monitoringObjHelper;
        this.mqHelper = mqHelper;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public Job findById(String id) throws NotFoundException, OperationFailureException {
        try {
            Optional<Job> job = jobRepository.findById(id);
            if (!job.isPresent()) {
                throw new NotFoundException("Job not exists");
            }
            return job.get();
        }catch (MongoException ex){
            throw new OperationFailureException("Failed to retrieve job", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public List<Job> findAllWithPage(int page, int size) throws OperationFailureException {
        try {
            Page<Job> jobs = jobRepository.findAll(PageRequest.of(page,size ));
            return jobs.getContent();
        } catch (MongoException ex) {
            throw new OperationFailureException("Failed to find jobs", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public Job create(JobDetails jobDetails) throws OperationFailureException, ConflictExcpetion {
        try {
            Optional<Job> job = jobRepository.findByName(jobDetails.getName());
            if (job.isPresent()) {
                throw new ConflictExcpetion("Job name already exists");
            }
            Job newJob = new Job();
            Job mergedJob = objectUtils.patchObject(newJob, jobDetails);
            MonitoringObj monitoringObj = monitoringObjHelper.createMonitoringObj();
            mergedJob.setMonitoring(monitoringObj);
            // Send an event with create
            mqHelper.sendCreateJobEvent(mergedJob);
            // Then save object
            mergedJob = jobRepository.save(mergedJob);

            return mergedJob;
        }catch (MQException | MongoException ex){
            throw new OperationFailureException("Failed to save job", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public Job update(String id, JobDetails jobDetails) throws OperationFailureException, NotFoundException {
        try {
            Optional<Job> job = jobRepository.findById(id);
            if (!job.isPresent()) {
                throw new NotFoundException("Job not exists");
            }
            Job mergedJob = objectUtils.patchObject(job.get(), jobDetails);
            MonitoringObj monitoringObj = job.get().getMonitoring();
            monitoringObj = monitoringObjHelper.updateMonitoringObj(monitoringObj);
            mergedJob.setMonitoring(monitoringObj);
            // Send an event with create
            mqHelper.sendUpdateJobEvent(mergedJob);
            // Then save object
            mergedJob = jobRepository.save(mergedJob);
            return mergedJob;
        }catch (MQException | MongoException ex){
            throw new OperationFailureException("Failed to update job", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public Job delete(String id) throws OperationFailureException, NotFoundException {
        try {
            Optional<Job> job = jobRepository.findById(id);
            if (!job.isPresent()) {
                throw new NotFoundException("Job not exists");
            }
            // Send an event with create
            mqHelper.sendDeleteJobEvent(job.get());
            // Then save object
            jobRepository.deleteById(id);
            return job.get();
        }catch (MQException | MongoException ex){
            throw new OperationFailureException("Failed to delete job", ex);
        }
    }
}
