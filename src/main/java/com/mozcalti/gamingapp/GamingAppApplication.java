package com.mozcalti.gamingapp;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class GamingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamingAppApplication.class, args);
    }
}
