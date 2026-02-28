package com.example.demo.repo;

import com.example.demo.model.StatsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatsSnapshotRepository extends JpaRepository<StatsSnapshot, Long> {
    Optional<StatsSnapshot> findTopByOrderByCreatedAtDesc();
}
