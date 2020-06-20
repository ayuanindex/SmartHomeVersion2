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
    private String des;
    private String tag;
    private String mHOST;
    private int PORT;
    private boolean connected;

    public LinkBean(String label, String des, String tag) {
        this.label = label;
        this.des = des;
        this.tag = tag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getmHOST() {
        return SpUtil.getString(tag + "ip", "192.168.50.247");
    }

    public int getPORT() {
        return SpUtil.getInt(tag + "port", 8527);
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        ValueUtil.getIsConnected().put(this.tag, connected);
    }

    public boolean isConnected() {
        Boolean aBoolean = ValueUtil.getIsConnected().get(this.tag);
        if (aBoolean != null) {
            return aBoolean;
        } else {
            return false;
        }
    }

    /**
     * 开启连接
     *
     * @param ip              连接的IP
     * @param port            连接的端口号
     * @param customerHandler 接受/发送数据
     * @param status          回调
     */
    public void connected(String ip, String port, CustomerHandlerBase customerHandler, NettyLinkUtil.Callback status) {
        try {
            ValueUtil.getHandlerHashMap().put(LinkBean.this.tag, customerHandler);

            int portInt = Integer.parseInt(port);
            NettyLinkUtil nettyLinkUtil = new NettyLinkUtil(ip, portInt);
            // 连接成功后将host和port存入sp中
            LinkBean.this.mHOST = ip;
            LinkBean.this.PORT = portInt;
            SpUtil.putString(tag + "ip", LinkBean.this.mHOST);
            SpUtil.putInt(tag + "port", LinkBean.this.PORT);
            nettyLinkUtil.start(status, customerHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "LinkBean{" +
                "label='" + label + '\'' +
                ", tag='" + tag + '\'' +
                ", connected=" + connected +
                '}';
    }
}
