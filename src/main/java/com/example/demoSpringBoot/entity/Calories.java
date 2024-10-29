package com.example.demoSpringBoot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Calories {
    private String mealTime;
    private int calories;
}