package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.google.gson.Gson;
import com.realmax.smarthomeversion2.App;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.activity.bean.LightBean;
import com.realmax.smarthomeversion2.activity.bean.RoomBean;
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
    private String tag = "control_01";
    private ArrayList<Integer> currentLightStatus;
    private int currentPosition;
    private LightBean lightBean;
    private ArrayList<RoomBean> roomBeans;

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
        currentPosition = 0;

        roomBeans = new ArrayList<>();
        roomBeans.add(new RoomBean("客厅", new int[]{1}));
        roomBeans.add(new RoomBean("洗手间", new int[]{2}));
        roomBeans.add(new RoomBean("仓储间", new int[]{3}));
        roomBeans.add(new RoomBean("餐厅", new int[]{4}));
        roomBeans.add(new RoomBean("门厅", new int[]{5}));
        roomBeans.add(new RoomBean("车库", new int[]{6}));
        roomBeans.add(new RoomBean("走廊", new int[]{7}));
        roomBeans.add(new RoomBean("卧室A", new int[]{8}));
        roomBeans.add(new RoomBean("洗浴间", new int[]{9, 10}));
        roomBeans.add(new RoomBean("卧室B", new int[]{11}));
        roomBeans.add(new RoomBean("卧室C洗手间", new int[]{12}));
        roomBeans.add(new RoomBean("卧室C", new int[]{13}));
        roomBeans.add(new RoomBean("更衣间", new int[]{14}));
        roomBeans.add(new RoomBean("书房", new int[]{17}));
        roomBeans.add(new RoomBean("庭院", new int[]{15}));
        roomBeans.add(new RoomBean("院墙", new int[]{16}));

        lightBean = new LightBean(new ArrayList<>());

        // 制定房间内灯的状态
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
                    uiHandler.post(() -> App.showToast("小灯断开连接"));
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
                            String lightS = "lightList_S";
                            if (jsonObject.has(lightS)) {
                                lightBean = new Gson().fromJson(msg, LightBean.class);
                                // 获取到数据刷新列表
                                uiHandler.post(() -> {
                                    currentLightStatus.clear();
                                    for (int i : roomBeans.get(currentPosition).getModel()) {
                                        // i 为灯的ID
                                        currentLightStatus.add(lightBean.getLightList_S().get(i - 1));
                                        /*if (i - 1 < lightBean.getLightList_S().size()) {
                                            // 现实中指定客厅的灯
                                            currentLightStatus.add(lightBean.getLightList_S().get(i - 1));
                                        }*/
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
        if (lightBean == null) {
            return;
        }

        switch (type) {
            case 0:
                if (currentPosition > 0) {
                    currentLightStatus.clear();
                    currentPosition--;
                    int[] model = roomBeans.get(currentPosition).getModel();
                    for (int i : model) {
                        L.e("" + i);
                        if (i - 1 < lightBean.getLightList_S().size()) {
                            currentLightStatus.add(lightBean.getLightList_S().get(i - 1));
                        }
                    }
                }
                break;
            case 1:
                if (currentPosition < roomBeans.size() - 1) {
                    currentLightStatus.clear();
                    currentPosition++;
                    int[] model = roomBeans.get(currentPosition).getModel();
                    for (int i : model) {
                        if (i - 1 < lightBean.getLightList_S().size()) {
                            currentLightStatus.add(lightBean.getLightList_S().get(i - 1));
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
            int[] model = roomBeans.get(currentPosition).getModel();
            tvLabel.setText(roomBeans.get(currentPosition).getRoomName() + model[position] + "号灯");

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
                            for (int value : model) {
                                property.put("light" + value, getItem(position) == OPEN ? CLOSE : OPEN);
                            }
                            lightControl.publish(property);
                        }

                        // 向控制起发送控制灯的指令
                        ArrayList<Integer> lightC = new ArrayList<>(lightBean.getLightList_S());
                        lightC.set(model[position] - 1, getItem(position) == OPEN ? CLOSE : OPEN);
                        LightBean lightBean = new LightBean(lightC);
                        ValueUtil.sendLightOpenOrCloseCmd(lightBean, tag);
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
