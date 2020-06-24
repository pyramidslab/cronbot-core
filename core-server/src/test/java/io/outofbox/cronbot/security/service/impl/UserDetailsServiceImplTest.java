package io.outofbox.cronbot.security.service.impl;

import io.outofbox.cronbot.model.user.User;
import io.outofbox.cronbot.security.model.LoginUser;
import io.outofbox.cronbot.security.repository.LoginUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
public class UserDetailsServiceImplTest {
    @TestConfiguration
    static class UserDetailsServiceImplTestConfiguration {
        @Bean
        public UserDetailsService userDetailsService() {
            return new UserDetailsServiceImpl();
        }
    }

    @Autowired
    UserDetailsService userDetailsService;

    @MockBean
    private LoginUserRepository loginUserRepository;

    @Before
    public void setUp() {
        Set<String> roles = new HashSet<>();
        roles.add("role1");

        LoginUser user = new LoginUser();
        user.setUsername("ahelmy");
        user.setPassword("12345");
        user.setRoles(roles);

        Mockito.when(loginUserRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));
    }

    @Test
    @DisplayName("loadUserByUsername_Exists")
    public void testLoadUserByUsername(){
        String username = "ahelmy";
        String password = "12345";
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Assertions.assertThat(userDetails.getUsername()).isEqualTo(username);
        Assertions.assertThat(userDetails.getAuthorities()).size().isEqualTo(1);
        Assertions.assertThat(userDetails.getPassword()).isEqualTo(password);
        boolean equals  = userDetails.getAuthorities().toArray(new SimpleGrantedAuthority[0])[0].getAuthority().equals("ROLE_role1");
        Assert.assertEquals(equals,true);
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        return user.getRoles().stream().map(role->new SimpleGrantedAuthority("ROLE_"+role)).collect(Collectors.toSet());
    }

    @Test
    public void getAuthorityTest(){
        Set<String> roles = new HashSet<>();
        roles.add("role1");
        User user = new User();
        user.setRoles(roles);

        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = getAuthority(user);
        boolean equals  = simpleGrantedAuthorities.toArray(new SimpleGrantedAuthority[0])[0].getAuthority().equals("ROLE_role1");
        Assert.assertEquals(equals,true);
    }

    @Test(expected = UsernameNotFoundException.class)
    @DisplayName("loadUserByUsername_NotFound")
    public void loadUserByUsername_NotFound(){
        String username = "ahelmy1";
        String password = "12345";
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    }
}
