package io.github.soulflys02.springbank.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import io.github.soulflys02.springbank.TestcontainersConfiguration;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public class SecurityConfigTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void swaggerUiIsPublic() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/swagger-ui/index.html"));
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void apiDocsArePublic() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    void apiIsProtected() throws Exception {
        mockMvc.perform(get("/api/v1/accounts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void clientCannotAccessBackOffice() throws Exception {
        mockMvc.perform(get("/api/v1/admin/customers")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_client"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void backOfficeCanAccessBackOffice() throws Exception {
        mockMvc.perform(get("/api/v1/admin/customers")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_backoffice"))))
                .andExpect(status().isNotFound());
    }
}
