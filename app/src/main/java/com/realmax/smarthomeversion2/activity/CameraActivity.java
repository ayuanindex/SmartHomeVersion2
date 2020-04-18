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
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.bean.CameraBodyBean;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.util.EncodeAndDecode;
import com.realmax.smarthomeversion2.util.MoveCamera;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String tag;
    private int currentCamera = 0;
    private int currentPosition = 0;
    private LinearLayout llTip;
    private int delayMillis = 100;
    private RelativeLayout rlBack;

    @Override
    protected int getLayout() {
        return R.layout.activity_camera;
    }

    @Override
    protected void initView() {
        tvAngleA = (TextView) findViewById(R.id.tv_angleA);
        tvAngleB = (TextView) findViewById(R.id.tv_angleB);
        ivCamera = (ImageView) findViewById(R.id.iv_camera);
        ivBtnLeft = (ImageView) findViewById(R.id.iv_btn_left);
        ivBtnRight = (ImageView) findViewById(R.id.iv_btn_right);
        ivBtnUp = (ImageView) findViewById(R.id.iv_btn_up);
        ivBtnDown = (ImageView) findViewById(R.id.iv_btn_down);
        ivSwitchLeft = (ImageView) findViewById(R.id.iv_switchLeft);
        ivSwitchRight = (ImageView) findViewById(R.id.iv_switchRight);
        tvCurrentRoom = (TextView) findViewById(R.id.tv_currentRoom);
        llTip = (LinearLayout) findViewById(R.id.ll_tip);
        rlBack = (RelativeLayout) findViewById(R.id.rl_back);
    }

    @Override
    protected void initEvent() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivCamera.setOnTouchListener(new View.OnTouchListener() {

            private float moveY;
            private float moveX;
            private float initY;
            private float initX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        llTip.setVisibility(View.VISIBLE);
                        // 拿到手指放下是的坐标
                        initX = event.getX(0);
                        initY = event.getY(0);
                        MoveCamera.touchStart(currentCamera);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 获取移动是的坐标
                        moveX = event.getX(0);
                        moveY = event.getY(0);
                        MoveCamera.touchMoveCamera((int) (initX - moveX) / 10, (int) (initY - moveY) / 10, new MoveCamera.Result() {
                            @Override
                            public void resultAngle(float a, float b) {
                                tvAngleA.setText("A:" + a + "º");
                                tvAngleB.setText("B:" + b + "º");
                            }
                        });
                        initX = moveX;
                        initY = moveY;
                        break;
                    case MotionEvent.ACTION_UP:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                llTip.setVisibility(View.GONE);
                            }
                        }, delayMillis);
                        MoveCamera.stop();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        ivBtnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        llTip.setVisibility(View.VISIBLE);
                        MoveCamera.move(MoveCamera.LEFT, currentCamera, new MoveCamera.Result() {
                            @Override
                            public void resultAngle(float a, float b) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvAngleA.setText("A:" + a + "º");
                                        tvAngleB.setText("B:" + b + "º");
                                    }
                                });
                            }
                        });
                        break;
                    case MotionEvent.ACTION_UP:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                llTip.setVisibility(View.GONE);
                            }
                        }, delayMillis);
                        MoveCamera.stop();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        ivBtnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        llTip.setVisibility(View.VISIBLE);
                        MoveCamera.move(MoveCamera.RIGHT, currentCamera, new MoveCamera.Result() {
                            @Override
                            public void resultAngle(float a, float b) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvAngleA.setText("A:" + a + "º");
                                        tvAngleB.setText("B:" + b + "º");
                                    }
                                });
                            }
                        });
                        break;
                    case MotionEvent.ACTION_UP:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                llTip.setVisibility(View.GONE);
                            }
                        }, delayMillis);
                        MoveCamera.stop();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        ivBtnUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        llTip.setVisibility(View.VISIBLE);
                        MoveCamera.move(MoveCamera.UP, currentCamera, new MoveCamera.Result() {
                            @Override
                            public void resultAngle(float a, float b) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvAngleA.setText("A:" + a + "º");
                                        tvAngleB.setText("B:" + b + "º");
                                    }
                                });
                            }
                        });
                        break;
                    case MotionEvent.ACTION_UP:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                llTip.setVisibility(View.GONE);
                            }
                        }, delayMillis);
                        MoveCamera.stop();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        ivBtnDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        llTip.setVisibility(View.VISIBLE);
                        MoveCamera.move(MoveCamera.DOWN, currentCamera, new MoveCamera.Result() {
                            @Override
                            public void resultAngle(float a, float b) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvAngleA.setText("A:" + a + "º");
                                        tvAngleB.setText("B:" + b + "º");
                                    }
                                });
                            }
                        });
                        break;
                    case MotionEvent.ACTION_UP:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                llTip.setVisibility(View.GONE);
                            }
                        }, delayMillis);
                        MoveCamera.stop();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        ivSwitchLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPage(0);
            }
        });

        ivSwitchRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPage(1);
            }
        });
    }

    @Override
    protected void initData() {
        tag = getIntent().getStringExtra("tag");

        if (roomBeans.get(currentPosition).getCameraId().length > 0) {
            currentCamera = roomBeans.get(currentPosition).getCameraId()[0];
            ValueUtil.sendCameraCmd(currentCamera, 0, 45);
        }

        // 获取Netty的Hander
        CustomerHandlerBase customerHandler = getCustomerHandler(tag);
        if (customerHandler != null) {
            customerHandler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    ValueUtil.getIsConnected().put(tag, false);
                    ValueUtil.getHandlerHashMap().put(tag, null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            App.showToast("摄像头断开连接");
                        }
                    });
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
                    if (roomBeans.get(currentPosition).getCameraId().length > 0) {
                        currentCamera = roomBeans.get(currentPosition).getCameraId()[0];
                        ValueUtil.sendCameraCmd(currentCamera, 0, 45);
                    }
                }
                break;
            case 1:
                if (currentPosition < roomBeans.size() - 1) {
                    currentPosition++;
                    if (roomBeans.get(currentPosition).getCameraId().length > 0) {
                        currentCamera = roomBeans.get(currentPosition).getCameraId()[0];
                        ValueUtil.sendCameraCmd(currentCamera, 0, 45);
                    }
                }
                break;
            /*default:
                break;*/
        }
        tvCurrentRoom.setText(roomBeans.get(currentPosition).getRoomName());
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
                if ("play".equals(cmd)) {
                    setImage(msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            String substring = msg.substring(1);
            checkJson(msg);
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivCamera.setImageBitmap(bitmap);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ValueUtil.sendStopCmd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
