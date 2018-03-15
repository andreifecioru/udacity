package com.example.android.inventorymanager;


import android.app.Application;
import android.content.Context;

public class InventoryManagerApplication extends Application {
    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        // Set this static field to make application
        // context available outside activity classes.
        appContext = getApplicationContext();
    }
}
