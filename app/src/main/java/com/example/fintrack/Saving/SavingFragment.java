package com.example.fintrack.Saving;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.fintrack.R;
import com.example.fintrack.adapter.AccountAdapter;
import com.example.fintrack.adapter.SavingAdapter;
import com.example.fintrack.db.AccountItem;
import com.example.fintrack.db.DBManager;
import com.example.fintrack.db.SavingItem;

import java.util.ArrayList;
import java.util.List;

public class SavingFragment extends Fragment {

    List<SavingItem> mDatas;
    SavingAdapter savingAdapter;

    ListView savingLv;
    Button addGoal;

    SavingItem currentItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saving, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addGoal = view.findViewById(R.id.saving_btn_addgoal);
        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.fragmentCreateSaving);
            }
        });
        savingLv = view.findViewById(R.id.saving_lv);
        mDatas = new ArrayList<>();
        savingAdapter = new SavingAdapter(requireContext(), mDatas);
        savingLv.setAdapter(savingAdapter);
        savingLv.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    addGoal.setVisibility(View.VISIBLE); // Show the button when scrolling stops
                    Log.d("ListenerCheck", "ScrollStateChanged Triggered: Button Visible");
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isInitialState = false; // User has started scrolling, mark initial state as finished
                    Log.d("ListenerCheck", "User started scrolling, exiting initial state");
                    addGoal.setVisibility(View.GONE);
                }
            }
        });
        savingLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SavingItem clickItem = mDatas.get(position);
                currentItem = clickItem;
                Log.d("df",clickItem.getGoalTitle());
                Bundle bundle = new Bundle();
                bundle.putInt("saving_id",clickItem.getId());
                bundle.putDouble("amount", clickItem.getAmount());
                bundle.putDouble("amountLeft", clickItem.getAmountLeft());
                bundle.putDouble("targetAmount", clickItem.getTargetAmount());
                bundle.putString("daysLeft",clickItem.getDayLeft());
                bundle.putDouble("percentage", clickItem.getPercentage());
                bundle.putInt("priority", clickItem.getPriority());
                Navigation.findNavController(view).navigate(R.id.savingTransactionFragment,bundle);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDBData();
    }



    private void loadDBData() {
        List<SavingItem> list = DBManager.getSavingGoals();
        mDatas.clear();
        mDatas.addAll(list);
        savingAdapter.notifyDataSetChanged();
    }
}