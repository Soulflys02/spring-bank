package io.github.soulflys02.springbank;

import org.springframework.boot.SpringApplication;

public class TestSpringBankApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringBankApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
