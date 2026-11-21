package io.github.soulflys02.springbank.web;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.soulflys02.springbank.TestcontainersConfiguration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@SpringBootTest
@AutoConfigureMockMvc
@Import({ TestcontainersConfiguration.class, ApiExceptionHandlerTests.TestController.class })
public class ApiExceptionHandlerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void unknownRouteReturnsProblemDetail() throws Exception {
        mockMvc.perform(get("/api/v1/unknown").with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.instance").value("/api/v1/unknown"));
    }

    @Test
    void invalidBodyListsFieldErrors() throws Exception {
        mockMvc.perform(
                post("/test/validation")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"amount\": -1}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors.name").value("must not be blank"))
                .andExpect(jsonPath("$.errors.amount").value("must be greater than 0"));
    }

    @Test
    void unexpectedErrorHidesTechnicalDetails() throws Exception {
        mockMvc.perform(get("/test/crash").with(jwt()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.detail").value("An unexpected error occurred."))
                .andExpect(content().string(not(containsString("1234"))));
    }

    @RequestMapping("/test")
    @RestController
    static class TestController {
        record Payload(@NotBlank String name, @Positive int amount) {
        }

        @PostMapping("/validation")
        void validate(@Valid @RequestBody Payload payload) {
        }

        @GetMapping("/crash")
        void crash() {
            throw new IllegalStateException("database password is 1234");
        }
    }
}
