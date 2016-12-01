package org.edu.config;

import org.edu.dao.CategoryDao;
import org.edu.dao.CommentDao;
import org.edu.dao.PrivilegeDao;
import org.edu.dao.RoleDao;
import org.edu.dao.StationDao;
import org.edu.dao.UserDao;
import org.edu.model.Category;
import org.edu.model.Comment;
import org.edu.model.Privilege;
import org.edu.model.Role;
import org.edu.model.Station;
import org.edu.model.User;
import org.edu.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    CategoryDao categoryDao;

    @Autowired
    StationDao stationDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    StorageService storageService;




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
        createRoleIfNotFound("ROLE_ADMIN", new HashSet<>(adminPrivileges));
        createRoleIfNotFound("ROLE_USER", new HashSet<>(Arrays.asList(readPrivilege)));

        final Role adminRole = roleDao.findByName("ROLE_ADMIN");
        final User user = new User();
        user.setId(1L);
        user.setName("Test");
        user.setSurname("Test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@test.com");
        user.setRoles(new HashSet<>(Arrays.asList(adminRole)));
//        user.setCategories(new HashSet<>(Arrays.asList(category)));
        user.setBirthday(LocalDate.now());
//        userDao.create(user);
        createIfNotFound(user);
        final Comment firstComment = new Comment();
        firstComment.setId(1);
        firstComment.setDate(LocalDateTime.now());
        firstComment.setText("First comment!");
        firstComment.setAuthor(user);
        createIfNotFound(firstComment);
        final Comment secondComment = new Comment();
        secondComment.setId(2);
        secondComment.setDate(LocalDateTime.now());
        secondComment.setText("Second comment!");
        secondComment.setAuthor(user);
        createIfNotFound(secondComment);
        final Category category = new Category();
        category.setId(1);
        category.setColor("red");
        category.setName("Спорт");
        category.setUsers(new HashSet<>(Arrays.asList(user)));
        createIfNotFound(category);
        final Category category2 = new Category();
        category2.setId(2);
        category2.setColor("blue");
        category2.setName("Концерт");
        category2.setUsers(new HashSet<>(Arrays.asList(user)));
        createIfNotFound(category2);
        final Station station = new Station();
        station.setId(1);
        station.setColor("blue");
        station.setName("Невский проспект");
        createIfNotFound(station);
        storageService.init();
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
    private Role createRoleIfNotFound(final String name, final Set<Privilege> privileges) {
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

    @Transactional
    private Category createIfNotFound(final Category newCategory) {
        Category category = categoryDao.findOne(newCategory.getId());
        if (category == null) {
            categoryDao.create(newCategory);
        }
        return category;
    }

    @Transactional
    private Station createIfNotFound(final Station station) {
        Station checkingStation = stationDao.findOne(station.getId());
        if (checkingStation == null) {
            stationDao.create(station);
        }
        return checkingStation;
    }
}
