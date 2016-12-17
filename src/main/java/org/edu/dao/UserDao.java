package org.edu.dao;

import org.edu.dao.common.IOperations;
import org.edu.model.Meeting;
import org.edu.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserDao extends IOperations<User> {

    User findByName(String name);

    User findByEmail(String email);

    List<Meeting> findSubscriptions(long userId, int pageNum, int num);

    Long findSubscriptionsNum(long userId, int pageNum, int num);

}
