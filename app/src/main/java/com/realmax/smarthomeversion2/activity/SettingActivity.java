package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.text.TextUtils;
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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.realmax.smarthomeversion2.App;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.bean.LinkBean;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandlerBase;
import com.realmax.smarthomeversion2.tcp.NettyLinkUtil;
import com.realmax.smarthomeversion2.util.CustomerThread;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import java.util.ArrayList;

import io.netty.channel.EventLoopGroup;

/**
 * @author ayuan
 */
public class SettingActivity extends BaseActivity {
    private RelativeLayout rlBack;
    private ListView lvList;
    private ArrayList<LinkBean> linkBeans;
    private CustomerAdapter customerAdapter;
    private boolean isHaveDialog = false;

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
                L.e("哈哈哈哈");
                App.showToast("已连接");
                Snackbar snackbar = Snackbar.make(view, "是否要断开连接", BaseTransientBottomBar.LENGTH_LONG);
                snackbar.setAction("断开连接", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomerThread.poolExecutor.execute(() -> {
                            try {
                                String tag = customerAdapter.getItem(position).getTag();
                                EventLoopGroup eventLoopGroup = ValueUtil.getEventLoopGroupHashMap().get(tag);
                                if (eventLoopGroup != null) {
                                    eventLoopGroup.shutdownGracefully().sync();
                                    ValueUtil.getIsConnected().put(tag, false);
                                    ValueUtil.getHandlerHashMap().put(tag, null);
                                    uiHandler.post(() -> {
                                        customerAdapter.notifyDataSetChanged();
                                    });
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                });
                snackbar.show();
            }
        });
    }

    @Override
    protected void initData() {
        linkBeans = new ArrayList<>();
        linkBeans.add(new LinkBean("虚拟场景", "直接获取虚拟场景中的数据", "virtual"));
        // 控制所有灯光
        linkBeans.add(new LinkBean("控制器1", "可控制所有灯光", "control_01"));
        // 控制所有窗帘和1号空调
        linkBeans.add(new LinkBean("控制器2", "可控制所有窗帘和客厅的号空调", "control_02"));
        // 控制所有门和空气质量传感器
        linkBeans.add(new LinkBean("控制器3", "可控制所有门和空气质量传感器", "control_03"));
        // 控制剩下的空调以及电视和音乐
        linkBeans.add(new LinkBean("控制器4", "可控制剩下的空调以及电视和音乐", "control_04"));
        // 所有人体传感器 + 扫地机器人 + 报警器
        linkBeans.add(new LinkBean("控制器5", "可控制所有人体传感器&扫地机器人&报警器", "control_05"));

        customerAdapter = new CustomerAdapter();
        lvList.setAdapter(customerAdapter);

        // 验证已经连接的
        for (LinkBean linkBean : linkBeans) {
            if (linkBean.isConnected()) {
                CustomerHandlerBase customerHandlerBase = ValueUtil.getHandlerHashMap().get(linkBean.getTag());
                if (customerHandlerBase != null) {
                    customerHandlerBase.setCustomerCallback(new CustomerCallback() {
                        @Override
                        public void disConnected() {
                            linkBean.setConnected(false);
                            L.e("连接已经断开");
                            runOnUiThread(() -> customerAdapter.notifyDataSetChanged());
                        }

                        @Override
                        public void getResultData(String msg) {

                        }
                    });
                }
            }
        }
    }

    /**
     * 显示Dialog
     *
     * @param linkBean 需要连接的连接对象
     */
    @SuppressLint("SetTextI18n")
    private void showDialog(LinkBean linkBean) {
        if (!isHaveDialog) {
            isHaveDialog = true;
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

                CustomerThread.poolExecutor.execute(() -> {
                    CustomerHandlerBase customerHandler = new CustomerHandlerBase();
                    linkBean.connected(ip, port, customerHandler, new NettyLinkUtil.Callback() {
                        @Override
                        public void success(EventLoopGroup eventLoopGroup) {
                            runOnUiThread(() -> {
                                linkBean.setConnected(true);
                                ValueUtil.getEventLoopGroupHashMap().put(linkBean.getTag(), eventLoopGroup);
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

                            runOnUiThread(() -> {
                                customerAdapter.notifyDataSetChanged();
                                App.showToast("请检查服务端是否打开、网络是否在通畅");
                            });
                        }
                    });
                });
                alertDialog.dismiss();
            });

            viewHolder.cardCancel.setOnClickListener((View v) -> alertDialog.dismiss());
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    isHaveDialog = false;
                }
            });
            alertDialog.show();
        }
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

    /**
     * @author ayuan
     */
    private class CustomerAdapter extends BaseAdapter {
        private TextView tvLabel;
        private TextView tvDes;
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
            tvDes.setText(getItem(position).getDes());
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
            tvDes = view.findViewById(R.id.tv_des);
            tvStatus = view.findViewById(R.id.tv_status);
        }
    }

}
