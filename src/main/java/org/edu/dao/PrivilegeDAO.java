package org.edu.dao;

import org.edu.dao.common.IOperations;
import org.edu.model.Privilege;

public interface PrivilegeDao extends IOperations<Privilege> {

//    hz che git tvorit
//    alo alo alo

    Privilege findByName(String name);

}
