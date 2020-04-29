package com.realmax.smarthomeversion2.util;

import com.realmax.smarthomeversion2.bean.TestBean;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author ayuan
 */
public class ValueUtil {

    /**
     * Netty的回调监听集合
     */
    private static HashMap<String, CustomerHandlerBase> handlerHashMap = new HashMap<>();

    /**
     * 连接状态的集合
     */
    private static HashMap<String, Boolean> isConnected = new HashMap<>();

    /*public static String getRoom() {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    }*/

    public static HashMap<String, CustomerHandlerBase> getHandlerHashMap() {
        return handlerHashMap;
    }

    public static HashMap<String, Boolean> getIsConnected() {
        return isConnected;
    }

    /**
     * 发送获取摄像头摄像数据的指令
     *
     * @param deviceId 摄像头的设备ID
     * @param angleA   横向旋转角度---默认0
     * @param angleB   纵向旋转角度---默认45
     */
    public static void sendCameraCmd(int deviceId, float angleA, float angleB) {
        CustomerHandlerBase customerHandler = getHandlerHashMap().get("camera");
        if (customerHandler == null) {
            return;
        }

        ChannelHandlerContext handlerContext = customerHandler.getHandlerContext();

        if (handlerContext == null) {
            return;
        }

        String command = "{\"cmd\": \"start\", \"deviceId\": " + deviceId + ", \"angleA\": " + angleA + ", \"angleB\": " + angleB + "}";
        /*String command = "{\"cmd\": \"start\", \"deviceType\": \"十字交叉路口\", \"deviceId\": 1, \"cameraNum\": 1}";*/
        handlerContext.writeAndFlush(Unpooled.copiedBuffer(option(EncodeAndDecode.getStrUnicode(command), (byte) 0x82)));
    }

    /**
     * 发送开启电灯或关闭电灯的指令
     *
     * @param testBean 需要修改的对象
     */
    public static void sendLightOpenOrCloseCmd(TestBean testBean) {
        CustomerHandlerBase customerHandler = getHandlerHashMap().get("light");
        if (customerHandler == null) {
            return;
        }

        ChannelHandlerContext handlerContext = customerHandler.getHandlerContext();

        if (handlerContext == null) {
            return;
        }

        HashMap<String, List<Integer>> hashMap = new HashMap<>(2);
        hashMap.put("Light_C", testBean.getLight_S());
        hashMap.put("Curtain_C", testBean.getCurtain_S());
        JSONObject jsonObject = new JSONObject(hashMap);
        String s = jsonObject.toString();
        L.e(s);
        handlerContext.writeAndFlush(Unpooled.copiedBuffer(EncodeAndDecode.getStrUnicode(s).getBytes()));
    }

    /**
     * 发送开启电灯或关闭电灯的指令
     *
     * @param testBean 需要修改的对象
     */
    public static void sendCurtainOpenOrCloseCmd(TestBean testBean) {
        CustomerHandlerBase customerHandler = getHandlerHashMap().get("light");
        /*CustomerHandler customerHandler = getHandlerHashMap().get("curtain");*/
        if (customerHandler == null) {
            return;
        }

        ChannelHandlerContext handlerContext = customerHandler.getHandlerContext();

        if (handlerContext == null) {
            return;
        }

        HashMap<String, List<Integer>> hashMap = new HashMap<>(2);
        hashMap.put("Light_C", testBean.getLight_S());
        hashMap.put("Curtain_C", testBean.getCurtain_S());
        JSONObject jsonObject = new JSONObject(hashMap);
        String s = jsonObject.toString();
        L.e(s);
        handlerContext.writeAndFlush(Unpooled.copiedBuffer(EncodeAndDecode.getStrUnicode(s).getBytes()));
    }

    public static void sendDoorCmd(String field, int door, int key, int password) {
        CustomerHandlerBase customerHandler = getHandlerHashMap().get("door");
        /*CustomerHandler customerHandler = getHandlerHashMap().get("curtain");*/
        if (customerHandler == null) {
            return;
        }

        ChannelHandlerContext handlerContext = customerHandler.getHandlerContext();

        if (handlerContext == null) {
            return;
        }

        HashMap<String, Object> childMap = new HashMap<>(1);
        if (door != -1) {
            childMap.put("door_C", door);
        }
        if (key != -1) {
            childMap.put("lock_c", key);
        }
        if (password != -1) {
            childMap.put("pass_c", password);
        }

        HashMap<String, Object> hashMap = new HashMap<>(1);
        hashMap.put(field, childMap);

        JSONObject jsonObject = new JSONObject(hashMap);
        String s = jsonObject.toString();

        L.e("msg:" + s);
        handlerContext.writeAndFlush(Unpooled.copiedBuffer(EncodeAndDecode.getStrUnicode(s).getBytes()));
    }

