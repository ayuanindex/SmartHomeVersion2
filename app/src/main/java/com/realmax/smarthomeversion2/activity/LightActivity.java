package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.google.gson.Gson;
import com.realmax.smarthomeversion2.App;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.bean.LightOrCurtainBean;
import com.realmax.smarthomeversion2.mqtt.LightControl;
import com.realmax.smarthomeversion2.mqtt.MqttControl;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author ayuan
 */
public class LightActivity extends BaseActivity {
    private RelativeLayout rlBack;
    private ListView lvList;
    private ImageView ivSwitchLeft;
    private ImageView ivSwitchRight;
    private TextView tvCurrentRoom;
    private CustomerAdapter customerAdapter;
    private String tag;
    private ArrayList<Integer> currentLightStatus;
    private int currentPosition;
    private LightOrCurtainBean lightOrCurtainBean;

    @Override
    protected int getLayout() {
        return R.layout.activity_light;
    }

    @Override
    protected void initView() {
        rlBack = findViewById(R.id.rl_back);
        lvList = findViewById(R.id.lv_list);
        ivSwitchLeft = findViewById(R.id.iv_switchLeft);
        ivSwitchRight = findViewById(R.id.iv_switchRight);
        tvCurrentRoom = findViewById(R.id.tv_currentRoom);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initEvent() {
        rlBack.setOnClickListener((View v) -> finish());

        ivSwitchLeft.setOnClickListener((View v) -> switchPage(0));

        ivSwitchRight.setOnClickListener((View v) -> switchPage(1));
    }

    @Override
    protected void initData() {
        tag = getIntent().getStringExtra("tag");
        currentPosition = 0;

        lightOrCurtainBean = new LightOrCurtainBean(new ArrayList<>(), new ArrayList<>());

        currentLightStatus = new ArrayList<>();

        // 初始化列表
        customerAdapter = new CustomerAdapter();
        lvList.setAdapter(customerAdapter);

        // 拿到当前连接的Handler用于接受消息
        CustomerHandlerBase customerHandler = getCustomerHandler(tag);
        if (customerHandler != null) {
            customerHandler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            App.showToast("小灯断开连接");
                        }
                    });
                    L.e("小灯断开连接");
                    ValueUtil.getIsConnected().put(tag, false);
                    ValueUtil.getHandlerHashMap().put(tag, null);
                }

                @Override
                public void getResultData(String msg) {
                    try {
                        /*L.e("msg:" + msg);*/
                        if (!TextUtils.isEmpty(msg)) {
                            JSONObject jsonObject = new JSONObject(msg);
                            // 验证是否是当前电灯的json数据
                            String lightS = "Light_S";
                            if (jsonObject.has(lightS)) {
                                lightOrCurtainBean = new Gson().fromJson(msg, LightOrCurtainBean.class);
                                // 获取到数据刷新列表
                                runOnUiThread(() -> {
                                    currentLightStatus.clear();
                                    for (int i : roomBeans.get(currentPosition).getLightId()) {
                                        if (i - 1 < lightOrCurtainBean.getLight_S().size()) {
                                            // 现实中指定客厅的灯
                                            currentLightStatus.add(lightOrCurtainBean.getLight_S().get(i - 1));
                                        }
                                    }
                                    customerAdapter.notifyDataSetChanged();
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

    /**
     * 切换客厅
     *
     * @param type 左右切换的标示符：0表示向左，1表示向右
     */
    @SuppressLint("SetTextI18n")
    private void switchPage(int type) {
        if (lightOrCurtainBean == null) {
            return;
        }

        switch (type) {
            case 0:
                if (currentPosition > 0) {
                    currentLightStatus.clear();
                    currentPosition--;
                    int[] lightId = roomBeans.get(currentPosition).getLightId();
                    for (int i : lightId) {
                        L.e("" + i);
                        if (i - 1 < lightOrCurtainBean.getLight_S().size()) {
                            currentLightStatus.add(lightOrCurtainBean.getLight_S().get(i - 1));
                        }
                    }
                }
                break;
            case 1:
                if (currentPosition < roomBeans.size() - 1) {
                    currentLightStatus.clear();
                    currentPosition++;
                    int[] lightId = roomBeans.get(currentPosition).getLightId();
                    for (int i : lightId) {
                        if (i - 1 < lightOrCurtainBean.getLight_S().size()) {
                            currentLightStatus.add(lightOrCurtainBean.getLight_S().get(i - 1));
                        }
                    }
                }
                break;
            default:
                break;
        }

        tvCurrentRoom.setText(roomBeans.get(currentPosition).getRoomName());
        customerAdapter.notifyDataSetChanged();
    }

    /**
     * @author ayuan
     */
    private class CustomerAdapter extends BaseAdapter {
        private static final int OPEN = 1;
        private static final int CLOSE = 0;
        private TextView tvLabel;
        private SwitchCompat swToggle;
        private CheckBox cbCheck;

        @Override
        public int getCount() {
            return currentLightStatus.size();
        }

        @Override
        public Integer getItem(int position) {
            return currentLightStatus.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(LightActivity.this, R.layout.item_light, null);
            } else {
                view = convertView;
            }
            initView(view);
            int[] lightId = roomBeans.get(currentPosition).getLightId();
            tvLabel.setText(roomBeans.get(currentPosition).getRoomName() + lightId[position] + "号灯");

            // 设置当前电灯的状态
            cbCheck.setChecked(getItem(position) == 1);

            swToggle.setOnTouchListener((View v, MotionEvent event) -> {
                try {
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        L.e("onTouch:" + position);

                        // 将设置指令同步至云端
                        MqttControl light = ValueUtil.getMqttControlHashMap().get("light");
                        if (light != null) {
                            LightControl lightControl = (LightControl) light;
                            JSONObject property = new JSONObject();
                            for (int value : lightId) {
                                property.put("light" + value, getItem(position) == OPEN ? CLOSE : OPEN);
                            }
                            lightControl.publish(property);
                        }

                        // 向控制起发送控制灯的指令
                        ArrayList<Integer> lightC = new ArrayList<>(lightOrCurtainBean.getLight_S());
                        lightC.set(lightId[position] - 1, getItem(position) == OPEN ? CLOSE : OPEN);
                        LightOrCurtainBean bean = new LightOrCurtainBean(lightC, LightActivity.this.lightOrCurtainBean.getCurtain_S());
                        ValueUtil.sendLightOpenOrCloseCmd(bean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            });
            swToggle.setChecked(getItem(position) == 1);
            return view;
        }

        private void initView(View view) {
            tvLabel = view.findViewById(R.id.tv_label);
            swToggle = view.findViewById(R.id.sw_toggle);
            cbCheck = view.findViewById(R.id.cb_check);
        }
    }
}
