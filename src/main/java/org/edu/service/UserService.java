package org.edu.service;

import org.edu.error.UserAlreadyExist;
import org.edu.model.Category;
import org.edu.model.Comment;
import org.edu.model.Meeting;
import org.edu.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Set;


@Service
public interface UserService {

    User getUserById(long id);

    User getUserByEmail(String email);

    User createUser(User newUser) throws UserAlreadyExist;

    boolean updateUser(User user, Principal principal);

    boolean deleteUser(long id, Principal principal);

    Set<Comment> getUserComments(long userID);

    Set<Category> getUserCategories(long userID);

    Set<Meeting> getMeetingsByCategories(Principal principal);

    String uploadPhoto(MultipartFile photo, Principal principal);

    boolean deletePhoto(Principal principal);

    List<Meeting> getSubscriptions(long userId, int pageNum, int num);

    Long getSubscriptionsNum(long userId, int pageNum, int num);
}
