package io.outofbox.cronbot.controller.integration;

import io.outofbox.cronbot.controller.util.ControllerUtils;
import io.outofbox.cronbot.error.ConflictExcpetion;
import io.outofbox.cronbot.error.NotFoundException;
import io.outofbox.cronbot.error.OperationFailureException;
import io.outofbox.cronbot.model.integration.Integration;
import io.outofbox.cronbot.model.integration.IntegrationDetails;
import io.outofbox.cronbot.model.job.Job;
import io.outofbox.cronbot.model.job.JobDetails;
import io.outofbox.cronbot.security.config.TokenProvider;
import io.outofbox.cronbot.service.integration.IIntegrationService;
import io.outofbox.cronbot.service.job.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.FailedLoginException;
import java.util.List;

/**
 * @author ahelmy
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/integrations")
@Slf4j
public class IntegrationController {

    private IIntegrationService integrationService;

    @Autowired
    public IntegrationController(IIntegrationService integrationService){
        this.integrationService = integrationService;
    }

    @RequestMapping(value = "/{integration-id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integration> getById(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                               @PathVariable("integration-id") String id) throws NotFoundException, OperationFailureException {

        Integration integration = integrationService.findById(id);

        return  ResponseEntity.ok(integration);
    }

    @RequestMapping(value = "/{integration-id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integration> updateJob(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                               @PathVariable("integration-id") String id,
                                               @RequestBody IntegrationDetails integrationDetails) throws NotFoundException, OperationFailureException {

        Integration job = integrationService.update(id, integrationDetails);

        return  ResponseEntity.ok(job);
    }

    @RequestMapping(value = "/{integration-id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integration> deleteById(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                             @PathVariable("integration-id") String id) throws FailedLoginException, NotFoundException, OperationFailureException {

        Integration job = integrationService.delete( id);

        return  ResponseEntity.ok(job);
    }


    @RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integration> createJob(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                            @RequestBody IntegrationDetails integrationDetails) throws ConflictExcpetion, OperationFailureException {

        Integration plugin = integrationService.create(integrationDetails);

        return  ResponseEntity.ok(plugin);
    }

    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List> getIntegrations(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                           @RequestParam(value = "page",required = false, defaultValue = "0") int page,
                                           @RequestParam(value = "size", required = false, defaultValue = "50") int size) throws  OperationFailureException {
        size = ControllerUtils.handleSize(size);
        page = ControllerUtils.handlePage(page);

        List<Integration> jobs = integrationService.findAllWithPage(page,size);

        return  ResponseEntity.ok(jobs);
    }

}
