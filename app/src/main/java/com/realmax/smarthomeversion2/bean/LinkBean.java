package com.realmax.smarthomeversion2.bean;

import androidx.annotation.NonNull;

import android.util.Log;

import com.realmax.smarthomeversion2.tcp.BaseNettyHandler;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandler;
import com.realmax.smarthomeversion2.tcp.NettyLinkUtil;
import com.realmax.smarthomeversion2.util.CustomerThread;
import com.realmax.smarthomeversion2.util.SpUtil;

import io.netty.util.concurrent.FastThreadLocalThread;

/**
 * 连接对象
 *
 * @author ayuan
 */
public class LinkBean {
    private static final String TAG = "LinkBean";
    private String linkName;
    private String linkDes;
    private String linkIp;
    private int linkPort;
    private String linkTag;
    private BaseNettyHandler baseNettyHandler = null;

    public LinkBean(String linkName, String linkDes) {
        this.linkName = linkName;
        this.linkDes = linkDes;
    }

    public LinkBean(String linkName, String linkDes, String linkTag) {
        this.linkName = linkName;
        this.linkDes = linkDes;
        this.linkTag = linkTag;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkDes() {
        return linkDes;
    }

    public void setLinkDes(String linkDes) {
        this.linkDes = linkDes;
    }

    public String getLinkIp() {
        return linkIp;
    }

    public String getLinkIpBySp() {
        return SpUtil.getString(linkTag + "IP", "192.168.50.247");
    }

    public void setLinkIp(String linkIp) {
        this.linkIp = linkIp;
    }

    public int getLinkPort() {
        return linkPort;
    }

    public int getLinkPortBySp() {
        return SpUtil.getInt(linkTag + "PORT", 8527);
    }

    public void setLinkPort(int linkPort) {
        this.linkPort = linkPort;
    }

    public String getLinkTag() {
        return linkTag;
    }

    public void setLinkTag(String linkTag) {
        this.linkTag = linkTag;
    }

    public BaseNettyHandler getBaseNettyHandler() {
        return baseNettyHandler;
    }

    public void setBaseNettyHandler(BaseNettyHandler baseNettyHandler) {
        this.baseNettyHandler = baseNettyHandler;
    }

    /**
     * 开启连接
     *
     * @param baseNettyHandler 数据回调
     * @param connectedStatus  连接状态回调
     */
    public void startConnected(BaseNettyHandler baseNettyHandler, ConnectedStatus connectedStatus) {
        try {
            this.baseNettyHandler = baseNettyHandler;

            SpUtil.putString(linkTag + "IP", linkIp);
            SpUtil.putInt(linkTag + "PORT", linkPort);

            NettyLinkUtil nettyLinkUtil = new NettyLinkUtil(linkIp, linkPort);
            nettyLinkUtil.start(new NettyLinkUtil.Callback() {
                @Override
                public void success() {
                    connectedStatus.success();

                    Log.e(TAG, "success: 连接成功");
                }

                @Override
                public void error() {
                    connectedStatus.error();
                    Log.e(TAG, "error: 连接出现问题");
                }
            }, baseNettyHandler);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "startConnected: 连接出现问题", e);
        }
    }

    /**
     * 获取数据回调
     *
     * @param customerCallback 数据回调
     */
    public void setCustomerCallback(CustomerCallback customerCallback) {
        if (this.baseNettyHandler != null) {
            this.baseNettyHandler.setCallback(customerCallback);
        }
    }

    /**
     * 获取了连接状态
     *
     * @return true：已连接，false：未连接
     */
    public boolean getLinkStatus() {
        if (this.baseNettyHandler != null) {
            return this.baseNettyHandler.isConnected();
        }
        return false;
    }

    public void closeTheConnection() {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                baseNettyHandler.closeTheConnection();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public String getCurrentCommand() {
        if (this.baseNettyHandler != null) {
            return this.baseNettyHandler.getCurrentCommand();
        }
        return "";
    }

    public interface ConnectedStatus {
        void success();

        void error();
    }

    @NonNull
    @Override
    public String toString() {
        return "LinkBean{" +
                "linkName='" + linkName + '\'' +
                ", linkDes='" + linkDes + '\'' +
                ", linkIp='" + linkIp + '\'' +
                ", linkPort=" + linkPort +
                ", linkTag='" + linkTag + '\'' +
                ", baseNettyHandler=" + baseNettyHandler +
                '}';
    }
}
