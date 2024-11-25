package com.example.fintrack;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp extends AppCompatActivity {

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
        text.setAnimation(bottomAnim);


        login = findViewById(R.id.login_button);
        login.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp.this, login.class);

            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair(welcome, "splash_image");
            pairs[1] = new Pair(text, "splash_text");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
            startActivity(intent, options.toBundle());
        });

        /*n Android, shared element transitions are triggered automatically when two activities have views with matching transitionName attributes. This is how it works under the hood:

Activity Transition Mechanism: When you start an activity with a shared element transition (via ActivityOptions.makeSceneTransitionAnimation()), Android matches views in both activities by their transitionName. For example, if SignUp and login both contain views with transitionName="splash_image", Android animates these views as if they are linked.

Automatic View Matching: The Android system automatically identifies and animates views with matching transitionName attributes, even if you don't directly specify them in the transition code. So, when an activity starts, the system looks for any views with the same transitionName and creates an animation between them. This behavior can create unintended animations if multiple views have the same transitionName in the target activity.

Single ActivityOptions Handles All Transitions: The ActivityOptions.makeSceneTransitionAnimation() method applies to all views with matching transitionNames, regardless of which button or event initiated the transition. Thus, any matching elements will participate in the animation automatically.*/

    }

}