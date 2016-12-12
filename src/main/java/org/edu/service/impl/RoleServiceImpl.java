package org.edu.service.impl;

import org.edu.dao.PrivilegeDao;
import org.edu.dao.RoleDao;
import org.edu.model.Privilege;
import org.edu.model.Role;
import org.edu.model.User;
import org.edu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;

    @Autowired
    PrivilegeDao privilegeDao;

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

    @Override
    public boolean setPrivileges(long id, List<Privilege> privileges) {
        Role role = roleDao.findOne(id);
        if (role == null)
            return false;
        for (Privilege privilege : privileges) {
            privilege = privilegeDao.findOne(privilege.getId());
            role.addPrivilege(privilege);
        }
        return true;
    }

    @Override
    public boolean removePrivileges(long id, List<Privilege> privileges) {
        Role role = roleDao.findOne(id);
        if (role == null)
            return false;
        for (Privilege privilege : privileges) {
            privilege = privilegeDao.findOne(privilege.getId());
            role.deletePrivilege(privilege);
        }
        return true;
    }

    @Override
    public List<Privilege> getRolePrivileges(long id) {
        Role role = roleDao.findOne(id);
        if (role == null)
            return null;
        return new ArrayList<>(role.getPrivileges());
    }
}
