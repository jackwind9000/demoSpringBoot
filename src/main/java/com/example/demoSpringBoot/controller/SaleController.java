package com.example.demoSpringBoot.controller;

import com.example.demoSpringBoot.entity.Sale;
import com.example.demoSpringBoot.service.SaleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor // No need @Autowire. Check in target\...\controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Make fields private final
public class SaleController {
    private SaleService saleService;

    @PostMapping
    Sale createSale(@RequestBody Sale sale) {
        return saleService.createSale(sale);
    }

    @GetMapping
    List<Sale> getSales() {
        return saleService.getSales();
    }
}
