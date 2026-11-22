package io.github.soulflys02.springbank.customer;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/me")
    public CustomerResponse me(@AuthenticationPrincipal Jwt jwt) {
        Customer customer = customerService.getCurrentCustomer(
                jwt.getSubject(),
                jwt.getClaimAsString("given_name"),
                jwt.getClaimAsString("family_name"),
                jwt.getClaimAsString("email"));
        return CustomerResponse.from(customer);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@AuthenticationPrincipal Jwt jwt) {
        Customer customer = customerService.create(
                jwt.getSubject(),
                jwt.getClaimAsString("given_name"),
                jwt.getClaimAsString("family_name"),
                jwt.getClaimAsString("email"));
        return ResponseEntity.created(URI.create("/api/v1/customers/me")).body(CustomerResponse.from(customer));
    }

}
