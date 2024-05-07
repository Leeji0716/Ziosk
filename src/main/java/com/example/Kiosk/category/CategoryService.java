package com.example.Kiosk.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category getCategory(int id) {
        Category category = this.categoryRepository.findById(id);
        return category;
    }

    public List<Category> getList(){
        List<Category> categoryList = this.categoryRepository.findAll();
        return categoryList;
    }

    public void create(String name){
        Category category = new Category();
        category.setName(name);
        this.categoryRepository.save(category);
    }

    public void modify(Category category, String name){
        category.setName(name);
        this.categoryRepository.save(category);
    }

    public void delete(Category category){
        this.categoryRepository.delete(category);
    }
}
