package com.realmax.smarthomeversion2;

import com.realmax.smarthomeversion2.bean.LinkBean;
import com.realmax.smarthomeversion2.tcp.HexStringHandler;
import com.realmax.smarthomeversion2.tcp.ReceiveJsonHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Constant {
    public static Map<String, LinkBean> linkBeanMap = new HashMap<>();

    public static ArrayList<String> tags = new ArrayList<>();

    public static LinkBean getLinkBeanByTag(String tag) {
        return linkBeanMap.get(tag);
    }

    public static void putLinkBean(String tag, LinkBean linkBean) {
        linkBeanMap.put(tag, linkBean);
    }

    public static Map<String, LinkBean> getLinkBeanMap() {
        return linkBeanMap;
    }

    public static ArrayList<String> getTags() {
        return tags;
    }

    public static void init() {
        tags.add("virtual");
        tags.add("control_01");
        tags.add("control_02");
        tags.add("control_03");
        tags.add("control_04");
        tags.add("control_05");

        // 虚拟场景连接
        Constant.getLinkBeanMap().put(tags.get(0), new LinkBean("虚拟场景", "直接获取虚拟场景中的数据", tags.get(0), HexStringHandler.class));
        // 控制所有灯光
        Constant.getLinkBeanMap().put(tags.get(1), new LinkBean("控制器1", "可控制所有灯光", tags.get(1), ReceiveJsonHandler.class));
        // 控制所有窗帘和1号空调
        Constant.getLinkBeanMap().put(tags.get(2), new LinkBean("控制器2", "可控制所有窗帘和客厅的号空调", tags.get(2), ReceiveJsonHandler.class));
        // 控制所有门和空气质量传感器
        Constant.getLinkBeanMap().put(tags.get(3), new LinkBean("控制器3", "可控制所有门和空气质量传感器", tags.get(3), ReceiveJsonHandler.class));
        // 控制剩下的空调以及电视和音乐
        Constant.getLinkBeanMap().put(tags.get(4), new LinkBean("控制器4", "可控制剩下的空调以及电视和音乐", tags.get(4), ReceiveJsonHandler.class));
        // 所有人体传感器 + 扫地机器人 + 报警器
        Constant.getLinkBeanMap().put(tags.get(5), new LinkBean("控制器5", "可控制所有人体传感器&扫地机器人&报警器", tags.get(5), ReceiveJsonHandler.class));
    }
}
