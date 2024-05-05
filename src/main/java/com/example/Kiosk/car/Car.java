package com.example.Kiosk.car;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TEXT")
    private String firstNum;

    @Column(columnDefinition = "TEXT")
    private String lastNum;

    private LocalDateTime inTime;

    private LocalDateTime outTime;
}
