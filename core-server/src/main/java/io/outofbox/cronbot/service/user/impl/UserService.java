package io.outofbox.cronbot.service.user.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private UserRepository userRepository;
    private ObjectUtils objectUtils;
    private BCryptPasswordEncoder encoder;
    private String defaultPassword;
    private SecurityHelper securityHelper;

    @Autowired
    public UserService(UserRepository userRepository, ObjectUtils objectUtils, BCryptPasswordEncoder encoder,
                       SecurityHelper securityHelper,
                       @Value("${cronbot.security.new-user-default-password:123456789}")String defaultPassword) {
        this.userRepository = userRepository;
        this.objectUtils = objectUtils;
        this.encoder =encoder;
        this.defaultPassword = defaultPassword;
        this.securityHelper = securityHelper;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN') or @securityHelper.canAccessOwn(#username)")
    @Override
    public User findById(String username) throws NotFoundException, OperationFailureException {
        try {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new NotFoundException("User not exists");
        }
        User userObj = user.get();

        return userObj;
        } catch (MongoException ex) {
            throw new OperationFailureException("Failed to find user", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Override
    public List<User> findAllWithPage(int page, int size) throws OperationFailureException {
        try {
            Page<User> users = userRepository.findAll(PageRequest.of(page,size ));
            return users.getContent();
        } catch (MongoException ex) {
            throw new OperationFailureException("Failed to find user", ex);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public User create(UserDetails userDetails) throws OperationFailureException, ConflictExcpetion {
        try {
            Optional<User> oldUser = userRepository.findByUsername(userDetails.getUsername());
            if(oldUser.isPresent()){
                throw new ConflictExcpetion("User already exists");
            }
            User user = new User();
            user.setUsername(userDetails.getUsername());
            User mergedUser = objectUtils.patchObject(user, userDetails);
            if(StringUtils.isEmpty(userDetails.getPassword())){
                userDetails.setPassword(defaultPassword);
            }
            mergedUser.setPassword(encoder.encode(userDetails.getPassword()));
            mergedUser = userRepository.save(mergedUser);
            return mergedUser;
        } catch (MongoException ex) {
            throw new OperationFailureException("Failed to save user", ex);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @securityHelper.canAccessOwn(#username)")
    @Override
    public User update(String username, UserDetails userDetails) throws OperationFailureException, NotFoundException {
        if(userDetails.getUsername()!= null && !userDetails.getUsername().equals(username)){
            throw new NotFoundException("Users not match");
        }
        try {
            Optional<User> oldUser = userRepository.findByUsername(username);
            if(!oldUser.isPresent()){
                throw new NotFoundException("User not exists");
            }
            User mergedUser = objectUtils.patchObject(oldUser.get(), userDetails);
            // Check if the username is null revert it back
            if(StringUtils.isEmpty(mergedUser.getUsername()) || StringUtils.isEmpty(userDetails.getUsername())|| !userDetails.getUsername().equals(username)){
                mergedUser.setUsername(username);
            }
            // Check if the password is null revert it back
            if(StringUtils.isEmpty(userDetails.getPassword())){
                mergedUser.setPassword(oldUser.get().getPassword());
            }else{
                mergedUser.setPassword(encoder.encode(userDetails.getPassword()));
            }
            // Check if the roles is null or not changed by admin revert it back
            if(mergedUser.getRoles()== null || mergedUser.getRoles().isEmpty() || securityHelper.canAccessOwn(username)){
                mergedUser.setRoles(oldUser.get().getRoles());
            }
            mergedUser = userRepository.save(mergedUser);
            return mergedUser;
        } catch (MongoException ex) {
            throw new OperationFailureException("Failed to save user", ex);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Override
    public User delete(String username) throws NotFoundException, OperationFailureException {
        try {
            Optional<User> user = userRepository.findByUsername(username);
            if (!user.isPresent()) {
                throw new NotFoundException("User not exists");
            }
            User userObj = user.get();
            userRepository.delete(userObj);
            return userObj;
        } catch (MongoException ex) {
            throw new OperationFailureException("Failed to find user", ex);
        }
    }
}
