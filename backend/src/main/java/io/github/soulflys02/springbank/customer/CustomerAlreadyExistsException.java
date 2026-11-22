package io.github.soulflys02.springbank.customer;

public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String keycloakId) {
        super("Customer with Keycloak ID %s already exists.".formatted(keycloakId));
    }
}
