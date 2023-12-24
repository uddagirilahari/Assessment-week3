package com.accolite.TransactionManagement.entity;

import javax.persistence.*;

@Entity
@Table(name = "Wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long walletId;

    @Column(name = "balance")
    private double balance;

    // Constructors, getters, and setters

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

