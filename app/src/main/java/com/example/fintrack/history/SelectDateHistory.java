package com.example.fintrack.history;

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
public class SelectDateHistory extends Dialog implements View.OnClickListener {
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

    public SelectDateHistory(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_calendar);

        datePicker = findViewById(R.id.history_time_dp);
        ensureBtn = findViewById(R.id.history_time_btn_ensure);
        cancelBtn = findViewById(R.id.history_time_btn_cancel);

        // Set click listeners for the confirm and cancel buttons.
        ensureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        // Hide the header of the DatePicker.
        hideDatePickerHeader();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.history_time_btn_cancel) {
            // Close the dialog when the Cancel button is clicked.
            cancel();
        }
        else if (v.getId() == R.id.history_time_btn_ensure){
            // Get the selected year, month, and day from the DatePicker.
            int year = datePicker.getYear();
            int month = datePicker.getMonth() + 1; // Adjust for zero-based index.
            int dayOfMonth = datePicker.getDayOfMonth();

            // Format month and day with leading zeros if necessary.
            String monthStr = (month < 10) ? "0" + month : String.valueOf(month);
            String dayStr = (dayOfMonth < 10) ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

            // Format the selected date and time as a string.
            String timeFormat = year + " - " + monthStr + " - " + dayStr;

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
