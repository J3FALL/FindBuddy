package org.edu.service;

import org.edu.model.User;
import org.edu.model.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User getUser(String email);

    User registerNewUser(UserDTO userDTO);

}
