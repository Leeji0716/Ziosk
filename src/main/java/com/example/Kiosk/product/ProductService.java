package com.example.Kiosk.product;

import com.example.Kiosk.category.Category;
import com.example.Kiosk.category.CategoryRepository;
import com.example.Kiosk.domain.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> getList(){
        List<Product> productList = productRepository.findAll();
        return productList;
    }

    public List<Product> getCategoryList(int id){
        Category category = categoryRepository.findById(id);
        List<Product> productList = productRepository.findByCategory(category);
        return productList;
    }

    public String create(String name, String price, String image, Category category){
        Product product = new Product();
        product.setProductName(name);
        product.setPrice(Integer.parseInt(price));
        product.setImage(image);
        product.setCategory(category);

        productRepository.save(product);
        return "redirect:/product/list";
    }

    public Product getProduct(Integer id) {
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new DataNotFoundException("product not found");
        }
    }

    public void modify(Product product, String productName, String price, String image,  Category category) {
        product.setProductName(productName);
        product.setPrice(Integer.parseInt(price));
        product.setImage(image);
        product.setCategory(category);
        this.productRepository.save(product);
    }

    public void delete(Product product){
        this.productRepository.delete(product);
    }

}
