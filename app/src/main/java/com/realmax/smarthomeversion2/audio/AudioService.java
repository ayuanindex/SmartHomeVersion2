package com.realmax.smarthomeversion2.audio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.realmax.smarthomeversion2.App;
import com.tencent.aai.exception.ClientException;
import com.tencent.aai.exception.ServerException;
import com.tencent.aai.listener.AudioRecognizeResultListener;
import com.tencent.aai.listener.AudioRecognizeStateListener;
import com.tencent.aai.listener.AudioRecognizeTimeoutListener;
import com.tencent.aai.model.AudioRecognizeRequest;
import com.tencent.aai.model.AudioRecognizeResult;

import java.util.HashMap;
import java.util.Map;

public class AudioService extends Service {
    private static final String TAG = "AudioService";
    private HashMap<String, String> hashMap = new HashMap<>();
    private VoiceToMessage voiceToMessage;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: 服务创建");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: 服务启动");

        voiceToMessage = VoiceToMessage.getInstance();

        voiceToMessage.init(getApplicationContext(), new AudioRecognizeResultListener() {
            @Override
            public void onSliceSuccess(AudioRecognizeRequest request, AudioRecognizeResult result, int order) {
                String text = result.getText();
                if (!TextUtils.isEmpty(text)) {
                    hashMap.put(String.valueOf(order), text);
                    Log.d(TAG, "onSliceSuccess: " + text);

                    // 返回识别好的文字
                    AudioControl audioControl = App.getAudioControl();
                    if (audioControl != null) {
                        audioControl.onSliceSuccess(text);
                    }
                }
            }

            @Override
            public void onSegmentSuccess(AudioRecognizeRequest request, AudioRecognizeResult result, int order) {
                String str = buildMessage(hashMap);
                hashMap.clear();
                Log.d(TAG, "onSegmentSuccess: " + str);
                if (!TextUtils.isEmpty(str)) {
                    AudioControl audioControl = App.getAudioControl();
                    String regex = ".*小[微|薇]你好.*";//定义一个正则表达式
                    if (str.matches(regex)) {
                        if (audioControl == null) {
                            Log.d(TAG, "onSegmentSuccess: 嘻嘻嘻");
                            Intent intent = new Intent(getApplicationContext(), CommendActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            audioControl.onSegmentSuccess(str);
                        }
                    } else {
                        if (audioControl != null) {
                            audioControl.onSegmentSuccess(str);
                        }
                    }
                }
            }

            @Override
            public void onSuccess(AudioRecognizeRequest request, String result) {
                Log.d(TAG, "onSuccess: " + result);
            }

            @Override
            public void onFailure(AudioRecognizeRequest request, ClientException
                    clientException, ServerException serverException) {
                Log.d(TAG, "onFailure: 出现错误");
            }
        }, new AudioRecognizeStateListener() {
            @Override
            public void onStartRecord(AudioRecognizeRequest request) {
                Log.d(TAG, "onStartRecord: 开始识别");
            }

            @Override
            public void onStopRecord(AudioRecognizeRequest request) {
                Log.d(TAG, "onStopRecord: 停止识别");
                voiceToMessage.startVoice();
            }

            @Override
            public void onVoiceFlowStart(AudioRecognizeRequest request, int seq) {
                Log.d(TAG, "onVoiceFlowStart: 在语音流开始时");
            }

            @Override
            public void onVoiceFlowFinish(AudioRecognizeRequest request, int seq) {
                Log.d(TAG, "onVoiceFlowFinish: 语音流完成");
            }

            @Override
            public void onVoiceFlowStartRecognize(AudioRecognizeRequest request, int seq) {
                Log.d(TAG, "onVoiceFlowStartRecognize: 语音流开始识别");
            }

            @Override
            public void onVoiceFlowFinishRecognize(AudioRecognizeRequest request, int seq) {
                Log.d(TAG, "onVoiceFlowFinishRecognize: 语音流完成识别");
            }

            @Override
            public void onVoiceVolume(AudioRecognizeRequest request, int volume) {
            }
        }, new AudioRecognizeTimeoutListener() {
            @Override
            public void onFirstVoiceFlowTimeout(AudioRecognizeRequest request) {
                Log.d(TAG, "onFirstVoiceFlowTimeout: 第一个语音流超时");
            }

            @Override
            public void onNextVoiceFlowTimeout(AudioRecognizeRequest request) {
                Log.d(TAG, "onNextVoiceFlowTimeout: 一个语音流超时");
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String buildMessage(HashMap<String, String> hashMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> stringStringEntry : hashMap.entrySet()) {
            stringBuilder.append(stringStringEntry.getValue());
        }
        return stringBuilder.toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ::::::::::::服务被销毁");
        // 服务销毁时关闭语音
        if (voiceToMessage != null) {
            voiceToMessage.close();
        }
    }
}
