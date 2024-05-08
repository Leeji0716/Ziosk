package com.example.Kiosk.item;

import com.example.Kiosk.product.Product;
import com.example.Kiosk.product.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/item")
@RequiredArgsConstructor
@Controller
public class ItemController {
    private final ProductService productService;
    private final ItemService itemService;
    @GetMapping("/create/{id}")
    public String createItem(ItemForm itemForm, @PathVariable("id") int id, Model model){
        Product product = this.productService.getProduct(id);
        model.addAttribute("product", product);
        return "product_detail";
    }

    @PostMapping("/create/{id}")
    public String createItem(@Valid ItemForm itemForm, BindingResult bindingResult, @PathVariable("id") int id, Model model){
        Product product = this.productService.getProduct(id);
        model.addAttribute("product", product);

        // 유효성 검사 및 필드 값이 비어 있는지 여부를 동시에 검사
        if (bindingResult.hasErrors() || itemForm.getContent() == null || itemForm.getItemPrice() == null) {
            if (itemForm.getContent() == null || itemForm.getContent().isEmpty()) {
                bindingResult.rejectValue("content", "content.error", "내용을 입력하세요");
            }
            if (itemForm.getItemPrice() == null || itemForm.getItemPrice().isEmpty()) {
                bindingResult.rejectValue("itemPrice", "itemPrice.error", "가격을 입력하세요");
            }
            return "redirect:/product/modify/" + product.getId();
        }

        this.itemService.create(itemForm.getContent(), itemForm.getItemPrice(), product);
        return "redirect:/product/modify/" + product.getId();
    }

    @GetMapping("/delete/{itemId}")
    public String deleteItem(@PathVariable("itemId") int itemId){
        Item item = this.itemService.getItem(itemId);
        Product product = item.getProduct();
        this.itemService.delete(item);
        return "redirect:/product/modify/" + product.getId();
    }

    @GetMapping("/plus/{id}")
    public String plusQuantity(@PathVariable("id") Integer id){
        Item item = this.itemService.getItem(id);

        item.setQuantity(item.getQuantity() + 1);
        this.itemService.updateItem(item);
        return "redirect:/product/detail/" + item.getProduct().getId();
    }

    @GetMapping("/minus/{id}")
    public String minusQuantity(@PathVariable("id") Integer id){
        Item item = this.itemService.getItem(id);

        item.setQuantity(item.getQuantity() - 1);
        if (item.getQuantity() < 0){
            item.setQuantity(0);
        }
        this.itemService.updateItem(item);
        return "redirect:/product/detail/" + item.getProduct().getId();
    }

    @GetMapping("/select/{id}")
    public String selectItem(@PathVariable("id") Integer id, @RequestParam(value = "totalPrice", defaultValue = "") String totalPrice){
        Product product = this.productService.getProduct(id);

        int total = Integer.parseInt(totalPrice);

        if (total > 0){
            product.setTotal(total);
        }
        this.productService.updateProduct(product);
        return "redirect:/product/select/" + product.getId();
    }

}
