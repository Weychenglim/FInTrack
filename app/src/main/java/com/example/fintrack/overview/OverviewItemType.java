package com.example.fintrack.overview;

public class OverviewItemType {
    int sImageId;
    String type;
    double percentage;
    double sum;

    int kind;

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public OverviewItemType(int sImageId, String type, double percentage, double sum,int kind) {
        this.sImageId = sImageId;
        this.type = type;
        this.percentage = percentage;
        this.sum = sum;
        this.kind = kind;
    }
}
