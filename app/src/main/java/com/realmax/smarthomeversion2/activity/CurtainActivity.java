package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.google.gson.Gson;
import com.realmax.smarthomeversion2.App;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.bean.CurtainBean;
import com.realmax.smarthomeversion2.bean.LightBean;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandler;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @ProjectName: SmartHomeVersion2
 * @Package: com.realmax.smarthomeversion2.activity
 * @ClassName: LightActivityy
 * @CreateDate: 2020/4/3 12:07
 */
public class CurtainActivity extends BaseActivity {
    private RelativeLayout rl_back;
    private ListView lv_list;
    private ImageView iv_switchLeft;
    private ImageView iv_switchRight;
    private TextView tv_currentRoom;
    private CurtainBean curtainBean;
    private CustomerAdapter customerAdapter;
    private String tag;
    private ArrayList<Integer> currentCurtainStatus;
    private ArrayList<Integer> currentCurtainControl;
    private int currentPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_light;
    }

    @Override
    protected void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        lv_list = (ListView) findViewById(R.id.lv_list);
        iv_switchLeft = (ImageView) findViewById(R.id.iv_switchLeft);
        iv_switchRight = (ImageView) findViewById(R.id.iv_switchRight);
        tv_currentRoom = (TextView) findViewById(R.id.tv_currentRoom);
    }

    @SuppressLint("SetTextI18n")
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
        tag = getIntent().getStringExtra("tag");
        currentPosition = 0;

       /* ArrayList<Integer> curtain_s = new ArrayList<>();
        ArrayList<Integer> curtain_c = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            curtain_s.add(0);
            curtain_c.add(0);
        }
        curtainBean = new CurtainBean(curtain_s, curtain_c);*/
        curtainBean = new CurtainBean(new ArrayList<>(), new ArrayList<>());

        currentCurtainStatus = new ArrayList<>();
        currentCurtainControl = new ArrayList<>();

        // 默认将显示客厅A的等
        /*currentCurtainStatus.add(curtainBean.getCurtain_S().get(currentPosition));
        currentCurtainControl.add(curtainBean.getCurtain_C().get(currentPosition));*/

        // 初始化列表
        customerAdapter = new CustomerAdapter();
        lv_list.setAdapter(customerAdapter);

        // 拿到当前连接的Handler用于接受消息
        CustomerHandler customerHandler = getCustomerHandler(tag);
        if (customerHandler != null) {
            customerHandler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    App.showToast("窗帘断开连接");
                    L.e("窗帘断开连接");
                }

                @Override
                public void getResultData(String msg) {
                    try {
                        if (!TextUtils.isEmpty(msg)) {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.has("Curtain_S") || jsonObject.has("Curtain_C")) {
                                curtainBean = new Gson().fromJson(msg, CurtainBean.class);

                                // 获取到数据刷新列表
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentCurtainStatus.clear();
                                        currentCurtainControl.clear();
                                        for (int i : roomBeans.get(currentPosition).getCurtailId()) {
                                            if (i - 1 < curtainBean.getCurtain_S().size()) {
                                                L.e("i:" + i);
                                                // 现实中指定客厅的灯
                                                currentCurtainStatus.add(curtainBean.getCurtain_S().get(currentPosition));
                                                currentCurtainControl.add(curtainBean.getCurtain_C().get(currentPosition));
                                            }
                                        }
                                        customerAdapter.notifyDataSetChanged();
                                    }
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
     */
    @SuppressLint("SetTextI18n")
    private void switchPage(int type) {
        if (curtainBean == null) {
            return;
        }

        // 切换客厅
        switch (type) {
            case 0:
                if (currentPosition > 0) {
                    currentCurtainStatus.clear();
                    currentCurtainControl.clear();
                    currentPosition--;
                    int[] curtailId = roomBeans.get(currentPosition).getCurtailId();
                    for (int i : curtailId) {
                        L.e("" + i);
                        if (i - 1 < curtainBean.getCurtain_S().size()) {
                            currentCurtainStatus.add(curtainBean.getCurtain_S().get(i - 1));
                            currentCurtainControl.add(curtainBean.getCurtain_C().get(i - 1));
                        }
                    }
                }
                break;
            case 1:
                if (currentPosition < roomBeans.size() - 1) {
                    currentCurtainStatus.clear();
                    currentCurtainControl.clear();
                    currentPosition++;
                    int[] curtailId = roomBeans.get(currentPosition).getCurtailId();
                    for (int i : curtailId) {
                        L.e("" + i);
                        if (i - 1 < curtainBean.getCurtain_S().size()) {
                            currentCurtainStatus.add(curtainBean.getCurtain_S().get(i - 1));
                            currentCurtainControl.add(curtainBean.getCurtain_C().get(i - 1));
                        }
                    }
                }
                break;
        }
        tv_currentRoom.setText(roomBeans.get(currentPosition).getRoomName());
        customerAdapter.notifyDataSetChanged();
    }

    private class CustomerAdapter extends BaseAdapter {
        public static final int OPEN = 1;
        public static final int CLOSE = 0;
        private TextView tvLabel;
        private SwitchCompat swToggle;
        private CheckBox cbCheck;

        @Override
        public int getCount() {
            return currentCurtainStatus.size();
        }

        @Override
        public Integer getItem(int position) {
            return currentCurtainControl.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SetTextI18n")
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

            swToggle.setOnCheckedChangeListener(null);

            swToggle.setChecked(getItem(position) == 1);

            swToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ArrayList<Integer> light_c = new ArrayList<>(curtainBean.getCurtain_C());
                    light_c.set(curtailId[position] - 1, isChecked ? OPEN : CLOSE);
                    CurtainBean curtainBean = new CurtainBean(new ArrayList<>(), light_c);
                    // 发送控制窗帘的开关的请求
                    ValueUtil.sendCurtainOpenOrCloseCmd(curtainBean);
                    // 模拟
                    notifyDataSetChanged();
                }
            });
            return view;
        }

        private void initView(View view) {
            tvLabel = (TextView) view.findViewById(R.id.tv_label);
            cbCheck = (CheckBox) view.findViewById(R.id.cb_check);
            swToggle = (SwitchCompat) view.findViewById(R.id.sw_toggle);
        }
    }
}
