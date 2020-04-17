package com.realmax.smarthomeversion2.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.realmax.smarthomeversion2.bean.RoomBean;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.util.ValueUtil;

import java.util.ArrayList;

/**
 * @author ayuan
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    public ArrayList<RoomBean> roomBeans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initView();
        initEvent();
        parameter();
        initData();
    }

    protected void parameter() {
        roomBeans = new ArrayList<>();
        roomBeans.add(new RoomBean("客厅", new int[]{1}, new int[]{1, 2}, new int[]{2}));
        roomBeans.add(new RoomBean("洗手间", new int[]{2}, new int[]{}, new int[]{}));
        roomBeans.add(new RoomBean("仓储间", new int[]{3}, new int[]{}, new int[]{}));
        roomBeans.add(new RoomBean("餐厅", new int[]{4}, new int[]{3}, new int[]{3}));
        roomBeans.add(new RoomBean("门厅", new int[]{5}, new int[]{4}, new int[]{4}));
        roomBeans.add(new RoomBean("车库", new int[]{6}, new int[]{9}, new int[]{5}));
        roomBeans.add(new RoomBean("走廊", new int[]{7}, new int[]{5}, new int[]{6}));
        roomBeans.add(new RoomBean("卧室A", new int[]{8}, new int[]{6}, new int[]{7}));
        roomBeans.add(new RoomBean("洗浴间", new int[]{9, 10}, new int[]{}, new int[]{}));
        roomBeans.add(new RoomBean("卧室B", new int[]{11}, new int[]{7}, new int[]{8}));
        roomBeans.add(new RoomBean("卧室C", new int[]{13}, new int[]{8}, new int[]{9}));
        roomBeans.add(new RoomBean("更衣间", new int[]{14}, new int[]{}, new int[]{}));
        roomBeans.add(new RoomBean("书房", new int[]{17, 18}, new int[]{10}, new int[]{10}));
        roomBeans.add(new RoomBean("庭院", new int[]{15}, new int[]{}, new int[]{11}));
        roomBeans.add(new RoomBean("院墙", new int[]{16}, new int[]{}, new int[]{12}));
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
    CustomerHandlerBase getCustomerHandler(String tag) {
        return ValueUtil.getHandlerHashMap().get(tag);
    }
}