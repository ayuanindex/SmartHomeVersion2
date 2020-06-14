package com.realmax.smarthomeversion2.mqtt;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.qcloud.iot_explorer.common.Status;
import com.qcloud.iot_explorer.data_template.TXDataTemplateDownStreamCallBack;
import com.qcloud.iot_explorer.mqtt.TXMqttActionCallBack;
import com.realmax.smarthomeversion2.bean.LightOrCurtainBean;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.util.CustomerThread;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONObject;

import java.util.Iterator;

public class LightControl extends MqttControl {
    private static final String TAG = "LightControl";

    public LightControl(Context context, String mJsonFileName, String mProductId, String mDevName, String mDevPsk) {
        super(context, mJsonFileName, mProductId, mDevName, mDevPsk);
    }

    /**
     * 更新设备状态
     */
    private void update() {
        customerMqttConnect.update();
    }

    public void publish(JSONObject property) {
        customerMqttConnect.publish(property);
    }

    @Override
    protected TXMqttActionCallBack getActionCallback() {
        return new TXMqttActionCallBack() {
            @Override
            public void onConnectCompleted(Status status, boolean reconnect, Object userContext, String msg) {
                Log.d(TAG, "在Connect上完成：" + msg);
                // 连接完成时订阅主题
                customerMqttConnect.subscribe();
                // 同步云端状态
                update();
            }

            @Override
            public void onConnectionLost(Throwable cause) {
                cause.printStackTrace();
                Log.d(TAG, "关于连接丢失：" + cause.getMessage());
            }

            @Override
            public void onDisconnectCompleted(Status status, Object userContext, String msg) {
                Log.d(TAG, "断开连接完成时：" + msg);
            }
        };
    }

    @Override
    protected TXDataTemplateDownStreamCallBack getStreamCallback() {
        return new TXDataTemplateDownStreamCallBack() {
            @Override
            public void onReplyCallBack(String msg) {
                Log.d(TAG, "回复时回电：" + msg);
            }

            @Override
            public void onGetStatusReplyCallBack(JSONObject data) {
                Log.d(TAG, "在获取状态回复回电上：" + data.toString());
                executeInstruction(data);
            }

            @Override
            public JSONObject onControlCallBack(JSONObject msg) {
                Log.e(TAG, "在控制回叫上：" + msg.toString());
                // 更新状态信息除法另一个回调方法统一处理同步更新
                update();
                return null;
            }

            @Override
            public JSONObject onActionCallBack(String actionId, JSONObject params) {
                Log.d(TAG, "在行动回电上：" + params.toString());
                return null;
            }
        };
    }

    /**
     * 执行小程序的控制指令
     *
     * @param data 当前状态的json数据
     */
    private void executeInstruction(JSONObject data) {
        CustomerThread.poolExecutor.execute(() -> {
            CustomerHandlerBase light = ValueUtil.getHandlerHashMap().get("light");
            if (light != null) {
                String currentCommand = light.getCurrentCommand();
                if (!TextUtils.isEmpty(currentCommand)) {
                    LightOrCurtainBean lightOrCurtainBean = new Gson().fromJson(currentCommand, LightOrCurtainBean.class);

                    JSONObject control = data.optJSONObject("control");
                    if (control != null) {
                        Iterator<String> keys = control.keys();
                        while (keys.hasNext()) {
                            String next = keys.next();
                            if (next.matches("light.*")) {
                                int lightKey = Integer.parseInt(next.replace("light", ""));
                                if (lightKey - 1 < lightOrCurtainBean.getLight_S().size()) {
                                    lightOrCurtainBean.getLight_S().set(lightKey - 1, control.optInt(next));
                                    Log.d(TAG, "在获取状态回复回电上：" + lightOrCurtainBean.getLight_S().get(lightKey - 1));
                                }
                            }
                        }
                        ValueUtil.sendLightOpenOrCloseCmd(lightOrCurtainBean);
                    }
                } else {
                    Log.d(TAG, "在获取状态回复回电上：命令为空");
                }
            }
        });
    }
}
