package com.realmax.smarthomeversion2.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.realmax.smarthomeversion2.R;

public class TransducerActivity extends BaseActivity {
    private RelativeLayout rl_back;
    private ImageView iv_one;
    private ImageView iv_two;
    private ImageView iv_three;
    private ImageView iv_four;
    private ImageView iv_weather;
    private TextView tv_temperature;
    private TextView tv_humidity;
    private TextView tv_smoke;
    private TextView tv_character;
    private ImageView iv_switchLeft;
    private ImageView iv_switchRight;
    private TextView tv_currentRoom;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_transducer;
    }

    @Override
    protected void initView() {
        // 返回按钮
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        // 时间显示
        iv_one = (ImageView) findViewById(R.id.iv_one);
        iv_two = (ImageView) findViewById(R.id.iv_two);
        iv_three = (ImageView) findViewById(R.id.iv_three);
        iv_four = (ImageView) findViewById(R.id.iv_four);
        // 天气控件
        iv_weather = (ImageView) findViewById(R.id.iv_weather);
        // 传感器状态值
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity);
        tv_smoke = (TextView) findViewById(R.id.tv_smoke);
        tv_character = (TextView) findViewById(R.id.tv_character);
        // 切换房间
        iv_switchLeft = (ImageView) findViewById(R.id.iv_switchLeft);
        iv_switchRight = (ImageView) findViewById(R.id.iv_switchRight);
        // 当前房间名称
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
