package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
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
 * 灯
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
    public static ArrayList<RoomBean> roomBeans;

    // 初始化房间中设备
    static {
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
        roomBeans.add(new RoomBean("书房", new int[]{17, 18}));
        roomBeans.add(new RoomBean("庭院", new int[]{15}));
        roomBeans.add(new RoomBean("院墙", new int[]{16}));
    }

    /**
     * 获取布局资源
     *
     * @return 布局资源ID
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_light;
    }

    /**
     * 初始化界面中的控件
     */
    @Override
    protected void initView() {
        rlBack = findViewById(R.id.rl_back);
        lvList = findViewById(R.id.lv_list);
        ivSwitchLeft = findViewById(R.id.iv_switchLeft);
        ivSwitchRight = findViewById(R.id.iv_switchRight);
        tvCurrentRoom = findViewById(R.id.tv_currentRoom);
    }

    /**
     * 初始化控件的监听
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void initEvent() {
        // 返回主页
        rlBack.setOnClickListener((View v) -> finish());

        // 切换房间
        ivSwitchLeft.setOnClickListener((View v) -> switchPage(0));

        // 切换房间
        ivSwitchRight.setOnClickListener((View v) -> switchPage(1));
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        // 设置当前所处房间的编号
        currentPosition = 0;

        lightBean = new LightBean(new ArrayList<>());

        // 制定房间内灯的状态
        currentLightStatus = new ArrayList<>();

        // 初始化列表
        customerAdapter = new CustomerAdapter();
        lvList.setAdapter(customerAdapter);

        // 设置TCP数据接受监听，拿到当前连接的Handler用于接受消息
        CustomerHandlerBase customerHandler = getCustomerHandler(tag);
        if (customerHandler != null) {
            customerHandler.setCustomerCallback(new CustomerCallback() {
                /**
                 * 连接断开，连接断开时自动调用
                 */
                @Override
                public void disConnected() {
                    uiHandler.post(() -> App.showToast("小灯断开连接"));
                    L.e("小灯断开连接");
                    // 获取控制器连接状态集合，并将当前控制器的状态设置上去
                    ValueUtil.getIsConnected().put(tag, false);
                    ValueUtil.getHandlerHashMap().put(tag, null);
                }

                /**
                 * 接受数据，有数据发送过来时自动调用
                 * @param msg 字符串数据
                 */
                @Override
                public void getResultData(String msg) {
                    try {
                        if (!TextUtils.isEmpty(msg)) {
                            L.e(tag + "-----------接收");
                            JSONObject jsonObject = new JSONObject(msg);
                            // 验证是否是当前电灯的json数据
                            String lightS = "lightList_S";
                            if (jsonObject.has(lightS)) {
                                lightBean = new Gson().fromJson(msg, LightBean.class);
                                // 获取到数据刷新列表
                                uiHandler.post(() -> {
                                    // 清空当前界面中显示的部分灯状态的集合
                                    currentLightStatus.clear();
                                    // 根据当前房间内的等来填充currentLightStatus集合，用于列表显示
                                    for (int i : roomBeans.get(currentPosition).getModel()) {
                                        // i 为灯的ID
                                        currentLightStatus.add(lightBean.getLightList_S().get(i - 1));
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
     * 集合的适配器
     *
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

            swToggle.setOnClickListener(null);
            // 设置列表中switch开关的监听，这里用的是点击事件
            swToggle.setOnClickListener((View v) -> {
                try {
                    // 控制switch开关不会立即改变状态
                    swToggle.toggle();
                    // 在主界面中填存入集合中的灯的控制类，通过获取制定设备的mqtt连接，将设置指令同步至云端
                    MqttControl light = ValueUtil.getMqttControlHashMap().get("light");
                    if (light != null) {
                        // 因为LightControl是继承MqttControl的，所以可以直接进行强转
                        LightControl lightControl = (LightControl) light;
                        // 包装json，发送MQTT指令
                        JSONObject property = new JSONObject();
                        property.put("light" + model[position], getItem(position) == OPEN ? CLOSE : OPEN);
                        lightControl.publish(property);
                    }

                    // 向控制起发送控制灯的指令
                    ArrayList<Integer> lightC = new ArrayList<>(lightBean.getLightList_S());
                    lightC.set(model[position] - 1, getItem(position) == OPEN ? CLOSE : OPEN);
                    LightBean lightBean = new LightBean(lightC);
                    ValueUtil.sendLightOpenOrCloseCmd(lightBean, tag);
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

            // 设置当前电灯的状态
            if (getItem(position) == 1) {
                cbCheck.setChecked(true);
                swToggle.setChecked(true);
            } else {
                cbCheck.setChecked(false);
                swToggle.setChecked(false);
            }
            return view;
        }

        private void initView(View view) {
            tvLabel = view.findViewById(R.id.tv_label);
            swToggle = view.findViewById(R.id.sw_toggle);
            cbCheck = view.findViewById(R.id.cb_check);
        }
    }
}
