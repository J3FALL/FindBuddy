package org.edu.service.impl;

import org.edu.dao.CategoryDao;
import org.edu.model.Category;
import org.edu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Override
    public void createCategory(Category category, Principal principal) {
        categoryDao.create(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryDao.findOne(id);
    }

    @Override
    public boolean updateCategory(Category category, Principal principal) {
//        Category updatingCategory = categoryDao.findOne(category.getId());
        categoryDao.update(category);
        return true;
    }

    @Override
    public boolean removeCategory(long id, Principal principal) {
        categoryDao.deleteById(id);
        return true;
    }
}
