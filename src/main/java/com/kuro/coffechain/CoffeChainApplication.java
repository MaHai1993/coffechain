package com.kuro.coffechain;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(title = "Coffee Chain API", version = "1.0", description = "API for Coffee Shop Management")
)
@SpringBootApplication
public class CoffeChainApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoffeChainApplication.class, args);
    }
}
