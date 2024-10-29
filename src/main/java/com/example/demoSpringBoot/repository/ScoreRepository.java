package com.example.demoSpringBoot.repository;

import com.example.demoSpringBoot.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, String> {
}