    /**
     * 发送停止获取摄像头拍摄信心的指令
     */
    public static void sendStopCmd() {
        try {
            CustomerHandlerBase customerHandler = getHandlerHashMap().get("camera");
            if (customerHandler == null) {
                return;
            }

            ChannelHandlerContext handlerContext = customerHandler.getHandlerContext();

            if (handlerContext == null) {
                return;
            }

            String command = "{\"cmd\": \"stop\"}";
            handlerContext.writeAndFlush(Unpooled.copiedBuffer(option(EncodeAndDecode.getStrUnicode(command), (byte) 0x02)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送获取天气信息的指令
     */
    public static void sendWeatherCmd() {
        CustomerHandlerBase customerHandler = getHandlerHashMap().get("camera");
        if (customerHandler == null) {
            return;
        }

        ChannelHandlerContext handlerContext = customerHandler.getHandlerContext();

        if (handlerContext == null) {
            return;
        }
        String command = "{\"cmd\": \"pull\"}";
        handlerContext.writeAndFlush(Unpooled.copiedBuffer(option(EncodeAndDecode.getStrUnicode(command), (byte) 0x83)));
    }

    /**
     * 将需要发送的消息加工成服务端可识别的数据
     *
     * @param command 需要发送的指令
     * @param b       版本号
     * @return 返回即将要发送的数据的byte数组
     */
    private static byte[] option(String command, byte b) {
        // 将指令转换成byte数组（此处的指令是已经转换成了Unicode编码，如果不转换长度计算会有问题）
        byte[] commandBytes = command.getBytes();
        // 这里的长度是字节长度（总长度是数据的字节长度+其他数据的长度：帧头、帧尾……）
        int size = commandBytes.length + 10;
        // 帧长度=总长度-帧头的长度（2byte）-帧尾的长度(2byte)
        int headLen = size - 4;
        // 将帧长度转换成小端模式
        byte[] lens = int2Bytesle(headLen);
        // 将需要验证的数据合并成一个byte数组
        // 将所有的参数放进去（其中帧头、协议版本号、帧尾是不变的数据）
        // 注意：需要将每个16进制的数据单独当成byte数组的一个元素，例：0xffaa -->  new byte[]{(byte) 0xff, (byte) 0xaa},需要拆分开
        byte[] combine = combine(new byte[]{(byte) 0xff, (byte) 0xaa, b}, lens, commandBytes, new byte[]{(byte) 0x00, (byte) 0xff, (byte) 0x55});
        // 进行加和校验
        int checkSum = checkSum(combine, size);
        return combine(
                new byte[]{
                        (byte) 0xff,
                        (byte) 0xaa,
                        b,
                        (byte) Integer.parseInt(Integer.toHexString(lens[0]), 16),
                        (byte) Integer.parseInt(Integer.toHexString(lens[1]), 16),
                        (byte) Integer.parseInt(Integer.toHexString(lens[2]), 16),
                        (byte) Integer.parseInt(Integer.toHexString(lens[3]), 16)
                },
                commandBytes,
                new byte[]{
                        (byte) Integer.parseInt(Integer.toHexString(checkSum), 16),
                        (byte) 0xff,
                        (byte) 0x55
                }
        );
    }

    /**
     * 加和校验
     *
     * @param bytes 需要校验的byte数组
     * @return 返回校验结果（16进制数据）
     */
    private static int checkSum(byte[] bytes, int size) {
        int cs = 0;
        int i = 2;
        int j = size - 3;
        while (i < j) {
            cs += bytes[i];
            i += 1;
        }
        return cs & 0xff;
    }

    /**
     * int转换为小端byte[]（高位放在高地址中）
     *
     * @param iValue 需要转换的数字
     * @return 返回小端模式的byte数组
     */
    private static byte[] int2Bytesle(int iValue) {
        byte[] rst = new byte[4];
        // 先写int的最后一个字节
        rst[0] = (byte) (iValue & 0xFF);
        // int 倒数第二个字节
        rst[1] = (byte) ((iValue & 0xFF00) >> 8);
        // int 倒数第三个字节
        rst[2] = (byte) ((iValue & 0xFF0000) >> 16);
        // int 第一个字节
        rst[3] = (byte) ((iValue & 0xFF000000) >> 24);
        return rst;
    }

    /**
     * 任意个byte数组合并
     *
     * @param bytes 多个byte数组
     * @return 发挥合并后的byte数组
     */
    private static byte[] combine(byte[]... bytes) {
        // 开始合并的位置
        int position = 0;
        // 新数组的总长度
        int length = 0;
        // 算出新数组的总长度
        for (byte[] aByte : bytes) {
            length += aByte.length;
        }
        // 创建一个新的byte数组
        byte[] ret = new byte[length];
        // 将byte数组合并成一个byte数组
        for (byte[] aByte : bytes) {
            // 参数1：待合并的数组
            // 参数2：开始合并的位置（从参数一的第n哥元素开始合并）
            // 参数3：合并的目标数组
            // 参数4：在目标数组的开始位置
            // 参数5：<=参数一的长度（这里取值为参数一的总长度相当于参数一的所有元素）
            System.arraycopy(aByte, 0, ret, position, aByte.length);
            // 计算合并下一个数组在新数组中的开始位置
            position += aByte.length;
        }
        return ret;
    }

    /*
      主动关闭连接
     */
    /*public static void closeLink() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    if (cameraEventExecutors != null) {
                        sendStopCmd();
                        cameraConnected = false;
                        cameraEventExecutors.shutdownGracefully().sync();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }*/
}
