package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan("org.example")
@SpringBootApplication
public class RestUtils {
    public static void main(String[] args) {
        SpringApplication.run(RestUtils.class, args);
    }
}