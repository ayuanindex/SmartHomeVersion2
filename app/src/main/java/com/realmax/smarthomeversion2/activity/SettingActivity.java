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
import com.realmax.smarthomeversion2.Constant;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.bean.LinkBean;
import com.realmax.smarthomeversion2.tcp.CustomerHandler;
import com.realmax.smarthomeversion2.util.CustomerThread;
import com.realmax.smarthomeversion2.util.L;

/**
 * @author ayuan
 */
public class SettingActivity extends BaseActivity {
    private RelativeLayout rlBack;
    private ListView lvList;
    private CustomerAdapter customerAdapter;
    private boolean dialogIsClose = true;

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
            LinkBean item = customerAdapter.getItem(position);
            if (item.getLinkStatus()) {
                L.e("哈哈哈哈");
                App.showToast("已连接");
                Snackbar snackbar = Snackbar.make(view, "是否要断开连接", BaseTransientBottomBar.LENGTH_LONG);
                snackbar.setAction("断开连接", (View view1) -> CustomerThread.poolExecutor.execute(() -> {
                    item.closeTheConnection();
                    uiHandler.post(() -> customerAdapter.notifyDataSetChanged());
                }));
                snackbar.show();
            } else {
                showDialog(item);
            }
        });
    }

    @Override
    protected void initData() {
        customerAdapter = new CustomerAdapter();
        lvList.setAdapter(customerAdapter);
    }

    /**
     * 显示Dialog
     *
     * @param linkBean 需要连接的连接对象
     */
    @SuppressLint("SetTextI18n")
    private void showDialog(LinkBean linkBean) {
        if (dialogIsClose) {
            dialogIsClose = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            AlertDialog alertDialog = builder.create();
            View inflate = View.inflate(SettingActivity.this, R.layout.dialog_link, null);
            alertDialog.setView(inflate);
            ViewHolder viewHolder = new ViewHolder(inflate);

            // 回显IP地址和端口号
            viewHolder.etIp.setText(linkBean.getLinkIpBySp());
            viewHolder.etPort.setText(String.valueOf(linkBean.getLinkPortBySp()));

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
                    linkBean.setLinkIp(ip);
                    linkBean.setLinkPort(Integer.parseInt(port));
                    linkBean.startConnected(new CustomerHandler(), new LinkBean.ConnectedStatus() {
                        @Override
                        public void success() {
                            runOnUiThread(() -> runOnUiThread(() -> customerAdapter.notifyDataSetChanged()));
                        }

                        @Override
                        public void error() {
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

            alertDialog.setOnDismissListener((DialogInterface dialogInterface) -> dialogIsClose = true);
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
            return Constant.getTags().size();
        }

        @Override
        public LinkBean getItem(int position) {
            return Constant.getLinkBeanMap().get(Constant.getTags().get(position));
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

            tvLabel.setText(getItem(position).getLinkName());
            tvDes.setText(getItem(position).getLinkDes());
            tvStatus.setText(getItem(position).getLinkStatus() ? "已连接" : "未连接");
            return view;
        }

        private void initView(View view) {
            tvLabel = view.findViewById(R.id.tv_label);
            tvDes = view.findViewById(R.id.tv_des);
            tvStatus = view.findViewById(R.id.tv_status);
        }
    }

}
