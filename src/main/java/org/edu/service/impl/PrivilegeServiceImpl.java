package org.edu.service.impl;

import org.edu.dao.PrivilegeDao;
import org.edu.model.Privilege;
import org.edu.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    PrivilegeDao privilegeDao;

    @Override
    public List<Privilege> getAllPrivileges() {
        return privilegeDao.findAll();
    }

    @Override
    public Privilege getPrivilegeById(long id) {
        return  privilegeDao.findOne(id);
    }
}
