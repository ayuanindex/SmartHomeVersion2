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
import com.realmax.smarthomeversion2.activity.bean.LightBean;
import com.realmax.smarthomeversion2.bean.LinkBean;
import com.realmax.smarthomeversion2.util.CustomerThread;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class LightControl extends MqttControl {
    private static final String TAG = "LightControl";
    private String tag = "control_01";
    private String currentCommand = "";

    public LightControl(Context context, String mJsonFileName, String mProductId, String mDevName, String mDevPsk) {
        super(context, mJsonFileName, mProductId, mDevName, mDevPsk);
        // 开启一个定时器循环检测等的状态
        startTimer();
    }

    /**
     * 更新设备状态
     */
    private void update() {
        customerMqttConnect.update();
    }

    /**
     * 向设备发送信息
     *
     * @param property 参数
     */
    public void publish(JSONObject property) {
        customerMqttConnect.publish(property);
    }

    /**
     * 父类的抽象方法的实现
     *
     * @return 连接回调的实现类
     */
    @Override
    protected TXMqttActionCallBack getActionCallback() {
        return new TXMqttActionCallBack() {
            @Override
            public void onConnectCompleted(Status status, boolean reconnect, Object userContext, String msg) {
                Log.d(TAG, "连接成功：" + msg);
                // 连接完成时订阅主题
                customerMqttConnect.subscribe();
                // 同步云端状态
                update();
            }

            @Override
            public void onConnectionLost(Throwable cause) {
                cause.printStackTrace();
                Log.d(TAG, "连接丢失：" + cause.getMessage());
            }

            @Override
            public void onDisconnectCompleted(Status status, Object userContext, String msg) {
                Log.d(TAG, "连接断开：" + msg);
            }
        };
    }

    /**
     * 父类抽象方法的实现
     *
     * @return 返回mqtt连接时实现类
     */
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
                // 通过微信小程序控制时，数据会在这里实时接收，并通过这个数据对智能家居的设备进行控制
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
                        if (!customerHandlerBase.getCurrentCommand().equals(LightControl.this.currentCommand)) {
                            LightControl.this.currentCommand = customerHandlerBase.getCurrentCommand();
                            if (!TextUtils.isEmpty(currentCommand)) {
                                LightBean lightBean = new Gson().fromJson(currentCommand, LightBean.class);
                                // 包装json，发送MQTT指令
                                HashMap<String, Object> copyFrom = new HashMap<>();
                                for (int i = 0; i < lightBean.getLightList_S().size(); i++) {
                                    copyFrom.put("light" + (i + 1), lightBean.getLightList_S().get(i));
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
            // 获取到制定控制器的连接
            LinkBean light = Constant.getLinkBeanByTag(tag);
            if (light != null) {
                // 获取最近一次灯的状态
                String currentCommand = light.getCurrentCommand();
                if (!TextUtils.isEmpty(currentCommand)) {
                    // 将获取到的json解析出来
                    LightBean lightBean = new Gson().fromJson(currentCommand, LightBean.class);
                    // 获取小程序短的控制指令
                    JSONObject control = data.optJSONObject("control");
                    if (control != null) {
                        // 获取每一个key
                        Iterator<String> keys = control.keys();
                        while (keys.hasNext()) {
                            String next = keys.next();
                            // 判断获取到的key是否包含light
                            if (next.matches("light.*")) {
                                int lightKey = Integer.parseInt(next.replace("light", ""));
                                // 根据控制指令和当前智能家居中设备的状态来设置小程序传输过来的状态
                                lightBean.getLightList_S().set(lightKey - 1, control.optInt(next));
                                Log.d(TAG, "在获取状态回复回电上：" + lightBean.getLightList_S().get(lightKey - 1));
                            }
                        }
                        // 发送开灯或关灯的指令
                        ValueUtil.sendLightOpenOrCloseCmd(lightBean, tag);
                    }
                } else {
                    Log.d(TAG, "在获取状态回复回电上：命令为空");
                }
            }
        });
    }
}
