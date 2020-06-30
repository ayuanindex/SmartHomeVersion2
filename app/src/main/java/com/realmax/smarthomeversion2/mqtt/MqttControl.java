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

    /**
     * 开始进行连接
     */
    public void connected() {
        // 子类在创建时回调用super来调用当前类的构造来实现参数的设置，所以这里可以直接进行连接
        // 创建一个mqtt连接类来进行连接
        customerMqttConnect = new CustomerMqttConnect(context, mBrokerUrl, mProductId, mDevName, mDevPsk);
        // 连接时需要传入两个回调还有一个json数据模版，因为当前类是抽象类，所有一个用抽象方法从子类获取回调的实现类
        customerMqttConnect.connected(mJsonFileName, getActionCallback(), getStreamCallback());
    }

    protected abstract TXDataTemplateDownStreamCallBack getStreamCallback();

    protected abstract TXMqttActionCallBack getActionCallback();

    /**
     * 断开连接
     */
    public void disConnected() {
        customerMqttConnect.disConnected();
    }
}
