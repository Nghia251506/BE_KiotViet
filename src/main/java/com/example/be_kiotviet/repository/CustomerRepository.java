package com.example.be_kiotviet.repository;

import com.example.be_kiotviet.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
