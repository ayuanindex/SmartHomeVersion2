package com.realmax.smarthomeversion2.audio;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.tencent.aai.AAIClient;
import com.tencent.aai.audio.data.AudioRecordDataSource;
import com.tencent.aai.auth.AbsCredentialProvider;
import com.tencent.aai.auth.LocalCredentialProvider;
import com.tencent.aai.config.ClientConfiguration;
import com.tencent.aai.exception.ClientException;
import com.tencent.aai.listener.AudioRecognizeResultListener;
import com.tencent.aai.listener.AudioRecognizeStateListener;
import com.tencent.aai.listener.AudioRecognizeTimeoutListener;
import com.tencent.aai.log.AAILogger;
import com.tencent.aai.model.AudioRecognizeRequest;
import com.tencent.aai.model.type.AudioRecognizeConfiguration;
import com.tencent.aai.model.type.AudioRecognizeTemplate;
import com.tencent.aai.model.type.EngineModelType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ayuan
 */
public class VoiceToMessage {
    private static final String TAG = "VoiceToMessage";
    private static VoiceToMessage voiceToMessage;
    private int appId = 1301676932;
    private int projectId = 0;
    private String secretId = "AKIDYqrzrcNJHyjEagH3M4WbRWLsCJNBB3D8";
    private String secretKey = "mIXEfKjz0sVstdQ2VjhPqAMSIwgCTSAc";

    private static final Logger logger = LoggerFactory.getLogger(VoiceToMessage.class);

