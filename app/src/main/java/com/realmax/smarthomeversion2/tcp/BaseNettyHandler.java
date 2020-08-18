package com.realmax.smarthomeversion2.tcp;


import android.util.Log;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author ayuan
 */
public abstract class BaseNettyHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final String TAG = "NettyHandler";
    /**
     * 回调接口
     */
    private CustomerCallback callback = null;


    /**
     * 最近一次收到的指令
     */
    private String currentCommand = "";

    /**
     * 向服务端发送数据时需要的Netty上线文
     */
    private ChannelHandlerContext handlerContext = null;

    /**
     * 当前的连接状态
     */
    private boolean isConnected = false;

    private EventLoopGroup eventLoopGroup = null;

    public CustomerCallback getCallback() {
        return callback;
    }

    public void setCallback(CustomerCallback callback) {
        this.callback = callback;
    }

    public String getCurrentCommand() {
        return currentCommand;
    }

    public void setCurrentCommand(String currentCommand) {
        this.currentCommand = currentCommand;
    }

    public ChannelHandlerContext getHandlerContext() {
        return handlerContext;
    }

    public void setHandlerContext(ChannelHandlerContext handlerContext) {
        this.handlerContext = handlerContext;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public EventLoopGroup getEventLoopGroup() {
        return eventLoopGroup;
    }

    public void setEventLoopGroup(EventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.handlerContext = ctx;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        Log.e(TAG, "exceptionCaught: 捕获到了Netty的异常", cause);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, ByteBuf msg) {
        ByteBuf buf = msg.readBytes(msg.readableBytes());
        String s = buf.toString(StandardCharsets.UTF_8);
        currentCommand = s;
        callbackFunction(s);
    }

    /**
     * 返回Json字符串
     *
     * @param jsonStr json格式的字符串
     */
    public abstract void callbackFunction(String jsonStr);

    /**
     * 释放线程池资源
     */
    public void closeTheConnection() throws InterruptedException {
        eventLoopGroup.shutdownGracefully().sync();
    }
}
