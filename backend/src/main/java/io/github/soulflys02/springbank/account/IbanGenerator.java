package io.github.soulflys02.springbank.account;

import java.math.BigInteger;
import java.util.Locale;

final class IbanGenerator {

    private static final String COUNTRY_CODE = "LU";

    private IbanGenerator() {
    }

    /**
     * Builds a Luxembourg IBAN: 20 characters, no spaces.
     *
     * @param bankCode      exactly 3 digits, e.g. "999"
     * @param accountNumber between 0 and 9 999 999 999 999 (13 digits at most)
     * @return the IBAN, e.g. "LU359990000000000042"
     */
    static String generate(String bankCode, long accountNumber) {
        String bban = bankCode + String.format(Locale.ROOT, "%013d", accountNumber);

        String rearranged = bban + COUNTRY_CODE + "00";
        StringBuilder digits = new StringBuilder();
        for (char c : rearranged.toCharArray()) {
            digits.append(Character.getNumericValue(c));
        }

        int remainder = new BigInteger(digits.toString()).mod(BigInteger.valueOf(97)).intValue();
        String checkDigits = String.format(Locale.ROOT, "%02d", 98 - remainder);

        return COUNTRY_CODE + checkDigits + bban;
    }
}
