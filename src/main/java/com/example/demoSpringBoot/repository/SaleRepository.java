package com.example.demoSpringBoot.repository;

import com.example.demoSpringBoot.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, String> {
}
