package com.example.Kiosk.product;

import com.example.Kiosk.category.Category;
import com.example.Kiosk.category.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/product")
@RequiredArgsConstructor
@Controller
@SessionAttributes("selectList")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;


    @GetMapping("/list")
    public String list(Model model, HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") int categoryId){
        List<Product> selectList = (List<Product>) session.getAttribute("selectList");
        List<Category> categoryList = categoryService.getList();
        List<Product> productList = new ArrayList<>();

        if (categoryId == 0){
            productList = productService.getList();
            if (productList.isEmpty()){
                Category category = this.categoryService.getCategory(1);
                productService.create("아이스 아메리카노", "2000", "https://i.imgur.com/vXFaLmp.png", category);
                return "redirect:/product/list";
            }
        }else {
            for (Category category : categoryList) {
                if (categoryId == category.getId()){
                    productList = productService.getCategoryList(categoryId);
                    break;
                }
            }
        }

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productList", productList);
        model.addAttribute("categoryId", categoryId);

        return "product_list";
    }

    @GetMapping("/create")
    public String ProductCreate(ProductForm productForm, Model model){
        List<Category> categoryList = this.categoryService.getList(); // 모든 카테고리를 가져옴
        model.addAttribute("categoryList", categoryList); // 카테고리 목록을 모델에 추가
        return "product_form";
    }

    @PostMapping("/create")
    public String ProductCreate(@Valid ProductForm productForm, BindingResult bindingResult){ //폼 바인딩
        if (bindingResult.hasErrors()){
            return "product_form";
        }

        Category category = this.categoryService.getCategory(productForm.getCategoryID());

        if (category == null) {
            // 해당 ID에 해당하는 카테고리가 없는 경우
            bindingResult.rejectValue("categoryID", "category.error", "유효하지 않은 카테고리입니다.");
            return "product_form";
        }

        this.productService.create(productForm.getProduct(), productForm.getPrice(), productForm.getImage(), category);
        return "redirect:/product/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String productModify(ProductForm productForm, @PathVariable("id") Integer id, Model model){
        // 제품 정보 가져오기
        Product product = productService.getProduct(id);

        // 모든 카테고리 가져오기
        List<Category> categoryList = categoryService.getList();

        // 제품의 카테고리 ID 가져오기
        int categoryId = product.getCategory().getId();

        // 모델에 추가
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("categoryId", categoryId);

        productForm.setProduct(product.getProductName());
        productForm.setPrice(String.valueOf(product.getPrice()));
        productForm.setImage(product.getImage());
        productForm.setCategoryID(categoryId);

        return "product_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String productModify(@Valid ProductForm productForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id){
        if (bindingResult.hasErrors()) {
            return "product_form";
        }

        Product product = this.productService.getProduct(id);

        int categoryId = productForm.getCategoryID();

        if (categoryId <= 0) {
            // 카테고리를 선택하지 않은 경우
            bindingResult.rejectValue("categoryID", "category.error", "카테고리를 선택해주세요.");
            return "product_form";
        }

        Category category = this.categoryService.getCategory(categoryId);

        this.productService.modify(product, productForm.getProduct(), productForm.getPrice(), productForm.getImage(), category);
        return "redirect:/product/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String productDelete(@PathVariable("id") Integer id) {
        Product product = this.productService.getProduct(id);
        this.productService.delete(product);
        return "redirect:/product/list";
    }

    @GetMapping("/select/{id}")
    public String selectProduct(@PathVariable("id") Integer id, HttpSession session,
                                @RequestParam(value = "cnt", defaultValue = "plus") String cnt){
        List<Product> selectList = (List<Product>) session.getAttribute("selectList");

        if (selectList == null) {
            // 세션에 "selectList" 속성이 없으면 새로운 리스트 생성
            selectList = new ArrayList<>();
        }
        Product selectedProduct = this.productService.getProduct(id);

        boolean productExists = false;

        for (Product product : selectList) {
            if (product.getId() == selectedProduct.getId()&&cnt.equals("plus")) {
                product.setQuantity(product.getQuantity() + 1);
                productExists = true;
                break;
            } else if (product.getId() == selectedProduct.getId()&&cnt.equals("minus")) {
                product.setQuantity(product.getQuantity() - 1);
                if (product.getQuantity() == 0){
                    selectList.remove(product);
                }
                return String.format("redirect:/product/list#product_%s", product.getId());
            }
        }

        // selectList에 선택된 제품이 없는 경우 새로 추가
        if (!productExists) {
            selectedProduct.setQuantity(1); // 새로 추가된 제품의 수량은 1로 설정
            selectList.add(selectedProduct);
        }

        session.setAttribute("selectList", selectList);
        return String.format("redirect:/product/list#product_%s", selectedProduct.getId());
    }

    @GetMapping("/reset")
    public String cartReset(HttpSession session){
        List<Object> selectList = (List<Object>) session.getAttribute("selectList");
        if (selectList != null && !selectList.isEmpty()){
            selectList.clear(); // 리스트 내의 모든 항목을 지움
        }

        return "redirect:/product/list";
    }

    @GetMapping("/success")
    public String successPay(){
        return "success_form";
    }

}
