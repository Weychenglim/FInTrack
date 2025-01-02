package com.example.fintrack.main;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fintrack.R;
import com.example.fintrack.adapter.AccountAdapter;
import com.example.fintrack.authentication.SignUp;
import com.example.fintrack.db.AccountItem;
import com.example.fintrack.db.DBManager;
import com.example.fintrack.utils.BudgetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main_Fragment extends Fragment implements View.OnClickListener, MainActivity.OnSearchQueryListener {

    // Constant for SharedPreferences file name
    private static final String PREF_FILE_NAME = "BudgetPrefs";

    // UI Components
    ListView todayLv;
    List<AccountItem> mDatas;
    AccountAdapter adapter;
    int year, month, day;

    // Overview TextViews and Buttons
    TextView overview1, overview2;
    Button record;
    View headerView;

    // Top-level TextViews and ImageView
    TextView topOutTv, topInTv, topbudgetTv, topConTv, topConTv2;
    ImageView topShowIv;

    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fragment initialization logic
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the current date
        initTime();

        // Initialize views and setup event listeners
        initView(view);

        // Initialize SharedPreferences for storing budget data
        preferences = requireContext().getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        // Setup ListView and adapter
        todayLv = view.findViewById(R.id.main_lv);
        addLVHeaderView(); // Add header to ListView
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(requireContext(), mDatas);
        todayLv.setAdapter(adapter);
    }

    private void initView(View view) {
        // Initialize UI components
        todayLv = view.findViewById(R.id.main_lv);
        record = view.findViewById(R.id.main_btn_edit);

        // Set the button to be visible initially
        record.setVisibility(View.VISIBLE);

        // Add scroll listener to hide/show button based on scroll state
        todayLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isInitialState = true;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Handle scroll events, hiding button during scrolling
                if (isInitialState) {
                    return; // Skip processing during the initial state
                }
                if (totalItemCount > visibleItemCount) {
                    record.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Show button when scrolling stops
                if (scrollState == SCROLL_STATE_IDLE) {
                    record.setVisibility(View.VISIBLE);
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isInitialState = false; // User starts scrolling, exiting initial state
                }
            }
        });

        // Set click listener for record button
        record.setOnClickListener(this);

        // Set long click listener for ListView items
        setLVLongClickListener();
    }

    private void setLVLongClickListener() {
        // Handle long clicks on ListView items to delete entries
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return false; // Ignore header click
                }
                int pos = position - 1; // Adjust for header offset
                AccountItem clickItem = mDatas.get(pos);
                showDeleteItemDialog(clickItem); // Show confirmation dialog
                return false;
            }
        });
    }

    private void showDeleteItemDialog(AccountItem clickItem) {
        // Create and show a dialog to confirm deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Notification")
                .setMessage("Do you really want to delete this record?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the item from database and update UI
                        int clickId = clickItem.getId();
                        DBManager.deleteFromAccountTbById(clickId);
                        mDatas.remove(clickItem);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(requireContext(), "Transaction record deleted successfully", Toast.LENGTH_SHORT).show();
                        setTopTvShow(); // Refresh overview
                    }
                });
        builder.create().show();
    }

    private void addLVHeaderView() {
        // Inflate and configure the ListView header
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);

        // Initialize header components
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topConTv2 = headerView.findViewById(R.id.item_mainlv_top_tv_day2);
        topShowIv = headerView.findViewById(R.id.item_main_top_Iv_hide);
        overview1 = headerView.findViewById(R.id.item_mainlv_top_overview1);
        overview2 = headerView.findViewById(R.id.item_mainlv_top_overview2);

        // Set click listeners for header components
        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);
        overview1.setOnClickListener(this);
        overview2.setOnClickListener(this);
    }

    private void initTime() {
        // Get current date values
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload data and refresh UI when fragment resumes
        loadDBData();
        setTopTvShow();
    }

    private void setTopTvShow() {
        // Update the overview data with daily and monthly stats
        double incomePerDay = DBManager.getSumMoneyPerDay(year, month, day, 1);
        double expensePerDay = DBManager.getSumMoneyPerDay(year, month, day, 0);
        String infoPerDay = "Today's Expense  -   RM " + String.format("%.2f", expensePerDay);
        topConTv.setText(infoPerDay);
        infoPerDay = "Today's Income    -   RM " + String.format("%.2f", incomePerDay);
        topConTv2.setText(infoPerDay);
        double incomePerMonth = DBManager.getSumMoneyPerMonth(year, month, 1);
        double expensePerMonth = DBManager.getSumMoneyPerMonth(year, month, 0);
        topInTv.setText("RM " + String.format("%.2f", incomePerMonth));
        topOutTv.setText("RM " + String.format("%.2f", expensePerMonth));
        float amount_update = preferences.getFloat("amount", 0);
        if (amount_update == 0) {
            topbudgetTv.setText("RM 0");
        } else {
            float final_amount = amount_update - (float) expensePerMonth;
            topbudgetTv.setText("RM " + String.format("%.2f", final_amount));
            checkNegativeBudget(final_amount); // Notify user if budget is negative
        }
    }

    private void loadDBData() {
        // Load daily data from the database and refresh the adapter
        List<AccountItem> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        // Handle click events for buttons and header components
        if (v.getId() == R.id.main_btn_edit) {
            Navigation.findNavController(v).navigate(R.id.recordFragment);
        } else if (v.getId() == R.id.item_main_top_Iv_hide) {
            toggleShow();
        } else if (v.getId() == R.id.item_mainlv_top_tv_budget) {
            showBudgetDialog();
        } else if (v.getId() == R.id.item_mainlv_top_overview1 || v.getId() == R.id.item_mainlv_top_overview2) {
            Navigation.findNavController(v).navigate(R.id.overview);
        }
    }

    private void showBudgetDialog() {
        // Show a dialog to update the budget
        BudgetDialog dialog = new BudgetDialog(requireContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnConfirmListener(new BudgetDialog.onConfirmListener() {
            @Override
            public void onConfirm(double amount) {
                // Save the new budget value
                preferences = requireContext().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("amount", (float) amount);
                editor.apply(); // Commit asynchronously

                // Update budget balance and check for negative value
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                double spending = DBManager.getSumMoneyPerMonth(year, month, 0);
                double balance = amount - spending;
                topbudgetTv.setText("RM " + String.format("%.2f", balance));

                checkNegativeBudget(balance);
            }
        });
    }

    private void checkNegativeBudget(double balance) {
        // Notify the user if the budget goes negative
        if (balance < 0) {
            Context context = requireContext();
            String channelId = "negative_budget_channel";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Negative Budget Alerts",
                        NotificationManager.IMPORTANCE_HIGH
                );
                NotificationManager manager = context.getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Budget Overrun Alert")
                    .setContentText("Your budget is negative: RM " + String.format("%.2f", balance))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            notificationManager.notify(1, builder.build());
        }
    }

    boolean isShown = true;

    private void toggleShow() {
        // Toggle visibility of income, expense, and budget data
        if (isShown) {
            PasswordTransformationMethod instance = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(instance);
            topOutTv.setTransformationMethod(instance);
            topbudgetTv.setTransformationMethod(instance);
            topShowIv.setImageResource(R.drawable.eye_close_svgrepo_com);
            isShown = false;
        } else {
            HideReturnsTransformationMethod instance = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(instance);
            topOutTv.setTransformationMethod(instance);
            topbudgetTv.setTransformationMethod(instance);
            topShowIv.setImageResource(R.drawable.show_svgrepo_com);
            isShown = true;
        }
    }

    public void onSearchQueryChanged(String query) {
        // Handle search query changes to filter ListView data
        if (adapter != null) {
            if (query.isEmpty()) {
                // Reset to full list if query is empty
                loadDBData();
                setTopTvShow();
            } else {
                // Filter the data based on the query
                loadDBData();
                List<AccountItem> filteredList = new ArrayList<>();
                for (AccountItem item : mDatas) {
                    if (item.getTypename().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                adapter.updateData(filteredList); // Update adapter with filtered data
            }
        }
    }
}

