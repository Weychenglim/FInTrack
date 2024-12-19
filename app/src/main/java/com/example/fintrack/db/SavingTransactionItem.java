package com.example.fintrack.db;

public class SavingTransactionItem {
    double amount;

    String date;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public SavingTransactionItem(String date, double amount) {
        this.date = date;
        this.amount = amount;
    }
}
