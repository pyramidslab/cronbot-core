package io.outofbox.cronbot.model.event;

import io.outofbox.cronbot.model.job.Job;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class JobEvent extends Event<Job> {
    private EventType event;
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private String _class;
    private Job object;

}
