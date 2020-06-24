package com.realmax.smarthomeversion2;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.realmax.smarthomeversion2.audio.AudioControl;

public class App extends Application {

    private static Context context;
    private static Toast toast;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
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
