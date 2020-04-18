package com.realmax.smarthomeversion2.bean;

import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.tcp.NettyLinkUtil;
import com.realmax.smarthomeversion2.util.SpUtil;
import com.realmax.smarthomeversion2.util.ValueUtil;

/**
 * 连接对象
 *
 * @author ayuan
 */
public class LinkBean {
    private String label;
    private String tag;
    private String mHOST;
    private int PORT;
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

    public String getmHOST() {
        return SpUtil.getString(tag + "1", "192.168.50.247");
    }

    public int getPORT() {
        return SpUtil.getInt(tag + "2", 8527);
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

    public void connected(String ip, String port, CustomerHandlerBase customerHandler, NettyLinkUtil.Callback status) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    /*CustomerHandler customerHandler = new CustomerHandler();*/
                    ValueUtil.getHandlerHashMap().put(LinkBean.this.tag, customerHandler);

                    int portInt = Integer.parseInt(port);
                    NettyLinkUtil nettyLinkUtil = new NettyLinkUtil(ip, portInt);
                    // 连接成功后将host和port存入sp中
                    LinkBean.this.mHOST = ip;
                    LinkBean.this.PORT = portInt;
                    SpUtil.putString(tag + "1", LinkBean.this.mHOST);
                    SpUtil.putInt(tag + "2", LinkBean.this.PORT);
                    nettyLinkUtil.start(status, customerHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
