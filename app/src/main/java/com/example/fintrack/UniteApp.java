package com.example.fintrack;

import android.app.Application;
import com.example.fintrack.db.DBManager;

// UniteApp class extends Application to manage global app state and resources
public class UniteApp extends Application {

    // onCreate is called when the app is first launched
    @Override
    public void onCreate(){
        super.onCreate();

        // Initialize the database when the app starts
        // This ensures that the database is ready for use globally across the app
        DBManager.initDB(getApplicationContext());

        // Register UniteApp as the custom Application class
    }
}
