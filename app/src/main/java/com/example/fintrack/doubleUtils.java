package com.example.fintrack;

import java.math.BigDecimal;

public class doubleUtils {
    public static double div (double v1, double v2){
        double v3 = v1/v2;
        BigDecimal b1 = new BigDecimal(v3);
        // Set the scale (precision) of the BigDecimal to 4 decimal places
        // The second argument "4" refers to the rounding mode (ROUND_HALF_UP is used here)
        // "setScale(4, BigDecimal.ROUND_HALF_UP)" rounds the result to 4 decimal places
        double val = b1.setScale(4,4).doubleValue();
        return val;
    }

    public static String ratioToPercentage(double val){
        double v =val*100;
        BigDecimal b1 = new BigDecimal(v);
        double v1 = b1.setScale(2,4).doubleValue();
        String per = v1+ "%";
        return per;
    }
}
