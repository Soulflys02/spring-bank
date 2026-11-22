package io.github.soulflys02.springbank.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.soulflys02.springbank.customer.Customer;

@Service
public class AccountService {
    private static final String BANK_CODE = "999";

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account openAccount(Customer customer) {
        String iban = IbanGenerator.generate(BANK_CODE, accountRepository.nextAccountNumber());
        return accountRepository.save(new Account(customer, iban));
    }
}
