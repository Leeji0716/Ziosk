package com.example.Kiosk.item;

import com.example.Kiosk.category.Category;
import com.example.Kiosk.domain.DataNotFoundException;
import com.example.Kiosk.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    public String create(String content, String price, Product product){
        Item item = new Item();
        item.setContent(content);
        item.setPrice(Integer.parseInt(price));
        item.setProduct(product);

        itemRepository.save(item);
        return "redirect:/product/list";
    }

    public List<Item> getList(Product product){
        return itemRepository.findByProduct(product);
    }

    public Item getItem(int id){
        Optional<Item> item = this.itemRepository.findById(id);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new DataNotFoundException("item not found");
        }
    }

    public void delete(Item item){
        this.itemRepository.delete(item);
    }
}
