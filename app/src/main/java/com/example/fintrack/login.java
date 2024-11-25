package com.example.fintrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class login extends AppCompatActivity {

    TextView sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sign_up = findViewById(R.id.signup_button);
        sign_up.setOnClickListener(view -> {
            // Start the SignupActivity when sign up is clicked
            Intent intent = new Intent(login.this, SignUp.class);
            startActivity(intent);
        });
    }


}

/*
Yes, in Android, if you do not explicitly set a text color for buttons (or other text-based UI components),
they will typically use the default text color defined by the theme. In the Material theme, the default text color
 for buttons is often derived from the primary color of the theme, specifically colorOnPrimary when the button is
 using the primary background.
 */

