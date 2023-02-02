package com.alperez.samples.rxcardverificationsample;

public class AppUtils {

    public static String charSequence2String(CharSequence cs) {
        return (cs instanceof String) ? (String) cs : cs.toString();
    }

    public static int[] convertCardNumberTextToDigits(String cardNumber) {
        int[] digits = new int[cardNumber.length()];
        int dig, i=0;
        for (char c : cardNumber.toCharArray()) {
            dig = (int) c - 0x30;
            digits[i++] = ((dig >= 0) && (dig < 10)) ? dig : -1;
        }
        return digits;
    }

    public static boolean checkStringIsNumber(String s) {
        try {
            Long.parseLong(s, 10);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
