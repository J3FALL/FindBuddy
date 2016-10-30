package org.edu.dao;

import org.edu.dao.common.IOperations;
import org.edu.model.User;

/**
 * Created by ILNUR on 29.10.2016.
 */
public interface IUserDao extends IOperations<User> {

    User findByName(String name);

    User findByEmail(String email);

}
