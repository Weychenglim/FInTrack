package com.example.fintrack.overview;

import android.graphics.Color;
import android.view.View;

import com.example.fintrack.adapter.OverviewItemAdapter;
import com.example.fintrack.db.DBManager;
import com.example.fintrack.db.OverviewItem;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class IncomeOverviewFragment extends BaseOverviewFragment{

    int kind = 1;
    public void onResume() {
        super.onResume();
        loadData(year,month,kind);
    }

    @Override
    protected void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();

        // Retrieve data for the specific year, month, and kind
        List<OverviewItem> list = DBManager.getSumMoneyOneDayInMonth(year, month, kind);

        if (list.size() == 0) {
            barChart.setVisibility(View.GONE); // Hide the chart if no data
            overviewTv.setVisibility(View.VISIBLE); // Show a message instead
        } else {
            barChart.setVisibility(View.VISIBLE); // Show the chart
            overviewTv.setVisibility(View.GONE); // Hide the message

            // Initialize a list of BarEntries with 31 days set to 0
            List<BarEntry> barEntries1 = new ArrayList<>();
            for (int i = 0; i < 31; i++) {
                barEntries1.add(new BarEntry(i, 0.0f));
            }

            // Populate BarEntries with data from the list
            for (OverviewItem item : list) {
                int day = item.getDay();
                int xIndex = day - 1;
                BarEntry barEntry = barEntries1.get(xIndex);
                barEntry.setY((float) item.getSum()); // Set the value for each day
            }

            BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
            barDataSet1.setValueTextColor(Color.parseColor("#FF00FF")); // Set the value text color
            barDataSet1.setValueTextSize(10f); // Set the value text size
            barDataSet1.setColor(Color.parseColor("#006400")); // Set the bar color

            // Set the value formatter using the new ValueFormatter
            barDataSet1.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    // Display value only if it's greater than 0
                    return value == 0 ? "" : String.valueOf(value);
                }
            });

            sets.add(barDataSet1);

            // Create BarData and configure it
            BarData barData = new BarData(sets);
            barData.setBarWidth(0.3f); // Set the width of each bar
            barChart.setData(barData);
        }
    }

    @Override
    protected void setYAxis(int year,int month) {
        // Get the highest income day of the current month and set it as the maximum value for the y-axis
        float maxMoney = DBManager.getMaxMoneyOneDayInMonth(year, month, kind);
        float max = (float) Math.ceil(maxMoney);   // Round the maximum amount up
        // Set the y-axis
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);  // Set the maximum value for the right y-axis
        yAxis_right.setAxisMinimum(0f);  // Set the minimum value for the right y-axis
        yAxis_right.setEnabled(false);  // Disable the right y-axis display

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);
        // Disable the legend display
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

    }

    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        if (mDatas == null) {
            mDatas = new ArrayList<>();
            overviewItemAdapter = new OverviewItemAdapter(getContext(),mDatas);
        }
        loadData(year,month,kind);
    }
}
