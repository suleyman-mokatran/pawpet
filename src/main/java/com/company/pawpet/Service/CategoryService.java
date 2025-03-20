package com.company.pawpet.Service;

import com.company.pawpet.Model.Category;
import com.company.pawpet.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Category addNewPetCategory(Category category){

        Category newCategory = new Category();
        newCategory.setType("PET");
       newCategory.setMSCategory(category.getMSCategory());

        return categoryRepository.save(newCategory);
    }

    public Category addNewProductCategory(Category category, String categoryName) {
        category.setType("PRODUCT");  // Set the category type to PRODUCT
        //category.setName(categoryName);  // Set the name of the category
        return categoryRepository.save(category);  // Save the category to the database
    }




    public Category updateCategory(int id, String categoryName) {
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isEmpty()) {
            throw new RuntimeException("Category not found");
        }

        Category categoryToUpdate = existingCategory.get();

       // categoryToUpdate.setName(categoryName);

        return categoryRepository.save(categoryToUpdate);
    }


    public void deleteCategory(int CategoryId){
        categoryRepository.deleteById(CategoryId);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public List<Map<String,String>> findPetCategory(){
        return categoryRepository.findAllPetCategories();
    }


    public Optional<Category> findById( int CategoryId){
        return categoryRepository.findById(CategoryId);
    }


}