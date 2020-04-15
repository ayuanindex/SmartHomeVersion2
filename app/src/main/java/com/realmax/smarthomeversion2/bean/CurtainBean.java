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

    public CurtainBean(List<Integer> curtainS, List<Integer> curtainC) {
        Curtain_S = curtainS;
        Curtain_C = curtainC;
    }

    public List<Integer> getCurtainS() {
        return Curtain_S;
    }

    public void setCurtainS(List<Integer> curtainS) {
        this.Curtain_S = curtainS;
    }

    public List<Integer> getCurtainC() {
        return Curtain_C;
    }

    public void setCurtainC(List<Integer> curtainC) {
        this.Curtain_C = curtainC;
    }

    @Override
    public String toString() {
        return "CurtainBean{" +
                "Curtain_S=" + Curtain_S +
                ", Curtain_C=" + Curtain_C +
                '}';
    }
}
