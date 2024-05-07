package com.example.Kiosk.product;

import com.example.Kiosk.category.Category;
import com.example.Kiosk.item.Item;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductForm {
    @NotEmpty(message = "제품명은 필수항목입니다.")
    @Size(max = 20)
    private String product;

    @NotEmpty(message = "가격은 필수항목입니다.")
    private String price;

    @NotEmpty(message = "이미지 경로는 필수항목입니다.")
    private String image;

    @NotNull
    private int categoryID;
}

