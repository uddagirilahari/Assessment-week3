package com.example.TransactionManagement.repository;

import com.example.TransactionManagement.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<List<Transaction>> findByStatus(TransactionStatus transactionStatus);
}
