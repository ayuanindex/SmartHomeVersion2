package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.realmax.smarthomeversion2.App;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.bean.LinkBean;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.tcp.NettyLinkUtil;
import com.realmax.smarthomeversion2.util.L;

import java.util.ArrayList;

import io.netty.channel.EventLoopGroup;

public class SettingActivity extends BaseActivity {
    private RelativeLayout rl_back;
    private ListView lv_list;
    private ArrayList<LinkBean> linkBeans;
    private CustomerAdapter customerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        lv_list = (ListView) findViewById(R.id.lv_list);
    }

    @Override
    protected void initEvent() {
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!linkBeans.get(position).isConnected()) {
                    showDialog(linkBeans.get(position));
                } else {
                    App.showToast("已连接");
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showDialog(LinkBean linkBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        AlertDialog alertDialog = builder.create();
        View inflate = View.inflate(SettingActivity.this, R.layout.dialog_link, null);
        alertDialog.setView(inflate);
        ViewHolder viewHolder = new ViewHolder(inflate);

        // 回显IP地址和端口号
        viewHolder.et_ip.setText(linkBean.getHOST());
        viewHolder.et_port.setText("" + linkBean.getPORT());

        viewHolder.tv_ok.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String ip = viewHolder.et_ip.getText().toString().trim();
                if (TextUtils.isEmpty(ip)) {
                    viewHolder.et_ip.setText("ip地址不能为空");
                    return;
                }

                String port = viewHolder.et_port.getText().toString().trim();
                if (TextUtils.isEmpty(port)) {
                    viewHolder.et_ip.setText("端口号不能为空");
                    return;
                }

                /*App.showToast("正在连接");*/
                CustomerHandlerBase customerHandler = new CustomerHandlerBase();
                linkBean.connected(ip, port, customerHandler, new NettyLinkUtil.Callback() {
                    @Override
                    public void success(EventLoopGroup eventLoopGroup) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                linkBean.setConnected(true);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        customerAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void error() {
                        linkBean.setConnected(false);
                        CustomerCallback customerCallback = customerHandler.getCustomerCallback();
                        if (customerCallback != null) {
                            customerCallback.disConnected();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customerAdapter.notifyDataSetChanged();
                                App.showToast("请检查服务端是否打开、网络是否在通畅");
                            }
                        });
                    }
                });

                alertDialog.dismiss();
            }
        });

        viewHolder.tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    static class ViewHolder {
        View rootView;
        EditText et_ip;
        EditText et_port;
        TextView tv_cancel;
        TextView tv_ok;

        ViewHolder(View rootView) {
            this.rootView = rootView;
            this.et_ip = (EditText) rootView.findViewById(R.id.et_ip);
            this.et_port = (EditText) rootView.findViewById(R.id.et_port);
            this.tv_cancel = (TextView) rootView.findViewById(R.id.tv_cancel);
            this.tv_ok = (TextView) rootView.findViewById(R.id.tv_ok);
        }
    }

    @Override
    protected void initData() {
        linkBeans = new ArrayList<>();
        linkBeans.add(new LinkBean("电灯&窗帘", "light"));
        /*linkBeans.add(new LinkBean("窗帘", "curtain"));*/
        linkBeans.add(new LinkBean("摄像头", "camera"));

        customerAdapter = new CustomerAdapter();
        lv_list.setAdapter(customerAdapter);
    }

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
            tvLabel = (TextView) view.findViewById(R.id.tv_label);
            tvStatus = (TextView) view.findViewById(R.id.tv_status);
        }
    }

}
