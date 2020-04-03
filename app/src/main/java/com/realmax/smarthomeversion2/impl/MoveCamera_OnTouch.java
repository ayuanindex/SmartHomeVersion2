package com.realmax.smarthomeversion2.impl;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import com.realmax.smarthomeversion2.util.MoveCamera;

public abstract class MoveCamera_OnTouch implements View.OnTouchListener {
    private static final String TAG = "MoveCamera_OnTouch";

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                MoveCamera.move(getType(), getDeviceId(), new MoveCamera.Result() {
                    @Override
                    public void resultAngle(float a, float b) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getAngle(a, b);
                            }
                        });
                    }
                });
                break;
            case MotionEvent.ACTION_UP:
                MoveCamera.stop();
                break;
        }
        return true;
    }

    public abstract int getType();

    public abstract Activity getActivity();

    public abstract int getDeviceId();

    public abstract void getAngle(float a, float b);
}
