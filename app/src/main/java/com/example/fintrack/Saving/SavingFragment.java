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

    private static final int REQUEST_CODE_READ_STORAGE = 101; // Request code for storage permission
    private static final int PICK_IMAGE_REQUEST = 102; // Request code for image picker

    List<SavingItem> mDatas; // List to hold saving items
    SavingAdapter savingAdapter; // Adapter for the ListView

    ListView savingLv; // ListView to display saving items
    Button addGoal; // Button to add a new saving goal

    SavingItem currentItem; // Currently selected saving item

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestStoragePermission(); // Request storage permission on fragment creation
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_saving, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the Add Goal button and set its click listener
        addGoal = view.findViewById(R.id.saving_btn_addgoal);
        addGoal.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.fragmentCreateSaving)
        );

        // Initialize the ListView and set up its adapter
        savingLv = view.findViewById(R.id.saving_lv);
        mDatas = new ArrayList<>();
        savingAdapter = new SavingAdapter(requireContext(), mDatas);
        savingLv.setAdapter(savingAdapter);

        // Set scroll listener for the ListView to hide/show the Add Goal button
        savingLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isInitialState = true; // Tracks initial state

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d("ListenerCheck", "OnScroll Triggered");

                // Prevent hiding the button during the initial state
                if (isInitialState) {
                    Log.d("ListenerCheck", "Initial State - Do not hide button");
                    return;
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    addGoal.setVisibility(View.VISIBLE); // Show button when scrolling stops
                    Log.d("ListenerCheck", "ScrollStateChanged Triggered: Button Visible");
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    isInitialState = false; // Exit initial state on scroll
                    Log.d("ListenerCheck", "User started scrolling, exiting initial state");
                    addGoal.setVisibility(View.GONE); // Hide button during scrolling
                }
            }
        });

        // Handle item click to navigate to SavingTransactionFragment with item details
        savingLv.setOnItemClickListener((parent, view1, position, id) -> {
            SavingItem clickItem = mDatas.get(position);
            currentItem = clickItem;
            Bundle bundle = new Bundle();
            bundle.putInt("saving_id", clickItem.getId());
            bundle.putDouble("amount", clickItem.getAmount());
            bundle.putDouble("amountLeft", clickItem.getAmountLeft());
            bundle.putDouble("targetAmount", clickItem.getTargetAmount());
            bundle.putString("daysLeft", clickItem.getDayLeft());
            bundle.putDouble("percentage", clickItem.getPercentage());
            bundle.putInt("priority", clickItem.getPriority());
            bundle.putString("status", clickItem.getStatus());
            Navigation.findNavController(view1).navigate(R.id.savingTransactionFragment, bundle);
        });

        // Handle long item click to show delete confirmation dialog
        savingLv.setOnItemLongClickListener((parent, view12, position, id) -> {
            SavingItem clickItem = mDatas.get(position);
            showDeleteItemDialog(clickItem);
            return true;
        });
    }

    // Shows a dialog to confirm deletion of a saving item
    private void showDeleteItemDialog(SavingItem clickItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Notification")
                .setMessage("Do you really want to delete this saving goal?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    int clickId = clickItem.getId();
                    DBManager.deleteFromSavingTbById(clickId); // Delete from database
                    DBManager.deleteFromSavingTransactionTbById(clickId); // Delete related transactions
                    mDatas.remove(clickItem); // Remove from the list
                    savingAdapter.notifyDataSetChanged(); // Refresh the ListView
                    Toast.makeText(requireContext(), "Saving goal deleted successfully", Toast.LENGTH_SHORT).show();
                });
        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Resume", "Resume");
        loadDBData(); // Load database data when the fragment resumes
    }

    // Load data from the database into the ListView
    private void loadDBData() {
        List<SavingItem> list = DBManager.getSavingGoals();
        for (SavingItem item : list) {
            String uriString = item.getImageUri();
            if (uriString != null) {
                Uri imageUri = Uri.parse(uriString);
                try {
                    requireContext().getContentResolver().openInputStream(imageUri).close();
                    Log.d("URICheck", "URI is accessible");
                } catch (Exception e) {
                    imageUri = null; // Reset if inaccessible
                    Log.d("URICheck", "URI is not accessible");
                }
                item.setImageUri(imageUri != null ? imageUri.toString() : null);
            }
        }
        mDatas.clear();
        mDatas.addAll(list); // Update the data list
        savingAdapter.notifyDataSetChanged(); // Refresh the adapter
    }

    // Request storage permission if not already granted
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_STORAGE);
        }
    }
}
