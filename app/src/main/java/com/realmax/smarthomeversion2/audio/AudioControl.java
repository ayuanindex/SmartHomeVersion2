package com.realmax.smarthomeversion2.audio;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.LayoutRes;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.activity.BaseActivity;
import com.realmax.smarthomeversion2.bean.DoorBean;
import com.realmax.smarthomeversion2.bean.LightOrCurtainBean;
import com.realmax.smarthomeversion2.bean.MessageBean;
import com.realmax.smarthomeversion2.bean.RoomBean;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.util.CustomerThreadManager;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class AudioControl {
    private static final String TAG = "AudioControl";
    private static final String CLOSE_REGEX = ".*退下.*";
    private static final String LIGTH = "灯";
    private static final String DOOR = "门";
    private static final String CURTAIN = "窗帘";
    private final BaseActivity mActivity;

    private final ArrayList<MessageBean> messageBeans;
    private final ArrayList<RoomBean> roomBeans;

    private static final String OPEN_LIGHT = ".*[开|灯].*[开|灯].*";
    private static final String CLOSE_LIGHT = ".*[关|灯].*[关|灯].*";
    private static final String OPEN_DOOR = ".*[开|门].*[开|门].*";
    private static final String CLOSE_DOOR = ".*[关|门].*[关|门].*";
    private static final String OPEN_CURTAIN = ".*[开|窗].*[开|窗].*";
    private static final String CLOSE_CURTAIN = ".*[关|窗].*[关|窗].*";

    /**
     * 当前的模式
     */
    private String currentModel = "";

    /**
     * 是否进入场景模式
     */
    private boolean isSingleModel = false;

    /**
     * 待操作房间集合
     */
    private ArrayList<String> roomToBeOperatedList = new ArrayList<>(0);
    private int passwordInt;
    private int door;
    private String field;
    private int lock;
    private int pass;

    AudioControl(BaseActivity mActivity, ArrayList<MessageBean> messageBeans, CommendActivity.CustomerAdapter customerAdapter) {
        this.mActivity = mActivity;
        this.messageBeans = messageBeans;
        roomBeans = mActivity.roomBeans;
    }

    /**
     * 识别过程中的回调
     *
     * @param msg 已经识别出的文字
     */
    public abstract void onSliceSuccess(String msg);

    /**
     * 识别完成时的回调
     *
     * @param msg 识别出的文字
     */
    public abstract void onSuccessString(String msg);

    /**
     * 更新列表
     */
    public abstract void updateItem();

    /**
     * 识别完成时的回调,调用识别完成时的抽象方法
     * 在此方法中可以根据语音提到的相关命令来执行对应的操作
     *
     * @param str 识别完成的字符串
     */
    void onSegmentSuccess(String str) {
        // 回调识别完成时的字符串
        onSuccessString(str);
        // 添加普通的聊天布局
        addSimpleList(str, R.layout.item_left_message);

        if (!isSingleModel) {
            // 非场景模式
            notScenes(str);
        } else {
            // 进入场景模式
            scenes();
        }
    }

    /**
     * 语音反馈
     *
     * @param msg    反馈的语音
     * @param layout 反馈的布局
     */
    private void feedBack(String msg, @LayoutRes int layout) {
        mActivity.runOnUiThread(() -> {
            SpeechMessage.getInstance()
                    .initLongTextTtsController(1301676932, "AKIDYqrzrcNJHyjEagH3M4WbRWLsCJNBB3D8", "mIXEfKjz0sVstdQ2VjhPqAMSIwgCTSAc")
                    .start(msg, (s, i) -> {
                        Log.d(TAG, "run: :::::::::" + s);
                    });
            addSimpleList(msg, layout);
        });
    }

    /**
     * 添加普通的文字识别界面
     *
     * @param str    提示文字
     * @param layout 反馈的布局
     */
    private void addSimpleList(String str, int layout) {
        mActivity.runOnUiThread(() -> {
            messageBeans.add(messageBeans.size(), new MessageBean(str, layout));
            // 刷新列表
            updateItem();
        });
    }

    /**
     * 非场景模式，执行普通场景模式的选择指令
     *
     * @param str 识别出的文字
     */
    private void notScenes(String str) {
        if (str.matches(CLOSE_REGEX)) {
            // 开始执行关闭语音界面指令
            finish();
        } else if (selectRoom(str)) {
            // 根据识别结果判断是否提到了某个房间,在根据关键词选择分别执行灯、门、窗帘灯操作
            // 根据识别到的指令根据场景来进行操作
            getControlModel(str);
            // 开始执行相应的操作操作指令
            switch (currentModel) {
                case LIGTH:
                    if (str.matches(OPEN_LIGHT)) {
                        lightInstruction(true);
                    } else if (str.matches(CLOSE_LIGHT)) {
                        lightInstruction(false);
                    }
                    break;
                case DOOR:
                    if (str.matches(OPEN_DOOR)) {
                        doorInstruction(true);
                    } else if (str.matches(CLOSE_DOOR)) {
                        doorInstruction(false);
                    }
                    break;
                case CURTAIN:
                    if (str.matches(OPEN_CURTAIN)) {
                        curtainInstruction(true);
                    } else if (str.matches(CLOSE_CURTAIN)) {
                        curtainInstruction(false);
                    }
                    break;
                default:
            }
        } else if (!selectRoom(str)) {
            // 没有检测到有关房间的指令
            feedBack("嗯...啊远大人，你要我干什么呢？", R.layout.item_left_message);
            /*feedBack("Um ... Ah Master, what do you want me to do?", R.layout.item_left_message);*/
        }/* else {
            // 没有匹配到任何指令
            feedBack("抱歉，我不知道你在说什么", R.layout.item_left_message);
        }*/
    }

    /**
     * 根据识别到的文字选择对应的房间
     *
     * @param str 需要进行判断的识别结果字符串
     * @return true表示又该房间，false表示没有该房间
     */
    private boolean selectRoom(String str) {
        roomToBeOperatedList.clear();
        for (RoomBean roomBean : roomBeans) {
            // 检查有没有提到的房间
            String regex = ".*" + roomBean.getRoomName() + ".*";
            if (str.matches(regex)) {
                // 设置待操作房间
                roomToBeOperatedList.add(roomBean.getRoomName());
                L.e(roomToBeOperatedList.toString());
            }
        }
        return roomToBeOperatedList.size() != 0;
    }

    /**
     * 识别相关者指令来进行操作
     *
     * @param str 识别成功的字符串
     */
    private void getControlModel(String str) {
        // 根据指令切换当前模式
        if (str.matches(OPEN_LIGHT) || str.matches(CLOSE_LIGHT)) {
            currentModel = LIGTH;
        } else if (str.matches(OPEN_CURTAIN) || str.matches(CLOSE_CURTAIN)) {
            currentModel = CURTAIN;
        } else if (str.matches(OPEN_DOOR) || str.matches(CLOSE_DOOR)) {
            currentModel = DOOR;
        }
        L.e(currentModel);
    }

    /**
     * 灯的指令
     *
     * @param isOpen 是否开启
     */
    private void lightInstruction(boolean isOpen) {
        // 获取当前提到的房间的灯的状态
        CustomerHandlerBase lightHandler = ValueUtil.getHandlerHashMap().get("light");
        if (lightHandler == null) {
            mActivity.runOnUiThread(() -> {
                feedBack("灯的连接尚未开启，请开启后再试吧……", R.layout.item_left_message);
            });
            return;
        }

        mActivity.runOnUiThread(() -> {
            feedBack("正在" + (isOpen ? "开" : "关") + "灯", R.layout.item_left_message);
        });

        // 获取最近收到的等的状态json数据
        String currentCommand = lightHandler.getCurrentCommand();
        L.e("灯的当前状态" + currentCommand);
        if (!TextUtils.isEmpty(currentCommand)) {
            // 根据获取到的状态信息生成JavaBean对象
            LightOrCurtainBean lightOrCurtainBean = new Gson().fromJson(currentCommand, LightOrCurtainBean.class);
            List<Integer> lightS = lightOrCurtainBean.getLight_S();
            changeState((RoomBean roomBean) -> {
                int[] lightId = roomBean.getLightId();
                if (lightId.length > 0) {
                    for (int i : lightId) {
                        // 将需要修改的房间等的状态修改
                        lightS.set(i - 1, isOpen ? 1 : 0);
                    }
                }
            });

            L.e(lightOrCurtainBean.toString());
            // 开始执行控制指令
            ValueUtil.sendLightOpenOrCloseCmd(lightOrCurtainBean);
        }
    }

    /**
     * 窗帘的开关操作
     *
     * @param isOpen true表示打开，false表示关闭
     */
    private void curtainInstruction(boolean isOpen) {
        // 获取当前提到的房间的窗帘的状态
        CustomerHandlerBase lightHandler = ValueUtil.getHandlerHashMap().get("light");
        if (lightHandler == null) {
            mActivity.runOnUiThread(() -> {
                feedBack("窗帘的连接尚未开启，请开启后再试吧", R.layout.item_left_message);
            });
            return;
        }

        mActivity.runOnUiThread(() -> {
            feedBack("正在" + (isOpen ? "打开" : "关闭") + "窗帘", R.layout.item_left_message);
        });

        String currentCommand = lightHandler.getCurrentCommand();
        L.e("窗帘的当前状态" + currentCommand);
        if (!TextUtils.isEmpty(currentCommand)) {
            // 根据获取到的状态信息生成JavaBean对象
            LightOrCurtainBean lightOrCurtainBean = new Gson().fromJson(currentCommand, LightOrCurtainBean.class);
            List<Integer> curtainS = lightOrCurtainBean.getCurtain_S();
            changeState((RoomBean roomBean) -> {
                int[] curtailId = roomBean.getCurtailId();
                if (curtailId.length > 0) {
                    for (int i : curtailId) {
                        // 将需要修改房间窗帘的状态修改
                        curtainS.set(i - 1, isOpen ? 1 : 0);
                    }
                }
            });

            L.e(lightOrCurtainBean.toString());
            // 开始执行控制指令
            ValueUtil.sendCurtainOpenOrCloseCmd(lightOrCurtainBean);
        }
    }

    /**
     * 切换门的状态
     *
     * @param isOpen true表示打开，false表示关闭
     */
    private void doorInstruction(boolean isOpen) {
        // 获取当前提到的房间的门的状态
        CustomerHandlerBase doorHandler = ValueUtil.getHandlerHashMap().get("door");
        if (doorHandler == null) {
            mActivity.runOnUiThread(() -> feedBack("门的连接尚未开启，请开启后再试吧", R.layout.item_left_message));
        }

        String currentCommand = doorHandler.getCurrentCommand();
        Log.e(TAG, "门的当前状态：" + currentCommand);
        if (!TextUtils.isEmpty(currentCommand)) {
            DoorBean doorBean = new Gson().fromJson(currentCommand, DoorBean.class);

            // 验证提到的房间有没有门
            changeState((RoomBean roomBean) -> {
                int[] doorId = roomBean.getDoorId();
                if (doorId.length == 0) {
                    feedBack(roomBean.getRoomName() + "的门不可以远程控制哦", R.layout.item_left_message);
                } else {
                    feedBack("正在" + (isOpen ? "打开" : "关闭") + "门", R.layout.item_left_message);
                    for (int i : doorId) {
                        setDoorStatus(i, isOpen, doorBean);
                    }
                }
            });
        }
    }

    /**
     * 修改门的状态
     *
     * @param i        对应房间的门的编号
     * @param isOpen   开关状态
     * @param doorBean 门的状态集合
     */
    private void setDoorStatus(int i, boolean isOpen, DoorBean doorBean) {
        field = "";

        door = -1;
        lock = -1;
        pass = -1;


        switch (i) {
            case 1:
                // 客厅大门--->电动门
                DoorBean.Door1SBean door1S = doorBean.getDoor1_S();
                field = "door1_C";
                door = isOpen ? 1 : 0;
                break;
            case 2:
                // 门厅--->锁+密码
                DoorBean.Door2SBean door2S = doorBean.getDoor2_S();
                field = "door2_C";
                lock = isOpen ? 1 : 0;
                // 返回信息，输入密码
                if (isOpen) {
                    feedBack("这扇门需要输入密码才能打开哦", R.layout.item_passoword);
                }
                Log.d(TAG, "setDoorStatus: 当前输入的密码：" + this.passwordInt);
                pass = door2S.getPass();
                break;
            case 3:
                // 院墙小门--->锁+密码
                DoorBean.Door3SBean door3S = doorBean.getDoor3_S();
                field = "door3_C";
                lock = isOpen ? 1 : 0;
                if (isOpen) {
                    feedBack("这扇门需要输入密码才能打开哦", R.layout.item_passoword);
                }
                pass = door3S.getPass();
                break;
            case 4:
                // 院墙大门--->锁+密码
                DoorBean.Door4SBean door4S = doorBean.getDoor4_S();
                field = "door4_C";
                lock = isOpen ? 1 : 0;
                if (isOpen) {
                    feedBack("这扇门需要输入密码才能打开哦", R.layout.item_passoword);
                }
                pass = door4S.getPass();
                break;
            case 5:
                // 车库门--->电动门+锁
                DoorBean.Door5SBean door5S = doorBean.getDoor5_S();
                field = "door5_C";
                door = isOpen ? 1 : 0;
                lock = isOpen ? 1 : 0;
                break;
            default:
                break;
        }

        // 发送指令开灯
        ValueUtil.sendDoorCmd(field, door, lock, pass);
    }

    public void sendPassword(int passwordInt) {
        this.passwordInt = passwordInt;
        ValueUtil.sendDoorCmd(field, door, lock, passwordInt);
        feedBack("正在验证您输入的密码，请稍后", R.layout.item_left_message);
    }

    private void changeState(ChangeStatusListener changeStatusListener) {
        for (String roomName : roomToBeOperatedList) {
            for (RoomBean roomBean : roomBeans) {
                if (roomBean.getRoomName().equals(roomName)) {
                    // 获取到对应房间的对象
                    changeStatusListener.changeStatus(roomBean);
                }
            }
        }
    }

    /**
     * 场景模式，根据之前的问题询问是否开启那个灯等等
     */
    private void scenes() {

    }

    /**
     * 关闭语音操作界面
     */
    public void finish() {
        mActivity.runOnUiThread(() -> {
            feedBack("好嘞，下次再聊", R.layout.item_left_message);
        });

        CustomerThreadManager.threadPoolExecutor.execute(() -> {
            try {
                Thread.sleep(1000);
                mActivity.finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    interface ChangeStatusListener {
        void changeStatus(RoomBean roomBean);
    }
}
