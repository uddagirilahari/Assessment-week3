//User repository

package com.accolite.TransactionManagement.repository;

import com.accolite.TransactionManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}

