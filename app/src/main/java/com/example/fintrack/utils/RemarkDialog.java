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

    onConfirmListener onConfirmListener;  // upcasting
    EditText et;
    Button cancelbtn, confirmbtn;
    public RemarkDialog(@NonNull Context context) {
        super(context);
    }

    public void setOnConfirmListener(onConfirmListener onConfirmListener){
        this.onConfirmListener = onConfirmListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remark);
        et = findViewById(R.id.dialog_remark_et);
        cancelbtn = findViewById(R.id.dialog_remark_brn_cancel);
        confirmbtn = findViewById(R.id.dialog_remark_btn_ensure);
        cancelbtn.setOnClickListener(this);
        confirmbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_remark_brn_cancel){
            cancel();
        }else if (v.getId() == R.id.dialog_remark_btn_ensure){
            if(onConfirmListener != null){
                onConfirmListener.onConfirm();
            }
        }
    }

    public interface onConfirmListener{
         void onConfirm();
    }

    public String getEditText(){
        return et.getText().toString().trim();
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
