package com.example.fintrack.overview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fintrack.R;

public class OverviewFragment extends Fragment {

    AppCompatButton inBtn, expBtn;
    TextView monthTv,inTv,expTv;
    ViewPager overviewVp;

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
    }

    private void intiView(View view) {
        inBtn = view.findViewById(R.id.overview_btn_income);
        expBtn = view.findViewById(R.id.overview_btn_expend);
        monthTv = view.findViewById(R.id.overview_tv_month);
        inTv = view.findViewById(R.id.overview_tv_income);
        expTv = view.findViewById(R.id.overview_tv_expense);
        overviewVp = view.findViewById(R.id.overview_vp);
    }
}