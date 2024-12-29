package com.example.fintrack.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fintrack.R;
import com.example.fintrack.authentication.login;
import com.example.fintrack.db.DBManager;
import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment implements View.OnClickListener {

    ImageButton clearAllRecord, signOut;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearAllRecord = view.findViewById(R.id.right_arrow_1);
        signOut = view.findViewById(R.id.right_arrow_2);
        clearAllRecord.setOnClickListener(this);
        signOut.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.right_arrow_1){
            showDeleteDialog();
        } else if (v.getId() == R.id.right_arrow_2) {
            showSignOutDialog();
        }
    }

    private void showSignOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext()); // Use `this` for Activity context
        builder.setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setNegativeButton("Cancel", null) // Close the dialog without action
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut(); // Sign out from Firebase
                        Toast.makeText(getActivity(), "Successfully signed out!", Toast.LENGTH_SHORT).show();

                        // Navigate back to the login screen
                        Intent intent = new Intent(getActivity(), login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the activity stack
                        startActivity(intent);
                        requireActivity().finish(); // Close the current activity
                    }
                });
        builder.create().show();
    }


    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Notification").setMessage("Do you really want to clear all the record?\nWarning: Deletion is permanent and cannot be undone. Please proceed with caution.").setNegativeButton("Cancel", null).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBManager.deleteAllRecord();
                Toast.makeText(requireContext(),"Successfully cleared!",Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

}