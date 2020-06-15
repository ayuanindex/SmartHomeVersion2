package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.activity.bean.WeatherBean;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String tag = "virtual";
    private int currentPosition = 0;

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

        iv_switchLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPage(0);
            }
        });

        iv_switchRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPage(1);
            }
        });
    }

    @Override
    protected void initData() {


        ValueUtil.sendWeatherCmd(tag);
        CustomerHandlerBase customerHandler = getCustomerHandler(tag);
        if (customerHandler != null) {

            customerHandler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    L.e("连接断开");
                }

                @Override
                public void getResultData(String msg) {
                    try {
                        if (!TextUtils.isEmpty(msg)) {
                            JSONObject jsonObject = new JSONObject(msg);
                            if ("ans".equals(jsonObject.optString("cmd"))) {
                                WeatherBean weatherBean = new Gson().fromJson(msg, WeatherBean.class);
                                L.e("weather：" + weatherBean.toString());
                                refreshUI(weatherBean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void switchPage(int i) {
        switch (i) {
            case 0:
                if (currentPosition > 0) {
                    currentPosition--;
                }
                break;
            case 1:
                if (currentPosition < roomBeans.size() - 1) {
                    currentPosition++;
                }
                break;
            default:
                break;
        }
        tv_currentRoom.setText(roomBeans.get(currentPosition).getRoomName());
    }

    /**
     * 更新界面
     *
     * @param weatherBean 需要显示的数据
     */
    private void refreshUI(WeatherBean weatherBean) {
        int numberPicOne = getNumberPic(weatherBean.getTime().charAt(0));
        int numberPicTwo = getNumberPic(weatherBean.getTime().charAt(1));
        int numberPicThree = getNumberPic(weatherBean.getTime().charAt(3));
        int numberPicFour = getNumberPic(weatherBean.getTime().charAt(4));
        int weatherPic = getWeatherPic(weatherBean.getWeather());
        runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                iv_one.setImageResource(numberPicOne);
                iv_two.setImageResource(numberPicTwo);
                iv_three.setImageResource(numberPicThree);
                iv_four.setImageResource(numberPicFour);

                iv_weather.setImageResource(weatherPic);

                tv_temperature.setText("温度：" + weatherBean.getTemp() + "℃");
                tv_humidity.setText("湿度：" + weatherBean.getHumi() + "%");
            }
        });
    }

    /**
     * 获取对应的天气图片
     *
     * @param weather 天气状态
     * @return 返回天气状态图片的资源ID
     */
    private int getWeatherPic(String weather) {
        String wt = "pic_weather_";
        switch (weather) {
            case "晴天":
                wt += "tianqing";
                break;
            case "多云":
                wt += "duoyun";
                break;
            case "雨天":
                wt += "dayu";
                break;
            case "雪天":
                wt += "daxue";
                break;
            default:
                break;
        }
        return getResources().getIdentifier(wt, "drawable", getPackageName());
    }

    /**
     * 获取指定数字的图片
     *
     * @param c 指定位置的Char
     * @return 返回指定图片的ID
     */
    private int getNumberPic(char c) {
        return getResources().getIdentifier("pic_number" + c, "drawable", getPackageName());
    }
}
