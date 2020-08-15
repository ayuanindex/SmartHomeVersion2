package com.realmax.smarthomeversion2.audio;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.LayoutRes;

import com.google.gson.Gson;
import com.realmax.smarthomeversion2.Constant;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.activity.BaseActivity;
import com.realmax.smarthomeversion2.activity.CurtainActivity;
import com.realmax.smarthomeversion2.activity.DoorActivity;
import com.realmax.smarthomeversion2.activity.LightActivity;
import com.realmax.smarthomeversion2.activity.bean.CurtainAndAcBean;
import com.realmax.smarthomeversion2.activity.bean.DoorAndAirQualityBean;
import com.realmax.smarthomeversion2.activity.bean.LightBean;
import com.realmax.smarthomeversion2.activity.bean.RoomBean;
import com.realmax.smarthomeversion2.bean.LinkBean;
import com.realmax.smarthomeversion2.bean.MessageBean;
import com.realmax.smarthomeversion2.tcp.BaseNettyHandler;
import com.realmax.smarthomeversion2.tcp.CustomerHandler;
import com.realmax.smarthomeversion2.tencentCloud.bean.InterlocutionBean;
import com.realmax.smarthomeversion2.util.CustomerThread;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AudioControl {
    private static final String TAG = "AudioControl";
    private static final String CLOSE_REGEX = ".*退下.*";
    private static final String LIGHT = "灯";
    private static final String DOOR = "门";
    private static final String CURTAIN = "窗帘";
    private final BaseActivity mActivity;

    private final ArrayList<MessageBean> messageBeans;
    private final InterlocutionBean interlocutionBean;
    private List<RoomBean> roomBeans;

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
    private int door;
    private String field;
    private int lock;
    private int pass;
    private String tag = "";
    private String tag1 = "control_01";
    private String tag2 = "control_02";
    private String tag3 = "control_03";
    private DoorAndAirQualityBean.DoorsSBean doorsSBean;

    AudioControl(BaseActivity mActivity, ArrayList<MessageBean> messageBeans, InterlocutionBean interlocutionBean) {
        this.mActivity = mActivity;
        this.messageBeans = messageBeans;
        this.interlocutionBean = interlocutionBean;
        /*roomBeans = mActivity.roomBeans;*/
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
     * 执行命令
     */
    public void excutingAnOrder() {
        //
        if ("lightControl".equals(interlocutionBean.getResponse().getIntentName())) {
            List<InterlocutionBean.ResponseBean.SlotInfoListBean> slotInfoList = interlocutionBean.getResponse().getSlotInfoList();
            if (slotInfoList.size() > 2) {
                // 获取设备
                String device = Objects.requireNonNull(getSlotBean(slotInfoList, "device")).getSlotValue();
                // 获取控制信息
                String control = Objects.requireNonNull(getSlotBean(slotInfoList, "control")).getSlotValue();
                // 获取房间
                String room = Objects.requireNonNull(getSlotBean(slotInfoList, "room")).getSlotValue();

                roomToBeOperatedList.clear();
                roomToBeOperatedList.add(room);
                switch (device) {
                    case "灯":
                        tag = "control_01";
                        roomBeans = LightActivity.roomBeans;
                        // 查看设备的开关控制
                        if (control.matches(".*开.*")) {
                            lightInstruction(true);
                        } else if (control.matches(".*关.*")) {
                            lightInstruction(false);
                        }
                        break;
                    case "窗帘":
                        tag = "control_02";
                        roomBeans = CurtainActivity.roomBeans;
                        // 查看设备的开关控制
                        if (control.matches(".*开.*")) {
                            curtainInstruction(true);
                        } else if (control.matches(".*关.*")) {
                            curtainInstruction(false);
                        }
                        break;
                    default:
                        L.e("没有匹配到任何设备");
                        break;
                }
            }
        }
    }

    /**
     * 获取集合中制定的JavaBean
     *
     * @param slotInfoList 目标集合
     * @param slotName     字段值
     * @return 返回JavaBean
     */
    private InterlocutionBean.ResponseBean.SlotInfoListBean getSlotBean(List<InterlocutionBean.ResponseBean.SlotInfoListBean> slotInfoList, String slotName) {
        for (InterlocutionBean.ResponseBean.SlotInfoListBean slotInfoListBean : slotInfoList) {
            if (slotInfoListBean.getSlotName().equals(slotName)) {
                return slotInfoListBean;
            }
        }
        return null;
    }

    /**
     * 语音反馈
     *
     * @param msg    反馈的语音
     * @param layout 反馈的布局
     */
    private void feedBack(String msg, @LayoutRes int layout) {
        mActivity.runOnUiThread(() -> {
            try {
                SpeechMessage.getInstance()
                        .initLongTextTtsController(1301676932, "AKIDYqrzrcNJHyjEagH3M4WbRWLsCJNBB3D8", "mIXEfKjz0sVstdQ2VjhPqAMSIwgCTSAc")
                        .start(msg, (s, i) -> {
                            Log.d(TAG, "run: :::::::::" + s);
                        });
                addSimpleList(msg, layout);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        } else if (!selectRoom(str)) {
            // 没有检测到有关房间的指令
            feedBack("嗯...啊远大人，你要我干什么呢？", R.layout.item_left_message);
        } else/* if (selectRoom(str)) */ {
            // 根据识别结果判断是否提到了某个房间,在根据关键词选择分别执行灯、门、窗帘灯操作
            // 根据识别到的指令根据场景来进行操作
            getControlModel(str);
            // 开始执行相应的操作操作指令
            switch (currentModel) {
                case LIGHT:
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
        }
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
            currentModel = LIGHT;
            this.roomBeans = LightActivity.roomBeans;
        } else if (str.matches(OPEN_CURTAIN) || str.matches(CLOSE_CURTAIN)) {
            currentModel = CURTAIN;
            this.roomBeans = CurtainActivity.roomBeans;
        } else if (str.matches(OPEN_DOOR) || str.matches(CLOSE_DOOR)) {
            this.roomBeans = DoorActivity.roomBeans;
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
        LinkBean lightHandler = Constant.getLinkBeanByTag(tag);
        if (lightHandler == null) {
            mActivity.runOnUiThread(() -> {
                feedBack("灯的连接尚未开启，请开启后再试吧……", R.layout.item_left_message);
            });
            return;
        }

        /*mActivity.runOnUiThread(() -> {
            feedBack("正在" + (isOpen ? "开" : "关") + "灯", R.layout.item_left_message);
        });*/

        // 获取最近收到的等的状态json数据
        String currentCommand = lightHandler.getCurrentCommand();
        L.e("灯的当前状态" + currentCommand);
        if (!TextUtils.isEmpty(currentCommand)) {
            // 根据获取到的状态信息生成JavaBean对象
            LightBean lightBean = new Gson().fromJson(currentCommand, LightBean.class);
            List<Integer> lightS = lightBean.getLightList_S();
            changeState((RoomBean roomBean) -> {
                int[] lightId = roomBean.getModel();
                if (lightId.length > 0) {
                    for (int i : lightId) {
                        // 将需要修改的房间等的状态修改
                        lightS.set(i - 1, isOpen ? 1 : 0);
                    }
                }
            });

            L.e(lightBean.toString());
            // 开始执行控制指令
            ValueUtil.sendLightOpenOrCloseCmd(lightBean, tag);
        }
    }

    /**
     * 窗帘的开关操作
     *
     * @param isOpen true表示打开，false表示关闭
     */
    private void curtainInstruction(boolean isOpen) {
        // 获取当前提到的房间的窗帘的状态
        LinkBean lightHandler = Constant.getLinkBeanByTag(tag);
        if (lightHandler == null) {
            mActivity.runOnUiThread(() -> {
                feedBack("窗帘的连接尚未开启，请开启后再试吧", R.layout.item_left_message);
            });
            return;
        }

        /*mActivity.runOnUiThread(() -> {\

        
            feedBack("正在" + (isOpen ? "打开" : "关闭") + "窗帘", R.layout.item_left_message);
        });*/

        String currentCommand = lightHandler.getCurrentCommand();
        L.e("窗帘的当前状态" + currentCommand);
        if (!TextUtils.isEmpty(currentCommand)) {
            // 根据获取到的状态信息生成JavaBean对象
            CurtainAndAcBean curtainAndAcBean = new Gson().fromJson(currentCommand, CurtainAndAcBean.class);
            List<Integer> curtainS = curtainAndAcBean.getCurtain_S();
            changeState((RoomBean roomBean) -> {
                int[] curtailId = roomBean.getModel();
                if (curtailId.length > 0) {
                    for (int i : curtailId) {
                        // 将需要修改房间窗帘的状态修改
                        curtainS.set(i - 1, isOpen ? 1 : 0);
                    }
                }
            });

            L.e(curtainAndAcBean.toString());
            // 开始执行控制指令
            ValueUtil.sendCurtainOpenOrCloseCmd(curtainAndAcBean, tag);
        }
    }

    /**
     * 切换门的状态
     *
     * @param isOpen true表示打开，false表示关闭
     */
    private void doorInstruction(boolean isOpen) {
        // 获取当前提到的房间的门的状态
        LinkBean doorHandler = Constant.getLinkBeanByTag("door");
        if (doorHandler == null) {
            mActivity.runOnUiThread(() -> feedBack("门的连接尚未开启，请开启后再试吧", R.layout.item_left_message));
            return;
        }

        String currentCommand = doorHandler.getCurrentCommand();
        Log.e(TAG, "门的当前状态：" + currentCommand);
        if (!TextUtils.isEmpty(currentCommand)) {
            DoorAndAirQualityBean doorAndAirQualityBean = new Gson().fromJson(currentCommand, DoorAndAirQualityBean.class);

            // 验证提到的房间有没有门
            changeState((RoomBean roomBean) -> {
                int[] doorId = roomBean.getModel();
                if (doorId.length == 0) {
                    feedBack(roomBean.getRoomName() + "的门不可以远程控制哦", R.layout.item_left_message);
                } else {
                    feedBack("正在" + (isOpen ? "打开" : "关闭") + "门", R.layout.item_left_message);
                    for (int i : doorId) {
                        setDoorStatus(i, isOpen, doorAndAirQualityBean);
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
    private void setDoorStatus(int i, boolean isOpen, DoorAndAirQualityBean doorBean) {
        if (doorBean == null) {
            return;
        }

        doorsSBean = doorBean.getDoors_S().get(i);

        door = -1;
        lock = -1;
        pass = -1;

        if (doorsSBean != null) {
            door = doorsSBean.getDoorSwitch() == -1 ? door : (isOpen ? 1 : 0);
            lock = doorsSBean.getDoorLock() == -1 ? lock : (isOpen ? 1 : 0);
            pass = doorsSBean.getSetPassword() == -1 ? pass : doorsSBean.getSetPassword();
            ValueUtil.sendDoorCmd(door, lock, pass, tag3);

            if (isOpen && pass != -1) {
                feedBack("这扇门需要输入密码", R.layout.item_passoword);
            }
        }
    }

    public void sendPassword(int passwordInt) {
        if (passwordInt == pass) {
            feedBack("密码输入正确，请稍后", R.layout.item_left_message);
            ValueUtil.sendDoorCmd(doorsSBean.getDoorSwitch(), doorsSBean.getDoorLock(), passwordInt, tag3);
        } else {
            feedBack("密码输入错误", R.layout.item_left_message);
        }
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

        CustomerThread.poolExecutor.execute(() -> {
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
