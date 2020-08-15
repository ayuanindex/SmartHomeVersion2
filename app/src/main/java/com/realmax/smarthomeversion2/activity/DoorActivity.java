package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.realmax.smarthomeversion2.App;
import com.realmax.smarthomeversion2.Constant;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.activity.bean.DoorAndAirQualityBean;
import com.realmax.smarthomeversion2.activity.bean.RoomBean;
import com.realmax.smarthomeversion2.bean.LinkBean;
import com.realmax.smarthomeversion2.tcp.BaseNettyHandler;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandler;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author ayuan
 * 门
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
    public static ArrayList<String> doorNameList;
    private int currentPosition = 0;
    private String currentDoor;
    private String tag = "control_03";
    private DoorAndAirQualityBean doorAndAirQualityBean;
    private DoorAndAirQualityBean.DoorsSBean doorsSBean;
    public static ArrayList<RoomBean> roomBeans;

    static {
        roomBeans = new ArrayList<>();
        roomBeans.add(new RoomBean("客厅门", new int[]{1}));
        roomBeans.add(new RoomBean("后门", new int[]{2}));
        roomBeans.add(new RoomBean("小门", new int[]{3}));
        roomBeans.add(new RoomBean("大门", new int[]{4}));
        roomBeans.add(new RoomBean("车库门", new int[]{5}));

        doorNameList = new ArrayList<>();
        doorNameList.add("客厅门");
        doorNameList.add("后门");
        doorNameList.add("小门");
        doorNameList.add("大门");
        doorNameList.add("车库门");
    }

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

        isVisible(false, llDoor, llKey, llPassword);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initEvent() {
        rlBack.setOnClickListener((View v) -> finish());

        swDoorToggle.setOnTouchListener((View v, MotionEvent event) -> {
            if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
                /*sendCmd();*/
                if (doorAndAirQualityBean == null) {
                    return true;
                }
                int door = -1;

                if (doorsSBean != null) {
                    door = doorsSBean.getDoorSwitch() == -1 ? door : (doorsSBean.getDoorSwitch() == 1 ? 0 : 1);
                    ValueUtil.sendDoorCmd(door, doorsSBean.getDoorLock(), doorsSBean.getSetPassword(), tag);
                }
            }
            return true;
        });

        swLockToggle.setOnTouchListener((View v, MotionEvent event) -> {
            if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
                /*sendCmd();*/
                if (doorAndAirQualityBean == null) {
                    return true;
                }
                int lock = -1;

                if (doorsSBean != null) {
                    lock = doorsSBean.getDoorLock() == -1 ? lock : (doorsSBean.getDoorLock() == 1 ? 0 : 1);
                    ValueUtil.sendDoorCmd(doorsSBean.getDoorSwitch(), lock, doorsSBean.getSetPassword(), tag);
                }
            }
            return true;
        });

        ivPasswordIcon.setOnClickListener((View v) -> putPassword());

        ivSwitchLeft.setOnClickListener((View v) -> switchPage(0));

        ivSwitchRight.setOnClickListener((View v) -> switchPage(1));
    }

    @Override
    protected void initData() {

        doorAndAirQualityBean = new DoorAndAirQualityBean();
        doorAndAirQualityBean.setDoors_S(new ArrayList<>());

        LinkBean customerHandlerBase = Constant.getLinkBeanByTag(tag);
        if (customerHandlerBase != null) {
            customerHandlerBase.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    uiHandler.post(() -> App.showToast("门的连接断开"));
                    L.e("网络连接已断开");
                }

                @Override
                public void getResultData(String msg) {
                    try {
                        if (!TextUtils.isEmpty(msg)) {
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.has("doors_S")) {
                                doorAndAirQualityBean = new Gson().fromJson(msg, DoorAndAirQualityBean.class);
                                L.e("toString:" + doorAndAirQualityBean.toString());
                                selectShowHide();
                            }
                        }
                    } catch (JsonSyntaxException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 选择控件进行显示或者隐藏
     */
    public void selectShowHide() {
        uiHandler.post(() -> {
            if (doorAndAirQualityBean.getDoors_S() != null) {
                switch (currentPosition) {
                    case 0:
                        doorsSBean = doorAndAirQualityBean.getDoors_S().get(currentPosition);
                        if (doorsSBean != null) {
                            swDoorToggle.setChecked(doorsSBean.getDoorSwitch() == 1);
                            swLockToggle.setChecked(doorsSBean.getDoorLock() == 1);
                            isVisible(true, llDoor, llKey, llPassword);
                        }
                        break;
                    case 1:
                        doorsSBean = doorAndAirQualityBean.getDoors_S().get(currentPosition);
                        if (doorsSBean != null) {
                            swLockToggle.setChecked(doorsSBean.getDoorLock() == 1);
                            isVisible(true, llPassword, llKey);
                            isVisible(false, llDoor);
                        }
                        break;
                    case 2:
                        doorsSBean = doorAndAirQualityBean.getDoors_S().get(currentPosition);
                        if (doorsSBean != null) {
                            swLockToggle.setChecked(doorsSBean.getDoorLock() == 1);
                            isVisible(true, llPassword, llKey);
                            isVisible(false, llDoor);
                        }
                        break;
                    case 3:
                        doorsSBean = doorAndAirQualityBean.getDoors_S().get(currentPosition);
                        if (doorsSBean != null) {
                            swDoorToggle.setChecked(doorsSBean.getDoorSwitch() == 1);
                            isVisible(true, llDoor);
                            isVisible(false, llPassword, llKey);
                        }
                        break;
                    case 4:
                        doorsSBean = doorAndAirQualityBean.getDoors_S().get(currentPosition);
                        if (doorsSBean != null) {
                            swDoorToggle.setChecked(doorsSBean.getDoorSwitch() == 1);
                            swLockToggle.setChecked(doorsSBean.getDoorLock() == 1);
                            isVisible(true, llDoor, llKey);
                            isVisible(false, llPassword);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 发送指定门的指令
     */
    private void sendCmd() {
        if (doorAndAirQualityBean == null) {
            return;
        }
        int door = -1;
        int lock = -1;
        int pass = -1;

        if (doorsSBean != null) {
            door = doorsSBean.getDoorSwitch() == -1 ? door : (doorsSBean.getDoorSwitch() == 1 ? 0 : 1);
            lock = doorsSBean.getDoorLock() == -1 ? lock : (doorsSBean.getDoorLock() == 1 ? 0 : 1);
            pass = doorsSBean.getSetPassword() == -1 ? pass : doorsSBean.getSetPassword();
            ValueUtil.sendDoorCmd(door, lock, pass, tag);
        }
    }

    /**
     * 执行输入密码的操作
     */
    private void putPassword() {
        // 显示弹窗输入密码
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.create();
        View inflate = View.inflate(this, R.layout.dialog_put_password, null);
        alertDialog.setView(inflate);
        ViewHolder holder = new ViewHolder(inflate);

        holder.cardUnlock.setOnClickListener((View v) -> {
            String checkPasswordStr = holder.etCheckPassword.getText().toString().trim();
            if (TextUtils.isEmpty(checkPasswordStr)) {
                App.showToast("请输入旧密码");
                return;
            }

            String passwordStr = holder.etPassword.getText().toString().trim();
            if (TextUtils.isEmpty(passwordStr)) {
                App.showToast("请输入密码");
                return;
            } else if (passwordStr.length() != 4) {
                App.showToast("密码长度不足，请重试");
                return;
            }
            // 执行发送密码的操作;
            // 1234
            int checkPassword = Integer.parseInt(checkPasswordStr);
            if (checkPassword == doorsSBean.getSetPassword()) {
                doorsSBean.setSetPassword(Integer.parseInt(passwordStr));
                App.showToast("新密码设置成功");
            } else {
                App.showToast("旧密码验证错误");
                return;
            }
            // 盗用指令发送的方法针对某个门发送指令x
            sendCmd();
            uiHandler.postDelayed(alertDialog::dismiss, 500);
        });

        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        alertDialog.show();
    }

    /**
     * 输入密码弹窗的ViewHolder
     */
    public static class ViewHolder {
        View rootView;

        EditText etPassword;
        EditText etCheckPassword;
        CardView cardUnlock;

        ViewHolder(View rootView) {
            this.rootView = rootView;
            this.etPassword = (EditText) rootView.findViewById(R.id.et_password);
            this.etCheckPassword = (EditText) rootView.findViewById(R.id.et_checkPassword);
            this.cardUnlock = (CardView) rootView.findViewById(R.id.cardUnlock);
        }

    }


    /**
     * 切换门
     */
    @SuppressLint("SetTextI18n")
    private void switchPage(int type) {
        if (doorAndAirQualityBean == null) {
            return;
        }

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
