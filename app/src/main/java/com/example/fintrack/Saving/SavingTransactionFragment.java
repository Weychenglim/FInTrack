package com.example.fintrack.Saving;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fintrack.R;
import com.example.fintrack.adapter.SavingAdapter;
import com.example.fintrack.adapter.SavingTransactionAdapter;
import com.example.fintrack.db.DBManager;
import com.example.fintrack.db.SavingItem;
import com.example.fintrack.db.SavingTransactionItem;
import com.example.fintrack.utils.BudgetDialog;
import com.example.fintrack.utils.SavingTransactionDialog;

import java.util.ArrayList;
import java.util.List;

public class SavingTransactionFragment extends Fragment {

    List<SavingTransactionItem> mDatas;

    SavingTransactionAdapter savingTransactionAdapter;

    ListView savingTransactionLv;

    Button savingTransactionRecord;

    View headerView;

    TextView currentAmount, goalAmount, daysLeftHeader, priorityHeader;
    ProgressBar progressBar;
    double amount, amountLeft,targetAmount,percentage;
    String daysLeft;
    int priority, saving_id;
    SavingTransactionDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saving_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        savingTransactionLv = view.findViewById(R.id.savingtransaction_lv);
        savingTransactionRecord = view.findViewById(R.id.savingtransaction_btn_record);
        savingTransactionRecord.setVisibility(View.VISIBLE);
        // Use a flag to prevent hiding the button during the initial rendering
        savingTransactionLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isInitialState = true; // Tracks whether it's the initial state

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("ListenerCheck", "OnScroll Triggered");

                if (isInitialState) {
                    Log.d("ListenerCheck", "Initial State - Do not hide button");
                    return; // Do nothing during the initial state
                }

                // Hide the button as soon as scrolling starts
                if (totalItemCount > visibleItemCount) {
                    savingTransactionRecord.setVisibility(View.GONE);
                    Log.d("ListenerCheck", "Set non-visible");
                } else {
                    Log.d("ListenerCheck", "List is not scrollable.");
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    savingTransactionRecord.setVisibility(View.VISIBLE); // Show the button when scrolling stops
                    Log.d("ListenerCheck", "ScrollStateChanged Triggered: Button Visible");
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isInitialState = false; // User has started scrolling, mark initial state as finished
                    Log.d("ListenerCheck", "User started scrolling, exiting initial state");
                }
            }
        });
        headerView = getLayoutInflater().inflate(R.layout.saving_transaction_header, null);
        savingTransactionLv.addHeaderView(headerView);
        if (getArguments() != null) {
            amount = getArguments().getDouble("amount");
            amountLeft = getArguments().getDouble("amountLeft");
            targetAmount = getArguments().getDouble("targetAmount");
            daysLeft = getArguments().getString("daysLeft");
            percentage = getArguments().getDouble("percentage");
            priority = getArguments().getInt("priority");
            saving_id = getArguments().getInt(("saving_id"));

            // Populate the header view
            populateHeaderView(amount, amountLeft, targetAmount,daysLeft, percentage,priority);
        }
        mDatas = new ArrayList<>();
        savingTransactionAdapter = new SavingTransactionAdapter(requireContext(), mDatas);
        savingTransactionLv.setAdapter(savingTransactionAdapter);
        savingTransactionRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSavingTransactionDialog();
            }
        });
        mDatas = new ArrayList<>();
        savingTransactionAdapter = new SavingTransactionAdapter(requireContext(), mDatas);
        savingTransactionLv.setAdapter(savingTransactionAdapter);
    }

    private void loadDBData() {
        List<SavingTransactionItem> list = DBManager.getSavingTransaction(saving_id);
        mDatas.clear();
        mDatas.addAll(list);
        savingTransactionAdapter.notifyDataSetChanged();
        updateDBData();
    }

    private void updateDBData() {
        DBManager.updateSavingGoal(saving_id);
        List<SavingItem> savingItems = DBManager.getSavingGoals();
        for (SavingItem items : savingItems){
            if (items.getId() == saving_id){
                amount = items.getAmount();
                percentage = items.getPercentage();
                amountLeft = items.getAmountLeft();
                populateHeaderView(amount, amountLeft, targetAmount,daysLeft, percentage,priority);
                break;
            }
        }

    }

    private void populateHeaderView(double amount, double amountLeft, double targetAmount, String daysLeft, double percentage, int priority) {
        currentAmount = headerView.findViewById(R.id.savingheader_currentamount);
        goalAmount = headerView.findViewById(R.id.savingheader_goalamount);
        daysLeftHeader = headerView.findViewById(R.id.savingheader_daysLeft);
        progressBar = headerView.findViewById(R.id.savingheader_progress);
        priorityHeader = headerView.findViewById(R.id.savingHeader_priority);

        currentAmount.setText("RM " + String.format("%.2f",amount));
        goalAmount.setText("Out of RM " + String.format("%.2f",targetAmount));
        int progress = (int) Math.round(percentage);
        progress = Math.max(0, Math.min(100, progress));
        progressBar.setProgress(progress);
        daysLeftHeader.setText(String.valueOf(progress) + "% | " + daysLeft + " Days Left");
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.priority_icon);
        if (drawable != null) {
            drawable.setTint(priority == 1 ? Color.RED :
                    priority == 2 ? Color.parseColor("#FFCC00") : // Orange Yellow
                            Color.GREEN);
            priorityHeader.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        priorityHeader.setText("Goal Priority is " + (priority == 1 ? "High" : priority == 2 ? "Normal" : "Low"));
    }

    @Override
    public void onResume() {
        super.onResume();
        populateHeaderView(amount, amountLeft, targetAmount,daysLeft, percentage,priority);
        loadDBData();
    }

    private void showSavingTransactionDialog() {
        dialog = new SavingTransactionDialog(requireContext(),amountLeft);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnConfirmListener(new SavingTransactionDialog.onConfirmListener() {
            @Override
            public void onConfirm() {
                String inputText = dialog.getEditText();
                if (inputText.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter an amount.", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    double amount = Double.parseDouble(inputText);
                    if (amount <= 0) {
                        Toast.makeText(requireContext(), "Amount must be greater than zero.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DBManager.addTransactionRecord(amount, saving_id);
                    Toast.makeText(requireContext(), "Transaction added successfully!", Toast.LENGTH_SHORT).show();
                    loadDBData();
                    dialog.dismiss(); // Close dialog after successful addition
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Invalid amount. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}