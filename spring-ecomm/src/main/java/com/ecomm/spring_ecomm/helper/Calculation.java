package com.ecomm.spring_ecomm.helper;

public class Calculation {

    public static Double calculateSpecialPrice(double price, double discountPercentage) {
        if(discountPercentage == 0){
            return price; // no discount → return original price
        }
        double value = price * (.01 * discountPercentage); // calculate discount amount
        double finalValue = price - value; // apply discount
        return Math.round(finalValue * 100.0) / 100.0; // round to 2 decimals
    }

    public static Double calculateSubTotal(double price, Integer quantity) {
        return Math.round(price * quantity*100.0) / 100.0;
    }
}
