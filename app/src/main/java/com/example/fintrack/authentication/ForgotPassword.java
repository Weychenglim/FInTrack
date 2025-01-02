package com.example.fintrack.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fintrack.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    // UI Elements
    Button btnForgotPassword, btnBackToLogin; // Buttons for reset action and navigating back to login
    EditText ETEmailForgotPassword; // Input field for the user's email
    FirebaseAuth mAuth; // Firebase Authentication instance
    String email; // Stores the user's email input

    // Animation variables
    Animation topAnim, bottomAnim;

    // TextView and ImageView for the UI
    TextView ForgotPasswordTV;
    ImageView ForgotPasswordIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge UI rendering
        EdgeToEdge.enable(this);

        // Set the layout for the activity
        setContentView(R.layout.activity_forgotpassword);

        // Adjust padding to accommodate system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set the activity to fullscreen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        ETEmailForgotPassword = findViewById(R.id.ETEmailForgotPassword); // Email input field
        btnForgotPassword = findViewById(R.id.btnForgotPassword); // Button to trigger password reset
        btnBackToLogin = findViewById(R.id.btnBackToLogin); // Button to navigate back to login
        ForgotPasswordTV = findViewById(R.id.ForgotPasswordTV); // Forgot password text view
        ForgotPasswordIV = findViewById(R.id.ForgotPasswordIV); // ImageView for Forgot Password

        // Load animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        // Apply animations to UI elements
        ForgotPasswordIV.setAnimation(bottomAnim);
        ForgotPasswordTV.setAnimation(topAnim);

        // Set the click listener for the "Forgot Password" button
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = ETEmailForgotPassword.getText().toString(); // Get the email input
                if (!TextUtils.isEmpty(email)) { // Check if the email field is not empty
                    resetPassword(); // Trigger password reset method
                } else {
                    Toast.makeText(ForgotPassword.this, "Email field can't be empty", Toast.LENGTH_SHORT).show(); // Notify user if the field is empty
                }
            }
        });

        // Set the click listener for the "Back to Login" button
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delay before navigating back to the login screen
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(ForgotPassword.this, login.class); // Intent to navigate to Login activity
                    startActivity(intent);
                    finish(); // Close the current activity
                }, 1000); // Delay of 1 second (1000 milliseconds)
            }
        });
    }

    /**
     * Sends a password reset email to the user.
     */
    private void resetPassword() {
        mAuth.sendPasswordResetEmail(email) // Trigger Firebase's password reset functionality
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Notify the user that the reset email has been sent
                        Toast.makeText(ForgotPassword.this, "Reset password link has been sent", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Notify the user about the failure and provide feedback
                        Toast.makeText(ForgotPassword.this, "Error trying to send email", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

