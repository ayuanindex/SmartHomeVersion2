package com.realmax.smarthomeversion2.impl;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import com.realmax.smarthomeversion2.util.MoveCamera;

/**
 * @author ayuan
 */
public abstract class BaseMoveCamera_OnTouch implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                MoveCamera.move(getType(), getDeviceId(), getTag(), (float a, float b) -> getActivity().runOnUiThread(() -> getAngle(a, b)));
                break;
            case MotionEvent.ACTION_UP:
                MoveCamera.stop();
                break;
            default:
                break;
        }
        return true;
    }

    abstract int getType();

    abstract String getTag();

    public abstract Activity getActivity();

    abstract int getDeviceId();

    abstract void getAngle(float a, float b);
}
