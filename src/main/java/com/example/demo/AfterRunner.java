package com.example.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class AfterRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        TcpServer.start();
    }
}