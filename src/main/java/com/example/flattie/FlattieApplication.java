package com.example.flattie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlattieApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlattieApplication.class, args);
        System.out.println("Web application is running locally at http://localhost:8080");
    }
}
