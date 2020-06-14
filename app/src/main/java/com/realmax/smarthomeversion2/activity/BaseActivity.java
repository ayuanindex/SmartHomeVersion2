package com.realmax.smarthomeversion2.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.realmax.smarthomeversion2.audio.AudioControl;
import com.realmax.smarthomeversion2.bean.RoomBean;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.util.ValueUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ayuan
 */
public abstract class BaseActivity extends AppCompatActivity {
    public ArrayList<RoomBean> roomBeans;
    public Handler uiHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initView();
        uiHandler = new Handler(Looper.getMainLooper());
        initEvent();
        checkPermissions();
    }

    /**
     * 权限验证
     */
    private void checkPermissions() {
        List<String> permissions = new LinkedList<>();
        addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        addPermission(permissions, Manifest.permission.RECORD_AUDIO);
        addPermission(permissions, Manifest.permission.INTERNET);
        addPermission(permissions, Manifest.permission.READ_PHONE_STATE);

        if (!permissions.isEmpty()) {
            // 请求还未请求的权限
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[0]), 1);
            checkPermissions();
        } else {
            parameter();
            initData();
        }
    }

    private void addPermission(List<String> permissionList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(permission);
        }
    }

    protected void parameter() {
        roomBeans = new ArrayList<>();
        roomBeans.add(new RoomBean("客厅", new int[]{1}, new int[]{1, 2}, new int[]{2}, new int[]{1}));
        roomBeans.add(new RoomBean("洗手间", new int[]{2}, new int[]{}, new int[]{}, new int[]{}));
        roomBeans.add(new RoomBean("仓储间", new int[]{3}, new int[]{}, new int[]{}, new int[]{}));
        roomBeans.add(new RoomBean("餐厅", new int[]{4}, new int[]{3}, new int[]{3}, new int[]{}));
        roomBeans.add(new RoomBean("门厅", new int[]{5}, new int[]{4}, new int[]{4}, new int[]{2}));
        roomBeans.add(new RoomBean("车库", new int[]{6}, new int[]{9}, new int[]{5}, new int[]{5}));
        roomBeans.add(new RoomBean("走廊", new int[]{7}, new int[]{5}, new int[]{6}, new int[]{}));
        roomBeans.add(new RoomBean("卧室A", new int[]{8}, new int[]{6}, new int[]{7}, new int[]{}));
        roomBeans.add(new RoomBean("洗浴间", new int[]{9, 10}, new int[]{}, new int[]{}, new int[]{}));
        roomBeans.add(new RoomBean("卧室B", new int[]{11}, new int[]{7}, new int[]{8}, new int[]{}));
        roomBeans.add(new RoomBean("卧室C", new int[]{13}, new int[]{8}, new int[]{9}, new int[]{}));
        roomBeans.add(new RoomBean("更衣间", new int[]{14}, new int[]{}, new int[]{}, new int[]{}));
        roomBeans.add(new RoomBean("书房", new int[]{17, 18}, new int[]{10}, new int[]{10}, new int[]{}));
        roomBeans.add(new RoomBean("庭院", new int[]{15}, new int[]{}, new int[]{11}, new int[]{}));
        roomBeans.add(new RoomBean("院墙", new int[]{16}, new int[]{}, new int[]{12}, new int[]{3, 4}));

    }

    /**
     * 获取布局资源的ID
     *
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