package org.edu.controller;

import org.edu.converter.Converter;
import org.edu.model.Category;
import org.edu.model.dto.CategoryDto;
import org.edu.service.CategoryService;
import org.edu.service.UserService;
import org.edu.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ResponseEntity<GenericResponse> createCategory(@RequestBody CategoryDto categoryDto, Principal principal) {
        System.out.println(categoryDto);
        System.out.println(Converter.convert(categoryDto, Category.class));
        categoryService.createCategory(Converter.convert(categoryDto, Category.class), principal);
        return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return new ResponseEntity<>(Converter.convert(categoryService.getAllCategories(), CategoryDto.class), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<GenericResponse> updateCategory(@RequestBody CategoryDto categoryDto, Principal principal, @PathVariable("id") long id) {
        categoryDto.setId(id);
        categoryService.updateCategory(Converter.convert(categoryDto, Category.class), principal);
        return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<GenericResponse> deleteCategory(@PathVariable("id") long id, Principal principal) {
        boolean isSuccess = categoryService.removeCategory(id, principal);
        if (isSuccess)
            return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
        else return new ResponseEntity<>(new GenericResponse("Fail."), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public ResponseEntity<GenericResponse> subscribeUser(@RequestBody CategoryDto[] categoryDtos, Principal principal) {
        if (principal == null)
            return new ResponseEntity<>(new GenericResponse("Please login"), HttpStatus.BAD_REQUEST);
        List<CategoryDto> categories = new ArrayList<>(Arrays.asList(categoryDtos));
        categoryService.subscribeCategories(Converter.convert(categories, Category.class), principal);
        return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
    }

    @RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
    public ResponseEntity<GenericResponse> unsubscribeUser(@RequestBody CategoryDto[] categoryDtos, Principal principal) {
        if (principal == null)
            return new ResponseEntity<>(new GenericResponse("Please login"), HttpStatus.BAD_REQUEST);
        List<CategoryDto> categories = new ArrayList<>(Arrays.asList(categoryDtos));
        categoryService.unsubscribeCategories(Converter.convert(categories, Category.class), principal);
        return new ResponseEntity<>(new GenericResponse("Successful."), HttpStatus.OK);
    }
}
