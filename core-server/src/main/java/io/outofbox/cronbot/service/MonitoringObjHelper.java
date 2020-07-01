package io.outofbox.cronbot.service;

import io.outofbox.cronbot.model.MonitoringObj;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class MonitoringObjHelper {


    @Bean("newMonitoringObj")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MonitoringObj createMonitoringObj(){
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MonitoringObj monitoringObj = new MonitoringObj();
        monitoringObj.setCreatedAt(new Date());
        monitoringObj.setCreatedBy(username);
        monitoringObj.setEnabled(true);
        monitoringObj.setLastModifiedAt(new Date());
        monitoringObj.setLastModifiedBy(username);
        return monitoringObj;
    }

    @Bean("updateMonitoringObj")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MonitoringObj updateMonitoringObj(MonitoringObj oldMonitoringObj){
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        oldMonitoringObj.setLastModifiedAt(new Date());
        oldMonitoringObj.setLastModifiedBy(username);
        return oldMonitoringObj;
    }
}
