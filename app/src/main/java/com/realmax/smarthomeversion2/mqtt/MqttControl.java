package com.realmax.smarthomeversion2.mqtt;

import android.content.Context;

import com.qcloud.iot_explorer.data_template.TXDataTemplateDownStreamCallBack;
import com.qcloud.iot_explorer.mqtt.TXMqttActionCallBack;

public abstract class MqttControl {
    /**
     * 默认测试参数
     */
    public final String mBrokerUrl = "ssl://iotcloud-mqtt.gz.tencentdevices.com:8883";
    private final String mJsonFileName;

    /**
     * 设备ID
     */
    public String mProductId = "";

    /**
     * 设备名称
     */
    public String mDevName = "";

    /**
     * 若使用证书验证，设为null
     */
    public String mDevPsk = "";

    public Context context;
    public CustomerMqttConnect customerMqttConnect;

    public MqttControl(Context context, String mJsonFileName, String mProductId, String mDevName, String mDevPsk) {
        this.context = context;
        this.mJsonFileName = mJsonFileName;
        this.mProductId = mProductId;
        this.mDevName = mDevName;
        this.mDevPsk = mDevPsk;
    }

    public void connected() {
        customerMqttConnect = new CustomerMqttConnect(context, mBrokerUrl, mProductId, mDevName, mDevPsk);
        customerMqttConnect.connected(mJsonFileName, getActionCallback(), getStreamCallback());
    }

    protected abstract TXDataTemplateDownStreamCallBack getStreamCallback();

    protected abstract TXMqttActionCallBack getActionCallback();

    public void disConnected() {
        customerMqttConnect.disConnected();
    }
}
