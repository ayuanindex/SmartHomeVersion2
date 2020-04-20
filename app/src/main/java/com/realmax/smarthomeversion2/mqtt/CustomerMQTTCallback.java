package com.realmax.smarthomeversion2.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * @author ayuan
 */
public interface CustomerMQTTCallback {
    /**
     * 默认已配置，修改配置时使用
     *
     * @param mqttConnectOptions Mqtt连接的配置项
     */
    void setOptions(MqttConnectOptions mqttConnectOptions);

    /**
     * 建立连接成功或者失败的回调
     *
     * @param isConnected true表示成功，false表示失败
     * @param msg         返回状态
     */
    void connectedStatus(boolean isConnected, String msg);

    /**
     * 主题订阅成功时的回调
     *
     * @param isSuccess 是否订阅成功的标示符
     * @param topic     需要订阅的主题
     */
    void subscribe(boolean isSuccess, String topic);

    /**
     * 提示消息
     *
     * @param msg 提示消息
     */
    void tips(String msg);
}
