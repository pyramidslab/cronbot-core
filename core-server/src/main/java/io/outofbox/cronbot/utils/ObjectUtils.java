package io.outofbox.cronbot.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ObjectUtils {

    private ObjectMapper objectMapper;

    @Autowired
    public ObjectUtils(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public <E, R>E patchObject(E toObj, R fromObj){
        Map<String,Object> oldObjMap  = objectMapper.convertValue(toObj, new TypeReference<Map<String, Object>>() {});
        Map<String,Object> newObjMap  = objectMapper.convertValue(fromObj, new TypeReference<Map<String, Object>>() {});
        newObjMap.entrySet().stream().forEach((entry)->{
            if(entry.getValue() != null){
                oldObjMap.put(entry.getKey(),entry.getValue());
            }
        });
        return (E)objectMapper.convertValue(oldObjMap, toObj.getClass());
    }
}
