package io.github.soulflys02.springbank.customer;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String keycloakId) {
        super("Customer not found for Keycloak user " + keycloakId);
    }
}
