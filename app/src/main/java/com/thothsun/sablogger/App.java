package com.thothsun.sablogger;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("benkong");
    }
}
