package io.outofbox.cronbot.controller.job;

import io.outofbox.cronbot.model.Job;
import io.outofbox.cronbot.service.job.IJobService;
import io.outofbox.cronbot.service.job.impl.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ahelmy
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/job")
@Slf4j
public class JobController {

    @Autowired
    IJobService jobService;

    @GetMapping()
    public ResponseEntity getJob(){
        Job job = new Job();
        job.setId("1345");
        job.setName("first Job");
        jobService.createJob(job);
        return ResponseEntity.ok().build();
    }
}
