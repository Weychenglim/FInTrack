package com.example.fintrack.Saving;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fintrack.R;
import com.example.fintrack.adapter.AccountAdapter;
import com.example.fintrack.adapter.SavingAdapter;
import com.example.fintrack.db.AccountItem;
import com.example.fintrack.db.DBManager;
import com.example.fintrack.db.SavingItem;

import java.util.ArrayList;
import java.util.List;

public class SavingFragment extends Fragment {

    private static final int REQUEST_CODE_READ_STORAGE = 101;
    private static final int PICK_IMAGE_REQUEST = 102;

    List<SavingItem> mDatas;
    SavingAdapter savingAdapter;

    ListView savingLv;
    Button addGoal;

    SavingItem currentItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestStoragePermission();
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
                Bundle bundle = new Bundle();
                bundle.putInt("saving_id",clickItem.getId());
                bundle.putDouble("amount", clickItem.getAmount());
                bundle.putDouble("amountLeft", clickItem.getAmountLeft());
                bundle.putDouble("targetAmount", clickItem.getTargetAmount());
                bundle.putString("daysLeft",clickItem.getDayLeft());
                bundle.putDouble("percentage", clickItem.getPercentage());
                bundle.putInt("priority", clickItem.getPriority());
                bundle.putString("status",clickItem.getStatus());
                Navigation.findNavController(view).navigate(R.id.savingTransactionFragment,bundle);
            }
        });

        savingLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SavingItem clickItem = mDatas.get(position);
                showDeleteItemDialog(clickItem);
                return true;
            }
        });
    }

    private void showDeleteItemDialog(SavingItem clickItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Notification").setMessage("Do you really want to delete this saving goal?").setNegativeButton("Cancel", null).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int clickId = clickItem.getId();
                DBManager.deleteFromSavingTbById(clickId);
                DBManager.deleteFromSavingTransactionTbById(clickId);
                mDatas.remove(clickItem);
                savingAdapter.notifyDataSetChanged();
            }
        });
        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Resume", "Resume");
        loadDBData();
    }



    private void loadDBData() {
        List<SavingItem> list = DBManager.getSavingGoals();
        for (SavingItem item : list) {
            // Retrieve the URI and validate it
            String uriString = item.getImageUri();
            if (uriString != null) {
                Uri imageUri = Uri.parse(uriString);
                // Check if the URI is accessible
                try {
                    requireContext().getContentResolver().openInputStream(imageUri).close();
                    Log.d("dsf", "Successful");
                } catch (Exception e) {
                    imageUri = null; // Reset if inaccessible
                    Log.d("dsf", "Failed");
                }
                item.setImageUri(imageUri != null ? imageUri.toString() : null);
            }
        }
        mDatas.clear();
        mDatas.addAll(list);
        savingAdapter.notifyDataSetChanged();
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_STORAGE);
        } else {

        }
    }

}