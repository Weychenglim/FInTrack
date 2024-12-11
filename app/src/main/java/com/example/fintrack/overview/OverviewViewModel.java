package com.example.fintrack.overview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OverviewViewModel extends ViewModel {
    private final MutableLiveData<DateData> dateData = new MutableLiveData<>();

    public LiveData<DateData> getDateData() {
        return dateData;
    }

    public void setDateData(DateData data) {
        dateData.setValue(data);
    }

    // Static nested class
    public static class DateData {
        public int year, month;

        public DateData(int year, int month) {
            this.year = year;
            this.month = month;

        }
    }
}
