package com.project.futabuslines.services;

import com.project.futabuslines.dtos.CategoryDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.ResourceAlreadyExistsException;
import com.project.futabuslines.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO) throws ResourceAlreadyExistsException;
    List<Category> getAllCategory();
    Category getCategoryById(long id) throws DataNotFoundException;
    Category updateCategory(long id, CategoryDTO categoryDTO) throws ResourceAlreadyExistsException, DataNotFoundException;
    void deleteCategory(long id) throws DataNotFoundException;
}
