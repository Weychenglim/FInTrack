package com.example.fintrack.Saving;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
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

    // Data list for saving transactions
    List<SavingTransactionItem> mDatas;

    // Adapter for the list view
    SavingTransactionAdapter savingTransactionAdapter;

    // UI components
    ListView savingTransactionLv;
    Button savingTransactionRecord;
    View headerView;
    TextView currentAmount, goalAmount, daysLeftHeader, priorityHeader;
    ProgressBar progressBar;

    // Variables for goal details
    double amount, amountLeft, targetAmount, percentage;
    String daysLeft, status;
    int priority, saving_id;

    // Custom dialog for transactions
    SavingTransactionDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        return inflater.inflate(R.layout.fragment_saving_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        savingTransactionLv = view.findViewById(R.id.savingtransaction_lv);
        savingTransactionRecord = view.findViewById(R.id.savingtransaction_btn_record);
        savingTransactionRecord.setVisibility(View.VISIBLE);

        // Handle scroll behavior for hiding/showing the button
        savingTransactionLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isInitialState = true; // Tracks the initial state

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isInitialState) {
                    return; // Skip during the initial state
                }
                if (totalItemCount > visibleItemCount) {
                    savingTransactionRecord.setVisibility(View.GONE); // Hide button while scrolling
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    savingTransactionRecord.setVisibility(View.VISIBLE); // Show button when scrolling stops
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isInitialState = false; // Exit the initial state after user scrolls
                }
            }
        });

        // Inflate and add header view to the ListView
        headerView = getLayoutInflater().inflate(R.layout.saving_transaction_header, null);
        savingTransactionLv.addHeaderView(headerView);

        // Retrieve data passed via arguments
        if (getArguments() != null) {
            amount = getArguments().getDouble("amount");
            amountLeft = getArguments().getDouble("amountLeft");
            targetAmount = getArguments().getDouble("targetAmount");
            daysLeft = getArguments().getString("daysLeft");
            percentage = getArguments().getDouble("percentage");
            priority = getArguments().getInt("priority");
            saving_id = getArguments().getInt(("saving_id"));
            status = getArguments().getString("status");

            // Populate header view with data
            populateHeaderView(amount, amountLeft, targetAmount, daysLeft, percentage, priority);
        }

        // Initialize data list and adapter
        mDatas = new ArrayList<>();
        savingTransactionAdapter = new SavingTransactionAdapter(requireContext(), mDatas);
        savingTransactionLv.setAdapter(savingTransactionAdapter);

        // Handle click event for adding a transaction
        savingTransactionRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("Expired") || status.equals("Completed")) {
                    Toast.makeText(requireContext(), "Current saving goal is no longer valid", Toast.LENGTH_SHORT).show();
                    return;
                }
                showSavingTransactionDialog();
            }
        });

        // Handle long-click event for deleting a transaction
        savingTransactionLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (status.equals("Expired") || status.equals("Completed")) {
                    Toast.makeText(requireContext(), "Current saving goal is no longer valid", Toast.LENGTH_SHORT).show();
                    return false;
                }
                int pos = position - 1; // Adjust for header
                SavingTransactionItem clickItem = mDatas.get(pos);
                showDeleteItemDialog(clickItem);
                return false;
            }
        });
    }

    private void showDeleteItemDialog(SavingTransactionItem clickItem) {
        // Show a confirmation dialog for deleting a transaction
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Notification")
                .setMessage("Do you really want to delete this transaction?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int clickId = clickItem.getId();
                        DBManager.deleteFromSavingTransactionTbBySavingTransactionId(clickId);

                        // Remove item from the list and update the adapter
                        mDatas.remove(clickItem);
                        savingTransactionAdapter.notifyDataSetChanged();
                        Toast.makeText(requireContext(), "Transaction record deleted successfully", Toast.LENGTH_SHORT).show();
                        // Update the header values
                        updateHeaderValues();
                        updateDBData();
                    }
                });
        builder.create().show();
    }

    private void updateHeaderValues() {
        // Query updated values from the database
        double newAmount = DBManager.getTotalTransactionsForSaving(saving_id);
        double newAmountLeft = targetAmount - newAmount;

        // Recalculate and update percentage
        double newPercentage = (targetAmount > 0) ? (newAmount / targetAmount) * 100 : 0;

        // Update header view with new values
        populateHeaderView(newAmount, newAmountLeft, targetAmount, daysLeft, newPercentage, priority);

        // Update local variables
        amount = newAmount;
        amountLeft = newAmountLeft;
        percentage = newPercentage;
    }

    private void loadDBData() {
        // Load data from the database
        List<SavingTransactionItem> list = DBManager.getSavingTransaction(saving_id);
        mDatas.clear();
        mDatas.addAll(list);
        savingTransactionAdapter.notifyDataSetChanged();
        updateDBData();
    }

    private void updateDBData() {
        // Update the saving goal data
        DBManager.updateSavingGoal(saving_id);
        List<SavingItem> savingItems = DBManager.getSavingGoals();
        for (SavingItem items : savingItems) {
            if (items.getId() == saving_id) {
                amount = items.getAmount();
                percentage = items.getPercentage();
                amountLeft = items.getAmountLeft();
                populateHeaderView(amount, amountLeft, targetAmount, daysLeft, percentage, priority);
                break;
            }
        }
    }

    private void populateHeaderView(double amount, double amountLeft, double targetAmount, String daysLeft, double percentage, int priority) {
        // Populate header UI components with data
        currentAmount = headerView.findViewById(R.id.savingheader_currentamount);
        goalAmount = headerView.findViewById(R.id.savingheader_goalamount);
        daysLeftHeader = headerView.findViewById(R.id.savingheader_daysLeft);
        progressBar = headerView.findViewById(R.id.savingheader_progress);
        priorityHeader = headerView.findViewById(R.id.savingHeader_priority);

        currentAmount.setText("RM " + String.format("%.2f", amount));
        goalAmount.setText("Out of RM " + String.format("%.2f", targetAmount));

        // Ensure progress is within 0-100%
        int progress = (int) Math.round(percentage);
        progress = Math.max(0, Math.min(100, progress));
        progressBar.setProgress(progress);

        daysLeftHeader.setText(String.valueOf(progress) + "% | " + daysLeft + " Days Left");

        // Set priority icon and text color
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
        // Refresh data and populate the header view
        populateHeaderView(amount, amountLeft, targetAmount, daysLeft, percentage, priority);
        loadDBData();
    }

    private void showSavingTransactionDialog() {
        // Show a dialog for adding a new transaction
        dialog = new SavingTransactionDialog(requireContext(), amountLeft);
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
                    if (amount > amountLeft) {
                        Toast.makeText(requireContext(), "Amount exceeds the remaining amount for current saving goal.", Toast.LENGTH_SHORT).show();
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
