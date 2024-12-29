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
    Button btnForgotPassword, btnBackToLogin;
    EditText ETEmailForgotPassword;
    FirebaseAuth mAuth;
    String email;

    Animation topAnim, bottomAnim;

    TextView ForgotPasswordTV;
    ImageView ForgotPasswordIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotpassword);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
        ETEmailForgotPassword = findViewById(R.id.ETEmailForgotPassword);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        ForgotPasswordTV = findViewById(R.id.ForgotPasswordTV);
        ForgotPasswordIV = findViewById(R.id.ForgotPasswordIV);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        ForgotPasswordIV.setAnimation(bottomAnim);
        ForgotPasswordTV.setAnimation(topAnim);

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = ETEmailForgotPassword.getText().toString();
                if (!TextUtils.isEmpty(email)) {
                    resetPassword();
                } else {
                    Toast.makeText(ForgotPassword.this, "Email field can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply animations
                // Use a delay before navigating
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(ForgotPassword.this, login.class);
                    startActivity(intent);
                    finish();
                }, 1000); // Delay of 1 second (1000 milliseconds)
            }
        });
    }

    private void resetPassword() {
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ForgotPassword.this, "Reset password link has been sent", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotPassword.this, "Error trying to send email", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
