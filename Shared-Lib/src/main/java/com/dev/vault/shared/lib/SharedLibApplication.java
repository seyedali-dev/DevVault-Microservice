package com.dev.vault.shared.lib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SharedLibApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedLibApplication.class, args);
    }

}
