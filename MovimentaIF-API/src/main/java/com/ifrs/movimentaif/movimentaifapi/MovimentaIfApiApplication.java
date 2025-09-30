package com.ifrs.movimentaif.movimentaifapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages = "com.ifrs.movimentaif.movimentaifapi")
public class MovimentaIfApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovimentaIfApiApplication.class, args);
    }

}
