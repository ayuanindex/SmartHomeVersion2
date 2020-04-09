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

public class AirConditioningActivity extends BaseActivity {
    private RelativeLayout rl_back;
    private ImageView iv_one;
    private ImageView iv_two;
    private ImageView iv_three;
    private ImageView iv_four;
    private ImageView iv_weather;
    private TextView tv_temperature;
    private TextView tv_setTemperature;
    private TextView tv_windSpeed;
    private TextView tv_mode;
    private CheckBox cb_powerSwitch;
    private ImageView iv_cold;
    private ImageView iv_hot;
    private ImageView iv_plus;
    private ImageView iv_reduce;
    private ImageView iv_up;
    private ImageView iv_down;
    private ImageView iv_switchLeft;
    private ImageView iv_switchRight;
    private TextView tv_currentRoom;

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
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        // 时间
        iv_one = (ImageView) findViewById(R.id.iv_one);
        iv_two = (ImageView) findViewById(R.id.iv_two);
        iv_three = (ImageView) findViewById(R.id.iv_three);
        iv_four = (ImageView) findViewById(R.id.iv_four);
        // 天气状态
        iv_weather = (ImageView) findViewById(R.id.iv_weather);
        // 空调状态
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_setTemperature = (TextView) findViewById(R.id.tv_setTemperature);
        tv_windSpeed = (TextView) findViewById(R.id.tv_windSpeed);
        tv_mode = (TextView) findViewById(R.id.tv_mode);
        // 空调功能
        cb_powerSwitch = (CheckBox) findViewById(R.id.cb_powerSwitch);
        iv_cold = (ImageView) findViewById(R.id.iv_cold);
        iv_hot = (ImageView) findViewById(R.id.iv_hot);
        iv_plus = (ImageView) findViewById(R.id.iv_plus);
        iv_reduce = (ImageView) findViewById(R.id.iv_reduce);
        iv_up = (ImageView) findViewById(R.id.iv_up);
        iv_down = (ImageView) findViewById(R.id.iv_down);
        // 切换房间
        iv_switchLeft = (ImageView) findViewById(R.id.iv_switchLeft);
        iv_switchRight = (ImageView) findViewById(R.id.iv_switchRight);
        tv_currentRoom = (TextView) findViewById(R.id.tv_currentRoom);
    }

    @Override
    protected void initEvent() {
        rl_back.setOnClickListener(new View.OnClickListener() {
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
