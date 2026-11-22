package io.github.soulflys02.springbank.customer;

public record CustomerResponse(String firstName, String lastName, String email) {
    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail());
    }
}
