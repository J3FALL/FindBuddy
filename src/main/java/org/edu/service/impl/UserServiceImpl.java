package org.edu.service.impl;


import org.apache.commons.beanutils.BeanUtilsBean;
import org.edu.dao.CommentDao;
import org.edu.dao.RoleDao;
import org.edu.dao.UserDao;
import org.edu.model.Comment;
import org.edu.model.User;
import org.edu.service.UserService;
import org.edu.util.NullAwareBeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CommentDao commentDao;

    @Override
    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User getUserById(long id) {
        return userDao.findOne(id);
    }

    @Override
    public User createUser(User newUser) {
        if (existEmail(newUser.getEmail())) {
            return null;
        }
        User user = new User();
        user.setBirthday(newUser.getBirthday());
        user.setName(newUser.getName());
        user.setSurname(newUser.getSurname());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setRoles(Arrays.asList(roleDao.findByName("ROLE_USER")));
        userDao.create(user);
        return user;
    }

    private boolean existEmail(String email) {
        return userDao.findByEmail(email) != null;
    }

    @Override
    public boolean updateUser(User user, Principal principal) {
        User checkingUser = userDao.findOne(user.getId());
        User principalUser = userDao.findByEmail(principal.getName());
        if (checkingUser != null && principalUser.getId() == user.getId()) {
            BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
            try {
                notNull.copyProperties(checkingUser, user);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            userDao.update(checkingUser);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(long id, Principal principal) {
        User checkingUser = userDao.findOne(id);
        User principalUser = userDao.findByEmail(principal.getName());
        if (checkingUser != null && id == principalUser.getId()) {
            userDao.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Comment> getUserComments(long userID) {
        User commentingUser = userDao.findOne(userID);
        if (commentingUser != null)
            return commentingUser.getComments();
        return null;
    }
}
