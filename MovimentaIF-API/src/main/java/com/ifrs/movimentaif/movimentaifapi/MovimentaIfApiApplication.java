package com.ifrs.movimentaif.movimentaifapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ifrs.movimentaif.movimentaifapi")
public class MovimentaIfApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovimentaIfApiApplication.class, args);
    }

}
