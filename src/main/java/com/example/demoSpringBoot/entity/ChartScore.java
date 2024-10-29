package com.example.demoSpringBoot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChartScore {
    private String chartName;
    private int chartScore;
}
