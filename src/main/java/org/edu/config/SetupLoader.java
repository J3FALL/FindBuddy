package org.edu.config;

import org.edu.dao.CommentDao;
import org.edu.dao.PrivilegeDao;
import org.edu.dao.RoleDao;
import org.edu.dao.UserDao;
import org.edu.model.Comment;
import org.edu.model.Privilege;
import org.edu.model.Role;
import org.edu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Component
public class SetupLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    PrivilegeDao privilegeDAO;

    @Autowired
    CommentDao commentDao;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        // == create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        // == create initial roles
        final List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        final Role adminRole = roleDao.findByName("ROLE_ADMIN");
        final User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setSurname("Test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@test.com");
        user.setRoles(Arrays.asList(adminRole));
        user.setBirthday(new Date());
//        userDao.create(user);
        createIfNotFound(user);
        final Comment firstComment = new Comment();
        firstComment.setId(1);
        firstComment.setDate(Calendar.getInstance());
        firstComment.setText("First comment!");
        firstComment.setAuthor(user);
        createIfNotFound(firstComment);
        final Comment secondComment = new Comment();
        secondComment.setId(2);
        secondComment.setDate(Calendar.getInstance());
        secondComment.setText("Second comment!");
        secondComment.setAuthor(user);
        createIfNotFound(secondComment);
        List<User> users = userDao.findAll();
        List<Comment> comments = commentDao.findAll();

        alreadySetup = true;
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeDAO.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeDAO.create(privilege);
        }
        return privilege;
    }

    @Transactional
    private Role createRoleIfNotFound(final String name, final List<Privilege> privileges) {
        Role role = roleDao.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleDao.create(role);
        }
        return role;
    }

    @Transactional
    private User createIfNotFound(final User newUser) {
        User user = userDao.findByEmail(newUser.getEmail());
        if (user == null) {
            userDao.create(newUser);
        }
        return user;
    }

    @Transactional
    private Comment createIfNotFound(final Comment newComment) {
        Comment comment = commentDao.findOne(newComment.getId());
        if (comment == null)
            commentDao.create(newComment);
        return comment;
    }
}
