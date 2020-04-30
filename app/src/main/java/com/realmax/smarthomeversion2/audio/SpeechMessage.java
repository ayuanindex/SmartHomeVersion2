package com.realmax.smarthomeversion2.audio;

import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;

import com.realmax.smarthomeversion2.activity.BaseActivity;

import java.util.Locale;

public class SpeechMessage {

    private static TextToSpeech textToSpeech;

    public static void initTts(String msg, BaseActivity baseActivity) {
        textToSpeech = new TextToSpeech(baseActivity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // 判断语音是否转换成功
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        // 设定语言为中文
        int result = textToSpeech.setLanguage(Locale.CHINESE);
        if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
            //不支持中文就将语言设置为英文
            textToSpeech.setLanguage(Locale.US);
        }
    }
}
