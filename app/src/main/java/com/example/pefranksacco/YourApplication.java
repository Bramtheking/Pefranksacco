package com.example.pefranksacco;

import android.app.Application;

public class YourApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize ApiService with the application context
        ApiService.init(getApplicationContext());
    }
}
