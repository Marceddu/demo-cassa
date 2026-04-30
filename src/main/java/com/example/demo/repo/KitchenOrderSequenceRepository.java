package com.example.demo.repo;

import com.example.demo.model.KitchenOrderSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface KitchenOrderSequenceRepository extends JpaRepository<KitchenOrderSequence, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<KitchenOrderSequence> findByKitchenId(Long kitchenId);
}
