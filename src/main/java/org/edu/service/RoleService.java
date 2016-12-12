package org.edu.service;


import org.edu.model.Privilege;
import org.edu.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
public interface RoleService {

    void createRole(Role role, Principal principal);

    List<Role> getAllRoles();

    Role getRoleById(long id);

    boolean updateRole(Role role, Principal principal);

    boolean removeRole(long id, Principal principal);

    boolean setPrivileges(long id, List<Privilege> privileges);

    boolean removePrivileges(long id, List<Privilege> privileges);

    List<Privilege> getRolePrivileges(long id);
}
