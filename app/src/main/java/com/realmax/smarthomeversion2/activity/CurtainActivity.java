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
import com.realmax.smarthomeversion2.bean.CurtainBean;
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
                    if (!TextUtils.isEmpty(msg)) {
                        curtainBean = new Gson().fromJson(msg, CurtainBean.class);

                        // 现实中指定客厅的灯
                        currentCurtainStatus.add(curtainBean.getCurtain_S().get(currentPosition));
                        currentCurtainControl.add(curtainBean.getCurtain_C().get(currentPosition));

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
                    currentCurtainStatus.add(curtainBean.getCurtain_S().get(currentPosition));
                    currentCurtainControl.add(curtainBean.getCurtain_C().get(currentPosition));
                    tv_currentRoom.setText("客厅" + ValueUtil.getRoom().charAt(currentPosition));
                    currentCurtainStatus.remove(0);
                    currentCurtainControl.remove(0);
                }
                break;
            case 1:
                if (currentPosition < curtainBean.getCurtain_S().size() - 1) {
                    currentPosition++;
                    currentCurtainStatus.add(curtainBean.getCurtain_S().get(currentPosition));
                    currentCurtainControl.add(curtainBean.getCurtain_C().get(currentPosition));
                    tv_currentRoom.setText("客厅" + ValueUtil.getRoom().charAt(currentPosition));
                    currentCurtainStatus.remove(0);
                    currentCurtainControl.remove(0);
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
            tvLabel.setText("客厅" + ValueUtil.getRoom().charAt(currentPosition) + (position + 1));

            // 设置电灯的状态
            ivLight.setImageResource(getItem(position) == 1 ? R.drawable.pic_curtain_open : R.drawable.pic_curtain_close);

            swToggle.setOnCheckedChangeListener(null);

            // 返回的数据跟将同步到灯和开关
            swToggle.setChecked(getItem(position) == 1);

            swToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // 修改对应位置的值
                    if (isChecked) {
                        curtainBean.getCurtain_S().set(currentPosition, OPEN);
                        curtainBean.getCurtain_C().set(currentPosition, OPEN);
                    } else {
                        curtainBean.getCurtain_S().set(currentPosition, CLOSE);
                        curtainBean.getCurtain_C().set(currentPosition, CLOSE);
                    }

                    // 发送控制窗帘开关的请求
                    ValueUtil.sendCurtainOpenOrCloseCmd(curtainBean);
                    // 模拟
                    /*notifyDataSetChanged();*/
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
