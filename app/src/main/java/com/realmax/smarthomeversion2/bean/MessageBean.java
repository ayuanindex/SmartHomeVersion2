package com.realmax.smarthomeversion2.bean;

import androidx.annotation.LayoutRes;

public class MessageBean {
    /**
     * 识别出的文字
     */
    private String messge;

    /**
     * 处理后需要的布局
     */
    private int layout;

    public MessageBean() {
    }

    public MessageBean(String messge, @LayoutRes int layout) {
        this.messge = messge;
        this.layout = layout;
    }

    public String getMessge() {
        return messge;
    }

    public void setMessge(String messge) {
        this.messge = messge;
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
                "messge='" + messge + '\'' +
                ", layout=" + layout +
                '}';
    }
}