    private LinkedHashMap<String, String> resMap = new LinkedHashMap<>();
    private int currentRequestId = 0;
    private AAIClient aaiClient;
    private AudioRecognizeResultListener audioRecognizeResultlistener = null;
    private AudioRecognizeStateListener audioRecognizeStateListener = null;
    private AudioRecognizeTimeoutListener audioRecognizeTimeoutListener = null;

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            4,
            10,
            1000,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1),
            r -> {
                Thread thread = new Thread(r);
                thread.setName("TEST");
                return thread;
            });

    /**
     * 实时监听
     */
    private boolean realTimeMonitoring;
    private AbsCredentialProvider credentialProvider;
    private AudioRecognizeRequest audioRecognizeRequest;
    private AudioRecognizeConfiguration audioRecognizeConfiguration;
    private Context context;

    public VoiceToMessage(boolean realTimeMonitoring) {
        this.realTimeMonitoring = realTimeMonitoring;
    }

    public VoiceToMessage(int appId, int projectId, String secretId, String secretKey, boolean realTimeMonitoring) {
        this.appId = appId;
        this.projectId = projectId;
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.realTimeMonitoring = realTimeMonitoring;
    }

    public static VoiceToMessage getInstance() {
        if (voiceToMessage == null) {
            synchronized (VoiceToMessage.class) {
                if (voiceToMessage == null) {
                    voiceToMessage = new VoiceToMessage(true);
                }
            }
        }
        return voiceToMessage;
    }


    /**
     * 初始化
     */
    public void init(Context context,
                     final AudioRecognizeResultListener audioRecognizeResultlistener,
                     final AudioRecognizeStateListener audioRecognizeStateListener,
                     final AudioRecognizeTimeoutListener audioRecognizeTimeoutListener) {
        this.context = context;
        // 签名鉴权类，sdk中给出了一个本地的鉴权类，但由于需要用户提供secretKey，这可能会导致一些安全上的问题，
        // 因此，请用户自行实现CredentialProvider接口
        credentialProvider = new LocalCredentialProvider(secretKey);

        // 用户配置
        // 是否启用https，默认启用
        ClientConfiguration.setServerProtocolHttps(false);
        // 语音识别的请求的最大并发数
        ClientConfiguration.setMaxAudioRecognizeConcurrentNumber(10);
        // 单个请求的分片最大并发数
        ClientConfiguration.setMaxRecognizeSliceConcurrentNumber(10);

        setAudioRecognizeResultlistener(audioRecognizeResultlistener);

        setAudioRecognizeStateListener(audioRecognizeStateListener);

        setAudioRecognizeTimeoutListener(audioRecognizeTimeoutListener);

        if (realTimeMonitoring) {
            startVoice();
        }
    }

    /**
     * 识别结果回调监听器
     *
     * @param audioRecognizeResultlistener 音频识别结果侦听器
     */
    private void setAudioRecognizeResultlistener(AudioRecognizeResultListener audioRecognizeResultlistener) {
        this.audioRecognizeResultlistener = audioRecognizeResultlistener;
    }

    /**
     * 识别状态监听器
     *
     * @param audioRecognizeStateListener 音频识别状态监听器
     */
    private void setAudioRecognizeStateListener(AudioRecognizeStateListener audioRecognizeStateListener) {
        this.audioRecognizeStateListener = audioRecognizeStateListener;
    }

    /**
     * 识别超时监听器
     *
     * @param audioRecognizeTimeoutListener 音频识别超时侦听器
     */
    private void setAudioRecognizeTimeoutListener(AudioRecognizeTimeoutListener audioRecognizeTimeoutListener) {
        this.audioRecognizeTimeoutListener = audioRecognizeTimeoutListener;
    }

    /**
     * 开始录音
     */
    void startVoice() {
        AudioRecognizeRequest.Builder builder = new AudioRecognizeRequest.Builder();

        // 初始化识别请求
        audioRecognizeRequest = builder
                // 设置数据源
                .pcmAudioDataSource(new AudioRecordDataSource())
                // 设置模板
                /*.templateName("customer")*/
                // 设置自定义模板
                .template(new AudioRecognizeTemplate(EngineModelType.EngineModelType16K, 0, 0))
                .build();

        // 自定义识别配置
        audioRecognizeConfiguration = new AudioRecognizeConfiguration.Builder()
                // 是否使能起点超时停止录音
                .enableAudioStartTimeout(true)
                // 是否使能终点超时停止录音
                .enableAudioEndTimeout(true)
                // 是否使能静音检测，true表示不检查静音部分
                .enableSilentDetect(true)
                // 语音流识别时的间隔时间
                .minAudioFlowSilenceTime(1000)
                // 语音终点超时时间
                .maxAudioFlowSilenceTime(10000)
                // 语音起点超时时间
                .maxAudioStartSilenceTime(10000)
                // 音量回调时间
                .minVolumeCallbackTime(80)
                .sensitive(1)
                .build();

        if (aaiClient == null) {
            try {
                aaiClient = new AAIClient(context, appId, projectId, secretId, credentialProvider);
            } catch (ClientException e) {
                e.printStackTrace();
                AAILogger.info(logger, e.toString());
            }
        }

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (audioRecognizeResultlistener == null) {
                    Log.d(TAG, "startVoice: 请设置audioRecognizeResultListener监听");
                    return;
                }

                if (audioRecognizeStateListener == null) {
                    Log.d(TAG, "startVoice: 请设置audioRecognizeStateListener监听");
                }

                if (audioRecognizeTimeoutListener == null) {
                    Log.d(TAG, "startVoice: 请设置audioRecognizeTimeoutListener监听");
                }

                if (audioRecognizeStateListener != null && audioRecognizeTimeoutListener != null) {
                    aaiClient.startAudioRecognize(
                            audioRecognizeRequest,
                            audioRecognizeResultlistener,
                            audioRecognizeStateListener,
                            audioRecognizeTimeoutListener,
                            audioRecognizeConfiguration);
                }
            }
        });
    }

    /**
     * 停止录音
     */
    public void stopVoice() {
        AAILogger.info(logger, "stop button is clicked..");
        threadPoolExecutor.execute(() -> {
            boolean taskExist = false;
            if (aaiClient != null) {
                taskExist = aaiClient.stopAudioRecognize(currentRequestId);
            }

            if (!taskExist) {
                Log.d(TAG, "run: 识别状态：不存在该任务，无法停止");
            }
        });
    }

    /**
     * 取消
     */
    public void cancelVoice() {
        threadPoolExecutor.execute(() -> {
            boolean taskExist = false;
            if (aaiClient != null) {
                taskExist = aaiClient.cancelAudioRecognize(currentRequestId);
            }

            if (!taskExist) {
                Log.d(TAG, "run: 识别状态：不存在该任务，无法取消");
            }
        });
    }

    /**
     * 设置是否始终实时语音识别
     *
     * @param realTimeMonitoring true标示不会暂停识别，false标示手动开启暂停或启动
     */
    public void setRealTimeMonitoring(boolean realTimeMonitoring) {
        this.realTimeMonitoring = realTimeMonitoring;
    }

    public void close() {
        if (aaiClient != null) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    aaiClient.release();
                }
            });
        }
    }

    public void setMic(boolean isStart) {
        AudioManager audioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        audioManager.setMicrophoneMute(isStart);
    }
}