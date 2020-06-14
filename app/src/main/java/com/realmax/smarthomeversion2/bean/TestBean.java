package com.realmax.smarthomeversion2.bean;

import java.util.List;

public class TestBean {

    private List<DoorsBean> doors;

    public List<DoorsBean> getDoors() {
        return doors;
    }

    public void setDoors(List<DoorsBean> doors) {
        this.doors = doors;
    }

    public static class DoorsBean {
        /**
         * doorName : 客厅门
         * doorLock : 0
         * doorSwitch : 0
         * setPassword : 1234
         * outsideDetection : 0
         * 门内检测 : 1
         */

        private String doorName;
        private int doorLock;
        private int doorSwitch;
        private int setPassword;
        private int outsideDetection;
        private int 门内检测;

        public String getDoorName() {
            return doorName;
        }

        public void setDoorName(String doorName) {
            this.doorName = doorName;
        }

        public int getDoorLock() {
            return doorLock;
        }

        public void setDoorLock(int doorLock) {
            this.doorLock = doorLock;
        }

        public int getDoorSwitch() {
            return doorSwitch;
        }

        public void setDoorSwitch(int doorSwitch) {
            this.doorSwitch = doorSwitch;
        }

        public int getSetPassword() {
            return setPassword;
        }

        public void setSetPassword(int setPassword) {
            this.setPassword = setPassword;
        }

        public int getOutsideDetection() {
            return outsideDetection;
        }

        public void setOutsideDetection(int outsideDetection) {
            this.outsideDetection = outsideDetection;
        }

        public int get门内检测() {
            return 门内检测;
        }

        public void set门内检测(int 门内检测) {
            this.门内检测 = 门内检测;
        }
    }
}
