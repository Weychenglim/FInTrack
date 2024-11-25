package com.example.fintrack.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.fintrack.R;


/*
 * This dialog allows users to select a date and time.
 * It is displayed on the record page.
 */
public class SelectTimeDialog extends Dialog implements View.OnClickListener {
    EditText hourEt, minuteEt;
    DatePicker datePicker;
    Button ensureBtn, cancelBtn;

    // Interface for confirming the selected time.
    public interface OnEnsureListener {
        void onEnsure(String time, int year, int month, int day);
    }

    OnEnsureListener onEnsureListener;

    // Method to set the listener for confirming the time.
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public SelectTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar1);

        // Initialize UI elements.
        hourEt = findViewById(R.id.dialog_time_et_hour);
        minuteEt = findViewById(R.id.dialog_time_et_minute);
        datePicker = findViewById(R.id.dialog_time_dp);
        ensureBtn = findViewById(R.id.dialog_time_btn_ensure);
        cancelBtn = findViewById(R.id.dialog_time_btn_cancel);

        // Set click listeners for the confirm and cancel buttons.
        ensureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        // Hide the header of the DatePicker.
        hideDatePickerHeader();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.dialog_time_btn_cancel) {
            // Close the dialog when the Cancel button is clicked.
            cancel();
        }
        else if (v.getId() == R.id.dialog_time_btn_ensure){
                // Get the selected year, month, and day from the DatePicker.
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1; // Adjust for zero-based index.
                int dayOfMonth = datePicker.getDayOfMonth();

                // Format month and day with leading zeros if necessary.
                String monthStr = (month < 10) ? "0" + month : String.valueOf(month);
                String dayStr = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                // Retrieve the entered hour and minute from EditText fields.
                String hourStr = hourEt.getText().toString();
                String minuteStr = minuteEt.getText().toString();

                // Ensure hour and minute are valid and within range.
                int hour = 0;
                if (!TextUtils.isEmpty(hourStr)) {
                    hour = Integer.parseInt(hourStr) % 24;
                }
                int minute = 0;
                if (!TextUtils.isEmpty(minuteStr)) {
                    minute = Integer.parseInt(minuteStr) % 60;
                }

                // Format hour and minute with leading zeros if necessary.
                hourStr = (hour < 10) ? "0" + hour : String.valueOf(hour);
                minuteStr = (minute < 10) ? "0" + minute : String.valueOf(minute);

                // Format the selected date and time as a string.
                String timeFormat = dayStr + "-" + monthStr + "-" + year + "  " + hourStr + ":" + minuteStr;

                // Trigger the confirm listener with the formatted time, year, month, and day.
                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure(timeFormat, year, month, dayOfMonth);
                }

                // Close the dialog after confirming.
                cancel();

        }
    }

    // Method to hide the DatePicker header layout.
    private void hideDatePickerHeader() {
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if (rootView == null) {
            return;
        }
        View headerView = rootView.getChildAt(0);
        if (headerView == null) {
            return;
        }

        // For Android 5.0+ devices, find and hide the "day_picker_selector_layout" header.
        int headerId = getContext().getResources().getIdentifier("day_picker_selector_layout", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);

            // Adjust layout width to fit the DatePicker after hiding the header.
            ViewGroup.LayoutParams layoutParamsRoot = rootView.getLayoutParams();
            layoutParamsRoot.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            rootView.setLayoutParams(layoutParamsRoot);

            ViewGroup animator = (ViewGroup) rootView.getChildAt(1);
            ViewGroup.LayoutParams layoutParamsAnimator = animator.getLayoutParams();
            layoutParamsAnimator.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animator.setLayoutParams(layoutParamsAnimator);

            View child = animator.getChildAt(0);
            ViewGroup.LayoutParams layoutParamsChild = child.getLayoutParams();
            layoutParamsChild.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            child.setLayoutParams(layoutParamsChild);
            return;
        }

        // For Android 6.0+ devices, find and hide the "date_picker_header".
        headerId = getContext().getResources().getIdentifier("date_picker_header", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
        }
    }
}
