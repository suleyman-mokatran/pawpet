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

    public Category addNewProductCategory(Category category) {

        Category newCategory = new Category();
        category.setType("PRODUCT");
        newCategory.setMSCategory(category.getMSCategory());

        return categoryRepository.save(category);
    }




    public Category updateCategory(int id, Category category) {
        Optional<Category> existingCategory = categoryRepository.findById(id);

        if (existingCategory.isEmpty()) {
            throw new RuntimeException("Category not found");
        }

        Category categoryToUpdate = existingCategory.get();
        categoryToUpdate.setMSCategory(category.getMSCategory());

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

    public List<Map<String,String>> findProductCategory(){
        return categoryRepository.findAllProductCategories();
    }


    public Category findById( int CategoryId){
        return categoryRepository.findById(CategoryId).orElseThrow();
    }


}