package com.company.pawpet;

import java.util.List;

public class PaymentRequest {
    private String cardNumber;
    private String expiryDate;
    private String cvc;
    private double amount;
    private List<Integer> cartItems;

    public List<Integer> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<Integer> cartItems) {
        this.cartItems = cartItems;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
