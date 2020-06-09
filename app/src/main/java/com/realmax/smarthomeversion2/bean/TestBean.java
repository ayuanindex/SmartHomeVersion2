package com.realmax.smarthomeversion2.bean;

import java.util.List;
import java.util.function.Consumer;

public class TestBean {

    private List<RoomsBean> rooms;

    public List<RoomsBean> getRooms() {
        rooms.forEach(new Consumer<RoomsBean>() {
            @Override
            public void accept(RoomsBean roomsBean) {

            }
        });
        return rooms;
    }

    public void setRooms(List<RoomsBean> rooms) {
        this.rooms = rooms;
    }

    public static class RoomsBean {
        /**
         * roomTag : A
         * humanBodySensing : [1,2,3]
         * lightIntensity : [100,200,300]
         * audibleAlarm : [0,1,2,3,4]
         * gasSensor : [{"humidity":1,"smoke":1}]
         */

        private String roomTag;
        private List<Integer> humanBodySensing;
        private List<Integer> lightIntensity;
        private List<Integer> audibleAlarm;
        private List<GasSensorBean> gasSensor;

        public String getRoomTag() {
            return roomTag;
        }

        public void setRoomTag(String roomTag) {
            this.roomTag = roomTag;
        }

        public List<Integer> getHumanBodySensing() {
            return humanBodySensing;
        }

        public void setHumanBodySensing(List<Integer> humanBodySensing) {
            this.humanBodySensing = humanBodySensing;
        }

        public List<Integer> getLightIntensity() {
            return lightIntensity;
        }

        public void setLightIntensity(List<Integer> lightIntensity) {
            this.lightIntensity = lightIntensity;
        }

        public List<Integer> getAudibleAlarm() {
            return audibleAlarm;
        }

        public void setAudibleAlarm(List<Integer> audibleAlarm) {
            this.audibleAlarm = audibleAlarm;
        }

        public List<GasSensorBean> getGasSensor() {
            return gasSensor;
        }

        public void setGasSensor(List<GasSensorBean> gasSensor) {
            this.gasSensor = gasSensor;
        }

        public static class GasSensorBean {
            /**
             * humidity : 1
             * smoke : 1
             */

            private int humidity;
            private int smoke;

            public int getHumidity() {
                return humidity;
            }

            public void setHumidity(int humidity) {
                this.humidity = humidity;
            }

            public int getSmoke() {
                return smoke;
            }

            public void setSmoke(int smoke) {
                this.smoke = smoke;
            }
        }
    }
}
