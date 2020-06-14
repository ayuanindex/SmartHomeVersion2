package com.realmax.smarthomeversion2.activity.bean;

import java.util.List;

public class DoorAndAirQualityBean {

    private List<DoorsSBean> doors_S;
    private List<AirQualityBean> airQuality;

    public DoorAndAirQualityBean() {

    }

    public List<DoorsSBean> getDoors_S() {
        return doors_S;
    }

    public void setDoors_S(List<DoorsSBean> doors_S) {
        this.doors_S = doors_S;
    }

    public List<AirQualityBean> getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(List<AirQualityBean> airQuality) {
        this.airQuality = airQuality;
    }

    public static class DoorsSBean {
        /**
         * doorName : 客厅门
         * doorLock : 0
         * doorSwitch : 0
         * setPassword : 1234
         * outsideDetection : 0
         */

        /*private String doorName;*/
        private int doorLock = -1;
        private int doorSwitch = -1;
        private int setPassword = -1;
        private int outsideDetection;

        /*public String getDoorName() {
            return doorName;
        }

        public void setDoorName(String doorName) {
            this.doorName = doorName;
        }*/

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

        @Override
        public String toString() {
            return "DoorsSBean{" +
                    ", doorLock=" + doorLock +
                    ", doorSwitch=" + doorSwitch +
                    ", setPassword=" + setPassword +
                    ", outsideDetection=" + outsideDetection +
                    '}';
        }
    }

    public static class AirQualityBean {
        /**
         * smoke : 0
         * humidity : 100
         */

        private int smoke;
        private int humidity;

        public int getSmoke() {
            return smoke;
        }

        public void setSmoke(int smoke) {
            this.smoke = smoke;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        @Override
        public String toString() {
            return "AirQualityBean{" +
                    "smoke=" + smoke +
                    ", humidity=" + humidity +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DoorAndAirQualityBean{" +
                "doors_S=" + doors_S +
                ", airQuality=" + airQuality +
                '}';
    }
}
