package com.example.Kiosk.product;

import com.example.Kiosk.category.Category;
import com.example.Kiosk.category.CategoryService;
import com.example.Kiosk.item.Item;
import com.example.Kiosk.item.ItemForm;
import com.example.Kiosk.item.ItemService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/product")
@RequiredArgsConstructor
@Controller
@SessionAttributes("selectList")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ItemService itemService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "categoryId", defaultValue = "0") int categoryId){
        List<Category> categoryList = categoryService.getList();
        List<Product> productList = new ArrayList<>();

        for (Category category : categoryList){
            if (categoryId == 0){
                productList.addAll(category.getProductList());
            }else {
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
    public String ProductCreate(ProductForm productForm, ItemForm itemForm, Model model){
        List<Category> categoryList = this.categoryService.getList(); // 모든 카테고리를 가져옴
        model.addAttribute("categoryList", categoryList); // 카테고리 목록을 모델에 추가
        return "product_form";
    }

    @PostMapping("/create")
    public String ProductCreate(@Valid ProductForm productForm, BindingResult bindingResult, Model model){ //폼 바인딩
        List<Category> categoryList = this.categoryService.getList(); // 모든 카테고리를 가져옴
        model.addAttribute("categoryList", categoryList); // 카테고리 목록을 모델에 추가
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
    public String productModify(ProductForm productForm, ItemForm itemForm, @PathVariable("id") Integer id, Model model){
        Product product = productService.getProduct(id);
        List<Category> categoryList = categoryService.getList();

        int categoryId = product.getCategory().getId();

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("categoryId", categoryId);

        List<Item> itemList = this.itemService.getList(product);
        model.addAttribute("itemList", itemList);

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

    @GetMapping("/detail/{id}")
    public String detailProduct(@PathVariable("id") Integer id, Model model){
        Product product = this.productService.getProduct(id);

        model.addAttribute("product", product);
        model.addAttribute("itemList", product.getItemList());

        return "product_detail";
    }

    @GetMapping("/reset")
    public String cartReset(HttpSession session){
        List<Object> selectList = (List<Object>) session.getAttribute("selectList");
        if (selectList != null && !selectList.isEmpty()){
            selectList.clear(); // 리스트 내의 모든 항목을 지움
        }
//        List<Product> productList = productService.getList();
//
//        for (Product product : productList){
//            List<Item> itemList = itemService.getList(product);
//            for (Item item : itemList){
//                item.setQuantity(0);
//                this.itemService.updateItem(item);
//            }
//            product.setTotal(0);
//            this.productService.updateProduct(product);
//        }

        return "redirect:/product/list";
    }

    @GetMapping("/success")
    public String successPay(){
        return "success_form";
    }

    @GetMapping("/getProduct")
    public ResponseEntity<Product> getProductById(@RequestParam String productId) {
        int id = Integer.parseInt(productId);
        Product product = productService.getProduct(id);
        return ResponseEntity.ok().body(product);
    }
}
