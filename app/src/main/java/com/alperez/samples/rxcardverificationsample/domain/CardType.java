package com.alperez.samples.rxcardverificationsample.domain;

import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;

public enum CardType {
    UNKNOWN(-1),
    VISA(3),
    MASTER_CARD(3),
    AMERICA_EXPRESS(4);

    private final int cvcLength;

    private CardType(int cvcLength) {
        this.cvcLength = cvcLength;
    }

    public int getCvcLength() {
        return cvcLength;
    }

    public static CardType fromString(String number) {
        if (regVisa.matcher(number).matches()) {
            return VISA;
        } else if (regMasterCard.matcher(number).matches()) {
            return MASTER_CARD;
        } else if (regAmericanExpress.matcher(number).matches()) {
            return AMERICA_EXPRESS;
        }
        return UNKNOWN;
    }

    private static Pattern regVisa = compile("^4[0-9]{12}(?:[0-9]{3})?$");
    private static Pattern regMasterCard = compile("^5[1-5][0-9]{14}$");
    private static Pattern regAmericanExpress = compile("^3[47][0-9]{13}$");
}
