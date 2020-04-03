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
import com.realmax.smarthomeversion2.tcp.CustomerHandler;
import com.realmax.smarthomeversion2.util.EncodeAndDecode;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.MoveCamera;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CameraActivity extends BaseActivity {
    private TextView tv_angleA;
    private TextView tv_angleB;
    private ImageView iv_camera;
    private ImageView iv_btn_left;
    private ImageView iv_btn_right;
    private ImageView iv_btn_up;
    private ImageView iv_btn_down;
    private ImageView iv_switchLeft;
    private ImageView iv_switchRight;
    private TextView tv_currentRoom;
    private String tag;
    private ArrayList<Integer> cameraId;
    private int currentCamera = 0;
    private LinearLayout ll_tip;
    private int delayMillis = 100;
    private RelativeLayout rl_back;

    @Override
    protected int getLayout() {
        return R.layout.activity_camera;
    }

    @Override
    protected void initView() {
        tv_angleA = (TextView) findViewById(R.id.tv_angleA);
        tv_angleB = (TextView) findViewById(R.id.tv_angleB);
        iv_camera = (ImageView) findViewById(R.id.iv_camera);
        iv_btn_left = (ImageView) findViewById(R.id.iv_btn_left);
        iv_btn_right = (ImageView) findViewById(R.id.iv_btn_right);
        iv_btn_up = (ImageView) findViewById(R.id.iv_btn_up);
        iv_btn_down = (ImageView) findViewById(R.id.iv_btn_down);
        iv_switchLeft = (ImageView) findViewById(R.id.iv_switchLeft);
        iv_switchRight = (ImageView) findViewById(R.id.iv_switchRight);
        tv_currentRoom = (TextView) findViewById(R.id.tv_currentRoom);
        ll_tip = (LinearLayout) findViewById(R.id.ll_tip);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
    }

    @Override
    protected void initEvent() {
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_camera.setOnTouchListener(new View.OnTouchListener() {

            private float moveY;
            private float moveX;
            private float initY;
            private float initX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        ll_tip.setVisibility(View.VISIBLE);
                        // 拿到手指放下是的坐标
                        initX = event.getX(0);
                        initY = event.getY(0);
                        MoveCamera.touchStart(cameraId.get(currentCamera));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 获取移动是的坐标
                        moveX = event.getX(0);
                        moveY = event.getY(0);
                        MoveCamera.touchMoveCamera((int) (initX - moveX) / 10, (int) (initY - moveY) / 10, new MoveCamera.Result() {
                            @Override
                            public void resultAngle(float a, float b) {
                                tv_angleA.setText("A:" + a + "º");
                                tv_angleB.setText("B:" + b + "º");
                            }
                        });
                        initX = moveX;
                        initY = moveY;
                        break;
                    case MotionEvent.ACTION_UP:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ll_tip.setVisibility(View.GONE);
                            }
                        }, delayMillis);
                        MoveCamera.stop();
                        break;
                }
                return true;
            }
        });

        iv_btn_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ll_tip.setVisibility(View.VISIBLE);
                        MoveCamera.move(MoveCamera.LEFT, cameraId.get(currentCamera), new MoveCamera.Result() {
                            @Override
                            public void resultAngle(float a, float b) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_angleA.setText("A:" + a + "º");
                                        tv_angleB.setText("B:" + b + "º");
                                    }
                                });
                            }
                        });
                        break;
                    case MotionEvent.ACTION_UP:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ll_tip.setVisibility(View.GONE);
                            }
                        }, delayMillis);
                        MoveCamera.stop();
                        break;
                }
                return true;
            }
        });

        iv_btn_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ll_tip.setVisibility(View.VISIBLE);
                        MoveCamera.move(MoveCamera.RIGHT, cameraId.get(currentCamera), new MoveCamera.Result() {
                            @Override
                            public void resultAngle(float a, float b) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_angleA.setText("A:" + a + "º");
                                        tv_angleB.setText("B:" + b + "º");
                                    }
                                });
                            }
                        });
                        break;
                    case MotionEvent.ACTION_UP:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ll_tip.setVisibility(View.GONE);
                            }
                        }, delayMillis);
                        MoveCamera.stop();
                        break;
                }
                return true;
            }
        });

        iv_btn_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ll_tip.setVisibility(View.VISIBLE);
                        MoveCamera.move(MoveCamera.UP, cameraId.get(currentCamera), new MoveCamera.Result() {
                            @Override
                            public void resultAngle(float a, float b) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_angleA.setText("A:" + a + "º");
                                        tv_angleB.setText("B:" + b + "º");
                                    }
                                });
                            }
                        });
                        break;
                    case MotionEvent.ACTION_UP:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ll_tip.setVisibility(View.GONE);
                            }
                        }, delayMillis);
                        MoveCamera.stop();
                        break;
                }
                return true;
            }
        });

        iv_btn_down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ll_tip.setVisibility(View.VISIBLE);
                        MoveCamera.move(MoveCamera.DOWN, cameraId.get(currentCamera), new MoveCamera.Result() {
                            @Override
                            public void resultAngle(float a, float b) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_angleA.setText("A:" + a + "º");
                                        tv_angleB.setText("B:" + b + "º");
                                    }
                                });
                            }
                        });
                        break;
                    case MotionEvent.ACTION_UP:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ll_tip.setVisibility(View.GONE);
                            }
                        }, delayMillis);
                        MoveCamera.stop();
                        break;
                }
                return true;
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

            }
        });
    }

    @Override
    protected void initData() {
        tag = getIntent().getStringExtra("tag");

        // 初始化摄像头编号集合
        cameraId = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            cameraId.add(i + 1);
        }

        ValueUtil.sendCameraCmd(cameraId.get(currentCamera).intValue(), 0, 45);

        // 获取Netty的Hander
        CustomerHandler customerHandler = getCustomerHandler(tag);
        if (customerHandler != null) {
            customerHandler.setCustomerCallback(new CustomerCallback() {
                @Override
                public void disConnected() {
                    ValueUtil.getIsConnected().put(tag, false);
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
                    L.e(msg + "ajksdlf");
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
                if (currentCamera > 0) {
                    currentCamera--;
                    ValueUtil.sendCameraCmd(cameraId.get(currentCamera), 0, 45);
                    tv_currentRoom.setText("客厅" + ValueUtil.getRoom().charAt(currentCamera));
                }
                break;
            case 1:
                if (currentCamera < cameraId.size() - 1) {
                    currentCamera++;
                    ValueUtil.sendCameraCmd(cameraId.get(currentCamera), 0, 45);
                    tv_currentRoom.setText("客厅" + ValueUtil.getRoom().charAt(currentCamera));
                }
                break;
        }
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
                if (cmd.equals("play")) {
                    setImage(msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
                    iv_camera.setImageBitmap(bitmap);
                }
            });
        }
    }

}
