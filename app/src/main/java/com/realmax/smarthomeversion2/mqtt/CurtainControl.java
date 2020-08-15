package com.realmax.smarthomeversion2.mqtt;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.qcloud.iot_explorer.common.Status;
import com.qcloud.iot_explorer.data_template.TXDataTemplateDownStreamCallBack;
import com.qcloud.iot_explorer.mqtt.TXMqttActionCallBack;
import com.realmax.smarthomeversion2.Constant;
import com.realmax.smarthomeversion2.activity.bean.CurtainAndAcBean;
import com.realmax.smarthomeversion2.bean.LinkBean;
import com.realmax.smarthomeversion2.tcp.BaseNettyHandler;
import com.realmax.smarthomeversion2.tcp.CustomerHandler;
import com.realmax.smarthomeversion2.util.CustomerThread;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class CurtainControl extends MqttControl {
    private static final String TAG = "LightControl";
    private String tag = "control_02";
    private String currentCommand = "";

    public CurtainControl(Context context, String mJsonFileName, String mProductId, String mDevName, String mDevPsk) {
        super(context, mJsonFileName, mProductId, mDevName, mDevPsk);
        startTimer();
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

    private void startTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    LinkBean customerHandlerBase = Constant.getLinkBeanByTag(tag);
                    if (customerHandlerBase != null) {
                        if (!customerHandlerBase.getCurrentCommand().equals(CurtainControl.this.currentCommand)) {
                            CurtainControl.this.currentCommand = customerHandlerBase.getCurrentCommand();
                            if (!TextUtils.isEmpty(currentCommand)) {
                                CurtainAndAcBean curtainAndAcBean = new Gson().fromJson(currentCommand, CurtainAndAcBean.class);
                                // 包装json，发送MQTT指令
                                HashMap<String, Object> copyFrom = new HashMap<>();
                                for (int i = 0; i < 10; i++) {
                                    copyFrom.put("curtain" + (i + 1), curtainAndAcBean.getCurtain_S().get(i));
                                }
                                JSONObject property = new JSONObject(copyFrom);
                                publish(property);
                            }
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);
    }

    /**
     * 执行小程序的控制指令
     *
     * @param data 当前状态的json数据
     */
    private void executeInstruction(JSONObject data) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                LinkBean light = Constant.getLinkBeanByTag(tag);
                if (light != null) {
                    // 获取最近一次窗帘的状态
                    String currentCommand = light.getCurrentCommand();
                    if (!TextUtils.isEmpty(currentCommand)) {
                        CurtainAndAcBean curtainAndAcBean = new Gson().fromJson(currentCommand, CurtainAndAcBean.class);

                        JSONObject control = data.optJSONObject("control");
                        if (control != null) {
                            // 获取每一个key
                            Iterator<String> keys = control.keys();
                            while (keys.hasNext()) {
                                String next = keys.next();
                                // 判断获取到的key是否包含curtain
                                if (next.matches("curtain.*")) {
                                    int curtainKey = Integer.parseInt(next.replace("curtain", ""));
                                    curtainAndAcBean.getCurtain_S().set(curtainKey - 1, control.optInt(next));
                                    Log.d(TAG, "在获取状态回复回电上：" + curtainAndAcBean.getCurtain_S().get(curtainKey - 1));
                                }
                            }
                            ValueUtil.sendCurtainOpenOrCloseCmd(curtainAndAcBean, tag);
                        }
                    } else {
                        Log.d(TAG, "在获取状态回复回电上：命令为空");
                    }
                }
            } catch (JsonSyntaxException | NumberFormatException e) {
                e.printStackTrace();
            }
        });
    }
}
