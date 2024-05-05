package com.example.Kiosk;

import com.example.Kiosk.category.Category;
import com.example.Kiosk.category.CategoryRepository;
import com.example.Kiosk.product.Product;
import com.example.Kiosk.product.ProductRepository;
import com.example.Kiosk.user.SiteUser;
import com.example.Kiosk.user.UserRepository;
import com.example.Kiosk.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class KioskApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private UserService userService;

	@Test
	void testUser(){
		String username = "dlwldud";
		String Password = "dlwldud0716";
		userService.create(username, Password);
	}

	@Autowired
	private ProductRepository productRepository;

	@Test
	void createList(){
		List<Product> selectList = new ArrayList<>();
		List<Product> productList = productRepository.findAll();
		selectList.add(productList.get(0));
	}

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	void test5(){
		Category category = new Category();
//		category.setId(1);
		category.setName("음료");
		this.categoryRepository.save(category);
	}

	@Test
	void test6(){
		Category category = categoryRepository.findById(3);
		List<Product> test = productRepository.findByCategory(category);
		for (Product product : test) {
			System.out.println(product.getId());
		}
	}
}
