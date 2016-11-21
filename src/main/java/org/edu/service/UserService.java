package org.edu.service;

import org.edu.model.Category;
import org.edu.model.Comment;
import org.edu.model.User;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Set;


@Service
public interface UserService {

    User getUserById(long id);

    User getUserByEmail(String email);

    User createUser(User newUser);

    boolean updateUser(User user, Principal principal);

    boolean deleteUser(long id, Principal principal);

    Set<Comment> getUserComments(long userID);

    Set<Category> getUserCategories(long userID);

    void subscribeCategories(List<Category> categories, Principal principal);
}
