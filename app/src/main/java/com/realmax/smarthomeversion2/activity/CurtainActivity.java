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
public class CurtainActivity extends BaseActivity {
    private RelativeLayout rlBack;
    private ListView lvList;
    private ImageView ivSwitchLeft;
    private ImageView ivSwitchRight;
    private TextView tvCurrentRoom;
    private CustomerAdapter customerAdapter;
    private String tag;
    private ArrayList<Integer> currentCurtainStatus;
    /**
     * 当前位置
     */
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

        currentCurtainStatus = new ArrayList<>();

        // 初始化列表
        customerAdapter = new CustomerAdapter();
        lvList.setAdapter(customerAdapter);

        // 拿到当前连接的Handler用于接受消息
        CustomerHandlerBase customerHandler = getCustomerHandler(tag);
        if (customerHandler != null) {
            customerHandler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    App.showToast("窗帘断开连接");
                    L.e("窗帘断开连接");
                    ValueUtil.getIsConnected().put(tag, false);
                    ValueUtil.getHandlerHashMap().put(tag, null);
                }

                @Override
                public void getResultData(String msg) {
                    try {
                        if (!TextUtils.isEmpty(msg)) {
                            JSONObject jsonObject = new JSONObject(msg);
                            String curtainS = "Curtain_S";
                            if (jsonObject.has(curtainS)) {
                                lightOrCurtainBean = new Gson().fromJson(msg, LightOrCurtainBean.class);

                                // 获取到数据刷新列表
                                runOnUiThread(() -> {
                                    currentCurtainStatus.clear();
                                    for (int i : roomBeans.get(currentPosition).getCurtailId()) {
                                        if (i - 1 < lightOrCurtainBean.getCurtain_S().size()) {
                                            L.e("i:" + i);
                                            // 现实中指定客厅的灯
                                            currentCurtainStatus.add(lightOrCurtainBean.getCurtain_S().get(i - 1));
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
     * @param type 0表示向左，1表示向右
     */
    @SuppressLint("SetTextI18n")
    private void switchPage(int type) {
        if (lightOrCurtainBean == null) {
            return;
        }

        // 切换客厅
        switch (type) {
            case 0:
                if (currentPosition > 0) {
                    currentCurtainStatus.clear();
                    currentPosition--;
                    int[] curtailId = roomBeans.get(currentPosition).getCurtailId();
                    for (int i : curtailId) {
                        L.e("" + i);
                        if (i - 1 < lightOrCurtainBean.getCurtain_S().size()) {
                            currentCurtainStatus.add(lightOrCurtainBean.getCurtain_S().get(i - 1));
                        }
                    }
                }
                break;
            case 1:
                if (currentPosition < roomBeans.size() - 1) {
                    currentCurtainStatus.clear();
                    /*currentCurtainControl.clear();*/
                    currentPosition++;
                    int[] curtailId = roomBeans.get(currentPosition).getCurtailId();
                    for (int i : curtailId) {
                        L.e("" + i);
                        if (i - 1 < lightOrCurtainBean.getCurtain_S().size()) {
                            currentCurtainStatus.add(lightOrCurtainBean.getCurtain_S().get(i - 1));
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
            return currentCurtainStatus.size();
        }

        @Override
        public Integer getItem(int position) {
            return currentCurtainStatus.get(position);
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
            int[] curtailId = roomBeans.get(currentPosition).getCurtailId();
            tvLabel.setText(roomBeans.get(currentPosition).getRoomName() + curtailId[position] + "号窗帘");

            // 设置当前电灯的状态
            cbCheck.setChecked(getItem(position) == 1);

            swToggle.setOnTouchListener(null);

            swToggle.setChecked(getItem(position) == 1);

            swToggle.setOnTouchListener((View v, MotionEvent event) -> {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    ArrayList<Integer> curtainC = new ArrayList<>(lightOrCurtainBean.getCurtain_S());
                    curtainC.set(curtailId[position] - 1, getItem(position) == OPEN ? CLOSE : OPEN);
                    LightOrCurtainBean bean = new LightOrCurtainBean(CurtainActivity.this.lightOrCurtainBean.getLight_S(), curtainC);
                    ValueUtil.sendCurtainOpenOrCloseCmd(bean);
                }
                return true;
            });
            return view;
        }

        private void initView(View view) {
            tvLabel = view.findViewById(R.id.tv_label);
            cbCheck = view.findViewById(R.id.cb_check);
            swToggle = view.findViewById(R.id.sw_toggle);
        }
    }
}
