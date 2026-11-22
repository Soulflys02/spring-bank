package io.github.soulflys02.springbank.customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.soulflys02.springbank.account.AccountService;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountService accountService;

    public CustomerService(CustomerRepository customerRepository, AccountService accountService) {
        this.customerRepository = customerRepository;
        this.accountService = accountService;
    }

    @Transactional
    public Customer getCurrentCustomer(String keycloakId, String firstName, String lastName, String email) {
        Customer customer = customerRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new CustomerNotFoundException(keycloakId));
        customer.updateProfile(firstName, lastName, email);
        return customer;
    }

    @Transactional
    public Customer create(String keycloakId, String firstName, String lastName, String email) {
        if (customerRepository.existsByKeycloakId(keycloakId)) {
            throw new CustomerAlreadyExistsException(keycloakId);
        }
        Customer customer = new Customer(keycloakId, firstName, lastName, email);
        customerRepository.save(customer);
        accountService.openAccount(customer);
        return customer;
    }
}
