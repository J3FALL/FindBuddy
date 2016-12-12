package org.edu.service;

import org.edu.model.Privilege;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PrivilegeService {

    List<Privilege> getAllPrivileges();

    Privilege getPrivilegeById(long id);
}
