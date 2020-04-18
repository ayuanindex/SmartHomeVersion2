package com.realmax.smarthomeversion2.tcp;


import android.util.Log;

import com.realmax.smarthomeversion2.util.L;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class BaseNettyHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final String TAG = "NettyHandler";
    // 回调接口
    private CustomerCallback callback;
    // 开始拼接数据的标记
    private char left = '{';
    // 结束拼接数据的标记
    private char right = '}';
    private boolean flag = false;
    private StringBuffer strings = new StringBuffer();
    private int leftCount = 0;
    private int rightCount = 0;

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, ByteBuf msg) throws Exception {
        Log.i(TAG, "channelRead0: client channelRead..哈哈哈");
        ByteBuf buf = msg.readBytes(msg.readableBytes());
        String s = buf.toString(StandardCharsets.UTF_8);
        /*String s = buf.toString(StandardCharsets.UTF_8)*/
        ;
        leftCount = 0;
        for (char c : s.toCharArray()) {
            // 判断是否已经开始记录阶段
            if (c == left) {
                // 设置flag标记，将开始记录数据
                flag = true;
                leftCount++;
            }
            if (flag) {
                // 通过stringBuilder来拼接字符串
                strings.append(c);
            }
            // 判断是否已经是右边的括号
            if (c == right) {
                leftCount--;
                rightCount++;
                // flag设置为false停止记录
                // 将StringBuilder记录的整段的字符串提取出来
                String jsonStr = strings.toString();
                // 初始化StringBuilder
                if (leftCount == 0) {
                    flag = false;
                    strings = new StringBuffer();
                    Log.d(TAG, "messageReceived: " + jsonStr);
                    callbackFunction(jsonStr);
                }
            }
        }
    }

    public abstract void callbackFunction(String jsonStr);

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) throws Exception {
        cause.printStackTrace();
        channelHandlerContext.close();
    }
}
