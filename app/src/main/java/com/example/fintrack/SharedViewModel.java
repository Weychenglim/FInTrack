package com.example.fintrack;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<DateData> dateData = new MutableLiveData<>();

    public LiveData<DateData> getDateData() {
        return dateData;
    }

    public void setDateData(DateData data) {
        dateData.setValue(data);
    }

    // Static nested class
    public static class DateData {
        public String time;
        public int year, month, day;

        public DateData(String time, int year, int month, int day) {
            this.time = time;
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }
}
