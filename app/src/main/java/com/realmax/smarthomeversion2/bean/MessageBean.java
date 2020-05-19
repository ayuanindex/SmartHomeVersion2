package com.realmax.smarthomeversion2.bean;

import androidx.annotation.LayoutRes;

public class MessageBean {
    /**
     * 识别出的文字
     */
    private String message;

    /**
     * 处理后需要的布局
     */
    private int layout;

    public MessageBean() {
    }

    public MessageBean(String message, @LayoutRes int layout) {
        this.message = message;
        this.layout = layout;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "messge='" + message + '\'' +
                ", layout=" + layout +
                '}';
    }
}
