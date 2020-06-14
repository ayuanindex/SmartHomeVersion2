package com.realmax.smarthomeversion2.activity.bean;

import java.util.List;

public class LightBean {

    private List<Integer> lightList_S;

    public LightBean() {
    }

    public LightBean(List<Integer> lightList_S) {
        this.lightList_S = lightList_S;
    }

    public List<Integer> getLightList_S() {
        return lightList_S;
    }

    public void setLightList_S(List<Integer> lightList_S) {
        this.lightList_S = lightList_S;
    }

    @Override
    public String toString() {
        return "LightBean{" +
                "lightList_S=" + lightList_S +
                '}';
    }
}
