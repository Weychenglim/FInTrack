package com.example.fintrack;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fintrack.adapter.AccountAdapter;
import com.example.fintrack.db.AccountItem;
import com.example.fintrack.db.DBManager;
import com.example.fintrack.utils.BudgetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main_Fragment extends Fragment implements View.OnClickListener, MainActivity.OnSearchQueryListener {

    ListView todayLv;
    List<AccountItem> mDatas;

    AccountAdapter adapter;

    int year, month, day;

    TextView overview1, overview2;
    Button record;

    View headerView;

    TextView topOutTv, topInTv, topbudgetTv, topConTv, topConTv2;

    ImageView topShowIv;

    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        initTime();
        initView(view);
        preferences = requireContext().getSharedPreferences("budget", MODE_PRIVATE);  /*This method retrieves and initialize a SharedPreferences instance that points to the file
        named "budget" where data can be stored. If this file doesn’t exist, Android will create it. file is private to the app*/
        todayLv = view.findViewById(R.id.main_lv);
        addLVHeaderView();
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(requireContext(), mDatas);
        todayLv.setAdapter(adapter);
    }


    private void initView(View view) {
        todayLv = view.findViewById(R.id.main_lv);
        record = view.findViewById(R.id.main_btn_edit);

        // Ensure the button is visible initially
        record.setVisibility(View.VISIBLE);
        // Use a flag to prevent hiding the button during the initial rendering
        todayLv.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    record.setVisibility(View.GONE);
                    Log.d("ListenerCheck", "Set non-visible");
                } else {
                    Log.d("ListenerCheck", "List is not scrollable.");
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    record.setVisibility(View.VISIBLE); // Show the button when scrolling stops
                    Log.d("ListenerCheck", "ScrollStateChanged Triggered: Button Visible");
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isInitialState = false; // User has started scrolling, mark initial state as finished
                    Log.d("ListenerCheck", "User started scrolling, exiting initial state");
                }
            }
        });

        record.setOnClickListener(this);
        setLVLongClickListener();
    }

    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return false;
                }
                int pos = position - 1;
                AccountItem clickItem = mDatas.get(pos);
                showDeleteItemDialog(clickItem);
                return false;
            }
        });
    }

    private void showDeleteItemDialog(AccountItem clickItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Notification").setMessage("Do you really want to delete this record?").setNegativeButton("Cancel", null).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int clickId = clickItem.getId();
                DBManager.deleteFromAccountTbById(clickId);
                mDatas.remove(clickItem);
                adapter.notifyDataSetChanged();
                setTopTvShow();
            }
        });
        builder.create().show();
    }


    private void addLVHeaderView() {
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);

        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topConTv2 = headerView.findViewById(R.id.item_mainlv_top_tv_day2);
        topShowIv = headerView.findViewById(R.id.item_main_top_Iv_hide);
        overview1 = headerView.findViewById(R.id.item_mainlv_top_overview1);
        overview2 = headerView.findViewById(R.id.item_mainlv_top_overview2);

        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);
        overview1.setOnClickListener(this);
        overview2.setOnClickListener(this);
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }

    private void setTopTvShow() {
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
        }
    }

    private void loadDBData() {
        List<AccountItem> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_btn_edit) {
            Navigation.findNavController(v).navigate(R.id.recordFragment);
        } else if (v.getId() == R.id.item_main_top_Iv_hide) {
            toggleShow();
        } else if (v.getId() == R.id.item_mainlv_top_tv_budget) {
            showBudgetDialog();
        } else if (v.getId() == R.id.item_mainlv_top_overview1 || v.getId() == R.id.item_mainlv_top_overview2){
            Navigation.findNavController(v).navigate(R.id.overview);
        }
    }

    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(requireContext()); // fragment use getContext() / requireContext()
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
                editor.putFloat("amount", (float) amount);
                editor.commit();

                double spending = DBManager.getSumMoneyPerMonth(year, month, 0);
                double balance = amount - spending;
                topbudgetTv.setText("RM " + String.format("%.2f", balance));
            }
        });

    }

    boolean isShown = true;

    private void toggleShow() {
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
        if (adapter != null) {
            if (query.isEmpty()) {
                // Restore the full list if the query is empty
                loadDBData(); // This resets mDatas to the full original list
                setTopTvShow();
            } else {
                // Filter starting from the original full data list (mDatas)
                loadDBData();
                List<AccountItem> filteredList = new ArrayList<>();
                for (AccountItem item : mDatas) {  //The underlying behavior here is how mDatas is used for filtering: once it is filtered, it holds only the filtered results unless explicitly reset.
                    if (item.getTypename().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                // Update the adapter with the new filtered list
                adapter.updateData(filteredList);
            }
        }
    }

}
