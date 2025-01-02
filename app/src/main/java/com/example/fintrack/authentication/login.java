package com.example.fintrack.authentication;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fintrack.main.MainActivity;
import com.example.fintrack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    // Firebase Authentication instance
    private FirebaseAuth mAuth;

    // Animations for UI elements
    Animation topAnim, bottomAnim;
    TextView LoginWelcome;
    ImageView LoginIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase and FirebaseAuth instance
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        EditText ETLoginEmail = findViewById(R.id.ETLoginEmail); // Email input field
        EditText ETLoginPassword = findViewById(R.id.ETLoginPassword); // Password input field
        Button btnLogin = findViewById(R.id.btnLogin); // Login button
        LoginIV = findViewById(R.id.LoginIv); // ImageView for login icon
        LoginWelcome = findViewById(R.id.LoginWelcome); // TextView for welcome message

        // Load animations for UI elements
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        // Set animations to respective UI elements
        LoginIV.setAnimation(bottomAnim);
        LoginWelcome.setAnimation(topAnim);

        // Check if the user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Navigate to MainActivity if user is already logged in
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the current activity to prevent back navigation
        }

        // Set up the login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input for email and password
                String email = ETLoginEmail.getText().toString();
                String password = ETLoginPassword.getText().toString();

                // Validate inputs
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(login.this, "Please enter an email address.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(login.this, "Please enter a password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Attempt to sign in the user with Firebase Authentication
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Navigate to MainActivity if login is successful
                                    Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Close the current activity
                                } else {
                                    // Display an error message if authentication fails
                                    Toast.makeText(login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Forgot Password TextView click listener
        TextView TVForgotPassword = findViewById(R.id.TVForgotPassword);
        TVForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ForgotPassword activity
                Intent intent = new Intent(login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        // Sign Up TextView click listener
        TextView TVGoSignup = findViewById(R.id.TVGoSignup);
        TVGoSignup.setOnClickListener(view -> {
            // Navigate to SignUp activity with shared element transition
            Intent intent = new Intent(login.this, SignUp.class);

            // Setting up transition pairs for shared element animations
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair(LoginIV, "splash_text"); // Shared transition for text
            pairs[1] = new Pair(LoginWelcome, "splash_image"); // Shared transition for image

            // Start activity with scene transition animation
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login.this, pairs);
            startActivity(intent, options.toBundle());
        });
    }
}


/*
Yes, in Android, if you do not explicitly set a text color for buttons (or other text-based UI components),
they will typically use the default text color defined by the theme. In the Material theme, the default text color
 for buttons is often derived from the primary color of the theme, specifically colorOnPrimary when the button is
 using the primary background.
 */

