package com.realmax.smarthomeversion2.audio;

import android.util.Log;

import com.tencent.qcloudtts.LongTextTTS.LongTextTtsController;
import com.tencent.qcloudtts.callback.QCloudPlayerCallback;
import com.tencent.qcloudtts.exception.TtsException;
import com.tencent.qcloudtts.exception.TtsNotInitializedException;

/**
 * @author ayuan
 */
public class SpeechMessage {
    private static final String TAG = "Text2Voice";
    private static SpeechMessage speechMessage;

    /**
     * 语速
     */
    private int voiceSpeed = 0;

    /**
     * 语音
     */
    private int voiceType = 5;

    /**
     * 语言
     */
    private int language = 1;
    private LongTextTtsController longTextTtsController;

    public SpeechMessage() {
    }

    public SpeechMessage(int voiceSpeed, int voiceType, int language) {
        this.voiceSpeed = voiceSpeed;
        this.voiceType = voiceType;
        this.language = language;
    }

    /**
     * 在使用云API之前，请前往 腾讯云API密钥页面 申请安全凭证。 安全凭证包括 SecretId 和 SecretKey
     *
     * @param appId     appId
     * @param secretId  用于标识 API 调用者身份
     * @param secretKey 用于加密签名字符串和服务器端验证签名字符串的密钥
     * @return 返回已经初始化的LongTextTtsController
     */
    public SpeechMessage initLongTextTtsController(long appId, String secretId, String secretKey) {
        if (longTextTtsController == null) {
            longTextTtsController = new LongTextTtsController();
        }
        longTextTtsController.init(appId, secretId, secretKey);
        // 设置语速
        longTextTtsController.setVoiceSpeed(voiceSpeed);

        // 设置音色
        longTextTtsController.setVoiceType(voiceType);

        // 设置语言
        longTextTtsController.setVoiceLanguage(language);

        // 设置ProjectId
        longTextTtsController.setProjectId(0);
        return speechMessage;
    }

    public LongTextTtsController getLongTextTtsController() {
        return longTextTtsController;
    }

    public static SpeechMessage getInstance() {
        if (speechMessage == null) {
            synchronized (SpeechMessage.class) {
                if (speechMessage == null) {
                    speechMessage = new SpeechMessage();
                }
            }
        }
        return speechMessage;
    }

    public void start(String str, ResultData resultData) {
        try {
            if (longTextTtsController != null) {
                longTextTtsController.startTts(str, (TtsException e) -> Log.e(TAG, "start: 问题---------", e), new QCloudPlayerCallback() {
                    @Override
                    public void onTTSPlayStart() {
                        Log.e(TAG, "在TTS Play开始");
                        // TODO: 2020/5/16
                        VoiceToMessage.getInstance().setMic(true);
                    }

                    @Override
                    public void onTTSPlayWait() {
                        Log.e(TAG, "在TTS播放等待中");
                    }

                    @Override
                    public void onTTSPlayResume() {
                        Log.e(TAG, "在TTS播放履历上");
                    }

                    @Override
                    public void onTTSPlayNext() {
                        Log.e(TAG, "在TTS播放下一步");
                    }

                    @Override
                    public void onTTSPlayStop() {
                        Log.e(TAG, "在TTS播放停止");
                    }

                    @Override
                    public void onTTSPlayEnd() {
                        Log.e(TAG, "在TTS播放结束");
                        // TODO: 2020/5/16
                        VoiceToMessage.getInstance().setMic(false);

                    }

                    @Override
                    public void onTTSPlayProgress(String s, int i) {
                        resultData.progress(s, i);
                        Log.e(TAG, "在TTS播放进度-----------" + s + ":" + i);
                    }
                });
            }
        } catch (TtsNotInitializedException e) {
            e.printStackTrace();
        }
    }

    public interface ResultData {
        void progress(String s, int i);
    }
}
