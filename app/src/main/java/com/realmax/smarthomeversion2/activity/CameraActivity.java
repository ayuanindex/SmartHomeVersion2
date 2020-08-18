package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.realmax.smarthomeversion2.App;
import com.realmax.smarthomeversion2.Constant;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.activity.bean.RoomBean;
import com.realmax.smarthomeversion2.bean.CameraBodyBean;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandler;
import com.realmax.smarthomeversion2.util.EncodeAndDecode;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.MoveCamera;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author ayuan
 * 摄像头
 */
public class CameraActivity extends BaseActivity {
    private TextView tvAngleA;
    private TextView tvAngleB;
    private ImageView ivCamera;
    private ImageView ivBtnLeft;
    private ImageView ivBtnRight;
    private ImageView ivBtnUp;
    private ImageView ivBtnDown;
    private ImageView ivSwitchLeft;
    private ImageView ivSwitchRight;
    private TextView tvCurrentRoom;
    private String tag = "virtual";
    private int currentCamera = 0;
    private int currentPosition = 0;
    private LinearLayout llTip;
    private int delayMillis = 100;
    private RelativeLayout rlBack;

    /**
     * 手指放在照片上时的Y坐标
     */
    private float initY;

    /**
     * 手指放在照片上时的X坐标
     */
    private float initX;
    public static ArrayList<RoomBean> roomBeans;

    static {
        roomBeans = new ArrayList<>();
        roomBeans.add(new RoomBean("客厅", new int[]{1}));
        roomBeans.add(new RoomBean("餐厅", new int[]{2}));
        roomBeans.add(new RoomBean("门厅", new int[]{3}));
        roomBeans.add(new RoomBean("车库", new int[]{4}));
        roomBeans.add(new RoomBean("走廊", new int[]{5}));
        roomBeans.add(new RoomBean("卧室A", new int[]{6}));
        roomBeans.add(new RoomBean("卧室B", new int[]{7}));
        roomBeans.add(new RoomBean("卧室C", new int[]{8}));
        roomBeans.add(new RoomBean("书房", new int[]{9}));
        roomBeans.add(new RoomBean("庭院西", new int[]{10}));
        roomBeans.add(new RoomBean("院墙", new int[]{11}));
        roomBeans.add(new RoomBean("院墙", new int[]{12}));
        roomBeans.add(new RoomBean("大门", new int[]{13}));
        roomBeans.add(new RoomBean("机器人", new int[]{14}));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_camera;
    }

