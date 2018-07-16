package com.xiaowo.provider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ThreadService implements ApplicationRunner {

    @Autowired
    private RabbitmqCacheService rabbitmqCacheService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("ThreadService");
        rabbitmqCacheService.start();
    }
}
