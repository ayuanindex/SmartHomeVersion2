package com.realmax.smarthomeversion2.bean;

import android.app.Activity;

import com.realmax.smarthomeversion2.App;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandler;
import com.realmax.smarthomeversion2.tcp.NettyLinkUtil;
import com.realmax.smarthomeversion2.util.ValueUtil;

import io.netty.channel.EventLoopGroup;

public class LinkBean {
    private String label;
    private String tag;
    private boolean connected;

    public LinkBean(String label, String tag) {
        this.label = label;
        this.tag = tag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isConnected() {
        Boolean aBoolean = ValueUtil.getIsConnected().get(this.tag);
        if (aBoolean != null) {
            return aBoolean;
        } else {
            return false;
        }
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        ValueUtil.getIsConnected().put(this.tag, connected);
    }

    @Override
    public String toString() {
        return "LinkBean{" +
                "label='" + label + '\'' +
                ", tag='" + tag + '\'' +
                ", connected=" + connected +
                '}';
    }

    public void connected(String ip, String port, CustomerHandler customerHandler, NettyLinkUtil.Callback status) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    CustomerHandler customerHandler = new CustomerHandler();
                    ValueUtil.getHandlerHashMap().put(LinkBean.this.tag, customerHandler);

                    int portInt = Integer.parseInt(port);
                    NettyLinkUtil nettyLinkUtil = new NettyLinkUtil(ip, portInt);
                    nettyLinkUtil.start(status, customerHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
