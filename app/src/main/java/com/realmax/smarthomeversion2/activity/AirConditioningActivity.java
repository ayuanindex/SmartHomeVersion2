package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.realmax.smarthomeversion2.Constant;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.activity.bean.AcAndTvAndMusicBean;
import com.realmax.smarthomeversion2.activity.bean.CurtainAndAcBean;
import com.realmax.smarthomeversion2.activity.bean.RoomBean;
import com.realmax.smarthomeversion2.activity.bean.WeatherBean;
import com.realmax.smarthomeversion2.bean.LinkBean;
import com.realmax.smarthomeversion2.tcp.BaseNettyHandler;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandler;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author ayuan
 * 空调
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
    private String tag = "virtual";
    private String control2 = "control_02";
    private String control4 = "control_04";
    public static ArrayList<RoomBean> roomBeans;
    private int currentPosition = 0;
    private AcAndTvAndMusicBean.AcSBean currentBean;
    private AcAndTvAndMusicBean acAndTvAndMusicBean;
    private CurtainAndAcBean curtainAndAcBean;

    static {
        roomBeans = new ArrayList<>();
        roomBeans.add(new RoomBean("客厅", new int[]{1}));
        roomBeans.add(new RoomBean("餐厅", new int[]{2}));
        roomBeans.add(new RoomBean("卧室A", new int[]{3}));
        roomBeans.add(new RoomBean("卧室B", new int[]{4}));
        roomBeans.add(new RoomBean("卧室C", new int[]{5}));
        roomBeans.add(new RoomBean("书房", new int[]{6}));
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
        rlBack.setOnClickListener(v -> {
            finish();
        });

        ivSwitchLeft.setOnClickListener((View v) -> switchPage(0));

        ivSwitchRight.setOnClickListener((View v) -> switchPage(1));

        cbPowerSwitch.setOnClickListener((View v) -> {
            cbPowerSwitch.toggle();
            if (currentPosition > 0) {
                if (acAndTvAndMusicBean != null) {
                    int acPower = acAndTvAndMusicBean.getAc_S().get(currentPosition - 1).getAcPower();
                    acAndTvAndMusicBean.getAc_S().get(currentPosition - 1).setAcPower(acPower == 1 ? 0 : 1);
                    ValueUtil.sendAcCmd(acAndTvAndMusicBean.getAc_S(), control4);
                }
            } else if (currentPosition == 0) {
                if (curtainAndAcBean != null) {
                    ValueUtil.sendSingleAc(
                            curtainAndAcBean.getAc_S().getAcPower() == 1 ? 0 : 1,
                            curtainAndAcBean.getAc_S().getMode(),
                            curtainAndAcBean.getAc_S().getWindSpeed(),
                            (int) curtainAndAcBean.getAc_S().getTemperature(),
                            control2);
                }
            }
        });

        ivCold.setOnClickListener((View v) -> {
            if (currentPosition > 0) {
                if (acAndTvAndMusicBean != null) {
                    acAndTvAndMusicBean.getAc_S().get(currentPosition - 1).setMode(0);
                    ValueUtil.sendAcCmd(acAndTvAndMusicBean.getAc_S(), control4);
                }
            } else if (currentPosition == 0) {
                if (curtainAndAcBean != null) {
                    ValueUtil.sendSingleAc(
                            curtainAndAcBean.getAc_S().getAcPower(),
                            0,
                            curtainAndAcBean.getAc_S().getWindSpeed(),
                            (int) curtainAndAcBean.getAc_S().getTemperature(),
                            control2);
                }
            }
        });

        ivHot.setOnClickListener((View v) -> {
            if (currentPosition > 0) {
                if (acAndTvAndMusicBean != null) {
                    acAndTvAndMusicBean.getAc_S().get(currentPosition - 1).setMode(1);
                    ValueUtil.sendAcCmd(acAndTvAndMusicBean.getAc_S(), control4);
                }
            } else if (currentPosition == 0) {
                if (curtainAndAcBean != null) {
                    ValueUtil.sendSingleAc(
                            curtainAndAcBean.getAc_S().getAcPower(),
                            1,
                            curtainAndAcBean.getAc_S().getWindSpeed(),
                            (int) curtainAndAcBean.getAc_S().getTemperature(),
                            control2);
                }
            }
        });

        ivPlus.setOnClickListener((View v) -> {
            if (currentPosition > 0) {
                if (acAndTvAndMusicBean != null) {
                    int windSpeed = acAndTvAndMusicBean.getAc_S().get(currentPosition - 1).getWindSpeed();
                    if (windSpeed < 5) {
                        windSpeed += 1;
                    }
                    acAndTvAndMusicBean.getAc_S().get(currentPosition - 1).setWindSpeed(windSpeed);
                    ValueUtil.sendAcCmd(acAndTvAndMusicBean.getAc_S(), control4);
                }
            } else if (currentPosition == 0) {
                if (curtainAndAcBean != null) {
                    int windSpeed = curtainAndAcBean.getAc_S().getWindSpeed();
                    if (windSpeed < 5) {
                        windSpeed += 1;
                    }
                    ValueUtil.sendSingleAc(
                            curtainAndAcBean.getAc_S().getAcPower(),
                            curtainAndAcBean.getAc_S().getMode(),
                            windSpeed,
                            (int) curtainAndAcBean.getAc_S().getTemperature(),
                            control2);
                }
            }
        });

        ivReduce.setOnClickListener((View v) -> {
            if (currentPosition > 0) {
                if (acAndTvAndMusicBean != null) {
                    int windSpeed = acAndTvAndMusicBean.getAc_S().get(currentPosition - 1).getWindSpeed();
                    if (windSpeed > 1) {
                        windSpeed -= 1;
                    }
                    acAndTvAndMusicBean.getAc_S().get(currentPosition - 1).setWindSpeed(windSpeed);
                    ValueUtil.sendAcCmd(acAndTvAndMusicBean.getAc_S(), control4);
                }
            } else if (currentPosition == 0) {
                if (curtainAndAcBean != null) {
                    int windSpeed = curtainAndAcBean.getAc_S().getWindSpeed();
                    if (windSpeed > 1) {
                        windSpeed -= 1;
                    }
                    ValueUtil.sendSingleAc(
                            curtainAndAcBean.getAc_S().getAcPower(),
                            curtainAndAcBean.getAc_S().getMode(),
                            windSpeed,
                            (int) curtainAndAcBean.getAc_S().getTemperature(),
                            control2);
                }
            }
        });

        ivUp.setOnClickListener((View v) -> {
            if (currentPosition > 0) {
                if (acAndTvAndMusicBean != null) {
                    int temperature = acAndTvAndMusicBean.getAc_S().get(currentPosition - 1).getTemperature();
                    if (temperature < 28) {
                        temperature += 1;
                    }
                    acAndTvAndMusicBean.getAc_S().get(currentPosition - 1).setTemperature(temperature);
                    ValueUtil.sendAcCmd(acAndTvAndMusicBean.getAc_S(), control4);
                }
            } else if (currentPosition == 0) {
                if (curtainAndAcBean != null) {
                    int temperature = (int) curtainAndAcBean.getAc_S().getTemperature();
                    if (temperature < 28) {
                        temperature += 1;
                    }
                    ValueUtil.sendSingleAc(
                            curtainAndAcBean.getAc_S().getAcPower(),
                            curtainAndAcBean.getAc_S().getMode(),
                            curtainAndAcBean.getAc_S().getWindSpeed(),
                            temperature,
                            control2);
                }
            }
        });

        ivDown.setOnClickListener((View v) -> {
            if (currentPosition > 0) {
                if (acAndTvAndMusicBean != null) {
                    int temperature = acAndTvAndMusicBean.getAc_S().get(currentPosition - 1).getTemperature();
                    if (temperature > 16) {
                        temperature -= 1;
                    }
                    acAndTvAndMusicBean.getAc_S().get(currentPosition - 1).setTemperature(temperature);
                    ValueUtil.sendAcCmd(acAndTvAndMusicBean.getAc_S(), control4);
                }
            } else if (currentPosition == 0) {
                if (curtainAndAcBean != null) {
                    int temperature = (int) curtainAndAcBean.getAc_S().getTemperature();
                    if (temperature > 16) {
                        temperature -= 1;
                    }
                    ValueUtil.sendSingleAc(
                            curtainAndAcBean.getAc_S().getAcPower(),
                            curtainAndAcBean.getAc_S().getMode(),
                            curtainAndAcBean.getAc_S().getWindSpeed(),
                            temperature,
                            control2);
                }
            }
        });
    }

    @Override
    protected void initData() {
        currentPosition = 0;
        currentBean = new AcAndTvAndMusicBean.AcSBean();

        setWeatherListener();
        setAcListener();
    }

    /**
     * 设置天气的TCP接收监听
     */
    private void setWeatherListener() {
        LinkBean virtualHandler = Constant.getLinkBeanByTag(tag);
        if (virtualHandler != null) {
            virtualHandler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    L.e("连接断开");
                }

                @Override
                public void getResultData(String msg) {
                    try {
                        if (!TextUtils.isEmpty(msg)) {
                            L.e("msg-------" + msg);
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.optString("cmd").equals("ans")) {
                                WeatherBean weatherBean = new Gson().fromJson(msg, WeatherBean.class);
                                setWeatherUi(weatherBean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        ValueUtil.sendWeatherCmd(tag);
    }

    /**
     * 设置空调的监听
     */
    private void setAcListener() {
        LinkBean control2Handler = Constant.getLinkBeanByTag(control2);
        if (control2Handler != null) {
            control2Handler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    L.e("控制器2断开连接");
                }

                @Override
                public void getResultData(String msg) {
                    try {
                        if (!TextUtils.isEmpty(msg)) {
                            L.e("msg----" + msg);
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.has("ac_S")) {
                                curtainAndAcBean = new Gson().fromJson(msg, CurtainAndAcBean.class);
                                if (currentPosition == 0) {
                                    CurtainAndAcBean.AcSBean ac_s = curtainAndAcBean.getAc_S();
                                    currentBean.setAcPower(ac_s.getAcPower());
                                    currentBean.setCurrentTemperature((int) ac_s.getCurrentTemperature());
                                    currentBean.setMode(ac_s.getMode());
                                    currentBean.setTemperature((int) ac_s.getTemperature());
                                    currentBean.setWindSpeed(ac_s.getWindSpeed());
                                    // 刷新界面UI
                                    refreshUi();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        LinkBean control4Handler = Constant.getLinkBeanByTag(control4);
        if (control4Handler != null) {
            control4Handler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    L.e("控制器4断开连接");
                }

                @Override
                public void getResultData(String msg) {
                    try {
                        if (!TextUtils.isEmpty(msg)) {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.has("ac_S")) {
                                acAndTvAndMusicBean = new Gson().fromJson(msg, AcAndTvAndMusicBean.class);
                                if (currentPosition > 0) {
                                    currentBean = acAndTvAndMusicBean.getAc_S().get(currentPosition - 1);
                                    refreshUi();
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void refreshUi() {
        uiHandler.post(() -> {
            L.e("当前空调状态------————" + currentBean.toString());
            tvTemperature.setText("当前温度：" + currentBean.getCurrentTemperature() + "℃");
            tvSetTemperature.setText("设定温度：" + currentBean.getTemperature() + "℃");
            tvWindSpeed.setText("风速：" + currentBean.getWindSpeed());
            tvMode.setText("模式：" + (currentBean.getMode() == 0 ? "冷风" : "暖风"));
            cbPowerSwitch.setChecked(currentBean.getAcPower() == 1);
        });
    }

    /**
     * 将获取到的天气数据设置到界面控件中
     *
     * @param weatherBean 天气数据
     */
    private void setWeatherUi(WeatherBean weatherBean) {
        int numberPicOne = getNumberPic(weatherBean.getTime().charAt(0));
        int numberPicTwo = getNumberPic(weatherBean.getTime().charAt(1));
        int numberPicThree = getNumberPic(weatherBean.getTime().charAt(3));
        int numberPicFour = getNumberPic(weatherBean.getTime().charAt(4));
        int weatherPic = getWeatherPic(weatherBean.getWeather());
        uiHandler.post(() -> {
            ivOne.setImageResource(numberPicOne);
            ivTwo.setImageResource(numberPicTwo);
            ivThree.setImageResource(numberPicThree);
            ivFour.setImageResource(numberPicFour);

            ivWeather.setImageResource(weatherPic);
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

    /**
     * 切换客厅
     *
     * @param type 0表示向左，1表示向右
     */
    @SuppressLint("SetTextI18n")
    private void switchPage(int type) {
        // 切换客厅
        switch (type) {
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

        tvCurrentRoom.setText(roomBeans.get(currentPosition).getRoomName());
    }

}
