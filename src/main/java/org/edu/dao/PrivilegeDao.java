package org.edu.dao;

import org.edu.dao.common.IOperations;
import org.edu.model.Privilege;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PrivilegeDao extends IOperations<Privilege> {

    Privilege findByName(String name);
}
