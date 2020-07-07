package io.outofbox.cronbot.controller.user;

import io.outofbox.cronbot.controller.util.ControllerUtils;
import io.outofbox.cronbot.error.ConflictExcpetion;
import io.outofbox.cronbot.error.NotFoundException;
import io.outofbox.cronbot.error.OperationFailureException;
import io.outofbox.cronbot.model.user.User;
import io.outofbox.cronbot.model.user.UserDetails;
import io.outofbox.cronbot.security.config.TokenProvider;
import io.outofbox.cronbot.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.FailedLoginException;
import javax.validation.Valid;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private IUserService userService;

    @Autowired
    public UserController(IUserService userService){
        this.userService = userService;
    }

    @RequestMapping(value = "/{user-id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByUsername(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                          @PathVariable("user-id") String username) throws NotFoundException, OperationFailureException {

        User user = userService.findById(username);

        return  ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/{user-id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUserByUsername(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                           @PathVariable("user-id") String username,
                                           @RequestBody UserDetails userDetails) throws NotFoundException, OperationFailureException {

        User user = userService.update(username, userDetails);

        return  ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/{user-id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> deleteUserByUsername(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                                  @PathVariable("user-id") String username) throws FailedLoginException, NotFoundException, OperationFailureException {

        User user = userService.delete( username);

        return  ResponseEntity.ok(user);
    }


    @RequestMapping( method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                           @Valid @RequestBody  UserDetails userDetails) throws ConflictExcpetion, OperationFailureException {

        User user = userService.create(userDetails);

        return  ResponseEntity.ok(user);
    }

    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List> getUsers(@RequestHeader(TokenProvider.HEADER_STRING) String authorization,
                                         @RequestParam(value = "page",required = false, defaultValue = "0") int page,
                                         @RequestParam(value = "size", required = false, defaultValue = "50") int size) throws  OperationFailureException {
        size = ControllerUtils.handleSize(size);
        page = ControllerUtils.handlePage(page);

        List<User> users = userService.findAllWithPage(page,size);

        return  ResponseEntity.ok(users);
    }


}
