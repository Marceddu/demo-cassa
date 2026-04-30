package com.example.demo.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class WeightFormatUtil {
    private WeightFormatUtil() {}

    public static String format(Integer grams) {
        if (grams == null) return null;
        if (grams < 1000) return grams + " g";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ITALIAN);
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("0.000", symbols);
        return df.format(grams / 1000.0) + " kg";
    }
}
