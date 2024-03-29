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
import com.realmax.smarthomeversion2.Constant;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.activity.bean.CurtainAndAcBean;
import com.realmax.smarthomeversion2.activity.bean.RoomBean;
import com.realmax.smarthomeversion2.bean.LinkBean;
import com.realmax.smarthomeversion2.mqtt.CurtainControl;
import com.realmax.smarthomeversion2.mqtt.MqttControl;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ayuan
 * 窗帘
 */
public class CurtainActivity extends BaseActivity {
    private RelativeLayout rlBack;
    private ListView lvList;
    private ImageView ivSwitchLeft;
    private ImageView ivSwitchRight;
    private TextView tvCurrentRoom;
    private CustomerAdapter customerAdapter;
    private String tag = "control_02";
    private ArrayList<Integer> currentCurtainStatus;
    private ArrayList<Integer> currentCurtainSwitchStatus;
    /**
     * 当前位置
     */
    private int currentPosition = 0;
    public static ArrayList<RoomBean> roomBeans;
    private CurtainAndAcBean curtainAndAcBean;

    static {
        roomBeans = new ArrayList<>();
        roomBeans.add(new RoomBean("客厅", new int[]{1, 2}));
        roomBeans.add(new RoomBean("餐厅", new int[]{3}));
        roomBeans.add(new RoomBean("门厅", new int[]{4}));
        roomBeans.add(new RoomBean("走廊", new int[]{5}));
        roomBeans.add(new RoomBean("卧室A", new int[]{6}));
        roomBeans.add(new RoomBean("卧室B", new int[]{7}));
        roomBeans.add(new RoomBean("卧室C", new int[]{8}));
        roomBeans.add(new RoomBean("车库", new int[]{9}));
        roomBeans.add(new RoomBean("书房", new int[]{10}));
    }

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

        curtainAndAcBean = new CurtainAndAcBean(new CurtainAndAcBean.AcSBean(), new ArrayList<>(), new ArrayList<>());

        currentCurtainStatus = new ArrayList<>();
        currentCurtainSwitchStatus = new ArrayList<>();

        // 初始化列表
        customerAdapter = new CustomerAdapter();
        lvList.setAdapter(customerAdapter);

        // 拿到当前连接的Handler用于接受消息
        LinkBean baseNettyHandler = Constant.getLinkBeanByTag(tag);
        if (baseNettyHandler != null) {
            baseNettyHandler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    uiHandler.post(() -> App.showToast("窗帘断开连接"));
                    L.e("窗帘断开连接");
                }

                @Override
                public void getResultData(String msg) {
                    try {
                        if (!TextUtils.isEmpty(msg)) {
                            L.e(tag + "-----------接收");
                            JSONObject jsonObject = new JSONObject(msg);
                            String curtainS = "curtain_S";
                            if (jsonObject.has(curtainS)) {
                                CurtainAndAcBean bean = new Gson().fromJson(msg, CurtainAndAcBean.class);
                                if (!bean.getCurtain_C().equals(curtainAndAcBean.getCurtain_C()) || !bean.getAc_S().equals(curtainAndAcBean.getAc_S())) {
                                    curtainAndAcBean = bean;

                                    L.e(curtainAndAcBean.toString());

                                    // 获取到数据刷新列表
                                    uiHandler.post(() -> {
                                        currentCurtainStatus.clear();
                                        currentCurtainSwitchStatus.clear();
                                        for (int i : roomBeans.get(currentPosition).getModel()) {
                                            currentCurtainStatus.add(CurtainActivity.this.curtainAndAcBean.getCurtain_S().get(i - 1));
                                            currentCurtainSwitchStatus.add(CurtainActivity.this.curtainAndAcBean.getCurtain_C().get(i - 1));
                                        }
                                        customerAdapter.notifyDataSetChanged();
                                    });
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

    /**
     * 切换客厅
     *
     * @param type 0表示向左，1表示向右
     */
    @SuppressLint("SetTextI18n")
    private void switchPage(int type) {
        if (curtainAndAcBean == null) {
            return;
        }

        // 切换客厅
        switch (type) {
            case 0:
                if (currentPosition > 0) {
                    currentCurtainStatus.clear();
                    currentCurtainSwitchStatus.clear();
                    currentPosition--;
                    int[] curtailId = roomBeans.get(currentPosition).getModel();
                    for (int i : curtailId) {
                        L.e("" + i);
                        if (i - 1 < curtainAndAcBean.getCurtain_S().size()) {
                            currentCurtainStatus.add(curtainAndAcBean.getCurtain_S().get(i - 1));
                            currentCurtainSwitchStatus.add(curtainAndAcBean.getCurtain_C().get(i - 1));
                        }
                    }
                }
                break;
            case 1:
                if (currentPosition < roomBeans.size() - 1) {
                    currentCurtainStatus.clear();
                    currentCurtainSwitchStatus.clear();
                    currentPosition++;
                    int[] curtailId = roomBeans.get(currentPosition).getModel();
                    for (int i : curtailId) {
                        L.e("" + i);
                        if (i - 1 < curtainAndAcBean.getCurtain_S().size()) {
                            currentCurtainStatus.add(curtainAndAcBean.getCurtain_S().get(i - 1));
                            currentCurtainSwitchStatus.add(curtainAndAcBean.getCurtain_C().get(i - 1));
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
        private final int CLOSE = 0;
        private TextView tvLabel;
        private SwitchCompat swToggle;
        private CheckBox cbCheck;

        @Override
        public int getCount() {
            return currentCurtainSwitchStatus.size();
        }

        @Override
        public Integer getItem(int position) {
            return currentCurtainSwitchStatus.get(position);
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
                view = View.inflate(CurtainActivity.this, R.layout.item_light, null);
            } else {
                view = convertView;
            }
            initView(view);
            int[] model = roomBeans.get(currentPosition).getModel();
            tvLabel.setText(roomBeans.get(currentPosition).getRoomName() + model[position] + "号窗帘");

            swToggle.setOnTouchListener(null);
            swToggle.setOnClickListener((View v) -> {
                swToggle.toggle();
                // 将设置指令同步至云端
                /*MqttControl curtain = ValueUtil.getMqttControlHashMap().get("Curtain");
                if (curtain != null) {
                    CurtainControl curtainControl = (CurtainControl) curtain;
                    // 包装json，发送MQTT指令
                    HashMap<String, Object> copyFrom = new HashMap<>();
                    copyFrom.put("curtain" + model[position], getItem(position) == OPEN ? CLOSE : OPEN);
                    JSONObject property = new JSONObject(copyFrom);
                    curtainControl.publish(property);
                }*/

                ArrayList<Integer> curtainC = new ArrayList<>(curtainAndAcBean.getCurtain_C());
                curtainC.set(model[position] - 1, getItem(position) == OPEN ? CLOSE : OPEN);
                CurtainAndAcBean curtainAndAcBean = new CurtainAndAcBean(curtainC);
                ValueUtil.sendCurtainOpenOrCloseCmd(curtainAndAcBean, tag);
                notifyDataSetChanged();
            });

            if (getItem(position) == 1) {
                swToggle.setChecked(true);
            } else {
                swToggle.setChecked(false);
            }

            cbCheck.setChecked(currentCurtainStatus.get(position) == 1);
            return view;
        }

        private void initView(View view) {
            tvLabel = view.findViewById(R.id.tv_label);
            cbCheck = view.findViewById(R.id.cb_check);
            swToggle = view.findViewById(R.id.sw_toggle);

            cbCheck.setBackgroundResource(R.drawable.xml_check_select_curtain);
        }
    }
}
