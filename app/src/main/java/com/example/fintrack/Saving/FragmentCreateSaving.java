package com.example.fintrack.Saving;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fintrack.R;
import com.example.fintrack.db.DBManager;

public class FragmentCreateSaving extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_READ_STORAGE = 101; // Code for storage permission request
    private static final int PICK_IMAGE_REQUEST = 1; // Code for picking an image from the gallery

    Button highBtn, normalBtn, lowBtn; // Priority selection buttons
    AppCompatButton submit; // Submit button
    EditText goalTitle, targetAmount, duration; // Input fields for saving goal
    TextView icon; // TextView to display selected icon/image status

    Uri selectedImageUri; // URI for the selected image
    private String selectedPriority; // Selected priority level

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_saving, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view); // Initialize views and set up listeners
    }

    // Initialize all views and set click listeners
    private void initView(View view) {
        highBtn = view.findViewById(R.id.Highbtn);
        normalBtn = view.findViewById(R.id.Normalbtn);
        lowBtn = view.findViewById(R.id.Lowbtn);
        goalTitle = view.findViewById(R.id.goal);
        targetAmount = view.findViewById(R.id.amount);
        duration = view.findViewById(R.id.duration);
        submit = view.findViewById(R.id.saving_btn_submit);
        icon = view.findViewById(R.id.IconTv1);

        highBtn.setOnClickListener(this);
        normalBtn.setOnClickListener(this);
        lowBtn.setOnClickListener(this);
        submit.setOnClickListener(this);
        icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.Highbtn){
            selectedPriority = "High";
            updatePriorityUI(highBtn, normalBtn, lowBtn);
        }else if (v.getId() == R.id.Normalbtn) {
            selectedPriority = "Normal";
            updatePriorityUI(normalBtn, highBtn, lowBtn);
        } else if ( v.getId() == R.id.Lowbtn){
            selectedPriority = "Low";
            updatePriorityUI(lowBtn, highBtn, normalBtn);
        } else if (v.getId() == R.id.saving_btn_submit) {
            DBManager.saveSavingGoal(requireContext(),goalTitle.getText().toString(),targetAmount.getText().toString(),duration.getText().toString(),selectedPriority,selectedImageUri != null ? selectedImageUri.toString() : null );
            goalTitle.setText("");
            targetAmount.setText("");
            duration.setText("");
            icon.setText("Select icon for your goal");
        }else if (v.getId() == R.id.IconTv1){
            requestStoragePermission();
        }
    }

    // Update UI to highlight the selected priority button and dim others
    private void updatePriorityUI(Button selected, Button... others) {
        Drawable selectedBackground = selected.getBackground().mutate(); // Clone the drawable
        selectedBackground.setAlpha(255); // Fully opaque
        selected.setBackground(selectedBackground); // Apply updated background to selected button

        for (Button btn : others) {
            Drawable background = btn.getBackground().mutate(); // Clone the drawable
            background.setAlpha(128); // Semi-transparent
            btn.setBackground(background); // Apply updated background to other buttons
        }
    }

    // Request permission to access external storage
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_STORAGE);
        } else {
            // If permission is granted, allow picking an image
            pickImageFromGallery();
        }
    }

    // Launch an intent to pick an image from the gallery
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*"); // Allow only image files
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of the image picker intent
    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                this.selectedImageUri = selectedImageUri; // Save the selected image URI
                icon.setText("Image Selected"); // Update icon TextView

                try {
                    // Persist URI permissions to maintain access even after app restarts
                    final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    requireContext().getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
                } catch (SecurityException e) {
                    Toast.makeText(requireContext(), "Unable to persist URI permissions", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, allow picking an image
            pickImageFromGallery();
        } else {
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}
