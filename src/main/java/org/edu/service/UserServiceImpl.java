package org.edu.service;


import org.edu.dao.RoleDao;
import org.edu.dao.UserDao;
import org.edu.model.User;
import org.edu.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User getUser(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User registerNewUser(UserDTO userDTO) {
        if (existEmail(userDTO.getEmail())) {
            return null;
        }
        User user = new User();
        user.setBirthday(userDTO.getBirthday());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(Arrays.asList(roleDao.findByName("ROLE_USER")));
        userDao.create(user);
        return user;
    }

    private boolean existEmail(String email) {
        return userDao.findByEmail(email) != null;
    }
}
