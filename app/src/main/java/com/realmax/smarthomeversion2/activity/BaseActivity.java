package com.realmax.smarthomeversion2.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.realmax.smarthomeversion2.tcp.CustomerHandler;
import com.realmax.smarthomeversion2.util.ValueUtil;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initView();
        initEvent();
        initData();
    }

    /**
     * @return 页面资源的ID
     */
    @LayoutRes
    protected abstract int getLayout();

    /**
     * 初始化界面控件
     */
    protected abstract void initView();

    /**
     * 初始化控件的监听
     */
    protected abstract void initEvent();

    /**
     * 数据初始化
     */
    protected abstract void initData();

    /**
     * 界面跳转
     *
     * @param jump 需要跳转的界面
     */
    public <T> void jump(Class<T> jump) {
        startActivity(new Intent(this, jump));
    }

    /**
     * 通过TAG来过去对应的连接监听
     *
     * @param tag 标示符
     * @return 返回自定义的handler
     */
    CustomerHandler getCustomerHandler(String tag) {
        return ValueUtil.getHandlerHashMap().get(tag);
    }
}