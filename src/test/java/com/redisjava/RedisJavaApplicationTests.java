package com.redisjava;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "app.grocery.load-on-startup=false")
class RedisJavaApplicationTests {

    @Test
    void contextLoads() {
    }
}
