package com.example.demoSpringBoot.repository;

import com.example.demoSpringBoot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username); //Spring JPA will auto generate a query to check Username existing. No implementation needed.
}
