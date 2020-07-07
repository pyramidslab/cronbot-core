package io.outofbox.cronbot.service.integration;

import io.outofbox.cronbot.model.integration.Integration;
import io.outofbox.cronbot.model.integration.IntegrationDetails;
import io.outofbox.cronbot.service.common.IGenericCRUDService;

public interface IIntegrationService  extends IGenericCRUDService<Integration, IntegrationDetails> {
}
