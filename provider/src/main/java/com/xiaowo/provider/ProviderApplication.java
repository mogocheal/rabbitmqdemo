package com.xiaowo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@EnableScheduling
public class ProviderApplication  {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
