package com.realmax.smarthomeversion2;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class App extends Application {

    private static Context context;
    private static Toast toast;

    @SuppressLint("ShowToast")
    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);

        Constant.init();
    }

    public static Context getContext() {
        return context;
    }

    public static void showToast(String msg) {
        toast.cancel();
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
