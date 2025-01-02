package com.example.fintrack.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.fintrack.R;

public class RemarkDialog extends Dialog implements View.OnClickListener {

    // Listener for confirm action, used to notify when the confirm button is clicked
    onConfirmListener onConfirmListener;

    // UI elements
    EditText et; // Input field for user remarks
    Button cancelbtn, confirmbtn; // Buttons for cancel and confirm actions

    /**
     * Constructor to create the dialog.
     * @param context The context where the dialog is used.
     */
    public RemarkDialog(@NonNull Context context) {
        super(context);
    }

    /**
     * Sets the listener for the confirm button.
     * @param onConfirmListener Listener that defines the behavior when the confirm button is clicked.
     */
    public void setOnConfirmListener(onConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the layout for the dialog
        setContentView(R.layout.dialog_remark);

        // Initialize UI elements
        et = findViewById(R.id.dialog_remark_et); // EditText for entering remarks
        cancelbtn = findViewById(R.id.dialog_remark_brn_cancel); // Button to cancel the dialog
        confirmbtn = findViewById(R.id.dialog_remark_btn_ensure); // Button to confirm the input

        // Set click listeners for the buttons
        cancelbtn.setOnClickListener(this);
        confirmbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Handle button clicks based on their IDs
        if (v.getId() == R.id.dialog_remark_brn_cancel) {
            cancel(); // Close the dialog when the cancel button is clicked
        } else if (v.getId() == R.id.dialog_remark_btn_ensure) {
            if (onConfirmListener != null) {
                onConfirmListener.onConfirm(); // Notify listener when confirm button is clicked
            }
        }
    }

    /**
     * Interface for the confirm button listener.
     * Implement this interface to define the behavior when the confirm button is clicked.
     */
    public interface onConfirmListener {
        void onConfirm();
    }

    /**
     * Gets the text entered in the EditText field.
     * @return The trimmed text from the EditText.
     */
    public String getEditText() {
        return et.getText().toString().trim();
    }

    /**
     * Sets the size and position of the dialog.
     * Positions the dialog at the bottom of the screen and makes it full width.
     */
    public void setDialogSize() {
        Window window = getWindow(); // Get the dialog's window
        WindowManager.LayoutParams wlp = window.getAttributes(); // Get the window attributes

        // Get the screen dimensions
        Display d = window.getWindowManager().getDefaultDisplay();

        // Set the width to match the screen width
        wlp.width = (int) (d.getWidth());

        // Position the dialog at the bottom of the screen
        wlp.gravity = Gravity.BOTTOM;

        // Set the background to be transparent
        window.setBackgroundDrawableResource(android.R.color.transparent);

        // Apply the updated attributes to the window
        window.setAttributes(wlp);
    }
}
