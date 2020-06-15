package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.activity.bean.DoorAndAirQualityBean;
import com.realmax.smarthomeversion2.activity.bean.HumenAndRobotAndAlarmBean;
import com.realmax.smarthomeversion2.activity.bean.RoomBean;
import com.realmax.smarthomeversion2.activity.bean.WeatherBean;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;

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
    private String control3 = "control_03";
    private String control5 = "control_05";
    private int currentPosition = 0;
    private ArrayList<RoomBean> roomBeans;
    private ArrayList<Integer> currentHumenSensor;

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
        rl_back.setOnClickListener((View v) -> finish());

        iv_switchLeft.setOnClickListener((View v) -> switchPage(0));

        iv_switchRight.setOnClickListener((View v) -> switchPage(1));
    }

    @Override
    protected void initData() {
        roomBeans = new ArrayList<>();
        roomBeans.add(new RoomBean("客厅", new int[]{1, 2, 4}));
        roomBeans.add(new RoomBean("洗手间", new int[]{6, 7}));
        roomBeans.add(new RoomBean("仓储间", new int[]{23}));
        roomBeans.add(new RoomBean("餐厅", new int[]{3, 8, 24}));
        roomBeans.add(new RoomBean("门厅", new int[]{5, 25, 26, 27, 10, 9}));
        roomBeans.add(new RoomBean("车库", new int[]{11, 12}));
        roomBeans.add(new RoomBean("走廊", new int[]{13, 28, 14, 29}));
        roomBeans.add(new RoomBean("卧室A", new int[]{15}));
        roomBeans.add(new RoomBean("洗浴间", new int[]{30, 31}));
        roomBeans.add(new RoomBean("卧室B", new int[]{16}));
        roomBeans.add(new RoomBean("卧室C洗浴间", new int[]{17}));
        roomBeans.add(new RoomBean("卧室C", new int[]{18}));
        roomBeans.add(new RoomBean("更衣间", new int[]{19}));
        roomBeans.add(new RoomBean("书房", new int[]{20, 21, 22}));

        currentHumenSensor = new ArrayList<>(31);

        setWeatherListener();
        setSensorListener();
    }

    /**
     * 设置天气的TCP消息监听
     */
    private void setWeatherListener() {
        ValueUtil.sendWeatherCmd(tag);
        CustomerHandlerBase customerHandler = getCustomerHandler(tag);
        if (customerHandler != null) {
            customerHandler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    L.e("连接断开");
                    ValueUtil.getIsConnected().put(tag, false);
                    ValueUtil.getHandlerHashMap().put(tag, null);
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

    /**
     * 设置传感器的TCP消息监听
     */
    private void setSensorListener() {
        CustomerHandlerBase control5Handler = ValueUtil.getHandlerHashMap().get(control5);
        if (control5Handler != null) {
            control5Handler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    L.e("控制器5断开连接");
                    ValueUtil.getIsConnected().put(control5, false);
                    ValueUtil.getHandlerHashMap().put(control5, null);
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void getResultData(String msg) {
                    try {
                        if (!TextUtils.isEmpty(msg)) {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.has("humanBodySensor_S")) {
                                HumenAndRobotAndAlarmBean humenAndRobotAndAlarmBean = new Gson().fromJson(msg, HumenAndRobotAndAlarmBean.class);
                                L.e("解析完成的数据---------" + humenAndRobotAndAlarmBean.toString());
                                int sum = 0;
                                int[] model = roomBeans.get(currentPosition).getModel();
                                for (int i : model) {
                                    Integer integer = humenAndRobotAndAlarmBean.getHumanBodySensor_S().get(i - 1);
                                    sum += integer;
                                }
                                int finalSum = sum;
                                uiHandler.post(() -> {
                                    tv_character.setText("人体传感器:" + (finalSum > 0 ? roomBeans.get(currentPosition).getRoomName() + "有人" : "无人"));
                                });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        CustomerHandlerBase control3Handler = ValueUtil.getHandlerHashMap().get(control3);
        if (control3Handler != null) {
            control3Handler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    L.e("控制器3断开连接");
                    ValueUtil.getIsConnected().put(control3, false);
                    ValueUtil.getHandlerHashMap().put(control3, null);
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void getResultData(String msg) {
                    try {
                        if (!TextUtils.isEmpty(msg)) {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.has("airQuality_S")) {
                                DoorAndAirQualityBean doorAndAirQualityBean = new Gson().fromJson(msg, DoorAndAirQualityBean.class);
                                uiHandler.post(() -> {
                                    List<DoorAndAirQualityBean.AirQualitySBean> airQuality = doorAndAirQualityBean.getAirQuality_S();
                                    for (DoorAndAirQualityBean.AirQualitySBean airQualityBean : airQuality) {
                                        L.e(airQuality.toString());
                                    }
                                    int smok = 0;
                                    int humidity = 0;
                                    switch (currentPosition) {
                                        case 0:
                                            smok = airQuality.get(0).getSmoke();
                                            humidity = airQuality.get(0).getHumidity();
                                            tv_smoke.setVisibility(View.VISIBLE);
                                            tv_humidity.setVisibility(View.VISIBLE);
                                            break;
                                        case 3:
                                            smok = airQuality.get(1).getSmoke();
                                            humidity = airQuality.get(1).getHumidity();
                                            tv_smoke.setVisibility(View.VISIBLE);
                                            tv_humidity.setVisibility(View.VISIBLE);
                                            break;
                                        case 5:
                                            smok = airQuality.get(2).getSmoke();
                                            humidity = airQuality.get(2).getHumidity();
                                            tv_smoke.setVisibility(View.VISIBLE);
                                            tv_humidity.setVisibility(View.VISIBLE);
                                            break;
                                        case 7:
                                            smok = airQuality.get(3).getSmoke();
                                            humidity = airQuality.get(3).getHumidity();
                                            tv_smoke.setVisibility(View.VISIBLE);
                                            tv_humidity.setVisibility(View.VISIBLE);
                                            break;
                                        case 9:
                                            smok = airQuality.get(4).getSmoke();
                                            humidity = airQuality.get(4).getHumidity();
                                            tv_smoke.setVisibility(View.VISIBLE);
                                            tv_humidity.setVisibility(View.VISIBLE);
                                            break;
                                        case 11:
                                            smok = airQuality.get(5).getSmoke();
                                            humidity = airQuality.get(5).getHumidity();
                                            tv_smoke.setVisibility(View.VISIBLE);
                                            tv_humidity.setVisibility(View.VISIBLE);
                                            break;
                                        case 13:
                                            smok = airQuality.get(6).getSmoke();
                                            humidity = airQuality.get(6).getHumidity();
                                            tv_smoke.setVisibility(View.VISIBLE);
                                            tv_humidity.setVisibility(View.VISIBLE);
                                            break;
                                        default:
                                            L.e("无选择项");
                                            break;
                                    }
                                    tv_humidity.setText("湿度:" + humidity + "%");
                                    tv_smoke.setText("烟雾:" + (smok == 1 ? "有" : "无"));
                                });
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
        tv_smoke.setVisibility(View.GONE);
        tv_humidity.setVisibility(View.GONE);
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

                tv_temperature.setText("温度:" + weatherBean.getTemp() + "℃");
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
