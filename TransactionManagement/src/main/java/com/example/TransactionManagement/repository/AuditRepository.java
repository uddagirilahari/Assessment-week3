package com.example.TransactionManagement.repository;

import com.example.TransactionManagement.AuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditEntry, Long> {
}
