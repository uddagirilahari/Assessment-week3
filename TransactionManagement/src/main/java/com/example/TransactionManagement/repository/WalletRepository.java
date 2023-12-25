package com.example.TransactionManagement.repository;

import com.example.TransactionManagement.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
