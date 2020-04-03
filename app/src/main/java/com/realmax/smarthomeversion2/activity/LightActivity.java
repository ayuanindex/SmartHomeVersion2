package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.realmax.smarthomeversion2.bean.LightBean;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandler;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import java.util.ArrayList;

/**
 * @ProjectName: SmartHomeVersion2
 * @Package: com.realmax.smarthomeversion2.activity
 * @ClassName: LightActivityy
 * @CreateDate: 2020/4/3 12:07
 */
public class LightActivity extends BaseActivity {
    private RelativeLayout rl_back;
    private ListView lv_list;
    private ImageView iv_switchLeft;
    private ImageView iv_switchRight;
    private TextView tv_currentRoom;
    private LightBean lightBean;
    private CustomerAdapter customerAdapter;
    private String tag;
    private ArrayList<Integer> currentLightStatus;
    private ArrayList<Integer> currentLightControl;
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

       /* ArrayList<Integer> light_c = new ArrayList<>();
        ArrayList<Integer> ligth_s = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            ligth_s.add(0);
            light_c.add(0);
        }*/
        lightBean = new LightBean(new ArrayList<>(), new ArrayList<>());

        currentLightStatus = new ArrayList<>();
        currentLightControl = new ArrayList<>();

        // 默认将显示客厅A的等
        /*currentLightStatus.add(lightBean.getLigth_S().get(currentPosition));
        currentLightControl.add(lightBean.getLight_C().get(currentPosition));*/

        // 初始化列表
        customerAdapter = new CustomerAdapter();
        lv_list.setAdapter(customerAdapter);

        // 拿到当前连接的Handler用于接受消息
        CustomerHandler customerHandler = getCustomerHandler(tag);
        if (customerHandler != null) {
            customerHandler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    App.showToast("小灯断开连接");
                    L.e("小灯断开连接");
                }

                @Override
                public void getResultData(String msg) {
                    if (!TextUtils.isEmpty(msg)) {
                        lightBean = new Gson().fromJson(msg, LightBean.class);

                        // 现实中指定客厅的灯
                        currentLightStatus.add(lightBean.getLigth_S().get(currentPosition));
                        currentLightControl.add(lightBean.getLight_C().get(currentPosition));

                        // 获取到数据刷新列表
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customerAdapter.notifyDataSetChanged();
                            }
                        });
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
        // 切换客厅
        switch (type) {
            case 0:
                if (currentPosition > 0) {
                    currentPosition--;
                    currentLightStatus.add(lightBean.getLigth_S().get(currentPosition));
                    currentLightControl.add(lightBean.getLight_C().get(currentPosition));
                    tv_currentRoom.setText("客厅" + ValueUtil.getRoom().charAt(currentPosition));
                    currentLightStatus.remove(0);
                    currentLightControl.remove(0);
                }
                break;
            case 1:
                if (currentPosition < lightBean.getLigth_S().size() - 1) {
                    currentPosition++;
                    currentLightStatus.add(lightBean.getLigth_S().get(currentPosition));
                    currentLightControl.add(lightBean.getLight_C().get(currentPosition));
                    tv_currentRoom.setText("客厅" + ValueUtil.getRoom().charAt(currentPosition));
                    currentLightStatus.remove(0);
                    currentLightControl.remove(0);
                }
                break;
        }
        customerAdapter.notifyDataSetChanged();
    }

    private class CustomerAdapter extends BaseAdapter {
        public static final int OPEN = 1;
        public static final int CLOSE = 0;
        private TextView tvLabel;
        private SwitchCompat swToggle;
        private ImageView ivLight;

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

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(LightActivity.this, R.layout.item_light, null);
            } else {
                view = convertView;
            }
            initView(view);
            tvLabel.setText("客厅" + ValueUtil.getRoom().charAt(currentPosition) + (position + 1));

            // 设置电灯的状态
            ivLight.setImageResource(getItem(position) == 1 ? R.drawable.pic_light_open : R.drawable.pic_light_close);

            swToggle.setOnCheckedChangeListener(null);

            // 返回的数据跟将同步到灯和开关
            swToggle.setChecked(getItem(position) == 1);

            swToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // 修改对应位置的值
                    if (isChecked) {
                        lightBean.getLigth_S().set(currentPosition, OPEN);
                        lightBean.getLight_C().set(currentPosition, OPEN);
                    } else {
                        lightBean.getLigth_S().set(currentPosition, CLOSE);
                        lightBean.getLight_C().set(currentPosition, CLOSE);
                    }

                    // 发送控制电灯开6关的请求
                    ValueUtil.sendLightOpenOrCloseCmd(lightBean);
                    // 模拟
                    notifyDataSetChanged();
                }
            });
            return view;
        }

        private void initView(View view) {
            tvLabel = (TextView) view.findViewById(R.id.tv_label);
            swToggle = (SwitchCompat) view.findViewById(R.id.sw_toggle);
            ivLight = (ImageView) view.findViewById(R.id.iv_light);
        }
    }
}
