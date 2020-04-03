package com.realmax.smarthomeversion2.bean;

import java.util.List;

/**
 * @ProjectName: SmartHomeVersion2
 * @Package: com.realmax.smarthomeversion2.bean
 * @ClassName: CurtainBean
 * @CreateDate: 2020/4/3 10:34
 */
public class CurtainBean {

    private List<Integer> Curtain_S;
    private List<Integer> Curtain_C;

    public CurtainBean(List<Integer> curtain_S, List<Integer> curtain_C) {
        Curtain_S = curtain_S;
        Curtain_C = curtain_C;
    }

    public List<Integer> getCurtain_S() {
        return Curtain_S;
    }

    public void setCurtain_S(List<Integer> Curtain_S) {
        this.Curtain_S = Curtain_S;
    }

    public List<Integer> getCurtain_C() {
        return Curtain_C;
    }

    public void setCurtain_C(List<Integer> Curtain_C) {
        this.Curtain_C = Curtain_C;
    }

    @Override
    public String toString() {
        return "CurtainBean{" +
                "Curtain_S=" + Curtain_S +
                ", Curtain_C=" + Curtain_C +
                '}';
    }
}
