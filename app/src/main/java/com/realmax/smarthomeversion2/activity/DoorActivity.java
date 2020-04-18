package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.google.gson.Gson;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.bean.DoorBean;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import java.util.ArrayList;

/**
 * @author ayuan
 */
public class DoorActivity extends BaseActivity {
    private RelativeLayout rlBack;
    private SwitchCompat swDoorToggle;
    private SwitchCompat swLockToggle;
    private ImageView ivPasswordIcon;
    private ImageView ivSwitchLeft;
    private ImageView ivSwitchRight;
    private TextView tvCurrentRoom;
    private LinearLayout llDoor;
    private LinearLayout llKey;
    private LinearLayout llPassword;
    private ArrayList<String> doorNameList;
    private int currentPosition;
    private String currentDoor;
    private DoorBean doorBean;

    @Override
    protected int getLayout() {
        return R.layout.activity_door;
    }

    @Override
    protected void initView() {
        rlBack = findViewById(R.id.rl_back);
        // 电动门
        swDoorToggle = findViewById(R.id.sw_doorToggle);
        // 门锁
        swLockToggle = findViewById(R.id.sw_lockToggle);
        // 密码
        ivPasswordIcon = findViewById(R.id.iv_passwordIcon);
        // 切换房间
        ivSwitchLeft = findViewById(R.id.iv_switchLeft);
        ivSwitchRight = findViewById(R.id.iv_switchRight);
        tvCurrentRoom = findViewById(R.id.tv_currentRoom);
        // 三个条目
        llDoor = findViewById(R.id.ll_door);
        llKey = findViewById(R.id.ll_key);
        llPassword = findViewById(R.id.ll_password);

        selectShowHide();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initEvent() {
        rlBack.setOnClickListener((View v) -> finish());

        swDoorToggle.setOnTouchListener((View v, MotionEvent event) -> {
            if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
                DoorActivity.this.sendCmd();
            }
            return true;
        });

        swLockToggle.setOnTouchListener((View v, MotionEvent event) -> {
            if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
                sendCmd();
            }
            return true;
        });

        ivPasswordIcon.setOnClickListener((View v) -> putPassword());

        ivSwitchLeft.setOnClickListener((View v) -> switchPage(0));

        ivSwitchRight.setOnClickListener((View v) -> switchPage(1));
    }

    /**
     * 执行输入密码的操作
     */
    private void putPassword() {

    }

    /**
     * 发送指定门的指令
     */
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
                DoorBean.Door1SBean door1S = doorBean.getDoor1_S();
                field = "door1_C";
                door = door1S.getDoor_s() == 1 ? 0 : 1;
                break;
            case 1:
                // 庭院后门--->锁+密码
                DoorBean.Door2SBean door2S = doorBean.getDoor2_S();
                field = "door2_C";
                lock = door2S.getLock_s() == 1 ? 0 : 1;
                pass = door2S.getPass();
                break;
            case 2:
                // 院墙小门--->锁+密码
                DoorBean.Door3SBean door3S = doorBean.getDoor3_S();
                field = "door3_C";
                lock = door3S.getLock_s() == 1 ? 0 : 1;
                pass = door3S.getPass();
                break;
            case 3:
                // 院墙大门--->锁+密码
                DoorBean.Door4SBean door4S = doorBean.getDoor4_S();
                field = "door4_C";
                lock = door4S.getLock_s() == 1 ? 0 : 1;
                pass = door4S.getPass();
                break;
            case 4:
                // 车库门--->电动门+锁
                DoorBean.Door5SBean door5S = doorBean.getDoor5_S();
                field = "door5_C";
                door = door5S.getDoor_s() == 1 ? 0 : 1;
                lock = door5S.getLock_s() == 1 ? 0 : 1;
                break;
            default:
                break;
        }
        ValueUtil.sendDoorCmd(field, door, lock, pass);
    }

    @Override
    protected void initData() {
        String tag = getIntent().getStringExtra("tag");

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
        tvCurrentRoom.setText(currentDoor);
    }

    /**
     * 选择控件进行显示或者隐藏
     */
    public void selectShowHide() {
        switch (currentPosition) {
            case 0:
                isVisible(true, llDoor);
                isVisible(false, llPassword, llKey);
                break;
            case 1:
            case 2:
                isVisible(true, llKey, llPassword);
                isVisible(false, llDoor);
                break;
            case 3:
                isVisible(true, llDoor, llPassword);
                isVisible(false, llKey);
                break;
            case 4:
                isVisible(true, llDoor, llKey);
                isVisible(false, llPassword);
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
