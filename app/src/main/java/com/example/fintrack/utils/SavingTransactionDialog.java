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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.fintrack.R;

public class SavingTransactionDialog extends Dialog implements View.OnClickListener {

    TextView savingTransactionTv;
    SavingTransactionDialog.onConfirmListener onConfirmListener;  // upcasting
    EditText savingTransactionEt;
    Button cancelbtn, confirmbtn;

    public double amount_Left;


    public SavingTransactionDialog(@NonNull Context context, double amount_Left) {
        super(context);
        this.amount_Left = amount_Left;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_addsavingtransaction);
        savingTransactionTv = findViewById(R.id.saving_goal_amount_left_tv);
        cancelbtn = findViewById(R.id.saving_transaction_cancel);
        confirmbtn = findViewById(R.id.saving_transaction_confirm);
        savingTransactionEt = findViewById(R.id.saving_goal_et);
        cancelbtn.setOnClickListener(this);
        confirmbtn.setOnClickListener(this);
        savingTransactionTv.setText("Keep going! You're only RM " + String.format("%.2f",amount_Left) + " away from your goal.");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.saving_transaction_cancel){
            cancel();
        }else if (v.getId() == R.id.saving_transaction_confirm){
            if(onConfirmListener != null){
                onConfirmListener.onConfirm();
            }
        }
    }

    public String getEditText(){
        return savingTransactionEt.getText().toString().trim();
    }

    public void setDialogSize() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wlp = window.getAttributes();
            Display d = window.getWindowManager().getDefaultDisplay();
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                Display display = wm.getDefaultDisplay();
                android.graphics.Point size = new android.graphics.Point();
                display.getSize(size);
                wlp.width = (int) (size.x * 0.95); // Use 95% of the screen width for safety
            }
            wlp.gravity = Gravity.BOTTOM;
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setAttributes(wlp);
        }
    }


    public interface onConfirmListener{
        void onConfirm();
    }
    public void setOnConfirmListener(SavingTransactionDialog.onConfirmListener onConfirmListener){
        this.onConfirmListener = onConfirmListener;
    }


}

