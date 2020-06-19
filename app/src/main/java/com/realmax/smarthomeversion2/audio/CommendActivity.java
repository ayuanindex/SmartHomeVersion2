package com.realmax.smarthomeversion2.audio;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.ArrayList;

public class CommendActivity extends BaseActivity {
    private static final String TAG = "CommendActivity";
    private CardView cardVoice;
    private RecyclerView rcList;
    private CustomerAdapter customerAdapter;
    private ArrayList<MessageBean> messageBeans;
    private TextView tvMessage;
    private AudioControl audioControl;
    private ImageView ivVoice;
    private boolean isStart = true;

    @Override
    protected int getLayout() {
        return R.layout.activity_commend;
    }

    @Override
    protected void initView() {
        cardVoice = findViewById(R.id.card_voice);
        rcList = findViewById(R.id.rc_list);
        tvMessage = findViewById(R.id.tv_message);
        ivVoice = (ImageView) findViewById(R.id.iv_voice);
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void initData() {
        messageBeans = new ArrayList<>();
        messageBeans.add(0, new MessageBean("哈喽啊远!", R.layout.item_left_message));

        rcList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        customerAdapter = new CustomerAdapter();
        rcList.setAdapter(customerAdapter);

        audioControl = new AudioControl(this, messageBeans, customerAdapter) {
            @Override
            public void onSliceSuccess(String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvMessage.setVisibility(View.VISIBLE);
                        tvMessage.setText(msg);
                    }
                });
            }

            @Override
            public void onSuccessString(String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d(TAG, "onSuccessString: 隐藏文字");
                        tvMessage.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void updateItem() {
                customerAdapter.notifyDataSetChanged();
                rcList.scrollToPosition(messageBeans.size() - 1);
            }
        };
        App.setAudioControl(audioControl);
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
         * 普通的布局
         */
        class MessageViewHolder extends RecyclerView.ViewHolder {
            View rootView;
            TextView tvMessage;

            MessageViewHolder(View rootView) {
                super(rootView);
                this.rootView = rootView;
                this.tvMessage = (TextView) rootView.findViewById(R.id.tv_message);
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

        class PutPasswordViewHolder extends RecyclerView.ViewHolder {
            View rootView;
            TextView tvMessage;
            EditText etPutPassword;
            CardView cardOk;

            PutPasswordViewHolder(View rootView) {
                super(rootView);
                this.rootView = rootView;
                this.tvMessage = (TextView) rootView.findViewById(R.id.tv_message);
                this.etPutPassword = (EditText) rootView.findViewById(R.id.et_putPassword);
                this.cardOk = rootView.findViewById(R.id.cardOk);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.setAudioControl(null);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
