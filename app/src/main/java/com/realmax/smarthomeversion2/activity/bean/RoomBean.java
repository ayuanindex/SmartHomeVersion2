package com.realmax.smarthomeversion2.activity.bean;

import java.util.Arrays;

public class RoomBean {
    private String roomName;
    private int[] model;

    public RoomBean() {
    }

    public RoomBean(String roomName, int[] model) {
        this.roomName = roomName;
        this.model = model;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int[] getModel() {
        return model;
    }

    public void setModel(int[] model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "RoomBean{" +
                "roomName='" + roomName + '\'' +
                ", model=" + Arrays.toString(model) +
                '}';
    }
}
