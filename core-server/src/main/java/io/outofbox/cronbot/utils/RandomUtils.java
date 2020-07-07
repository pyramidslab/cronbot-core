package io.outofbox.cronbot.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class RandomUtils {

    private static final int LENGTH = 50;

    public String random(int length){
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public String random(){
        return  random(LENGTH);
    }
}
