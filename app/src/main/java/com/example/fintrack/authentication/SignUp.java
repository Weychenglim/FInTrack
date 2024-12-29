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

    private FirebaseAuth mAuth;
    Animation topAnim, bottomAnim;

    TextView welcome;
    TextView text;

    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        welcome = findViewById(R.id.welcome);
        text = findViewById(R.id.text11);

        welcome.setAnimation(topAnim);
        text.setAnimation(topAnim);


        login = findViewById(R.id.TVLogin);
        login.setOnClickListener(view -> {
            // Set up the shared element transitions
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair(welcome, "splash_image");
            pairs[1] = new Pair(text, "splash_image");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);

            // Introduce a delay before starting the new activity
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SignUp.this, com.example.fintrack.authentication.login.class);
                startActivity(intent, options.toBundle());
            }, 1000); // Delay of 300 milliseconds
        });

        /*n Android, shared element transitions are triggered automatically when two activities have views with matching transitionName attributes. This is how it works under the hood:

Activity Transition Mechanism: When you start an activity with a shared element transition (via ActivityOptions.makeSceneTransitionAnimation()), Android matches views in both activities by their transitionName. For example, if SignUp and login both contain views with transitionName="splash_image", Android animates these views as if they are linked.

Automatic View Matching: The Android system automatically identifies and animates views with matching transitionName attributes, even if you don't directly specify them in the transition code. So, when an activity starts, the system looks for any views with the same transitionName and creates an animation between them. This behavior can create unintended animations if multiple views have the same transitionName in the target activity.

Single ActivityOptions Handles All Transitions: The ActivityOptions.makeSceneTransitionAnimation() method applies to all views with matching transitionNames, regardless of which button or event initiated the transition. Thus, any matching elements will participate in the animation automatically.*/
        mAuth = FirebaseAuth.getInstance();

        EditText ETUsername = findViewById(R.id.ETUsername);
        EditText ETSignupEmail = findViewById(R.id.ETSignupEmail);
        EditText ETSignupPassword = findViewById(R.id.ETSignupPassword);
        EditText ETSignupPasswordConfirm = findViewById(R.id.ETSignupPasswordConfirm);
        Button btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ETUsername.getText().toString();
                String email = ETSignupEmail.getText().toString();
                String password = ETSignupPassword.getText().toString();
                String passwordConfirm = ETSignupPasswordConfirm.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(SignUp.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUp.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUp.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(passwordConfirm)) {
                    Toast.makeText(SignUp.this, "Confirm password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!password.equals(passwordConfirm)) {
                    Toast.makeText(SignUp.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUp.this,"SignUp successfull" , Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUp.this,   login.class);
                                    startActivity(intent);
                                    finish(); // Close the current activity
                                } else {
                                    Toast.makeText(SignUp.this, "SignUp failed."+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        TextView TVGoLogin = findViewById(R.id.TVLogin);
        TVGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, login.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity
            }
        });
    }
}
