package org.edu.dao;

import org.edu.dao.common.IOperations;
import org.edu.model.Privilege;

public interface PrivilegeDao extends IOperations<Privilege> {

    Privilege findByName(String name);
}
