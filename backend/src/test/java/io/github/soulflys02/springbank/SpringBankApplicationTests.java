package io.github.soulflys02.springbank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SpringBankApplicationTests {

	@Test
	void contextLoads() {
	}

}
