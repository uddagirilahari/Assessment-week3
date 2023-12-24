package com.accolite.TransactionManagement.controller;

import com.accolite.TransactionManagement.entity.Wallet;
import com.accolite.TransactionManagement.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallet")
public class WalletController{

    @Autowired
    private WalletService walletService;

    @GetMapping
    public List<Wallet> getAllWallets() {
        return walletService.getAllWallets();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{walletId}")
    public Wallet getWalletById(@PathVariable Long walletId) {
        return walletService.getWalletById(walletId);
    }

    @PostMapping
    public Wallet createWallet(@RequestBody Wallet wallet) {
        return walletService.createWallet(wallet);
    }

    @PutMapping("/{walletId}")
    public Wallet updateWallet(@PathVariable Long walletId, @RequestBody Wallet updatedWallet) {
        return walletService.updateWallet(walletId, updatedWallet);
    }

    @DeleteMapping("/{walletId}")
    public void deleteWallet(@PathVariable Long walletId) {
        walletService.deleteWallet(walletId);
    }
}
