package io.outofbox.cronbot.service.user;

import io.outofbox.cronbot.model.user.User;
import io.outofbox.cronbot.model.user.UserDetails;
import io.outofbox.cronbot.service.common.IGenericCRUDService;

public interface IUserService extends IGenericCRUDService<User, UserDetails> {
}
