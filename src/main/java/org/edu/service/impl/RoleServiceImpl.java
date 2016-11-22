package org.edu.service.impl;

import org.edu.dao.RoleDao;
import org.edu.model.Role;
import org.edu.model.User;
import org.edu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;

    @Override
    public void createRole(Role role, Principal principal) {
        roleDao.create(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDao.findAll();
    }

    @Override
    public Role getRoleById(long id) {
        return roleDao.findOne(id);
    }

    @Override
    public boolean updateRole(Role role, Principal principal) {
        Role checkingRole = roleDao.findOne(role.getId());
        if (checkingRole == null)
            return false;
        try {
            roleDao.update(role);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean removeRole(long id, Principal principal) {
        Role role = roleDao.findOne(id);
        if (role == null)
            return false;
        for (User user : role.getUsers()) {
            user.getRoles().remove(role);
        }
        role.getPrivileges().clear();
        roleDao.delete(role);
        return true;
    }
}
