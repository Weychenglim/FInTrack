package com.example.fintrack.fragment;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private KeyboardUtils keyboardUtils; // Custom keyboard

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


    private void initView(View view){
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        remarkTv = view.findViewById(R.id.frag_record_tv_remark);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        remarkTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);

        KeyboardUtils boardUtils = new KeyboardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();
        boardUtils.setOnEnsureListener(new KeyboardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String moneyStr = moneyEt.getText().toString();

                // Check if input is empty or 0
                if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("0")) {
                    // Handle the case where input is empty or 0, if needed
                    return;
                }

                try {
                    // Try parsing the input as a double
                    double money = Double.parseDouble(moneyStr);
                    accountItem.setMoney(money);
                    saveAccountToDB();
                    getActivity().finish();

                    // Do something with the valid number, if necessary
                } catch (NumberFormatException e) {
                    // Input is not a valid double, show an error message
                    Toast.makeText(moneyEt.getContext(), "Invalid input. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
