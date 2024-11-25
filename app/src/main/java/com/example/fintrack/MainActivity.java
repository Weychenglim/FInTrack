package com.example.fintrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fintrack.adapter.AccountAdapter;
import com.example.fintrack.db.AccountItem;
import com.example.fintrack.db.DBManager;
import com.example.fintrack.utils.BudgetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ListView todayLv;
    List<AccountItem>mDatas;

    AccountAdapter adapter;

    int year, month , day;

    ImageView search;
    ImageButton record, menu ;

    View headerView;

    TextView topOutTv, topInTv, topbudgetTv , topConTv;

    ImageView topShowIv;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setStatusBarColor(getResources().getColor(R.color.teal_200, getTheme()));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initTime();
        initView();
        preferences = getSharedPreferences("budget",MODE_PRIVATE);  /*This method retrieves and initialize a SharedPreferences instance that points to the file
        named "budget" where data can be stored. If this file doesn’t exist, Android will create it. file is private to the app*/
        todayLv = findViewById(R.id.main_lv);
        addLVHeaderView();
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this,mDatas);
        todayLv.setAdapter(adapter);

    }

    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        search = findViewById(R.id.main_iv_search);
        record = findViewById(R.id.main_btn_edit);
        menu  = findViewById(R.id.main_btn_more);
        search.setOnClickListener(this);
        record.setOnClickListener(this);
        menu.setOnClickListener(this);
    }

    private void addLVHeaderView() {
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);

        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_main_top_Iv_hide);

        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }


    private void setTopTvShow() {
        double incomePerDay = DBManager.getSumMoneyPerDay(year, month, day, 1);
        double expensePerDay= DBManager.getSumMoneyPerDay(year, month, day, 0);
        String infoPerDay = "Today's\nSpending - RM " + String.format("%.2f" , expensePerDay) + "      Income - RM " + String.format("%.2f" , incomePerDay);
        topConTv.setText(infoPerDay);
        double incomePerMonth = DBManager.getSumMoneyPerMonth(year, month, 1);
        double expensePerMonth = DBManager.getSumMoneyPerMonth(year, month, 0);
        topInTv.setText("RM " + String.format("%.2f" , incomePerMonth));
        topOutTv.setText("RM " + String.format("%.2f" , expensePerMonth));
        float amount_update = preferences.getFloat("amount",0);
        if(amount_update == 0){
            topbudgetTv.setText("RM 0");
        }else{
            float final_amount = amount_update- (float)expensePerMonth;
            topbudgetTv.setText("RM " + String.format("%.2f" , final_amount));
        }
    }

    private void loadDBData() {
        List<AccountItem> list = DBManager.getAccountListOneDayFromAccounttb(year, month,day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_iv_search){

        }else if (v.getId() == R.id.main_btn_edit){
            Intent it1 = new Intent(this, RecordActivity.class);
            startActivity(it1);
        }else if (v.getId() == R.id.main_btn_more){

        }else if (v.getId() == R.id.item_main_top_Iv_hide){
            toggleShow();
        }else if (v.getId() == R.id.item_mainlv_top_tv_budget){
            showBudgetDialog();
        }
    }

    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this); // fragment use getContext() / requireContext()
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnConfirmListener(new BudgetDialog.onConfirmListener() {
            @Override
            public void onConfirm(double amount) {
                SharedPreferences.Editor editor = preferences.edit();
                /*SharedPreferences in Android is a lightweight storage mechanism that allows you to store small amounts of data in key-value pairs.
                It’s typically used for storing user preferences, settings, or other data that needs to persist across app sessions, even after the app
                is closed. SharedPreferences is ideal for simple data types like boolean, int, float, long, and String. Data stored in SharedPreferences
                 remains available until it is specifically cleared.*/
                editor.putFloat("amount",(float)amount);
                editor.commit();

                double spending = DBManager.getSumMoneyPerMonth(year,month,0);
                double balance = amount-spending;
                topbudgetTv.setText("RM " + String.format("%.2f" , balance));
            }
        });

    }

    boolean isShown = true;
    private void toggleShow() {
        if (isShown){
            PasswordTransformationMethod instance = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(instance);
            topOutTv.setTransformationMethod(instance);
            topbudgetTv.setTransformationMethod(instance);
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShown = false;
        }else{
            HideReturnsTransformationMethod instance = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(instance);
            topOutTv.setTransformationMethod(instance);
            topbudgetTv.setTransformationMethod(instance);
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShown = true;
        }
    }


}