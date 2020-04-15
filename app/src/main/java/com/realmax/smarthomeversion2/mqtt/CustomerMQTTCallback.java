package com.realmax.smarthomeversion2.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public interface CustomerMQTTCallback {
    /**
     * 默认已配置，修改配置时使用
     *
     * @param mqttConnectOptions
     */
    void setOptions(MqttConnectOptions mqttConnectOptions);

    /**
     * 建立连接成功或者失败的回调
     *
     * @param isConnected true表示成功，false表示失败
     * @param msg
     */
    void connectedStatus(boolean isConnected, String msg);

    /**
     * 主题订阅成功时的回调
     *
     * @param isSuccess
     */
    void subscribe(boolean isSuccess, String topic);

    /**
     * 提示消息
     *
     * @param msg 提示消息
     */
    void tips(String msg);
}
