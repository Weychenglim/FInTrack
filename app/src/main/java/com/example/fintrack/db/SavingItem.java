package com.example.fintrack.db;

public class SavingItem {

    int id;
    String goalTitle;
    double amount;
    double targetAmount;

    public double getAmountLeft() {
        return amountLeft;
    }

    public void setAmountLeft(double amountLeft) {
        this.amountLeft = amountLeft;
    }

    double amountLeft;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoalTitle() {
        return goalTitle;
    }

    public void setGoalTitle(String goalTitle) {
        this.goalTitle = goalTitle;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDayLeft() {
        return dayLeft;
    }

    public void setDayLeft(String dayLeft) {
        this.dayLeft = dayLeft;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public SavingItem(int id, String goalTitle, double amount, String dayLeft, int priority, double percentage, double targetAmount,double amountLeft) {
        this.id = id;
        this.goalTitle = goalTitle;
        this.amount = amount;
        this.dayLeft = dayLeft;
        this.priority = priority;
        this.percentage = percentage;
        this.targetAmount = targetAmount;
        this.amountLeft = amountLeft;
    }

    String dayLeft;
    int priority;
    double percentage;


}
