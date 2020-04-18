package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.google.gson.Gson;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.bean.DoorBean;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import java.util.ArrayList;

public class DoorActivity extends BaseActivity {
    private RelativeLayout rl_back;
    private TextView tv_doorLabel;
    private SwitchCompat sw_doorToggle;
    private CheckBox cb_doorIcon;
    private TextView tv_doorLock;
    private SwitchCompat sw_lockToggle;
    private CheckBox cb_keyIcon;
    private TextView tv_doorPassword;
    private ImageView iv_passwordIcon;
    private TextView tv_Password;
    private ImageView iv_switchLeft;
    private ImageView iv_switchRight;
    private TextView tv_currentRoom;
    private String tag;
    private LinearLayout ll_door;
    private LinearLayout ll_key;
    private LinearLayout ll_password;
    private ArrayList<String> doorNameList;
    private int currentPosition;
    private String currentDoor;
    private DoorBean doorBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_door;
    }

    @Override
    protected void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        // 电动门
        tv_doorLabel = (TextView) findViewById(R.id.tv_doorLabel);
        sw_doorToggle = (SwitchCompat) findViewById(R.id.sw_doorToggle);
        cb_doorIcon = (CheckBox) findViewById(R.id.cb_doorIcon);
        // 门锁
        tv_doorLock = (TextView) findViewById(R.id.tv_doorLock);
        sw_lockToggle = (SwitchCompat) findViewById(R.id.sw_lockToggle);
        cb_keyIcon = (CheckBox) findViewById(R.id.cb_keyIcon);
        // 密码
        tv_doorPassword = (TextView) findViewById(R.id.tv_doorPassword);
        iv_passwordIcon = (ImageView) findViewById(R.id.iv_passwordIcon);
        tv_Password = (TextView) findViewById(R.id.tv_Password);
        // 切换房间
        iv_switchLeft = (ImageView) findViewById(R.id.iv_switchLeft);
        iv_switchRight = (ImageView) findViewById(R.id.iv_switchRight);
        tv_currentRoom = (TextView) findViewById(R.id.tv_currentRoom);
        // 三个条目
        ll_door = (LinearLayout) findViewById(R.id.ll_door);
        ll_key = (LinearLayout) findViewById(R.id.ll_key);
        ll_password = (LinearLayout) findViewById(R.id.ll_password);

        selectShowHide();
    }

    @Override
    protected void initEvent() {
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sw_doorToggle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
                    // TODO: 4/17/20 请求门的开关
                    sendCmd();
                }
                return true;
            }
        });

        sw_lockToggle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
                    // TODO: 4/17/20 请求锁的开关
                    sendCmd();
                }
                return true;
            }
        });

        iv_passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 4/17/20 请求输入密码
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

    private void sendCmd() {
        if (doorBean == null) {
            return;
        }

        L.e("hhahh");

        String field = "";
        int door = -1;
        int lock = -1;
        int pass = -1;

        switch (currentPosition) {
            case 0:
                // 客厅大门--->电动门
                DoorBean.Door1SBean door1_s = doorBean.getDoor1_S();
                field = "door1_C";
                door = door1_s.getDoor_s() == 1 ? 0 : 1;
                break;
            case 1:
                // 庭院后门--->锁+密码
                DoorBean.Door2SBean door2_s = doorBean.getDoor2_S();
                field = "door2_C";
                lock = door2_s.getLock_s() == 1 ? 0 : 1;
                pass = door2_s.getPass();
                break;
            case 2:
                // 院墙小门--->锁+密码
                DoorBean.Door3SBean door3_s = doorBean.getDoor3_S();
                field = "door3_C";
                lock = door3_s.getLock_s() == 1 ? 0 : 1;
                pass = door3_s.getPass();
                break;
            case 3:
                // 院墙大门--->锁+密码
                DoorBean.Door4SBean door4_s = doorBean.getDoor4_S();
                field = "door4_C";
                lock = door4_s.getLock_s() == 1 ? 0 : 1;
                pass = door4_s.getPass();
                break;
            case 4:
                // 车库门--->电动门+锁
                DoorBean.Door5SBean door5_s = doorBean.getDoor5_S();
                field = "door5_C";
                door = door5_s.getDoor_s() == 1 ? 0 : 1;
                lock = door5_s.getLock_s() == 1 ? 0 : 1;
                break;
            default:
                break;
        }
        ValueUtil.sendDoorCmd(field, door, lock, pass);
    }

    @Override
    protected void initData() {
        tag = getIntent().getStringExtra("tag");

        doorNameList = new ArrayList<>();
        doorNameList.add("客厅大门");
        doorNameList.add("庭院后门");
        doorNameList.add("院墙小门");
        doorNameList.add("院墙大门");
        doorNameList.add("车库门");
        CustomerHandlerBase customerHandlerBase = ValueUtil.getHandlerHashMap().get(tag);
        if (customerHandlerBase != null) {
            customerHandlerBase.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    ValueUtil.getIsConnected().put("door", false);
                    L.e("网络连接已断开");
                }

                @Override
                public void getResultData(String msg) {
                    doorBean = new Gson().fromJson(msg, DoorBean.class);
                    L.e("toString:" + doorBean.toString());
                }
            });
        }
    }

    /**
     * 切换门
     */
    @SuppressLint("SetTextI18n")
    private void switchPage(int type) {
        switch (type) {
            case 0:
                if (currentPosition > 0) {
                    currentPosition--;
                    if (doorNameList.size() > 0) {
                        currentDoor = doorNameList.get(currentPosition);
                    }
                }
                break;
            case 1:
                if (currentPosition < doorNameList.size() - 1) {
                    currentPosition++;
                    if (doorNameList.size() > 0) {
                        currentDoor = doorNameList.get(currentPosition);
                    }
                }
                break;
            default:
                break;
        }
        selectShowHide();
        tv_currentRoom.setText(doorNameList.get(currentPosition));
    }

    public void selectShowHide() {
        switch (currentPosition) {
            case 0:
                isVisible(true, ll_door);
                isVisible(false, ll_password, ll_key);
                break;
            case 1:
            case 2:
                isVisible(true, ll_key, ll_password);
                isVisible(false, ll_door);
                break;
            case 3:
                isVisible(true, ll_door, ll_password);
                isVisible(false, ll_key);
                break;
            case 4:
                isVisible(true, ll_door, ll_key);
                isVisible(false, ll_password);
                break;
            default:
                break;
        }
    }

    /**
     * 显示或隐藏控件
     *
     * @param isVisible 显示或隐藏的标示符true表示显示、false表示隐藏
     * @param views     需要显示或隐藏的控件
     */
    private void isVisible(boolean isVisible, View... views) {
        for (View view : views) {
            if (isVisible) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }
}
