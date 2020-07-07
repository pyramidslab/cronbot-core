package io.outofbox.cronbot.controller.job;

import io.outofbox.cronbot.controller.util.ControllerUtils;
import io.outofbox.cronbot.error.ConflictExcpetion;
import io.outofbox.cronbot.error.NotFoundException;
import io.outofbox.cronbot.error.OperationFailureException;
import io.outofbox.cronbot.model.integration.IntegrationDefinition;
import io.outofbox.cronbot.model.job.Job;
import io.outofbox.cronbot.model.job.JobDetails;
import io.outofbox.cronbot.model.plugin.Plugin;
import io.outofbox.cronbot.model.plugin.PluginDetails;
import io.outofbox.cronbot.security.config.TokenProvider;
import io.outofbox.cronbot.service.job.IJobService;
import io.outofbox.cronbot.service.plugin.IPluginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.FailedLoginException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ahelmy
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/jobs")
@Slf4j
public class JobController {

    private IJobService jobService;

    @Autowired
    public JobController(IJobService jobService){
        this.jobService = jobService;
    }

    @RequestMapping(value = "/{job-id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Job> getById(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                          @PathVariable("job-id") String id) throws NotFoundException, OperationFailureException {

        Job job = jobService.findById(id);

        return  ResponseEntity.ok(job);
    }

    @RequestMapping(value = "/{job-id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Job> updateJob(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                               @PathVariable("job-id") String id,
                                               @RequestBody JobDetails jobDetails) throws NotFoundException, OperationFailureException {

        Job job = jobService.update(id, jobDetails);

        return  ResponseEntity.ok(job);
    }

    @RequestMapping(value = "/{job-id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Job> deleteById(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                             @PathVariable("job-id") String id) throws FailedLoginException, NotFoundException, OperationFailureException {

        Job job = jobService.delete( id);

        return  ResponseEntity.ok(job);
    }


    @RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Job> createJob(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                            @RequestBody JobDetails jobDetails) throws ConflictExcpetion, OperationFailureException {

        Job plugin = jobService.create(jobDetails);

        return  ResponseEntity.ok(plugin);
    }

    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List> getJobs(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                           @RequestParam(value = "page",required = false, defaultValue = "0") int page,
                                           @RequestParam(value = "size", required = false, defaultValue = "50") int size) throws  OperationFailureException {
        size = ControllerUtils.handleSize(size);
        page = ControllerUtils.handlePage(page);

        List<Job> jobs = jobService.findAllWithPage(page,size);

        return  ResponseEntity.ok(jobs);
    }

}
