package io.github.soulflys02.springbank.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Spring Bank API", version = "v1", description = "API of a fictional online bank."))
public class OpenApiConfig {
}
