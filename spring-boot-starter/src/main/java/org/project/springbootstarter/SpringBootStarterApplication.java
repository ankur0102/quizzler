package org.project.springbootstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class SpringBootStarterApplication {
    public static void main(String[] args) {
        System.out.println("Spring Boot Starter Application has started by Sharma");
        SpringApplication.run(SpringBootStarterApplication.class, args);
    }
}
