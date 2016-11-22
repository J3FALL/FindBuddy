package org.edu.service;


import org.edu.model.Role;

import java.security.Principal;
import java.util.List;

public interface RoleService {

    void createRole(Role role, Principal principal);

    List<Role> getAllRoles();

    Role getRoleById(long id);

    boolean updateRole(Role role, Principal principal);

    boolean removeRole(long id, Principal principal);
}
