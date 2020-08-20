package com.realmax.smarthomeversion2.tcp;

public class ReceiveJsonHandler extends BaseNettyHandler {
    private static final String TAG = "CustomerHandler2";

    @Override
    public void callbackFunction(String jsonStr) {
        if (getCallback() != null) {
            getCallback().getResultData(jsonStr);
        }
    }
}
