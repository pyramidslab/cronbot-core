package io.outofbox.cronbot.model.job;

import lombok.Data;

import java.util.Date;

@Data
public class JobSchedule {
    private String cron;
    private Date start;
    private Date end;
}
