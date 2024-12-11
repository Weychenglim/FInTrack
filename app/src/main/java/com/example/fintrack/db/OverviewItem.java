package com.example.fintrack.db;

public class OverviewItem {
    int year;
    int month;
    int day;

    public OverviewItem(int year, int month, int day, double sum) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.sum = sum;
    }

    double sum;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
