package com.example.apk;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import java.util.Date;

public class ApplicationMy extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, ServiceMy.class));

        System.out.println("\r\n" + new Date().toString() + "\r\n" + this + " -> " + new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
