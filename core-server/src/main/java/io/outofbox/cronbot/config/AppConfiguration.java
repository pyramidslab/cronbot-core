package io.outofbox.cronbot.config;


import io.outofbox.cronbot.model.user.User;
import io.outofbox.cronbot.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

@Slf4j
@Configuration
public class AppConfiguration implements CommandLineRunner {

    private String adminUsername;
    private String adminPassword;
    private BCryptPasswordEncoder encoder;
    private UserRepository userRepository;

    public AppConfiguration(@Value("${cronbot.security.admin.username:admin}")  String adminUsername,
                            @Value("${cronbot.security.admin.password:admin}") String adminPassword,
                            @Autowired BCryptPasswordEncoder encoder,
                            @Autowired UserRepository userRepository){
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if there is no users in DB add first admin
        Long usersCount = userRepository.count();
        if(usersCount==0){
            User adminUser = new User();
            adminUser.setAdmin(true);
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(encoder.encode(adminPassword));
            adminUser.setFirstName("Super");
            adminUser.setLastName("Admin");
            adminUser.setRoles(new HashSet<>(Arrays.asList("ADMIN")));
            log.info("Adding default admin user [{}]", adminUsername);
            userRepository.save(adminUser);
        }
    }

}
