package com.realmax.smarthomeversion2.bean;

import java.util.List;

/**
 * @ProjectName: SmartHomeVersion2
 * @Package: com.realmax.smarthomeversion2.bean
 * @ClassName: LightBean
 * @CreateDate: 2020/4/3 10:33
 */
public class LightBean {

    private List<Integer> Ligth_S;
    private List<Integer> Light_C;

    public LightBean(List<Integer> ligth_S, List<Integer> light_C) {
        Ligth_S = ligth_S;
        Light_C = light_C;
    }

    public List<Integer> getLigth_S() {
        return Ligth_S;
    }

    public void setLigth_S(List<Integer> Ligth_S) {
        this.Ligth_S = Ligth_S;
    }

    public List<Integer> getLight_C() {
        return Light_C;
    }

    public void setLight_C(List<Integer> Light_C) {
        this.Light_C = Light_C;
    }

    @Override
    public String toString() {
        return "LightBean{" +
                "Ligth_S=" + Ligth_S +
                ", Light_C=" + Light_C +
                '}';
    }
}
