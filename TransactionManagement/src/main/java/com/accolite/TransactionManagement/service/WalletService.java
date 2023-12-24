package com.accolite.TransactionManagement.service;

import com.accolite.TransactionManagement.entity.Wallet;

import java.util.List;

public interface WalletService {
    List<Wallet> getAllWallets();
    Wallet getWalletById(Long walletId);
    Wallet createWallet(Wallet wallet);
    void deleteWallet(Long walletId);
    Wallet updateWallet(Long walletId, Wallet updatedWallet);
}

