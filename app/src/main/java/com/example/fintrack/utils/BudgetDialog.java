package com.example.fintrack.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.fintrack.R;

public class BudgetDialog extends Dialog implements View.OnClickListener {

    public interface onConfirmListener{
        void onConfirm(double amount);
    }

    public void setOnConfirmListener(onConfirmListener onConfirmListener){
        this.onConfirmListener = onConfirmListener;
    }

    onConfirmListener onConfirmListener;

    ImageView close_btn;
    EditText set_budget;
    AppCompatButton confirm_button;  // subclass of button
    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget);
        close_btn = findViewById(R.id.dialog_budget_iv_error);
        set_budget = findViewById(R.id.dialog_budget_et);
        confirm_button = findViewById(R.id.dialog_budget_btn_ensure);
        close_btn.setOnClickListener(this);
        confirm_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == close_btn.getId()){
            cancel();
        }
        else if (v.getId() == confirm_button.getId()) {
            String budget = set_budget.getText().toString();

            // Check if the budget field is empty
            if (TextUtils.isEmpty(budget)) {
                Toast.makeText(getContext(), "Please enter a budget amount.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert to a double and check if it's a valid amount
            double v1 = Double.parseDouble(budget);
            if (v1 <= 0) {
                Toast.makeText(getContext(), "Budget must be greater than zero.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(onConfirmListener != null){
                onConfirmListener.onConfirm(v1);
            }

            cancel();
        }

    }
    public void setDialogSize(){
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}

