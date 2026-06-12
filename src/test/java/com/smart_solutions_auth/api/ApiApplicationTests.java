package com.smart_solutions_auth.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@SpringBootTest
class ApiApplicationTests {

	@MockBean
    private RedisConnectionFactory redisConnectionFactory;
	
	@Test
	void contextLoads() {
	}

}
