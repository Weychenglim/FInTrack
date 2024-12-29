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

    private FirebaseAuth mAuth;

    Animation topAnim, bottomAnim;
    TextView LoginWelcome;
    ImageView LoginIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        EditText ETLoginEmail = findViewById(R.id.ETLoginEmail);
        EditText ETLoginPassword = findViewById(R.id.ETLoginPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        LoginIV = findViewById(R.id.LoginIv);
        LoginWelcome =findViewById(R.id.LoginWelcome);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        LoginIV.setAnimation(bottomAnim);
        LoginWelcome.setAnimation(topAnim);

        // Check if the user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // If user is signed in, navigate to the new activity
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ETLoginEmail.getText().toString();
                String password = ETLoginPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Close the current activity
                                } else {
                                    Toast.makeText(login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        TextView TVForgotPassword = findViewById(R.id.TVForgotPassword);
        TVForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        TextView TVGoSignup = findViewById(R.id.TVGoSignup);
        TVGoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, SignUp.class);
                startActivity(intent);
            }
        });
        TVGoSignup.setOnClickListener(view -> {
            Intent intent = new Intent(login.this, SignUp.class);

            // Setting up transition pairs for shared element animations
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair(LoginIV, "splash_text");
            pairs[1] = new Pair(LoginWelcome, "splash_image");

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

