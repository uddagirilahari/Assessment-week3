package com.accolite.TransactionManagement.service;

import com.accolite.TransactionManagement.entity.Wallet;
import com.accolite.TransactionManagement.exception.WalletNotFound;
import com.accolite.TransactionManagement.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    @Override
    public Wallet getWalletById(Long walletId) {
        Optional<Wallet> walletOpt = walletRepository.findById(walletId);
        if (walletOpt.isPresent()) {
            return walletOpt.get();
        }
        throw new WalletNotFound("Wallet not found");
    }

    @Override
    public Wallet createWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public void deleteWallet(Long walletId) {
        Optional<Wallet> walletOpt = walletRepository.findById(walletId);
        if (walletOpt.isPresent()) {
            walletRepository.deleteById(walletId);
        } else {
            throw new WalletNotFound("Wallet not found");
        }
    }

    @Override
    public Wallet updateWallet(Long walletId, Wallet updatedWallet) {
        Optional<Wallet> walletOpt = walletRepository.findById(walletId);
        if (walletOpt.isPresent()) {
            Wallet existingWallet = walletOpt.get();
            existingWallet.setBalance(updatedWallet.getBalance());
            return walletRepository.save(existingWallet);
        }
        throw new WalletNotFound("Wallet not found");
    }
}
