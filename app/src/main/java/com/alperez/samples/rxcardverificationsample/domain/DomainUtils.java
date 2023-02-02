package com.alperez.samples.rxcardverificationsample.domain;

public class DomainUtils {

    public static boolean validateExpirationDate(String expiration) {
        return expiration.matches("\\d\\d\\/\\d\\d");
    }

    public static boolean validateCardNumberChecksum(int[] cardDigits) {
        int sum = 0;
        int length = cardDigits.length;
        for (int i = 0; i < length; i++) {

            // Get digits in reverse order
            int digit = cardDigits[length - i - 1];

            // Every 2nd number multiply with 2
            if (i % 2 == 1) {
                digit *= 2;
            }
            sum += digit > 9 ? digit - 9 : digit;
        }
        return sum % 10 == 0;
    }

}
