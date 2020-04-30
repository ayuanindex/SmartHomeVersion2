package com.realmax.smarthomeversion2.bean;

import java.util.List;

public class LightOrCurtainBean {

    private List<Integer> Light_S;
    private List<Integer> Curtain_S;

    public LightOrCurtainBean(List<Integer> light_S, List<Integer> curtain_S) {
        Light_S = light_S;
        Curtain_S = curtain_S;
    }

    public List<Integer> getLight_S() {
        return Light_S;
    }

    public void setLight_S(List<Integer> Light_S) {
        this.Light_S = Light_S;
    }

    public List<Integer> getCurtain_S() {
        return Curtain_S;
    }

    public void setCurtain_S(List<Integer> Curtain_S) {
        this.Curtain_S = Curtain_S;
    }
}
