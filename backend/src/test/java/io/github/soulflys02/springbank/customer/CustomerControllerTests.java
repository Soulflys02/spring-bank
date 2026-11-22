package io.github.soulflys02.springbank.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.MockMvc;

import io.github.soulflys02.springbank.TestcontainersConfiguration;
import io.github.soulflys02.springbank.account.Account;
import io.github.soulflys02.springbank.account.AccountRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class CustomerControllerTests {
        private static final String KEYCLOAK_ID = "3f1c2a9e-5b7d-4c8e-9a10-2b3c4d5e6f70";

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private CustomerRepository customerRepository;

        @Autowired
        private AccountRepository accountRepository;

        @BeforeEach
        void emptyTables() {
                accountRepository.deleteAll();
                customerRepository.deleteAll();
        }

        @Test
        void createsTheCustomerAndTheirAccountFromTheToken() throws Exception {
                mockMvc.perform(post("/api/v1/customers").with(client("Ada", "Lovelace")))
                                .andExpect(status().isCreated())
                                .andExpect(header().string("Location", endsWith("/api/v1/customers/me")))
                                .andExpect(jsonPath("$.firstName").value("Ada"))
                                .andExpect(jsonPath("$.lastName").value("Lovelace"))
                                .andExpect(jsonPath("$.email").value("ada@spring-bank.test"));

                assertThat(customerRepository.findByKeycloakId(KEYCLOAK_ID)).isPresent();
                assertThat(customerRepository.count()).isEqualTo(1);
                assertThat(accountRepository.count()).isEqualTo(1);
                Account account = accountRepository.findAll().getFirst();
                assertThat(account.getIban()).matches("LU\\d{2}999\\d{13}");
        }

        @Test
        void secondCreationReturnsConflictWithoutDuplicates() throws Exception {
                mockMvc.perform(post("/api/v1/customers").with(client("Ada", "Lovelace")))
                                .andExpect(status().isCreated());

                mockMvc.perform(post("/api/v1/customers").with(client("Ada", "Lovelace")))
                                .andExpect(status().isConflict())
                                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

                assertThat(customerRepository.count()).isEqualTo(1);
                assertThat(accountRepository.count()).isEqualTo(1);
        }

        @Test
        void meReturnsNotFoundBeforeCreation() throws Exception {
                mockMvc.perform(get("/api/v1/customers/me").with(client("Ada", "Lovelace")))
                                .andExpect(status().isNotFound())
                                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));
        }

        @Test
        void meReturnsTheProfileAfterCreation() throws Exception {
                mockMvc.perform(post("/api/v1/customers").with(client("Ada", "Lovelace")));

                mockMvc.perform(get("/api/v1/customers/me").with(client("Ada", "Lovelace")))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.firstName").value("Ada"))
                                .andExpect(jsonPath("$.lastName").value("Lovelace"))
                                .andExpect(jsonPath("$.email").value("ada@spring-bank.test"));
        }

        @Test
        void meUpdatesTheProfileWhenTheTokenChanged() throws Exception {
                mockMvc.perform(post("/api/v1/customers").with(client("Ada", "Lovelace")));

                mockMvc.perform(get("/api/v1/customers/me").with(client("Ada", "King")))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.lastName").value("King"));

                assertThat(customerRepository.findByKeycloakId(KEYCLOAK_ID))
                                .hasValueSatisfying(customer -> assertThat(customer.getLastName()).isEqualTo("King"));
        }

        @Test
        void customersEndpointsRequireTheClientRole() throws Exception {
                mockMvc.perform(post("/api/v1/customers").with(jwt()))
                                .andExpect(status().isForbidden());

                mockMvc.perform(get("/api/v1/customers/me").with(jwt()))
                                .andExpect(status().isForbidden());
        }

        private static JwtRequestPostProcessor client(String firstName, String lastName) {
                return jwt()
                                .jwt(token -> token
                                                .subject(KEYCLOAK_ID)
                                                .claim("given_name", firstName)
                                                .claim("family_name", lastName)
                                                .claim("email", "ada@spring-bank.test"))
                                .authorities(new SimpleGrantedAuthority("ROLE_client"));
        }
}
