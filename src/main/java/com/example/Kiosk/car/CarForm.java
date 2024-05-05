package com.example.Kiosk.car;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarForm {
    @NotEmpty(message = "차량 앞 번호 입력은 필수항목입니다.")
    @Size(max = 20)
    private String firstNum;

    @NotEmpty(message = "차량 뒷 번호 입력은 필수항목입니다.")
    @Size(max = 20)
    private String lastNum;
}

