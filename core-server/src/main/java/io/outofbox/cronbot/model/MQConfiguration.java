package io.outofbox.cronbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MQConfiguration {
    @JsonProperty("virtual_host")
    private String virtualHost;
    private String username;
    private String password;
    private String host;
    private int port;
    @JsonProperty("in_queue")
    private String inQueue;
    @JsonProperty("out_queue")
    private String outQueue;
}
