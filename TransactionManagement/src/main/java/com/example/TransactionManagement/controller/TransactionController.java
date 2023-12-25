package com.example.TransactionManagement.controller;


import com.example.TransactionManagement.*;
import com.example.TransactionManagement.repository.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/make-payment-online")
    public ResponseEntity<String> makePayment(@RequestBody PaymentRequestOnline paymentRequest) {
        // Implement payment logic
        User user = userRepository.findById(paymentRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Vendor vendor = vendorRepository.findById(paymentRequest.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // Check if the payment is within 20km radius
        if (!isWithinRadius(paymentRequest.getLatitude(), paymentRequest.getLongitude(), vendor.getLatitude(), vendor.getLongitude(), 20)) {
            Transaction transaction = new Transaction();
            transaction.setUserId(paymentRequest.getUserId());
            transaction.setVendorId(paymentRequest.getVendorId());
            transaction.setAmount(paymentRequest.getAmount());
            transaction.setStatus(TransactionStatus.FLAGGED);
            transaction.setPaymentMode(PaymentMode.ONLINE);
            transaction.setTransactionDate(new Date());


            // Update user's wallet balance
            Wallet userWallet = user.getWallet();
            userWallet.setBalance(userWallet.getBalance() - paymentRequest.getAmount());
            walletRepository.save(userWallet);
//
//            // Update vendor's wallet balance
//            Wallet vendorWallet = vendor.getStoreWallet();
//            vendorWallet.setBalance(vendorWallet.getBalance() + paymentRequest.getAmount());
//            walletRepository.save(vendorWallet);

            // Save the transaction
            transactionRepository.save(transaction);

            return ResponseEntity.ok("Payment flagged. Payment made from > 20 km");
        } else {
            Transaction transaction = new Transaction();
            transaction.setUserId(paymentRequest.getUserId());
            transaction.setVendorId(paymentRequest.getVendorId());
            transaction.setAmount(paymentRequest.getAmount());
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transaction.setPaymentMode(PaymentMode.ONLINE);
            transaction.setTransactionDate(new Date());


            // Update user's wallet balance
            Wallet userWallet = user.getWallet();
            userWallet.setBalance(userWallet.getBalance() - paymentRequest.getAmount());
            walletRepository.save(userWallet);

            // Update vendor's wallet balance
            Wallet vendorWallet = vendor.getStoreWallet();
            vendorWallet.setBalance(vendorWallet.getBalance() + paymentRequest.getAmount());
            walletRepository.save(vendorWallet);

            // Save the transaction
            transactionRepository.save(transaction);

            return ResponseEntity.ok("Payment successful.");
        }
    }



    @PostMapping("/make-payment-offline")
    public ResponseEntity<String> makePaymentOffline(@RequestBody PaymentRequestOffline paymentRequestOffline) {
        // Implement offline payment logic
        User user = userRepository.findById(paymentRequestOffline.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the provided code matches any of the codes in the user's set
        if (!user.getWallet().getCodes().contains(paymentRequestOffline.getCode())) {
            return ResponseEntity.badRequest().body("Invalid code. Transaction failed.");
        }

        Vendor vendor = vendorRepository.findById(paymentRequestOffline.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        if (!isWithinRadius(paymentRequestOffline.getLatitude(), paymentRequestOffline.getLongitude(), vendor.getLatitude(), vendor.getLongitude(), 20)) {

            Transaction transaction = new Transaction();
            transaction.setUserId(paymentRequestOffline.getUserId());
            transaction.setVendorId(paymentRequestOffline.getVendorId());
            transaction.setAmount(paymentRequestOffline.getAmount());
            transaction.setStatus(TransactionStatus.FLAGGED);
            transaction.setPaymentMode(PaymentMode.OFFLINE);
            transaction.setTransactionDate(new Date());

            // Update user's wallet balance
            Wallet userWallet = user.getWallet();
            userWallet.setOfflineBalance(userWallet.getOfflineBalance() - paymentRequestOffline.getAmount());
            walletRepository.save(userWallet);

            // Update vendor's wallet balance
//            Wallet vendorWallet = vendor.getPersonalWallet();
//            vendorWallet.setBalance(vendorWallet.getBalance() + paymentRequestOffline.getAmount());
//            walletRepository.save(vendorWallet);

            // Save the transaction
            transactionRepository.save(transaction);

            return ResponseEntity.ok("payment flagged. distance > 20km");

        } else {

            Transaction transaction = new Transaction();
            transaction.setUserId(paymentRequestOffline.getUserId());
            transaction.setVendorId(paymentRequestOffline.getVendorId());
            transaction.setAmount(paymentRequestOffline.getAmount());
            transaction.setStatus(TransactionStatus.SUCCESSFUL);
            transaction.setPaymentMode(PaymentMode.OFFLINE);
            transaction.setTransactionDate(new Date());

            // Update user's wallet balance
            Wallet userWallet = user.getWallet();
            userWallet.setOfflineBalance(userWallet.getOfflineBalance() - paymentRequestOffline.getAmount());
            walletRepository.save(userWallet);

            // Update vendor's wallet balance
            Wallet vendorWallet = vendor.getStoreWallet();
            vendorWallet.setBalance(vendorWallet.getBalance() + paymentRequestOffline.getAmount());
            walletRepository.save(vendorWallet);

            // Save the transaction
            transactionRepository.save(transaction);

            return ResponseEntity.ok("Offline payment successful.");
        }
    }

    @GetMapping("/flagged-transactions")
    public ResponseEntity<List<Transaction>> getFlaggedTransactions() {
        // Retrieve flagged transactions for admin review
        List<Transaction> flaggedTransactions = transactionRepository.findByStatus(TransactionStatus.FLAGGED).get();
        return ResponseEntity.ok(flaggedTransactions);
    }

    @PostMapping("/review-transaction/{adminId}/{transactionId}/{approval}")
    public ResponseEntity<String> reviewTransaction(
            @PathVariable Long adminId,
            @PathVariable Long transactionId,
            @PathVariable Boolean approval) {
        // Implement logic for admin review
        Admin admin = adminRepository.findById(Math.toIntExact(adminId))
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() == TransactionStatus.FLAGGED) {
            if (approval) {
                // Approve transaction
                transferAmountToVendor(transaction);
            } else {
                // Reject transaction
                returnAmountToUser(transaction);
            }
        }

        return ResponseEntity.ok("Transaction reviewed successfully.");
    }


    // Helper method to check if a location is within a certain radius
    private boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2, double radius) {
        double earthRadius = 6371; // in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = earthRadius * c;

        return distance <= radius;
    }

    // Helper method to transfer amount to the vendor's wallet
    private void transferAmountToVendor(Transaction transaction) {
        Vendor vendor = vendorRepository.findById(transaction.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // Transfer amount to vendor's wallet
        Wallet vendorWallet = vendor.getStoreWallet();
        vendorWallet.setBalance(vendorWallet.getBalance() + transaction.getAmount());

        // Save the updated vendor's wallet
        walletRepository.save(vendorWallet);

        // Update the transaction status to SUCCESSFUL
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository.save(transaction);
    }

    // Helper method to return amount to the user's wallet
    private void returnAmountToUser(Transaction transaction) {
        User user = userRepository.findById(transaction.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Return amount to user's wallet
        Wallet userWallet = user.getWallet();
        userWallet.setBalance(userWallet.getBalance() + transaction.getAmount());

        // Save the updated user's wallet
        walletRepository.save(userWallet);

        // Update the transaction status to FAILED
        transaction.setStatus(TransactionStatus.FAILED);
        transactionRepository.save(transaction);
    }

    @Data
    static class PaymentRequestOnline {
        private Integer userId;
        private Integer vendorId;
        private Double amount;
        private Double latitude;

        private Double longitude;

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getVendorId() {
            return vendorId;
        }

        public void setVendorId(Integer vendorId) {
            this.vendorId = vendorId;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

    }

    @Data
    static class PaymentRequestOffline {
        private Integer userId;
        private Integer vendorId;
        private Double amount;
        private Double latitude;

        private Double longitude;

        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getVendorId() {
            return vendorId;
        }

        public void setVendorId(Integer vendorId) {
            this.vendorId = vendorId;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

    }
}
