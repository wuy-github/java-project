package com.project.futabuslines.services;

import com.project.futabuslines.dtos.CategoryDTO;
import com.project.futabuslines.exceptions.DataNotFoundException;
import com.project.futabuslines.exceptions.ResourceAlreadyExistsException;
import com.project.futabuslines.models.Category;
import com.project.futabuslines.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    // Danh muc chi co admin co the chinh sua

    @Override
    // Ham tao moi Category
    public Category createCategory(CategoryDTO categoryDTO) throws ResourceAlreadyExistsException {
        // Khong cho phep nhap trung ten category
        boolean exists = categoryRepository.existsByNameIgnoreCase(categoryDTO.getName());
        if (exists) {
            throw new ResourceAlreadyExistsException("Category with name '" + categoryDTO.getName() + "' already exists.");
        }
        Category newCategory = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    // Lay toan bo danh sach Category
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    // Lay Category theo id
    public Category getCategoryById(long id) throws DataNotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Category's Id cannot found or not empty"));
    }

    @Override
    // Cap nhat Category
    public Category updateCategory(long id, CategoryDTO categoryDTO) throws ResourceAlreadyExistsException, DataNotFoundException {
        // Khong cho phep nhap trung ten category
        boolean exists = categoryRepository.existsByNameIgnoreCase(categoryDTO.getName());
        if (exists) {
            throw new ResourceAlreadyExistsException("Category with name '" + categoryDTO.getName() + "' already exists.");
        }
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(categoryDTO.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    // Xoa Category
    public void deleteCategory(long id) throws DataNotFoundException {
        Category existingCategory = categoryRepository.findById(id)
                        .orElseThrow(()->new DataNotFoundException(("Category's Id cannot found or not empty")));
        categoryRepository.deleteById(id);
    }
}
