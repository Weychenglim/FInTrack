package com.example.fintrack.authentication;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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


import com.example.fintrack.utils.MyBroadcastReceiver;
import com.example.fintrack.R;

import java.util.Calendar;

public class SplashScreen extends AppCompatActivity {

    private static final long DELAY_TIME = 3000; // Delay time in milliseconds
    Animation topAnim, bottomAnim;
    ImageView imageView;
    TextView app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        imageView = findViewById(R.id.imageView);
        app_name = findViewById(R.id.app_name);

        imageView.setAnimation(topAnim);
        app_name.setAnimation(bottomAnim);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, login.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair(imageView, "splash_image");
                pairs[1] = new Pair(app_name, "splash_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this, pairs);
                startActivity(i, options.toBundle());
            }
        }, DELAY_TIME);

        createNotificationChannel();
        scheduleDailyAlarm();
    }

        private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Daily Budget Notifications";
            String description = "Notifications for daily budget updates";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("daily_budget_channel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void scheduleDailyAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, intent, PendingIntent.FLAG_IMMUTABLE);

        // Schedule the alarm at midnight
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // If the current time is after midnight, schedule for the next day
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Set the repeating alarm
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

}

/*The new Handler(Looper.getMainLooper()).postDelayed line creates a Handler on the main UI
thread, ensuring that UI updates occur in sync with the app’s interface. The postDelayed method
introduces a delay, set by the constant DELAY_TIME, before executing the code inside run().
This delay keeps the splash screen visible for the specified time before transitioning. Inside
the run() method, an Intent is created to move from SplashScreen to Login, using SplashScreen.this
as the current context and login.class as the target activity. The Pair[] pairs array sets up shared
element transitions by linking specific UI elements (imageView and app_name) in SplashScreen to transition
names ("splash_image" and "splash_text") that should match the corresponding elements in Login.
These pairs enable a smooth visual flow between activities. The ActivityOptions.makeSceneTransitionAnimation
method then uses pairs to build a transition animation, and startActivity(i, options.toBundle())
launches the Login activity with the specified animation, resulting in a visually cohesive movement from the
splash screen to the login screen.
 */
