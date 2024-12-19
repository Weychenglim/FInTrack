package com.example.fintrack.fragment;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fintrack.R;
import com.example.fintrack.db.AccountItem;
import com.example.fintrack.db.DBManager;
import com.example.fintrack.db.TypeItem;
import com.example.fintrack.utils.KeyboardUtils;
import com.example.fintrack.utils.RemarkDialog;
import com.example.fintrack.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener{

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv, remarkTv , timeTv;
    GridView typeGv;
    List<TypeItem> typeList;
    TypeBaseAdapter adapter;
    AccountItem accountItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountItem = new AccountItem();
        accountItem.setTypename("Other");
        accountItem.setSimageId(R.mipmap.ic_other_fs);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        setInitTime();
        loadDataToGV();
        setGVListener();
        return view;
    }

    protected void loadDataToGV(){
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);

    }

    private void setGVListener(){
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetInvalidated();
                TypeItem typeItem = typeList.get(position);
                String typename = typeItem.getTypename();
                typeTv.setText(typename);
                accountItem.setTypename(typename);
                int simageId = typeItem.getSimageid();
                typeIv.setImageResource(simageId);
                accountItem.setSimageId(simageId);
            }
        });
    }

    private void setInitTime(){
        // Set the time zone to GMT+8
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");

        // Get the current date and time with the specified time zone
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        sdf.setTimeZone(timeZone);

        // Format the date according to the time zone
        String time = sdf.format(date);
        timeTv.setText(time);
        accountItem.setTime(time);

        // Set the calendar instance with the specified time zone
        Calendar calendar = Calendar.getInstance(timeZone);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        accountItem.setYear(year);
        accountItem.setMonth(month);
        accountItem.setDay(day);
    }


    private void initView(View view) {
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        remarkTv = view.findViewById(R.id.frag_record_tv_remark);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        remarkTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);

        moneyEt.setInputType(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL); // Ensure numeric input
        moneyEt.setImeOptions(EditorInfo.IME_ACTION_DONE); // Set action to "Done"

        // Add a listener for the "Done" action on the default keyboard
        moneyEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handleInputLogic(); // Call the logic when "Done" is pressed
                    return true; // Indicate that the action was handled
                }
                return false;
            }
        });

        // Optional: Add a TextWatcher to handle changes in the input text dynamically
        moneyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Optionally handle real-time input validation here
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changes
            }
        });
    }

        // Method to handle input validation and processing
        private void handleInputLogic() {
            String moneyStr = moneyEt.getText().toString();

            // Check if input is empty or 0
            if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("0")) {
                Toast.makeText(moneyEt.getContext(), "Please enter a valid amount.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Parse the input as a double
                double money = Double.parseDouble(moneyStr);
                accountItem.setMoney(money); // Save to the account object
                saveAccountToDB(); // Save to the database
                Toast.makeText(moneyEt.getContext(), "Record saved successfully.", Toast.LENGTH_SHORT).show();

                // Clear the input fields
                resetForm();

            } catch (NumberFormatException e) {
                // Show error if the input is not a valid number
                Toast.makeText(moneyEt.getContext(), "Invalid input. Please enter a valid number.", Toast.LENGTH_SHORT).show();
            }
        }

    // Method to reset the form
    private void resetForm() {
        moneyEt.setText(""); // Clear the input field
        remarkTv.setText(""); // Clear the input field
        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");

        // Get the current date and time with the specified time zone
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        sdf.setTimeZone(timeZone);

        // Format the date according to the time zone
        String time = sdf.format(date);
        timeTv.setText(time);
        moneyEt.requestFocus();
        // Set focus back to the input field
    }

    public abstract void saveAccountToDB();

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.frag_record_tv_time){
            showTimeDialog();
        }else if( v.getId() == R.id.frag_record_tv_remark){
            showRemarkDialog();
        }
    }

    private void showTimeDialog() {
        SelectTimeDialog Dialog = new SelectTimeDialog(getContext());
        Dialog.show();
        Dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountItem.setTime(time);
                accountItem.setYear(year);
                accountItem.setMonth(month);
                accountItem.setDay(day);
            }
        });

    }

    private void showRemarkDialog() {
        final RemarkDialog dialog = new RemarkDialog(getContext());
        dialog.show();
        dialog.setDialogSize();

        dialog.setOnConfirmListener(new RemarkDialog.onConfirmListener() {   /*Creating an instance of anonymous inner class inside this class that implements the onConfirmLister interface in Remark Dialog class */
            @Override
            public void onConfirm() {                   /*Override the method of the interface to meet the implementation requirement*/
                String msg = dialog.getEditText();
                if (!TextUtils.isEmpty(msg)){
                    remarkTv.setText(msg);
                    accountItem.setRemark(msg);
                }
                dialog.cancel();
            }
        });
    }

    /*
    * 1. after the remark dialog ui is created , setOnConfirmListener would be called to set up a listener*/
}
