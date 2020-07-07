package io.outofbox.cronbot.model.job;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.outofbox.cronbot.model.MonitoringObj;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document("jobs")
public class Job  extends BaseJob{
    private String name;
    private String description;
    private JobStatus status = JobStatus.NEW;
    private MonitoringObj monitoring;
    @JsonProperty("integration_id")
    private String integrationId;
    @JsonProperty("integration_properties")
    private Map<String, Object> integrationProperties;
    private JobSchedule schedule;

}
