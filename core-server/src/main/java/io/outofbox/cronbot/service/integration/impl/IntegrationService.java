package io.outofbox.cronbot.service.integration.impl;

import com.mongodb.MongoException;
import io.outofbox.cronbot.error.ConflictExcpetion;
import io.outofbox.cronbot.error.NotFoundException;
import io.outofbox.cronbot.error.OperationFailureException;
import io.outofbox.cronbot.model.integration.Integration;
import io.outofbox.cronbot.model.integration.IntegrationDetails;
import io.outofbox.cronbot.repository.integration.IntegrationRepository;
import io.outofbox.cronbot.service.common.MonitoringObjHelper;
import io.outofbox.cronbot.service.integration.IIntegrationService;
import io.outofbox.cronbot.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public class IntegrationService implements IIntegrationService {

    private IntegrationRepository integrationRepository;
    private ObjectUtils objectUtils;
    private MonitoringObjHelper monitoringObjHelper;

    @Autowired
    public IntegrationService(IntegrationRepository integrationRepository, ObjectUtils objectUtils,
                              MonitoringObjHelper monitoringObjHelper){
        this.integrationRepository = integrationRepository;
        this.objectUtils = objectUtils;
        this.monitoringObjHelper = monitoringObjHelper;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public Integration findById(String id) throws NotFoundException, OperationFailureException {
        try {
            Optional<Integration> integration = integrationRepository.findById(id);
            if (!integration.isPresent()) {
                throw new NotFoundException("Integration not exists");
            }
            return integration.get();
        }catch (MongoException ex){
            throw new OperationFailureException("Failed to retrieve integration", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public List<Integration> findAllWithPage(int page, int size) throws OperationFailureException {
        try {
            Page<Integration> integrations = integrationRepository.findAll(PageRequest.of(page,size ));
            return integrations.getContent();
        } catch (MongoException ex) {
            throw new OperationFailureException("Failed to find integrations", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public Integration create(IntegrationDetails integrationDetails) throws OperationFailureException, ConflictExcpetion {
        try {
            Optional<Integration> integration = integrationRepository.findByName(integrationDetails.getName());
            if (integration.isPresent()) {
                throw new ConflictExcpetion("Integration name already exists");
            }
            Integration newIntegration = new Integration();
            Integration mergedIntegration = objectUtils.patchObject(newIntegration, integrationDetails);
            // MonitoringObj monitoringObj = monitoringObjHelper.createMonitoringObj();
            // mergedIntegration.setMonitoring(monitoringObj);
            mergedIntegration = integrationRepository.save(mergedIntegration);

            return mergedIntegration;
        }catch (MongoException ex){
            throw new OperationFailureException("Failed to save integration", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public Integration update(String id, IntegrationDetails integrationDetails) throws OperationFailureException, NotFoundException {
        try {
            Optional<Integration> integration = integrationRepository.findById(id);
            if (!integration.isPresent()) {
                throw new NotFoundException("Integration not exists");
            }
            Integration mergedIntegration = objectUtils.patchObject(integration.get(), integrationDetails);
            // MonitoringObj monitoringObj = integration.get().getMonitoring();
            // monitoringObj = monitoringObjHelper.updateMonitoringObj(monitoringObj);
            // mergedIntegration.setMonitoring(monitoringObj);
            mergedIntegration = integrationRepository.save(mergedIntegration);
            return mergedIntegration;
        }catch (MongoException ex){
            throw new OperationFailureException("Failed to update integration", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @Override
    public Integration delete(String id) throws OperationFailureException, NotFoundException {
        try {
            Optional<Integration> job = integrationRepository.findById(id);
            if (!job.isPresent()) {
                throw new NotFoundException("Integration not exists");
            }
            integrationRepository.deleteById(id);
            return job.get();
        }catch (MongoException ex){
            throw new OperationFailureException("Failed to delete integration", ex);
        }
    }
}
