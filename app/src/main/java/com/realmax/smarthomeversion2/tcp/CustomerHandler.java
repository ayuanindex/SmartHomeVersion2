package com.realmax.smarthomeversion2.tcp;

import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import io.netty.channel.ChannelHandlerContext;

public class CustomerHandler extends BaseNettyHandler {
    private static final String TAG = "CustomerHandlerBase";
    private boolean flag = false;
    private StringBuffer strings = new StringBuffer();
    private CustomerCallback callback = null;

    @Override
    public void callbackFunction(String jsonStr) {
        if (callback != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                super.setCurrentCommand(jsonStr);
                callback.getResultData(jsonStr);
            } catch (JSONException e) {
                getJson(jsonStr);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private void getJson(String jsonStr) {
        for (char c : jsonStr.toCharArray()) {
            // 判断是否已经开始记录阶段
            if (c == '{') {
                // 设置flag标记，将开始记录数据
                flag = true;
            }
            if (flag) {
                // 通过stringBuilder来拼接字符串44
                strings.append(c);
            }
            // 判断是否已经是右边的括号
            if (c == '}') {
                // flag设置为false停止记录
                // 将StringBuilder记录的整段的字符串提取出来
                // 初始化StringBuilder
                flag = false;
                String json = strings.toString();
                strings = new StringBuffer();
                super.setCurrentCommand(json);
                callback.getResultData(json);
            }
        }
    }

    @Override
    public void setCallback(CustomerCallback callback) {
        this.callback = callback;
    }
}