    @Override
    protected void initView() {
        tvAngleA = findViewById(R.id.tv_angleA);
        tvAngleB = findViewById(R.id.tv_angleB);
        ivCamera = findViewById(R.id.iv_camera);
        ivBtnLeft = findViewById(R.id.iv_btn_left);
        ivBtnRight = findViewById(R.id.iv_btn_right);
        ivBtnUp = findViewById(R.id.iv_btn_up);
        ivBtnDown = findViewById(R.id.iv_btn_down);
        ivSwitchLeft = findViewById(R.id.iv_switchLeft);
        ivSwitchRight = findViewById(R.id.iv_switchRight);
        tvCurrentRoom = findViewById(R.id.tv_currentRoom);
        llTip = findViewById(R.id.ll_tip);
        rlBack = findViewById(R.id.rl_back);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initEvent() {
        rlBack.setOnClickListener((View v) -> finish());

        ivCamera.setOnTouchListener((View v, MotionEvent event) -> {
            touchMoveCamera(event);
            return true;
        });

        ivBtnLeft.setOnTouchListener((View v, MotionEvent event) -> {
            changeCameraDirection(event, MoveCamera.LEFT);
            return true;
        });

        ivBtnRight.setOnTouchListener((View v, MotionEvent event) -> {
            changeCameraDirection(event, MoveCamera.RIGHT);
            return true;
        });

        ivBtnUp.setOnTouchListener((View v, MotionEvent event) -> {
            changeCameraDirection(event, MoveCamera.UP);
            return true;
        });

        ivBtnDown.setOnTouchListener((View v, MotionEvent event) -> {
            changeCameraDirection(event, MoveCamera.DOWN);
            return true;
        });

        ivSwitchLeft.setOnClickListener((View v) -> switchPage(0));

        ivSwitchRight.setOnClickListener((View v) -> switchPage(1));
    }

    @Override
    protected void initData() {

        if (roomBeans.get(currentPosition).getModel().length > 0) {
            currentCamera = roomBeans.get(currentPosition).getModel()[0];
            ValueUtil.sendCameraCmd(currentCamera, 0, 45, tag);
        }

        // 获取Netty的Hander
        CustomerHandler customerHandler = (CustomerHandler) Constant.getLinkBeanByTag(tag).getBaseNettyHandler();
        if (customerHandler != null) {
            customerHandler.setCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    runOnUiThread(() -> App.showToast("摄像头断开连接"));
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void getResultData(String msg) {
                    checkJson(msg);
                }
            });
        }
    }

    /**
     * 切换摄像头
     */
    @SuppressLint("SetTextI18n")
    private void switchPage(int type) {
        switch (type) {
            case 0:
                if (currentPosition > 0) {
                    currentPosition--;
                    if (roomBeans.get(currentPosition).getModel().length > 0) {
                        currentCamera = roomBeans.get(currentPosition).getModel()[0];
                        ValueUtil.sendCameraCmd(currentCamera, 0, 45, tag);
                    }
                }
                break;
            case 1:
                if (currentPosition < roomBeans.size() - 1) {
                    currentPosition++;
                    if (roomBeans.get(currentPosition).getModel().length > 0) {
                        currentCamera = roomBeans.get(currentPosition).getModel()[0];
                        ValueUtil.sendCameraCmd(currentCamera, 0, 45, tag);
                    }
                }
                break;
            default:
                break;
        }

        String roomName = roomBeans.get(currentPosition).getRoomName();
        if (roomName.equals("机器人")) {
            ivBtnUp.setVisibility(View.GONE);
            ivBtnDown.setVisibility(View.GONE);
        } else {
            ivBtnUp.setVisibility(View.VISIBLE);
            ivBtnDown.setVisibility(View.VISIBLE);
        }
        tvCurrentRoom.setText(roomName);
    }

    /**
     * 检查是否是图片数据
     *
     * @param msg json字符串
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkJson(String msg) {
        try {
            if (!TextUtils.isEmpty(msg)) {
                JSONObject jsonObject = new JSONObject(msg);
                String cmd = jsonObject.optString("cmd");
                String play = "play";
                if (play.equals(cmd)) {
                    setImage(msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            String substring = msg.substring(1);
            L.e("Json出现异常：" + substring);
            checkJson(substring);
        }
    }

    /**
     * 解析图片并设置
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setImage(String msg) {
        CameraBodyBean cameraBodyBean = new Gson().fromJson(msg, CameraBodyBean.class);
        if (cameraBodyBean != null && !TextUtils.isEmpty(cameraBodyBean.getCameraImg())) {
            Bitmap bitmap = EncodeAndDecode.base64ToImage(cameraBodyBean.getCameraImg());
            runOnUiThread(() -> ivCamera.setImageBitmap(bitmap));
        }
    }

    @SuppressLint("SetTextI18n")
    private void touchMoveCamera(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                llTip.setVisibility(View.VISIBLE);
                // 拿到手指放下是的坐标
                initX = event.getX(0);
                initY = event.getY(0);
                MoveCamera.touchStart(currentCamera,tag);
                break;
            case MotionEvent.ACTION_MOVE:
                // 获取移动是的坐标
                float moveX = event.getX(0);
                float moveY = event.getY(0);
                MoveCamera.touchMoveCamera((int) (initX - moveX) / 10, (int) (initY - moveY) / 10, (a, b) -> {
                    tvAngleA.setText("A:" + a + "º");
                    tvAngleB.setText("B:" + b + "º");
                });
                initX = moveX;
                initY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                new Handler().postDelayed(() -> llTip.setVisibility(View.GONE), delayMillis);
                MoveCamera.stop();
                break;
            default:
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void changeCameraDirection(MotionEvent event, int direction) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                llTip.setVisibility(View.VISIBLE);
                MoveCamera.move(direction, currentCamera, tag, (a, b) -> runOnUiThread(() -> {
                    tvAngleA.setText("A:" + a + "º");
                    tvAngleB.setText("B:" + b + "º");
                }));
                break;
            case MotionEvent.ACTION_UP:
                new Handler().postDelayed(() -> llTip.setVisibility(View.GONE), delayMillis);
                MoveCamera.stop();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ValueUtil.sendStopCmd("camera");
    }
}
