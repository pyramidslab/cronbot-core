package io.outofbox.cronbot.service.user.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import io.outofbox.cronbot.error.ConflictExcpetion;
import io.outofbox.cronbot.error.NotFoundException;
import io.outofbox.cronbot.error.OperationFailureException;
import io.outofbox.cronbot.model.user.User;
import io.outofbox.cronbot.model.user.UserDetails;
import io.outofbox.cronbot.repository.user.UserRepository;
import io.outofbox.cronbot.security.util.SecurityHelper;
import io.outofbox.cronbot.service.user.IUserService;
import io.outofbox.cronbot.utils.ObjectUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class UserServiceTest {

    @TestConfiguration
    static class UserServiceTestConfiguration {

        @Bean
        public ObjectUtils objectUtils() {
            return new ObjectUtils(new ObjectMapper());
        }
    }

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectUtils objectUtils;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @MockBean
    private SecurityHelper securityHelper;

    private IUserService userService;

    private static final String USERNAME = "user1";
    private static final String ID="1234";

    @Before
    public void setUp() {

        userService = new UserService(userRepository, objectUtils, encoder, securityHelper,"123456789");

        User user1 = new User();
        user1.setId(ID);
        user1.setUsername(USERNAME);
        user1.setPassword("1234567");
        user1.setEmail("user1@mail.com");

        Mockito.when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(user1));

        Page<User> page = new PageImpl(Arrays.asList(user1));
        Mockito.when(userRepository.findAll(PageRequest.of(1,1)))
                .thenReturn(page);

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user1);

        Mockito.when(securityHelper.canAccessOwn(Mockito.anyString(),Mockito.anyString())).thenReturn(true);

        Mockito.when(encoder.encode("12345678")).thenReturn("12345678");

        Mockito.doNothing().when(userRepository).delete(Mockito.any());
        //Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(USERNAME);
    }


    @Test
    @DisplayName("findByUsername")
    public void testFindByUsername_Exists() throws NotFoundException, OperationFailureException {
        User user = userService.findUserByUsername("", USERNAME);
        Assertions.assertThat(user.getUsername()).isEqualTo(USERNAME);
    }

    @Test(expected = NotFoundException.class)
    @DisplayName("findByUsernameNotExists")
    public void testFindByUsername_NotFound() throws NotFoundException, OperationFailureException {
        User user = userService.findUserByUsername("", USERNAME+"1");
    }

    @Test(expected = OperationFailureException.class)
    @DisplayName("findByUsernameError")
    public void testFindByUsername_Error() throws NotFoundException, OperationFailureException {
        Mockito.when(userRepository.findByUsername(USERNAME)).thenThrow(new MongoException("Error"){});
        User user = userService.findUserByUsername("", USERNAME);
    }

    @Test
    @DisplayName("findAllByPage")
    public void testFindAllByPage_Exists() throws  OperationFailureException {
        List<User> users = userService.findAllWithPage(1,1);
        Assertions.assertThat(users.size()).isEqualTo(1);
    }

    @Test(expected = OperationFailureException.class)
    @DisplayName("findAllByPageError")
    public void testFindAllByPage_Error() throws  OperationFailureException {
        Mockito.when(userRepository.findAll(PageRequest.of(1,1))).thenThrow(new MongoException("Error"){});
        List<User> users = userService.findAllWithPage(1,1);
    }


    @Test
    @DisplayName("createUser")
    public void testCreateUser() throws ConflictExcpetion,OperationFailureException {
        Mockito.when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        UserDetails user1 = new UserDetails();
        user1.setUsername(USERNAME);
        user1.setEmail("user1@mail.com");
        User user11 =  userService.createUser(USERNAME, user1);
        Assertions.assertThat(user11.getId()).isEqualTo(ID);
    }

    @Test(expected = ConflictExcpetion.class)
    @DisplayName("createUserExists")
    public void testCreateUser_Exists() throws ConflictExcpetion,OperationFailureException {
        UserDetails user1 = new UserDetails();
        user1.setUsername(USERNAME);
        user1.setPassword("1234567");
        user1.setEmail("user1@mail.com");
        User user11 =  userService.createUser(USERNAME, user1);
    }

    @Test(expected = OperationFailureException.class)
    @DisplayName("createUserError")
    public void testCreateUser_Error() throws  ConflictExcpetion, OperationFailureException {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenThrow(new MongoException("Error"){});

        UserDetails user1 = new UserDetails();
        user1.setUsername(USERNAME);
        user1.setPassword("1234567");
        user1.setEmail("user1@mail.com");
        User user11 =  userService.createUser(USERNAME, user1);
    }

    @Test
    @DisplayName("updateUser")
    public void testUpdateUser() throws NotFoundException,OperationFailureException {
        User user1 = new User();
        user1.setId(ID);
        user1.setUsername(USERNAME);
        user1.setPassword("12345678");
        user1.setEmail("user1@mail.com1");
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user1);

        UserDetails userDetails = new UserDetails();
        userDetails.setUsername("");
        userDetails.setPassword("12345678");
        userDetails.setEmail("user1@mail.com1");
        User user11 =  userService.updateUser(Mockito.any() ,USERNAME, userDetails);
        Assertions.assertThat(user11.getEmail()).isEqualTo("user1@mail.com1");
    }

    @Test
    @DisplayName("updateUserEmptyPassword")
    public void testUpdateUserEmptyPassword() throws NotFoundException,OperationFailureException {
        User user1 = new User();
        user1.setId(ID);
        user1.setUsername(USERNAME);
        user1.setPassword("123456789");
        user1.setEmail("user1@mail.com1");
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user1);

        UserDetails userDetails = new UserDetails();
        userDetails.setEmail("user1@mail.com1");
        User user11 =  userService.updateUser(Mockito.any() ,USERNAME, userDetails);
        Assertions.assertThat(user11.getPassword()).isEqualTo("123456789");
    }

    @Test(expected = NotFoundException.class)
    @DisplayName("updateUserNotFound")
    public void testUpdateUser_NotFound() throws NotFoundException,OperationFailureException {
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername(USERNAME);
        userDetails.setPassword("1234567");
        userDetails.setEmail("user1@mail.com");
        User user11 =  userService.updateUser(Mockito.any() ,USERNAME+1, userDetails);
       // User user11 =  userService.createUser(USERNAME, user1);
    }

    @Test(expected = OperationFailureException.class)
    @DisplayName("updateUserError")
    public void testUpdateUser_Error() throws  NotFoundException, OperationFailureException {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenThrow(new MongoException("Error"){});

        UserDetails userDetails = new UserDetails();
        userDetails.setUsername(USERNAME);
        userDetails.setPassword("1234567");
        userDetails.setEmail("user1@mail.com");
        User user11 =  userService.updateUser(Mockito.any(), USERNAME, userDetails);
    }

    @Test
    @DisplayName("deleteUser")
    public void testDeleteUser() throws NotFoundException,OperationFailureException {
        userService.deleteUser(Mockito.any(),USERNAME);
    }

    @Test(expected = NotFoundException.class)
    @DisplayName("deleteUserNotFound")
    public void testDeleteUser_NotFound() throws NotFoundException,OperationFailureException {
        userService.deleteUser(Mockito.any(),USERNAME+1);
    }

    @Test(expected = OperationFailureException.class)
    @DisplayName("deleteUserError")
    public void testDeleteUser_Error() throws NotFoundException, OperationFailureException {
        Mockito.doThrow(new MongoException("Error"){}).when(userRepository).delete(Mockito.any());

        userService.deleteUser(Mockito.any(),USERNAME);
    }
}
