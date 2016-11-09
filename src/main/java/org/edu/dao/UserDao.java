package org.edu.dao;

import org.edu.dao.common.IOperations;
import org.edu.model.User;

public interface UserDao extends IOperations<User> {

    User findByName(String name);

    User findByEmail(String email);
}
