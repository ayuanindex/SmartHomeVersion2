package com.realmax.smarthomeversion2.bean;

/**
 * @author ayuan
 */
public class RoomBean {
    private String roomName;
    private int[] lightId;
    private int[] curtailId;
    private int[] cameraId;
    private int[] doorId;

    public RoomBean(String roomName, int[] lightId, int[] curtailId, int[] cameraId, int[] doorId) {
        this.roomName = roomName;
        this.lightId = lightId;
        this.curtailId = curtailId;
        this.cameraId = cameraId;
        this.doorId = doorId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int[] getLightId() {
        return lightId;
    }

    public void setLightId(int[] lightId) {
        this.lightId = lightId;
    }

    public int[] getCurtailId() {
        return curtailId;
    }

    public void setCurtailId(int[] curtailId) {
        this.curtailId = curtailId;
    }

    public int[] getCameraId() {
        return cameraId;
    }

    public void setCameraId(int[] cameraId) {
        this.cameraId = cameraId;
    }

    public int[] getDoorId() {
        return doorId;
    }

    public void setDoorId(int[] doorId) {
        this.doorId = doorId;
    }
}
