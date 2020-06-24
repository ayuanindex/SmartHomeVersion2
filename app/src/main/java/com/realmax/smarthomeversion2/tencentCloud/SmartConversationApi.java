package com.realmax.smarthomeversion2.tencentCloud;

import com.realmax.smarthomeversion2.network.HttpUtil;
import com.realmax.smarthomeversion2.tencentCloud.bean.InterlocutionBean;
import com.realmax.smarthomeversion2.util.CustomerThread;

import java.util.TreeMap;

public class SmartConversationApi {
    /**
     * 获取人员库列表
     *
     * @param result  请求回调
     * @param message 需要发送的消息
     */
    public static void interlocution(String message, HttpUtil.Result<InterlocutionBean> result) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                TreeMap<String, Object> params = new TreeMap<>();
                // 公共参数
                params.put("Action", "TextProcess");
                // 公共参数
                params.put("Version", "2019-06-27");
                params.put("BotId", "3c1f934f-5655-4a8d-888c-721e8a3b4d13");
                params.put("BotEnv", "dev");
                params.put("TerminalId", "1");
                params.put("InputText", message);
                TencentCloudAPIInitUtil.setIP("tbp.tencentcloudapi.com");
                TreeMap<String, Object> init = TencentCloudAPIInitUtil.init(params);
                HttpUtil.doPost(init, InterlocutionBean.class, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
