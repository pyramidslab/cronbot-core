package io.outofbox.cronbot.model.job;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.outofbox.cronbot.model.MonitoringObj;
import lombok.Data;

import java.util.Map;

@Data
public class JobDetails {
    private String name;
    private String description;
    @JsonProperty("integration_id")
    private String integrationId;
    @JsonProperty("integration_properties")
    private Map<String, Object> integrationProperties;
    private JobSchedule schedule;
}
