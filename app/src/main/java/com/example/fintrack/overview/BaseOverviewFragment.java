package com.example.fintrack.overview;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fintrack.R;
import com.example.fintrack.adapter.OverviewItemAdapter;
import com.example.fintrack.db.DBManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseOverviewFragment extends Fragment {

    ListView overviewLv;

    List<OverviewItemType>mDatas;

    OverviewItemAdapter overviewItemAdapter;
    BarChart barChart;

    TextView overviewTv;

     int year,month;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_income_overview, container, false);
        overviewLv = view.findViewById(R.id.overview_income_lv);
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        mDatas = new ArrayList<>();
        overviewItemAdapter = new OverviewItemAdapter(getContext(),mDatas);
        overviewLv.setAdapter(overviewItemAdapter);
        addLvHeaderView();
        return view;
    }

    private void addLvHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.item_overview_top,null);
        overviewLv.addHeaderView(headerView);

        barChart = headerView.findViewById(R.id.item_overview_chart);
        overviewTv = headerView.findViewById(R.id.item_overview_top_tv);
        barChart.getDescription().setEnabled(false);
        barChart.setExtraOffsets(20,20,20,20);
        setAxis(year,month);
        setAxisData(year,month);
    }

    protected abstract void setAxisData(int year, int month);

    private void setAxis(int year, int month) {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set X-axis position at the bottom of the chart
        xAxis.setDrawGridLines(true); // Enable grid lines on the X-axis
        xAxis.setLabelCount(31);
        xAxis.enableGridDashedLine(10f, 5f, 0f); // Dashed grid lines for better visibility
        xAxis.setGridColor(Color.LTGRAY);       // Use a light gray for better contrast
        xAxis.setGridLineWidth(1f); // Display labels for up to 31 days
        xAxis.setTextSize(12f); // Set text size for the labels
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int val = (int) value;

                // Handle specific days for labeling
                if (val == 0) {
                    return month + "-1"; // First day of the month
                }
                if (val == 14) {
                    return month + "-15"; // Middle of the month
                }

                // Check for the last day of the month
                if (month == 2) {
                    // Handle February with leap year logic
                    if (isLeapYear(year) && val == 28) {
                        return month + "-29"; // Leap year February has 29 days
                    } else if (val == 27) {
                        return month + "-28"; // Non-leap year February has 28 days
                    }
                } else if (month == 1 || month == 3 || month == 5 || month == 7 ||
                        month == 8 || month == 10 || month == 12) {
                    if (val == 30) {
                        return month + "-31"; // Months with 31 days
                    }
                } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                    if (val == 29) {
                        return month + "-30"; // Months with 30 days
                    }
                }

                // Empty string for labels not shown
                return "";
            }
        });
        xAxis.setYOffset(10);
        setYAxis(year, month);

    }

    protected abstract void setYAxis(int year, int month);

    // Helper method to check for a leap year
    private boolean isLeapYear(int year) {
        // A leap year is divisible by 4 but not 100 unless also divisible by 400
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public void setDate(int year, int month){
        this.year = year;
        this.month = month;
        barChart.clear();
        barChart.invalidate();
        setAxis(year,month);
        setAxisData(year,month);
    }

    protected void loadData(int year, int month, int i) {
        List<OverviewItemType> list = DBManager.getOverviewListFromAccounttb(year,month,i);
        mDatas.clear();
        mDatas.addAll(list);
        overviewItemAdapter.notifyDataSetChanged();
    }
}