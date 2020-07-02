package io.outofbox.cronbot.repository.job;

import io.outofbox.cronbot.model.job.Job;
import io.outofbox.cronbot.model.plugin.Plugin;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface JobRepository extends PagingAndSortingRepository<Job, String> {
    Optional<Job> findByName(String name);
}
