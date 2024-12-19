package com.example.fintrack.Saving;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.fintrack.R;
import com.example.fintrack.db.DBManager;

public class FragmentCreateSaving extends Fragment implements View.OnClickListener {

    Button highBtn, normalBtn, lowBtn;
    AppCompatButton submit;

    EditText goalTitle, targetAmount, duration;

    private String selectedPriority;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_saving, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        highBtn = view.findViewById(R.id.Highbtn);
        normalBtn = view.findViewById(R.id.Normalbtn);
        lowBtn = view.findViewById(R.id.Lowbtn);
        goalTitle = view.findViewById(R.id.goal);
        targetAmount = view.findViewById(R.id.amount);
        duration = view.findViewById(R.id.duration);
        submit = view.findViewById(R.id.saving_btn_submit);
        highBtn.setOnClickListener(this);
        normalBtn.setOnClickListener(this);
        lowBtn.setOnClickListener(this);
        submit.setOnClickListener(this);
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
            DBManager.saveSavingGoal(requireContext(),goalTitle.getText().toString(),targetAmount.getText().toString(),duration.getText().toString(),selectedPriority);
            goalTitle.setText("");
            targetAmount.setText("");
            duration.setText("");
        }
    }

    private void updatePriorityUI(Button selected, Button... others) {
        // Highlight the selected button
        Drawable selectedBackground = selected.getBackground().mutate(); // Create a unique drawable instance
        selectedBackground.setAlpha(255); // Fully opaque
        selected.setBackground(selectedBackground); // Apply the updated background

        // Dim the other buttons
        for (Button btn : others) {
            Drawable background = btn.getBackground().mutate(); // Create a unique drawable instance
            background.setAlpha(128); // Semi-transparent
            btn.setBackground(background); // Apply the updated background
        }
    }


}