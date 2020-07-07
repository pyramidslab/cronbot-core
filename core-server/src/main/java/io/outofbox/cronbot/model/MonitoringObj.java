package io.outofbox.cronbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class MonitoringObj {
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("last_modified_at")
    private Date lastModifiedAt;
    @JsonProperty("last_modified_by")
    private String lastModifiedBy;
    private boolean enabled;


}
