package io.outofbox.cronbot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.outofbox.cronbot.model.user.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ObjectUtilsTest {

    @TestConfiguration
    static class ObjectUtilsTestConfiguration {
        @Bean
        public ObjectUtils objectUtils(){
            return  new ObjectUtils(new ObjectMapper());
        }
    }

    @Autowired
    private ObjectUtils objectUtils;

    @Test
    @DisplayName("TestPatchObjects")
    public void testPatchObjects(){
        User u1 = new User();
        u1.setUsername("ali");
        User u2 = new User();
        u2.setUsername("ali2");
        User mergedUser = objectUtils.patchObject(u1, u2);
        Assertions.assertThat(mergedUser.getUsername()).isEqualTo("ali2");
    }
}
