package com.realmax.smarthomeversion2.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.realmax.smarthomeversion2.R;

/**
 * @author ayuan
 */
public class AirConditioningActivity extends BaseActivity {
    private RelativeLayout rlBack;
    private ImageView ivOne;
    private ImageView ivTwo;
    private ImageView ivThree;
    private ImageView ivFour;
    private ImageView ivWeather;
    private TextView tvTemperature;
    private TextView tvSetTemperature;
    private TextView tvWindSpeed;
    private TextView tvMode;
    private CheckBox cbPowerSwitch;
    private ImageView ivCold;
    private ImageView ivHot;
    private ImageView ivPlus;
    private ImageView ivReduce;
    private ImageView ivUp;
    private ImageView ivDown;
    private ImageView ivSwitchLeft;
    private ImageView ivSwitchRight;
    private TextView tvCurrentRoom;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_airconditioning;
    }

    @Override
    protected void initView() {
        rlBack = (RelativeLayout) findViewById(R.id.rl_back);
        // 时间
        ivOne = (ImageView) findViewById(R.id.iv_one);
        ivTwo = (ImageView) findViewById(R.id.iv_two);
        ivThree = (ImageView) findViewById(R.id.iv_three);
        ivFour = (ImageView) findViewById(R.id.iv_four);
        // 天气状态
        ivWeather = (ImageView) findViewById(R.id.iv_weather);
        // 空调状态
        tvTemperature = (TextView) findViewById(R.id.tv_temperature);
        tvSetTemperature = (TextView) findViewById(R.id.tv_setTemperature);
        tvWindSpeed = (TextView) findViewById(R.id.tv_windSpeed);
        tvMode = (TextView) findViewById(R.id.tv_mode);
        // 空调功能
        cbPowerSwitch = (CheckBox) findViewById(R.id.cb_powerSwitch);
        ivCold = (ImageView) findViewById(R.id.iv_cold);
        ivHot = (ImageView) findViewById(R.id.iv_hot);
        ivPlus = (ImageView) findViewById(R.id.iv_plus);
        ivReduce = (ImageView) findViewById(R.id.iv_reduce);
        ivUp = (ImageView) findViewById(R.id.iv_up);
        ivDown = (ImageView) findViewById(R.id.iv_down);
        // 切换房间
        ivSwitchLeft = (ImageView) findViewById(R.id.iv_switchLeft);
        ivSwitchRight = (ImageView) findViewById(R.id.iv_switchRight);
        tvCurrentRoom = (TextView) findViewById(R.id.tv_currentRoom);
    }

    @Override
    protected void initEvent() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }
}
