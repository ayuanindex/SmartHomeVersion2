package com.realmax.smarthomeversion2.activity.bean;

import java.util.List;

public class DoorAndAirQualityBean {

    private List<DoorsSBean> doors_S;
    private List<AirQualitySBean> airQuality_S;

    public List<DoorsSBean> getDoors_S() {
        return doors_S;
    }

    public void setDoors_S(List<DoorsSBean> doors_S) {
        this.doors_S = doors_S;
    }

    public List<AirQualitySBean> getAirQuality_S() {
        return airQuality_S;
    }

    public void setAirQuality_S(List<AirQualitySBean> airQuality_S) {
        this.airQuality_S = airQuality_S;
    }

    public static class DoorsSBean {
        /**
         * doorLock : 0
         * doorSwitch : 0
         * setPassword : 1234
         * outsideDetection : 0
         */

        private int doorLock;
        private int doorSwitch;
        private int setPassword;
        private int outsideDetection;

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
                    "doorLock=" + doorLock +
                    ", doorSwitch=" + doorSwitch +
                    ", setPassword=" + setPassword +
                    ", outsideDetection=" + outsideDetection +
                    '}';
        }
    }

    public static class AirQualitySBean {
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
            return "AirQualitySBean{" +
                    "smoke=" + smoke +
                    ", humidity=" + humidity +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DoorAndAirQualityBean{" +
                "doors_S=" + doors_S +
                ", airQuality_S=" + airQuality_S +
                '}';
    }
}
