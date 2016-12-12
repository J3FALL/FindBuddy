package org.edu.service;


import org.edu.model.Category;

import java.security.Principal;
import java.util.List;

public interface CategoryService {

    void createCategory(Category category, Principal principal);

    List<Category> getAllCategories();

    Category getCategoryById(long id);

    boolean updateCategory(Category category, Principal principal);

    boolean removeCategory(long id, Principal principal);

    void subscribeCategories(List<Category> categories, Principal principal);

    void unsubscribeCategories(List<Category> categories, Principal principal);
}
