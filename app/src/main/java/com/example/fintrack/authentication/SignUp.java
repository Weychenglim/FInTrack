package com.example.fintrack.authentication;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fintrack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    // Firebase Authentication instance
    private FirebaseAuth mAuth;

    // Animations for UI elements
    Animation topAnim, bottomAnim;

    TextView welcome; // Welcome message TextView
    TextView text; // Additional TextView for welcome section
    TextView login; // Login TextView for navigation to Login screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge UI rendering
        EdgeToEdge.enable(this);

        // Set the layout for the activity
        setContentView(R.layout.activity_sign_up);

        // Adjust padding to accommodate system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set the activity to fullscreen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Load animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        // Initialize UI elements for the welcome section
        welcome = findViewById(R.id.welcome);
        text = findViewById(R.id.text11);

        // Set animations to UI elements
        welcome.setAnimation(topAnim);
        text.setAnimation(topAnim);

        // Initialize the login TextView and set click listener
        login = findViewById(R.id.TVLogin);
        login.setOnClickListener(view -> {
            // Set up shared element transitions
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair(welcome, "splash_image");
            pairs[1] = new Pair(text, "splash_image");

            // Create transition options
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);

            // Delay before navigating to the Login activity
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SignUp.this, com.example.fintrack.authentication.login.class);
                startActivity(intent, options.toBundle());
            }, 1000); // Delay of 1000 milliseconds
        });

        // Initialize Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();

        // Input fields for the sign-up form
        EditText ETUsername = findViewById(R.id.ETUsername); // Username input
        EditText ETSignupEmail = findViewById(R.id.ETSignupEmail); // Email input
        EditText ETSignupPassword = findViewById(R.id.ETSignupPassword); // Password input
        EditText ETSignupPasswordConfirm = findViewById(R.id.ETSignupPasswordConfirm); // Confirm password input

        // Button to handle the sign-up process
        Button btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String username = ETUsername.getText().toString();
                String email = ETSignupEmail.getText().toString();
                String password = ETSignupPassword.getText().toString();
                String passwordConfirm = ETSignupPasswordConfirm.getText().toString();

                // Validate user input
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(SignUp.this, "Please enter a username.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUp.this, "Please enter an email address.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!email.contains("@") || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignUp.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUp.this, "Please enter a password.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.length() < 8 ||
                        !password.matches(".*[A-Z].*") ||
                        !password.matches(".*[a-z].*") ||
                        !password.matches(".*[0-9].*") ||
                        !password.matches(".*[!@#$%^&*()].*")) {
                    Toast.makeText(SignUp.this, "Password must be at least 8 characters long, including at least one uppercase letter, one lowercase letter, one digit, and one special character.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(passwordConfirm)) {
                    Toast.makeText(SignUp.this, "Please confirm your password.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!password.equals(passwordConfirm)) {
                    Toast.makeText(SignUp.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new user with Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Navigate to the Login activity if sign-up is successful
                                    Toast.makeText(SignUp.this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUp.this, login.class);
                                    startActivity(intent);
                                    finish(); // Close the current activity
                                } else {
                                    // Display error message if sign-up fails
                                    Toast.makeText(SignUp.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Additional TextView to navigate to the Login screen
        TextView TVGoLogin = findViewById(R.id.TVLogin);
        TVGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Login activity
                Intent intent = new Intent(SignUp.this, login.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity
            }
        });
    }
}


/*n Android, shared element transitions are triggered automatically when two activities have views with matching transitionName attributes. This is how it works under the hood:

Activity Transition Mechanism: When you start an activity with a shared element transition (via ActivityOptions.makeSceneTransitionAnimation()), Android matches views in both activities by their transitionName. For example, if SignUp and login both contain views with transitionName="splash_image", Android animates these views as if they are linked.

Automatic View Matching: The Android system automatically identifies and animates views with matching transitionName attributes, even if you don't directly specify them in the transition code. So, when an activity starts, the system looks for any views with the same transitionName and creates an animation between them. This behavior can create unintended animations if multiple views have the same transitionName in the target activity.

Single ActivityOptions Handles All Transitions: The ActivityOptions.makeSceneTransitionAnimation() method applies to all views with matching transitionNames, regardless of which button or event initiated the transition. Thus, any matching elements will participate in the animation automatically.*/
