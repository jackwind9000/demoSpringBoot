package com.example.demoSpringBoot.service;

import com.example.demoSpringBoot.entity.Sale;
import com.example.demoSpringBoot.repository.SaleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SaleService {
    private SaleRepository saleRepository;

    public Sale createSale(Sale sale) {
        return saleRepository.save(sale);
    }

    public List<Sale> getSales() {
        return saleRepository.findAll();
    }
}
