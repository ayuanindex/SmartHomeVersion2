package com.realmax.smarthomeversion2.activity.bean;

import java.util.List;

public class CurtainAndAcBean {

    /**
     * curtain_S : [0,0,0,0,0,0,0,0,0,0,0]
     * ac_S : {"acPower":0,"mode":0,"windSpeed":5,"temperature":19,"currentTemperature":20}
     */

    private AcSBean ac_S;
    private List<Integer> curtain_S;

    public CurtainAndAcBean() {

    }

    public CurtainAndAcBean(List<Integer> curtain_S) {
        this.curtain_S = curtain_S;
    }

    public CurtainAndAcBean(AcSBean ac_S) {
        this.ac_S = ac_S;
    }

    public CurtainAndAcBean(AcSBean ac_S, List<Integer> curtain_S) {
        this.ac_S = ac_S;
        this.curtain_S = curtain_S;
    }


    public AcSBean getAc_S() {
        return ac_S;
    }

    public void setAc_S(AcSBean ac_S) {
        this.ac_S = ac_S;
    }

    public List<Integer> getCurtain_S() {
        return curtain_S;
    }

    public void setCurtain_S(List<Integer> curtain_S) {
        this.curtain_S = curtain_S;
    }

    public static class AcSBean {
        /**
         * acPower : 0
         * mode : 0
         * windSpeed : 5
         * temperature : 19
         * currentTemperature : 20
         */

        private int acPower;
        private int mode;
        private int windSpeed;
        private int temperature;
        private int currentTemperature;

        public int getAcPower() {
            return acPower;
        }

        public void setAcPower(int acPower) {
            this.acPower = acPower;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public int getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(int windSpeed) {
            this.windSpeed = windSpeed;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public int getCurrentTemperature() {
            return currentTemperature;
        }

        public void setCurrentTemperature(int currentTemperature) {
            this.currentTemperature = currentTemperature;
        }

        @Override
        public String toString() {
            return "AcSBean{" +
                    "acPower=" + acPower +
                    ", mode=" + mode +
                    ", windSpeed=" + windSpeed +
                    ", temperature=" + temperature +
                    ", currentTemperature=" + currentTemperature +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CurtainAndAcBean{" +
                "ac_S=" + ac_S +
                ", curtain_S=" + curtain_S +
                '}';
    }
}
