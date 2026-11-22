package io.github.soulflys02.springbank.account;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class IbanGeneratorTests {

    @Test
    void generatesTheOfficialLuxembourgExample() {
        String iban = IbanGenerator.generate("001", 9400644750000L);

        assertThat(iban).isEqualTo("LU280019400644750000");
    }

    @Test
    void padsASmallAccountNumberWithZeros() {
        String iban = IbanGenerator.generate("999", 42);

        assertThat(iban).isEqualTo("LU359990000000000042");
    }
}
