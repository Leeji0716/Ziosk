package com.example.Kiosk.category;

import com.example.Kiosk.product.Product;
import com.example.Kiosk.product.ProductForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/category")
@RequiredArgsConstructor
@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/create")
    public String createCategory(Model model, CategoryForm categoryForm) {
        List<Category> categoryList = this.categoryService.getList();
        model.addAttribute("categoryList", categoryList);
        return "category_form";
    }

    @PostMapping("/create")
    public String createCategory(Model model, @Valid CategoryForm categoryForm, BindingResult bindingResult) {
        List<Category> categoryList = this.categoryService.getList();
        model.addAttribute("categoryList", categoryList);

        if (bindingResult.hasErrors()){
            return "category_form";
        }

        for (Category category : categoryList){
            if (category.getName().equals(categoryForm.getName())){
                bindingResult.rejectValue("name", "category.error", "이미 존재합니다.");
                return "category_form";
            }
        }
        this.categoryService.create(categoryForm.getName());

        return "redirect:/category/create";
    }

    @GetMapping("/modify/{id}")
    public String modifyCategory(Model model, @PathVariable int id, CategoryForm categoryForm) {
        List<Category> categoryList = this.categoryService.getList();
        model.addAttribute("categoryList", categoryList);
        Category category = this.categoryService.getCategory(id);

        categoryForm.setName(category.getName());

        return "category_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String categoryModify(@Valid CategoryForm categoryForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id){
        if (bindingResult.hasErrors()) {
            return "category_form";
        }

        Category category = this.categoryService.getCategory(id);

        this.categoryService.modify(category, categoryForm.getName());
        return "redirect:/category/modify/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String categoryDelete(@PathVariable("id") Integer id) {
        Category category = this.categoryService.getCategory(id);
        this.categoryService.delete(category);
        return "redirect:/category/create";
    }
}
