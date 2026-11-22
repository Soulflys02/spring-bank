package io.github.soulflys02.springbank.customer;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByKeycloakId(String keycloakId);

    boolean existsByKeycloakId(String keycloakId);
}
