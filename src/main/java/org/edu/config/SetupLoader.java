package org.edu.config;

import org.edu.dao.PrivilegeDao;
import org.edu.dao.RoleDao;
import org.edu.dao.UserDao;
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
import java.util.List;

@Component
public class SetupLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    PrivilegeDao privilegeDao;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
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
        user.setName("Test");
        user.setSurname("Test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@test.com");
        user.setRoles(Arrays.asList(adminRole));
        userDao.create(user);
        alreadySetup = true;
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeDao.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeDao.create(privilege);
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
}
