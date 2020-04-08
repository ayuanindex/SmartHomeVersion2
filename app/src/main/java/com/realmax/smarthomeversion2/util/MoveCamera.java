package com.realmax.smarthomeversion2.util;

import java.util.Timer;
import java.util.TimerTask;

public class MoveCamera {
    /**
     * 初始角度
     */
    private static float LONGITUDINAL = 45f;
    private static float HORIZONTAL = 0f;

    /**
     * 调整的方向
     */
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    /**
     * 定时发送数据
     */
    private static Timer timer;

    /**
     * 发送数据的方法体
     */
    private static TimerTask timerTask;

    /**
     * 设备的ID，相当于摄像头的编号
     */
    private static int DEVICEID = 0;

    /**
     * 移动摄像头
     *
     * @param type     移动方向
     * @param deviceId 需要移动的摄像头ID
     */
    public static void move(int type, int deviceId, Result result) {
        // 重置角度
        if (MoveCamera.DEVICEID != deviceId) {
            HORIZONTAL = 0;
            LONGITUDINAL = 45;
        }
        MoveCamera.DEVICEID = deviceId;
        switch (type) {
            case 1:
                moveUp(result);
                break;
            case 2:
                moveDown(result);
                break;
            case 3:
                moveLeft(result);
                break;
            case 4:
                moveRight(result);
                break;
        }
    }

    /**
     * 向上移动
     * @param result
     */
    private static void moveUp(Result result) {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (LONGITUDINAL < 90f) {
                    LONGITUDINAL++;
                    ValueUtil.sendCameraCmd(DEVICEID, HORIZONTAL, LONGITUDINAL);
                    result.resultAngle(HORIZONTAL, LONGITUDINAL);
                }
            }
        };
        timer.schedule(timerTask, 0, 50);
    }

    /**
     * 向下移动
     * @param result
     */
    private static void moveDown(Result result) {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (LONGITUDINAL > -90f) {
                    LONGITUDINAL--;
                    ValueUtil.sendCameraCmd(DEVICEID, HORIZONTAL, LONGITUDINAL);
                    result.resultAngle(HORIZONTAL, LONGITUDINAL);
                }
            }
        };
        timer.schedule(timerTask, 0, 50);
    }

    /**
     * 向左移动
     * @param result
     */
    private static void moveLeft(Result result) {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (HORIZONTAL > -180f) {
                    HORIZONTAL--;
                    ValueUtil.sendCameraCmd(DEVICEID, HORIZONTAL, LONGITUDINAL);
                    result.resultAngle(HORIZONTAL, LONGITUDINAL);
                }
            }
        };
        timer.schedule(timerTask, 0, 50);
    }

    /**
     * 向右移动
     * @param result
     */
    private static void moveRight(Result result) {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (HORIZONTAL < 180f) {
                    HORIZONTAL++;
                    ValueUtil.sendCameraCmd(DEVICEID, HORIZONTAL, LONGITUDINAL);
                    result.resultAngle(HORIZONTAL, LONGITUDINAL);
                }
            }
        };
        timer.schedule(timerTask, 0, 50);
    }

    /**
     * 手指触摸图片时开始发送切换角度消息
     *
     * @param deviceId 需要切换角度的摄像头
     */
    public static void touchStart(int deviceId) {
        if (MoveCamera.DEVICEID != deviceId) {
            HORIZONTAL = 0f;
            LONGITUDINAL = 45f;
        }
        MoveCamera.DEVICEID = deviceId;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                ValueUtil.sendCameraCmd(DEVICEID, HORIZONTAL, LONGITUDINAL);
            }
        };
        timer.schedule(timerTask, 0, 10);
    }

    /**
     * 摄像头移动方向测算
     *
     * @param addHor  横向增量
     * @param addLong 纵向增量
     */
    public static void touchMoveCamera(int addHor, int addLong, Result result) {
        // 上下滑
        if (LONGITUDINAL <= 90f && addLong > 0) {
            LONGITUDINAL--;
        } else if (LONGITUDINAL >= -90f && addLong < 0) {
            LONGITUDINAL++;
        }

        // 左右滑
        if (HORIZONTAL <= 180f && addHor > 0) {
            HORIZONTAL++;
        } else if (HORIZONTAL >= -180f && addHor < 0) {
            HORIZONTAL--;
        }

        // 对纵向的距离最大值与最小值再一次进行判定，防止角度溢出
        LONGITUDINAL = Math.min(LONGITUDINAL, 90f);
        LONGITUDINAL = Math.max(LONGITUDINAL, -90f);

        // 对横向的距离最大值与最小值再一次进行判定，防止角度溢出
        HORIZONTAL = Math.min(HORIZONTAL, 180f);
        HORIZONTAL = Math.max(HORIZONTAL, -180f);

        result.resultAngle(HORIZONTAL, LONGITUDINAL);
    }

    /**
     * 停止发送控制指令
     */
    public static void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    public interface Result {
        void resultAngle(float a, float b);
    }
}
