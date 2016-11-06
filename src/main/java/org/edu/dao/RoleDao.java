package org.edu.dao;

import org.edu.dao.common.IOperations;
import org.edu.model.Role;

public interface RoleDao extends IOperations<Role> {

    Role findByName(String name);
}
