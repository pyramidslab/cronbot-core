package io.outofbox.cronbot.model.event;

import io.outofbox.cronbot.model.Job;
import lombok.Builder;
import lombok.NonNull;


public class JobEvent extends Event<Job> {
    @NonNull
    private EventType event;
    private String _class;
    @NonNull
    private Job object;

    @Builder
    public JobEvent(EventType type, Job object){
        super(type, "Job", object);
        this._class = "Job";
        this.event = type;
        this.object = object;
    }

}
