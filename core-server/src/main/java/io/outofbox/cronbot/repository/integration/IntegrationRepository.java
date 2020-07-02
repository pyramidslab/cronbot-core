package io.outofbox.cronbot.repository.integration;

import io.outofbox.cronbot.model.integration.Integration;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface IntegrationRepository extends PagingAndSortingRepository<Integration, String> {
    Optional<Integration> findByName(String name);
}
