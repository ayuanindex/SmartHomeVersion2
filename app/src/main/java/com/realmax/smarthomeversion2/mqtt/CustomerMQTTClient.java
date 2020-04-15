package com.realmax.smarthomeversion2.mqtt;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;

/**
 * @ProjectName: IoT
 * @Package: com.ayuan.iot
 * @ClassName: TencentCloudIot
 * @CreateDate: 2020/4/11 15:05
 */

public class CustomerMQTTClient {

    public String DEVICE_NAME = "airConditioner1";
    /**
     * 在这里替换产品ID
     */
    private String PRODUCT_ID = "5JSNV8ZAWH";
    /**
     * 在这里替换连接地址连接地址
     */
    private String URL = "tcp://" + PRODUCT_ID + ".iotcloud.tencentdevices.com:1883";
    /**
     * 在这里替换设备ID
     */
    private String CLIENT_ID = "5JSNV8ZAWHairConditioner1";
    private String USERNAME = "5JSNV8ZAWHairConditioner1;12010126;G2EJ3;1622586265";
    private String PASSWORD = "2106abed4b5f4a49ff161f287649d91bbe5fd6895904f841b39549655b45f8d0;hmacsha256";

    /**
     * 可订阅主题
     */
    private final ArrayList<String> TOPIC_SELECTS = new ArrayList<String>();

    /**
     * 回调
     */
    private CustomerMQTTCallback customerMQTTCallback;

    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;

    public CustomerMQTTClient(CustomerMQTTCallback customerMQTTCallback) {
        this.customerMQTTCallback = customerMQTTCallback;
        addSubscribe();
    }

    public CustomerMQTTClient(String PRODUCT_ID, String URL, String CLIENT_ID, String DEVICE_NAME, String USERNAME, String PASSWORD, CustomerMQTTCallback customerMQTTCallback) {
        this.PRODUCT_ID = PRODUCT_ID;
        this.URL = URL;
        this.CLIENT_ID = CLIENT_ID;
        this.DEVICE_NAME = DEVICE_NAME;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.customerMQTTCallback = customerMQTTCallback;
        addSubscribe();
    }

    /**
     * 获取主题列表集合
     *
     * @return 返回主题集合
     */
    public ArrayList<String> getTOPIC_SELECTS() {
        return TOPIC_SELECTS;
    }

    /**
     * 添加需要订阅的主题
     */
    private void addSubscribe() {
        TOPIC_SELECTS.add(PRODUCT_ID + "/" + DEVICE_NAME + "/control");
        TOPIC_SELECTS.add(PRODUCT_ID + "/" + DEVICE_NAME + "/data");
    }


    /**
     * 初始化MQTT
     *
     * @return
     */
    public void startMQTT(MqttCallback mqttCallback) {
        try {
            // 参数一：主机地址；
            // 参数二：客户端ID(设备信息中可见)，一般以客户端唯一标识符，不能够和其他客户端重名；
            // 参数三：数据保存在内存
            mqttClient = new MqttClient(URL, CLIENT_ID, new MemoryPersistence());
            mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(false);// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            mqttConnectOptions.setUserName(USERNAME);// 设置连接的用户名(自己的服务器没有设置用户名)
            mqttConnectOptions.setPassword(PASSWORD.toCharArray());// 设置连接的密码(自己的服务器没有设置密码)
            mqttConnectOptions.setConnectionTimeout(10);// 设置连接超时时间 单位为秒
            mqttConnectOptions.setKeepAliveInterval(20);// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            customerMQTTCallback.setOptions(mqttConnectOptions);//自定义mqtt配置
            mqttClient.setCallback(mqttCallback);
            connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        if (mqttClient.isConnected()) {
            customerMQTTCallback.tips("连接已经打开,请勿重复开启");
        } else {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        mqttClient.connect(CustomerMQTTClient.this.mqttConnectOptions);
                        // 连接建立成功
                        customerMQTTCallback.connectedStatus(true, "连接成功");
                    } catch (MqttSecurityException e) {
                        e.printStackTrace();
                        //安全问题连接失败
                        Log.e("安全问题连接失败", e.getMessage() + "");
                        customerMQTTCallback.connectedStatus(false, "安全问题连接失败");
                    } catch (MqttException e) {
                        e.printStackTrace();
                        //连接失败原因
                        Log.e("连接失败原因", "" + e.getMessage());
                        customerMQTTCallback.connectedStatus(false, "连接失败原因");
                    } catch (Exception e) {
                        e.printStackTrace();
                        customerMQTTCallback.connectedStatus(false, "未知原因");
                    }
                }
            }.start();
        }
    }

    /**
     * 关闭连接
     */
    public void disConnect() {
        if (mqttClient.isConnected()) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        mqttClient.disconnect();
                        customerMQTTCallback.connectedStatus(false, "连接断开");
                    } catch (MqttException e) {
                        e.printStackTrace();
                        customerMQTTCallback.connectedStatus(true, "断开连接失败");
                    }
                }
            }.start();
        }
    }


    /**
     * 订阅主题
     *
     * @param topic 需要订阅的主题
     */
    public void subscribe(String topic) {
        if (!mqttClient.isConnected()) {
            customerMQTTCallback.tips("请建立连接后再试！！！");
        } else {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Log.d(getName(), "run: ======" + topic);
                        mqttClient.subscribe(topic, 0);
                        customerMQTTCallback.subscribe(true, topic + "主题订阅成功");
                    } catch (MqttException e) {
                        e.printStackTrace();
                        customerMQTTCallback.subscribe(false, topic + "主题订阅失败");
                    }
                }
            }.start();
        }
    }

    /**
     * 向指定的主题发送消息
     *
     * @param targetTopic
     * @param msg
     */
    public void sendMsgToTopic(String targetTopic, String msg) {
        if (mqttClient.isConnected()) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        MqttMessage message = new MqttMessage(msg.getBytes());
                        message.setQos(0);
                        mqttClient.publish(targetTopic, message);
                        customerMQTTCallback.tips("消息发送成功");
                    } catch (MqttException e) {
                        e.printStackTrace();
                        customerMQTTCallback.tips("消息发送失败，可能目标主题错误");
                    }
                }
            }.start();
        } else {
            customerMQTTCallback.tips("请先建立连接");
        }
    }
}
