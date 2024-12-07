package com.example.fintrack.db;

public class AccountItem {
    int id;
    String typename;
    int simageId; // selected item icon
    String remark;
    String time;

    double money;


    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getSimageId() {
        return simageId;
    }

    public void setSimageId(int simageId) {
        this.simageId = simageId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    int year;
    int month;
    int day;
    int kind;

    public AccountItem(int id, String typename, int simageId, String remark, String time, double money, int year, int month, int day, int kind) {
        this.id = id;
        this.typename = typename;
        this.simageId = simageId;
        this.remark = remark;
        this.time = time;
        this.money = money;
        this.year = year;
        this.month = month;
        this.day = day;
        this.kind = kind;
    }

    public AccountItem() {
    }
}
