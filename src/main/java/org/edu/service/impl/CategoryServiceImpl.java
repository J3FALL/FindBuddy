package org.edu.service.impl;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.edu.dao.CategoryDao;
import org.edu.dao.UserDao;
import org.edu.model.Category;
import org.edu.model.User;
import org.edu.service.CategoryService;
import org.edu.util.NullAwareBeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    UserDao userDao;

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
        Category checkingCategory = categoryDao.findOne(category.getId());
        if (checkingCategory != null) {
            BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
            try {
                notNull.copyProperties(checkingCategory, category);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return false;
            }
            categoryDao.update(checkingCategory);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeCategory(long id, Principal principal) {
        try {
            categoryDao.deleteById(id);
        }
        catch (IllegalStateException e) {
            return false;
        }
        return true;
    }

    @Override
    public void subscribeCategories(List<Category> categories, Principal principal){
        User user = userDao.findByEmail(principal.getName());
        for (Category category : categories) {
            Category updatedCategory = categoryDao.findOne(category.getId());
            updatedCategory.addUser(user);
        }
    }

    @Override
    public void unsubscribeCategories(List<Category> categories, Principal principal) {
        User user = userDao.findByEmail(principal.getName());
        for (Category category:categories) {
            Category updateCategory = categoryDao.findOne(category.getId());
            updateCategory.deleteUser(user);
        }
    }
}
