package com.realmax.smarthomeversion2.activity.bean;

import java.util.List;

public class HumenAndRobotAndAlarmBean {

    /**
     * humanBodySensor_S : [0,0,0,0,0,0,0]
     * robot_S : {"broom":0,"chargeStatus":0,"leftWheel":10,"rightWheel":10,"positionX":10,"positionY":10,"power":90}
     * alarmSystem_S : [1,2,3,4]
     * lightSensing_S : [1,2,3,4,5,6,7,8,9]
     */

    private RobotSBean robot_S;
    private List<Integer> humanBodySensor_S;
    private List<Integer> alarmSystem_S;
    private List<Integer> lightSensing_S;

    public RobotSBean getRobot_S() {
        return robot_S;
    }

    public void setRobot_S(RobotSBean robot_S) {
        this.robot_S = robot_S;
    }

    public List<Integer> getHumanBodySensor_S() {
        return humanBodySensor_S;
    }

    public void setHumanBodySensor_S(List<Integer> humanBodySensor_S) {
        this.humanBodySensor_S = humanBodySensor_S;
    }

    public List<Integer> getAlarmSystem_S() {
        return alarmSystem_S;
    }

    public void setAlarmSystem_S(List<Integer> alarmSystem_S) {
        this.alarmSystem_S = alarmSystem_S;
    }

    public List<Integer> getLightSensing_S() {
        return lightSensing_S;
    }

    public void setLightSensing_S(List<Integer> lightSensing_S) {
        this.lightSensing_S = lightSensing_S;
    }

    public static class RobotSBean {
        /**
         * broom : 0
         * chargeStatus : 0
         * leftWheel : 10
         * rightWheel : 10
         * positionX : 10
         * positionY : 10
         * power : 90
         */

        private int broom;
        private int chargeStatus;
        private int leftWheel;
        private int rightWheel;
        private int positionX;
        private int positionY;
        private int power;

        public int getBroom() {
            return broom;
        }

        public void setBroom(int broom) {
            this.broom = broom;
        }

        public int getChargeStatus() {
            return chargeStatus;
        }

        public void setChargeStatus(int chargeStatus) {
            this.chargeStatus = chargeStatus;
        }

        public int getLeftWheel() {
            return leftWheel;
        }

        public void setLeftWheel(int leftWheel) {
            this.leftWheel = leftWheel;
        }

        public int getRightWheel() {
            return rightWheel;
        }

        public void setRightWheel(int rightWheel) {
            this.rightWheel = rightWheel;
        }

        public int getPositionX() {
            return positionX;
        }

        public void setPositionX(int positionX) {
            this.positionX = positionX;
        }

        public int getPositionY() {
            return positionY;
        }

        public void setPositionY(int positionY) {
            this.positionY = positionY;
        }

        public int getPower() {
            return power;
        }

        public void setPower(int power) {
            this.power = power;
        }

        @Override
        public String toString() {
            return "RobotSBean{" +
                    "broom=" + broom +
                    ", chargeStatus=" + chargeStatus +
                    ", leftWheel=" + leftWheel +
                    ", rightWheel=" + rightWheel +
                    ", positionX=" + positionX +
                    ", positionY=" + positionY +
                    ", power=" + power +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HumenAndRobotAndAlarmBean{" +
                "robot_S=" + robot_S +
                ", humanBodySensor_S=" + humanBodySensor_S +
                ", alarmSystem_S=" + alarmSystem_S +
                ", lightSensing_S=" + lightSensing_S +
                '}';
    }
}
