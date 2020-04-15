package com.realmax.smarthomeversion2.bean;

import java.util.List;

/**
 * @ProjectName: SmartHomeVersion2
 * @Package: com.realmax.smarthomeversion2.bean
 * @ClassName: LightBean
 * @CreateDate: 2020/4/3 10:33
 */
public class LightBean {

    private List<Integer> Light_S;
    private List<Integer> Light_C;

    public LightBean(List<Integer> light_S, List<Integer> light_C) {
        Light_S = light_S;
        Light_C = light_C;
    }

    public List<Integer> getLight_S() {
        return Light_S;
    }

    public void setLight_S(List<Integer> Light_S) {
        this.Light_S = Light_S;
    }

    public List<Integer> getLight_C() {
        return Light_C;
    }

    public void setLight_C(List<Integer> Light_C) {
        this.Light_C = Light_C;
    }
}
