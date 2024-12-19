package com.example.fintrack.history;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fintrack.PDFGenerator;
import com.example.fintrack.R;
import com.example.fintrack.adapter.AccountAdapter;
import com.example.fintrack.db.AccountItem;
import com.example.fintrack.db.DBManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListHistoryFragment extends Fragment {

    ListView historyList;

    List<AccountItem>mDatas;

    AccountAdapter adapter;
    TextView dateTv;
    String time;
    int year, month, day;

    ImageButton toPdf;

    public ListHistoryFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_history, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        HistoryViewModel viewModel = new ViewModelProvider(requireActivity()).get(HistoryViewModel.class);
        viewModel.getDateData().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                dateTv.setText(data.time);
                loadData(data.year, data.month, data.day);
            }
        });
        /*The fragment has an observer in onViewCreated() that listens to changes in viewModel.getDateData().
When the dialog updates the SharedViewModel, the observer triggers.
This does not re-initialize or recall onViewCreated(). Instead, the observer merely updates the dateTv and calls loadData().*/
    }

    private void initView(View view) {
        dateTv = view.findViewById(R.id.dateTv);
        historyList = view.findViewById(R.id.main_lv_history);
        toPdf = view.findViewById(R.id.toPdfBtn);
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(requireContext(),mDatas);
        historyList.setAdapter(adapter);
        historyList.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isInitialState = true; // Tracks whether it's the initial state

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("ListenerCheck", "OnScroll Triggered");

                if (isInitialState) {
                    Log.d("ListenerCheck", "Initial State - Do not hide button");
                    return; // Do nothing during the initial state
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    toPdf.setVisibility(View.VISIBLE); // Show the button when scrolling stops
                    Log.d("ListenerCheck", "ScrollStateChanged Triggered: Button Visible");
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isInitialState = false; // User has started scrolling, mark initial state as finished
                    Log.d("ListenerCheck", "User started scrolling, exiting initial state");
                    toPdf.setVisibility(View.GONE);
                }
            }
        });
        loadData(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH)+1,Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dateTv.setText(Calendar.getInstance().get(Calendar.YEAR) + " - " + (Calendar.getInstance().get(Calendar.MONTH)+1) + " - " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        toPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Pdfclicked","clickPDF");
                PDFGenerator.createPDF(requireContext(), DBManager.getSortedAccountList());
            }
        });

    }


    protected void loadData(int year, int month, int day) {

    }


}