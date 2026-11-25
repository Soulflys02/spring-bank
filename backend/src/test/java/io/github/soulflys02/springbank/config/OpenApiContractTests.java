package io.github.soulflys02.springbank.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.json.JsonContent;
import org.springframework.test.web.servlet.MockMvc;

import io.github.soulflys02.springbank.TestcontainersConfiguration;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public class OpenApiContractTests {
    private static final Path CONTRACT_FILE = Path.of("openapi.json");
    private static final String HOW_TO_FIX = "run ./mvnw verify -Popenapi -DskipTests and commit openapi.json";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contractMatchesOpenapiJson() throws Exception {
        String contract = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(CONTRACT_FILE)
                .as("openapi.json is missing: " + HOW_TO_FIX)
                .exists();
        // Compares the JSON, not the text: whitespace and the final newline are not part of the contract
        assertThat(new JsonContent(Files.readString(CONTRACT_FILE)))
                .as("openapi.json is out of date: " + HOW_TO_FIX)
                .isStrictlyEqualTo(contract);
    }
}
