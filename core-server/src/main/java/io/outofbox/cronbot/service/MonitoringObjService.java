package io.outofbox.cronbot.service;

import io.outofbox.cronbot.model.MonitoringObj;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MonitoringObjService {

    private String username;
    public MonitoringObjService(){
        this.username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Bean("newMonitoringObj")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MonitoringObj createMonitoringObj(){
        return MonitoringObj.builder().createdAt(new Date()).createdBy(username).enabled(true).lastModifiedAt(new Date()).lastModifiedBy(username).build();
    }

    @Bean("updateMonitoringObj")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MonitoringObj updateMonitoringObj(MonitoringObj oldMonitoringObj){
        oldMonitoringObj.setLastModifiedAt(new Date());
        oldMonitoringObj.setLastModifiedBy(username);
        return oldMonitoringObj;
    }
}
