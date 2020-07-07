package io.outofbox.cronbot.model.job;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
public enum JobStatus {
    NEW("new"),
    STARTED("started"),
    RUNNING("running"),
    STOPPED("stopped"),
    FINISHED("finished"),
    INTERRUPTED("interrupted"),
    ERROR("error");

    private String name;

}
