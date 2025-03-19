package com.example.flattie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class containing the entry point for the application within the main method.
 */
@SpringBootApplication
public class FlattieApplication {

    /**
     * Main method/entry point for the application. Hosts the web application on
     * port 8080 locally.
     * 
     * @param args Command line arguments to pass to the application, not necessary.
     */
    public static void main(String[] args) {
        SpringApplication.run(FlattieApplication.class, args);
        System.out.println("Web application is running locally at http://localhost:8080");
        System.out.println("H2 console is running locally at http://localhost:8080/h2-console");
    }
}
