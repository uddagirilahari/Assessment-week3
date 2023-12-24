package com.accolite.TransactionManagement;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.accolite.TransactionManagement")
public class TransactionManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionManagementApplication.class, args);
	}

}
