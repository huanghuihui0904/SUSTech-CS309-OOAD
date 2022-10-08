package com.example.demo;

import com.example.demo.repository.GiftRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Demo2ApplicationTests {
    GiftRepository giftRepository;
    @Test
    void contextLoads() {
        giftRepository.findAll();
    }

}
