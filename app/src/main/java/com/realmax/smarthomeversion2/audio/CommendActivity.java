package com.realmax.smarthomeversion2.audio;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.realmax.smarthomeversion2.App;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.activity.BaseActivity;
import com.realmax.smarthomeversion2.bean.MessageBean;
import com.realmax.smarthomeversion2.network.HttpUtil;
import com.realmax.smarthomeversion2.tencentCloud.SmartConversationApi;
import com.realmax.smarthomeversion2.tencentCloud.bean.InterlocutionBean;
import com.realmax.smarthomeversion2.util.CustomerThread;
import com.realmax.smarthomeversion2.util.L;
import com.tencent.aai.exception.ClientException;
import com.tencent.aai.exception.ServerException;
import com.tencent.aai.listener.AudioRecognizeResultListener;
import com.tencent.aai.listener.AudioRecognizeStateListener;
import com.tencent.aai.listener.AudioRecognizeTimeoutListener;
import com.tencent.aai.model.AudioRecognizeRequest;
import com.tencent.aai.model.AudioRecognizeResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class CommendActivity extends BaseActivity {
    private static final String TAG = "CommendActivity";
    private CardView cardVoice;
    private HashMap<String, String> hashMap = new HashMap<>();
    private RecyclerView rcList;
    private CustomerAdapter customerAdapter;
    private ArrayList<MessageBean> messageBeans;
    private EditText etMessage;
    private AudioControl audioControl;
    private ImageView ivVoice;
    private boolean isVoice = true;
    private VoiceToMessage voiceToMessage;

    @Override
    protected int getLayout() {
        return R.layout.activity_commend;
    }

    @Override
    protected void initView() {
        cardVoice = findViewById(R.id.card_voice);
        rcList = findViewById(R.id.rc_list);
        etMessage = findViewById(R.id.et_message);
        ivVoice = findViewById(R.id.iv_voice);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initEvent() {
        ivVoice.setOnTouchListener((View v, MotionEvent event) -> {
            if (isVoice) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        etMessage.setText("");
                        etMessage.setHint("正在聆听");
                        voiceToMessage.startVoice();
                        break;
                    case MotionEvent.ACTION_UP:
                        etMessage.setText("");
                        /*voiceToMessage.cancelVoice();*/
                        voiceToMessage.stopVoice();
                        etMessage.setHint("长按说话");
                        L.e("手指抬起");
                        break;
                }
            } else {
                String message = etMessage.getText().toString().trim();
                messageBeans.add(new MessageBean(message, R.layout.item_right_message));
                updateList();
                sendConversationRequest(message);
                isVoice = true;
                ivVoice.setFocusable(true);
                etMessage.setText("");
                etMessage.setHint("长按说话");
            }
            return true;
        });

        etMessage.setOnTouchListener((View v, MotionEvent event) -> {
            isVoice = false;
            return false;
        });
    }

    @Override
    protected void initData() {
        messageBeans = new ArrayList<>();
        messageBeans.add(0, new MessageBean("哈喽啊远!", R.layout.item_left_message));

        rcList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        customerAdapter = new CustomerAdapter();
        rcList.setAdapter(customerAdapter);

        voiceToMessage = VoiceToMessage.getInstance();

        voiceToMessage.init(this, new AudioRecognizeResultListener() {
            @Override
            public void onSliceSuccess(AudioRecognizeRequest audioRecognizeRequest, AudioRecognizeResult audioRecognizeResult, int i) {
                String text = audioRecognizeResult.getText();
                if (!TextUtils.isEmpty(text)) {
                    hashMap.put(String.valueOf(i), text);
                    L.e("onSliceSuccess: " + text);
                    uiHandler.post(() -> {
                        etMessage.setHint(text);
                    });
                }
            }

            @Override
            public void onSegmentSuccess(AudioRecognizeRequest audioRecognizeRequest, AudioRecognizeResult audioRecognizeResult, int i) {
                String str = buildMessage(hashMap);
                hashMap.clear();
                Log.d(TAG, "onSegmentSuccess: " + str);
                if (!TextUtils.isEmpty(str)) {
                    messageBeans.add(new MessageBean(str, R.layout.item_right_message));
                    // 发送对话请求
                    sendConversationRequest(str);
                    uiHandler.post(() -> {
                        etMessage.setText(str);
                        updateList();
                        etMessage.setText("");
                        etMessage.setHint("长按说话");
                    });
                    voiceToMessage.stopVoice();
                }
            }

            @Override
            public void onSuccess(AudioRecognizeRequest audioRecognizeRequest, String s) {
                L.e("onSuccess: " + s);
            }

            @Override
            public void onFailure(AudioRecognizeRequest audioRecognizeRequest, ClientException e, ServerException e1) {
                L.e("onFailure: 出现错误");
            }
        }, new AudioRecognizeStateListener() {
            @Override
            public void onStartRecord(AudioRecognizeRequest audioRecognizeRequest) {
                L.e("onStartRecord: 开始识别");
            }

            @Override
            public void onStopRecord(AudioRecognizeRequest audioRecognizeRequest) {
                L.e("onStopRecord: 停止识别");
            }

            @Override
            public void onVoiceFlowStart(AudioRecognizeRequest audioRecognizeRequest, int i) {
                L.e("onVoiceFlowStart: 在语音流开始时");
            }

            @Override
            public void onVoiceFlowFinish(AudioRecognizeRequest audioRecognizeRequest, int i) {
                L.e("onVoiceFlowFinish: 语音流完成");
            }

            @Override
            public void onVoiceFlowStartRecognize(AudioRecognizeRequest audioRecognizeRequest, int i) {
                L.e("onVoiceFlowStartRecognize: 语音流开始识别");
            }

            @Override
            public void onVoiceFlowFinishRecognize(AudioRecognizeRequest audioRecognizeRequest, int i) {
                L.e("onVoiceFlowFinishRecognize: 语音流完成识别");
            }

            @Override
            public void onVoiceVolume(AudioRecognizeRequest audioRecognizeRequest, int i) {

            }
        }, new AudioRecognizeTimeoutListener() {
            @Override
            public void onFirstVoiceFlowTimeout(AudioRecognizeRequest audioRecognizeRequest) {
                L.e("onFirstVoiceFlowTimeout: 第一个语音流超时");
            }

            @Override
            public void onNextVoiceFlowTimeout(AudioRecognizeRequest audioRecognizeRequest) {
                L.e("onNextVoiceFlowTimeout: 一个语音流超时");
            }
        });
    }

    private void updateList() {
        customerAdapter.notifyDataSetChanged();
        rcList.scrollToPosition(messageBeans.size() - 1);
    }

    /**
     * 发送对话请求
     *
     * @param str 消息
     */
    private void sendConversationRequest(String str) {
        CustomerThread.poolExecutor.execute(() -> {
            SmartConversationApi.interlocution(str, new HttpUtil.Result<InterlocutionBean>() {
                @Override
                public void getData(InterlocutionBean interlocutionBean, Call call, Response response) {
                    if (interlocutionBean.getResponse().getError() != null) {
                        L.e("请求成功，返回错误信息------" + interlocutionBean.getResponse().getError().toString());
                        uiHandler.post(() -> {
                            String message = interlocutionBean.getResponse().getError().getMessage();
                            messageBeans.add(new MessageBean(message, R.layout.item_left_message));
                            updateList();
                            feedBack(message);
                        });
                    } else {
                        L.e("请求成功,返回正确信息-----------" + interlocutionBean.toString());
                        uiHandler.post(() -> {
                            String content = interlocutionBean.getResponse().getResponseMessage().getGroupList().get(0).getContent();
                            messageBeans.add(new MessageBean(content, R.layout.item_left_message));
                            updateList();
                            feedBack(content);

                            // 执行控制指令
                            execution(interlocutionBean);
                        });
                    }
                }

                @Override
                public void error(Call call, IOException e) {
                    e.printStackTrace();
                    L.e("对话请求出现错误-----------" + e.getMessage());
                }
            });
        });
    }

    /**
     * 开始执行槽位匹配上的控制指令
     *
     * @param interlocutionBean 获取到的回复
     */
    private void execution(InterlocutionBean interlocutionBean) {
        L.e("开始执行控制指令");
        // 判断腾讯智能对话平台上是否有匹配山的槽位

        // 帮我把灯开一下
        List<InterlocutionBean.ResponseBean.SlotInfoListBean> slotInfoList = interlocutionBean.getResponse().getSlotInfoList();
        if (slotInfoList.size() > 0) {
            // 有匹配上的槽位,准备控制设备
            audioControl = new AudioControl(this, messageBeans, interlocutionBean) {
                @Override
                public void onSliceSuccess(String msg) {

                }

                @Override
                public void onSuccessString(String msg) {

                }

                @Override
                public void updateItem() {

                }
            };

            audioControl.excutingAnOrder();
        }
    }

    /**
     * 语音反馈
     *
     * @param msg 反馈的语音
     */
    private void feedBack(String msg) {
        uiHandler.post(() -> {
            SpeechMessage.getInstance()
                    .initLongTextTtsController(1301676932, "AKIDYqrzrcNJHyjEagH3M4WbRWLsCJNBB3D8", "mIXEfKjz0sVstdQ2VjhPqAMSIwgCTSAc")
                    .start(msg, (s, i) -> {
                        Log.d(TAG, "run: :::::::::" + s);
                    });
        });
    }


    class CustomerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case R.layout.item_left_select_light:
                    view = View.inflate(CommendActivity.this, R.layout.item_left_select_light, null);
                    return new SelectLightViewHolder(view);
                case R.layout.item_passoword:
                    view = View.inflate(CommendActivity.this, R.layout.item_passoword, null);
                    return new PutPasswordViewHolder(view);
                case R.layout.item_right_message:
                    view = View.inflate(CommendActivity.this, R.layout.item_right_message, null);
                    return new MessageViewHolder(view);
                default:
                    view = View.inflate(CommendActivity.this, R.layout.item_left_message, null);
                    return new MessageViewHolder(view);
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MessageViewHolder) {
                loadSimpleViewHolder((MessageViewHolder) holder, position);
            } else if (holder instanceof SelectLightViewHolder) {
                loadSelectLightViewHolder((SelectLightViewHolder) holder, position);
            } else if (holder instanceof PutPasswordViewHolder) {
                loadPutPasswordViewHolder((PutPasswordViewHolder) holder, position);
            }
        }

        /**
         * 加载普通的ViewHolder
         *
         * @param holder   holder
         * @param position 当前的元素
         */
        private void loadSimpleViewHolder(@NonNull MessageViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: message");
            holder.tvMessage.setText(messageBeans.get(position).getMessage());
        }

        /**
         * 加载选择灯的ViewHolder
         *
         * @param holder   holder
         * @param position 当前的元素
         */
        private void loadSelectLightViewHolder(@NonNull SelectLightViewHolder holder, int position) {
            holder.tvMsg.setText(messageBeans.get(position).getMessage());
        }

        /**
         * 加载输入密码的ViewHolder
         *
         * @param holder   holder
         * @param position 当前的元素
         */
        private void loadPutPasswordViewHolder(@NonNull PutPasswordViewHolder holder, int position) {
            holder.tvMessage.setText(messageBeans.get(position).getMessage());
            holder.cardOk.setOnClickListener((View v) -> {
                String passwordStr = holder.etPutPassword.getText().toString().trim();
                if (TextUtils.isEmpty(passwordStr)) {
                    App.showToast("请输入密码");
                    return;
                }

                int passwordInt = Integer.parseInt(passwordStr);
                holder.etPutPassword.setText("");
                messageBeans.remove(position);
                customerAdapter.notifyDataSetChanged();
                audioControl.sendPassword(passwordInt);
            });
        }

        @Override
        public int getItemViewType(int position) {
            return messageBeans.get(position).getLayout();
        }

        @Override
        public int getItemCount() {
            return messageBeans.size();
        }

        /**
         * 普通对话布局
         */
        class MessageViewHolder extends RecyclerView.ViewHolder {
            View rootView;
            TextView tvMessage;

            MessageViewHolder(View rootView) {
                super(rootView);
                this.rootView = rootView;
                this.tvMessage = (TextView) rootView.findViewById(R.id.et_message);
            }

        }

        /**
         * 选择灯的布局
         */
        class SelectLightViewHolder extends RecyclerView.ViewHolder {
            View rootView;
            TextView tvMsg;
            ListView lvList;

            SelectLightViewHolder(View rootView) {
                super(rootView);
                this.rootView = rootView;
                this.tvMsg = rootView.findViewById(R.id.tv_msg);
                this.lvList = rootView.findViewById(R.id.lv_list);
            }
        }

        /**
         * 密码输入
         */
        class PutPasswordViewHolder extends RecyclerView.ViewHolder {
            View rootView;
            TextView tvMessage;
            EditText etPutPassword;
            CardView cardOk;

            PutPasswordViewHolder(View rootView) {
                super(rootView);
                this.rootView = rootView;
                this.tvMessage = (TextView) rootView.findViewById(R.id.et_message);
                this.etPutPassword = (EditText) rootView.findViewById(R.id.et_putPassword);
                this.cardOk = rootView.findViewById(R.id.cardOk);
            }

        }
    }

    private String buildMessage(HashMap<String, String> hashMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> stringStringEntry : hashMap.entrySet()) {
            stringBuilder.append(stringStringEntry.getValue());
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (voiceToMessage != null) {
            voiceToMessage.cancelVoice();
            voiceToMessage.stopVoice();
            voiceToMessage.close();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
