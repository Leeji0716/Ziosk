package com.example.Kiosk.item;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemForm {
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

    @NotEmpty(message = "가격은 필수항목입니다.")
    private String itemPrice;
}
