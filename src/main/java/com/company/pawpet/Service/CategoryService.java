package com.company.pawpet.Service;

import com.company.pawpet.Model.Category;
import com.company.pawpet.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Category addNewPetCategory(Category category, String categoryName){
        category.setType("PET");
        category.setName(categoryName);
        return categoryRepository.save(category);
    }

    public Category addNewProductCategory(Category category, String categoryName) {
        category.setType("PRODUCT");  // Set the category type to PRODUCT
        category.setName(categoryName);  // Set the name of the category
        return categoryRepository.save(category);  // Save the category to the database
    }




    public Category updateCategory(int id, String categoryName) {
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isEmpty()) {
            throw new RuntimeException("Category not found");
        }

        Category categoryToUpdate = existingCategory.get();

        categoryToUpdate.setName(categoryName);

        return categoryRepository.save(categoryToUpdate);
    }


    public void deleteCategory(int CategoryId){
        categoryRepository.deleteById(CategoryId);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public List<Category> findPetCategory(){
        return categoryRepository.findByType("PET");
    }

    public List<Category> findProductCategory(){
        return categoryRepository.findByType("PRODUCT");
    }

    public Optional<Category> findById( int CategoryId){
        return categoryRepository.findById(CategoryId);
    }


}