package com.smart_solutions_auth.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@SpringBootTest
class ApiApplicationTests {

    @MockitoBean
    private RedisConnectionFactory redisConnectionFactory;

    @Test
    void contextLoads() {
    }

}