package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.qcloud.iot_explorer.common.Status;
import com.qcloud.iot_explorer.data_template.TXDataTemplateDownStreamCallBack;
import com.qcloud.iot_explorer.mqtt.TXMqttActionCallBack;
import com.realmax.smarthomeversion2.App;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.bean.LinkBean;
import com.realmax.smarthomeversion2.mqtt.MqttControl;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.tcp.NettyLinkUtil;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONObject;

import java.util.ArrayList;

import io.netty.channel.EventLoopGroup;

import static com.qcloud.iot_explorer.data_template.TXDataTemplate.TAG;

/**
 * @author ayuan
 */
public class SettingActivity extends BaseActivity {
    private RelativeLayout rlBack;
    private ListView lvList;
    private ArrayList<LinkBean> linkBeans;
    private CustomerAdapter customerAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        rlBack = findViewById(R.id.rl_back);
        lvList = findViewById(R.id.lv_list);
    }

    @Override
    protected void initEvent() {
        rlBack.setOnClickListener((View v) -> finish());

        lvList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            if (!linkBeans.get(position).isConnected()) {
                showDialog(linkBeans.get(position));
            } else {
                App.showToast("已连接");
            }
        });
    }

    /**
     * 显示Dialog
     *
     * @param linkBean 需要连接的连接对象
     */
    @SuppressLint("SetTextI18n")
    private void showDialog(LinkBean linkBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        AlertDialog alertDialog = builder.create();
        View inflate = View.inflate(SettingActivity.this, R.layout.dialog_link, null);
        alertDialog.setView(inflate);
        ViewHolder viewHolder = new ViewHolder(inflate);

        // 回显IP地址和端口号
        viewHolder.etIp.setText(linkBean.getmHOST());
        viewHolder.etPort.setText("" + linkBean.getPORT());

        viewHolder.cardOk.setOnClickListener((View v) -> {
            // 验证IP是否为空看那个
            String ip = viewHolder.etIp.getText().toString().trim();
            if (TextUtils.isEmpty(ip)) {
                viewHolder.etIp.setText("ip地址不能为空");
                return;
            }

            // 验证port是否为空
            String port = viewHolder.etPort.getText().toString().trim();
            if (TextUtils.isEmpty(port)) {
                viewHolder.etIp.setText("端口号不能为空");
                return;
            }

            CustomerHandlerBase customerHandler = new CustomerHandlerBase();
            linkBean.connected(ip, port, customerHandler, new NettyLinkUtil.Callback() {
                @Override
                public void success(EventLoopGroup eventLoopGroup) {
                    // 连接MQTT
                    MqttControl mqttControl = ValueUtil.getMqttControlHashMap().get(linkBean.getTag());
                    if (mqttControl != null) {
                        mqttControl.connected();
                    }

                    runOnUiThread(() -> {
                        linkBean.setConnected(true);
                        runOnUiThread(() -> customerAdapter.notifyDataSetChanged());
                    });
                }

                @Override
                public void error() {
                    linkBean.setConnected(false);
                    CustomerCallback customerCallback = customerHandler.getCustomerCallback();
                    if (customerCallback != null) {
                        customerCallback.disConnected();
                    }

                    // 断开MQTT连接
                    MqttControl mqttControl = ValueUtil.getMqttControlHashMap().get(linkBean.getTag());
                    if (mqttControl != null) {
                        mqttControl.disConnected();
                    }

                    runOnUiThread(() -> {
                        customerAdapter.notifyDataSetChanged();
                        App.showToast("请检查服务端是否打开、网络是否在通畅");
                    });
                }
            });

            alertDialog.dismiss();
        });

        viewHolder.cardCancel.setOnClickListener((View v) -> alertDialog.dismiss());

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        alertDialog.show();
    }

    static class ViewHolder {
        View rootView;
        EditText etIp;
        EditText etPort;
        CardView cardCancel;
        CardView cardOk;

        ViewHolder(View rootView) {
            this.rootView = rootView;
            this.etIp = rootView.findViewById(R.id.et_ip);
            this.etPort = rootView.findViewById(R.id.et_port);
            this.cardCancel = rootView.findViewById(R.id.cardCancel);
            this.cardOk = rootView.findViewById(R.id.cardOk);
        }
    }

    @Override
    protected void initData() {
        linkBeans = new ArrayList<>();
        linkBeans.add(new LinkBean("电灯&窗帘", "light"));
        linkBeans.add(new LinkBean("门", "door"));
        linkBeans.add(new LinkBean("摄像头", "camera"));

        customerAdapter = new CustomerAdapter();
        lvList.setAdapter(customerAdapter);
    }

    /**
     * @author ayuan
     */
    private class CustomerAdapter extends BaseAdapter {
        private TextView tvLabel;
        private TextView tvStatus;

        @Override
        public int getCount() {
            return linkBeans.size();
        }

        @Override
        public LinkBean getItem(int position) {
            return linkBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(SettingActivity.this, R.layout.item_setting, null);
            } else {
                view = convertView;
            }
            initView(view);

            tvLabel.setText(getItem(position).getLabel());
            boolean connected = getItem(position).isConnected();
            L.e(connected + "");
            if (connected) {
                tvStatus.setText("已连接");
            } else {
                tvStatus.setText("未连接");
            }
            return view;
        }

        private void initView(View view) {
            tvLabel = view.findViewById(R.id.tv_label);
            tvStatus = view.findViewById(R.id.tv_status);
        }
    }

}
