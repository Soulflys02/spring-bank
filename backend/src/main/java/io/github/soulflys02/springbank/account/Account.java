package io.github.soulflys02.springbank.account;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import io.github.soulflys02.springbank.customer.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Account {
    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Customer customer;
    private String iban;
    @CreationTimestamp
    private Instant createdAt;

    public Account(Customer customer, String iban) {
        this.customer = customer;
        this.iban = iban;
    }

    protected Account() {
    }

    public String getIban() {
        return iban;
    }
}
