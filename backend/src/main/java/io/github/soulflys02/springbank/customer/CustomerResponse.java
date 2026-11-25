package io.github.soulflys02.springbank.customer;

import io.swagger.v3.oas.annotations.media.Schema;

public record CustomerResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String firstName,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String lastName,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) String email) {
    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail());
    }
}
