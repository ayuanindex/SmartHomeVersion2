package com.realmax.smarthomeversion2.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.util.ValueUtil;

/**
 * @author ayuan
 */
public abstract class BaseFragment extends Fragment {
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public abstract View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onStart() {
        super.onStart();
        initEvent();
        initData();
    }

    abstract void initEvent();

    abstract void initData();

    /**
     * 通过TAG来过去对应的连接监听
     *
     * @param tag 标示符
     * @return 返回自定义的handler
     */
    CustomerHandlerBase getCustomerHandler(String tag) {
        return ValueUtil.getHandlerHashMap().get(tag);
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }
}
