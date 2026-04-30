package com.example.demo.repo;

import com.example.demo.model.CustomerOrderV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderV2Repository extends JpaRepository<CustomerOrderV2, Long> {}
