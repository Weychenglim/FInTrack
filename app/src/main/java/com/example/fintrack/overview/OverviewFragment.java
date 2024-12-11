package com.example.fintrack.overview;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fintrack.R;
import com.example.fintrack.adapter.OverviewVpAdapter;
import com.example.fintrack.db.DBManager;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OverviewFragment extends Fragment implements View.OnClickListener {

    AppCompatButton inBtn, expBtn;
    TextView monthTv,inTv,expTv;
    ViewPager overviewVp;

    int year;
    int month;

    List<Fragment>overviewFragList;

    IncomeOverviewFragment inOverviewFrag;
    ExpendOverviewFragment exOverviewFrag;

    OverviewVpAdapter overviewVpAdapter;

    double incomePerMonth;
    double expendPerMonth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intiView(view);
        initTime();
        initStatistics(year,month);
        intiFrag();
        setVpSelectListener();
    }

    private void setVpSelectListener() {
        overviewVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    private void intiFrag() {
        overviewFragList = new ArrayList<>();
        exOverviewFrag = new ExpendOverviewFragment();
        inOverviewFrag = new IncomeOverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        inOverviewFrag.setArguments(bundle);
        exOverviewFrag.setArguments(bundle);
        overviewFragList.add(exOverviewFrag);
        overviewFragList.add(inOverviewFrag);

        overviewVpAdapter = new OverviewVpAdapter(getChildFragmentManager(),overviewFragList);
        overviewVp.setAdapter(overviewVpAdapter);
    }

    private void initStatistics(int year, int month) {
        incomePerMonth = DBManager.getSumMoneyPerMonth(year, month, 1);
        expendPerMonth = DBManager.getSumMoneyPerMonth(year, month, 0);
        OverviewViewModel viewModel = new ViewModelProvider(requireActivity()).get(OverviewViewModel.class);
        String monthName = new DateFormatSymbols().getMonths()[month - 1]; // Month is 0-based in DateFormatSymbols
        monthTv.setText(monthName + " " + year + " Record");
        viewModel.getDateData().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                this.year = data.year;
                this.month = data.month;
                exOverviewFrag.setDate(data.year,data.month);
                inOverviewFrag.setDate(data.year,data.month);
                String monthName2 = new DateFormatSymbols().getMonths()[data.month - 1]; // Month is 0-based in DateFormatSymbols
                monthTv.setText(monthName2 + " " + data.year + " Record");
                initTotalExpenseIncome(data.year, data.month);
            }
        });
        expTv.setText("Total Expense  -  RM" + String.format("%.2f", expendPerMonth));
        inTv.setText("Total Income    -  RM" + String.format("%.2f", incomePerMonth));
    }

    public void initTotalExpenseIncome(int year, int month){
        incomePerMonth = DBManager.getSumMoneyPerMonth(year, month, 1);
        expendPerMonth = DBManager.getSumMoneyPerMonth(year, month, 0);
        expTv.setText("Total Expense  -  RM" + String.format("%.2f", expendPerMonth));
        inTv.setText("Total Income    -  RM" + String.format("%.2f", incomePerMonth));
    }


    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        Log.d("dsf",String.valueOf(month));
    }

    private void intiView(View view) {
        inBtn = view.findViewById(R.id.overview_btn_income);
        inBtn.setOnClickListener(this);
        expBtn = view.findViewById(R.id.overview_btn_expend);
        expBtn.setOnClickListener(this);
        monthTv = view.findViewById(R.id.overview_tv_month);
        inTv = view.findViewById(R.id.overview_tv_income);
        expTv = view.findViewById(R.id.overview_tv_expense);
        overviewVp = view.findViewById(R.id.overview_vp);

    }

    public void setButtonStyle(int kind){
        if (kind == 0){
            expBtn.setBackgroundResource(R.drawable.overview_button1);
            expBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.overview_button2);
            inBtn.setTextColor(getResources().getColor(R. color. teal_700));
        }else if (kind == 1){
                inBtn.setBackgroundResource(R.drawable.overview_button1);
                inBtn.setTextColor(Color.WHITE);
                expBtn.setBackgroundResource(R.drawable.overview_button2);
                expBtn.setTextColor(getResources().getColor(R. color. teal_700));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.overview_btn_income){
            setButtonStyle(1);
            overviewVp.setCurrentItem(1);
        }else if (v.getId() == R.id.overview_btn_expend){
            setButtonStyle(0);
            overviewVp.setCurrentItem(0);
        }
    }
}