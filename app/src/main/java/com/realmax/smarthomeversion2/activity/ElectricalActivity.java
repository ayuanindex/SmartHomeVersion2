package com.realmax.smarthomeversion2.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.realmax.smarthomeversion2.Constant;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.activity.bean.AcAndTvAndMusicBean;
import com.realmax.smarthomeversion2.bean.LinkBean;
import com.realmax.smarthomeversion2.tcp.BaseNettyHandler;
import com.realmax.smarthomeversion2.tcp.CustomerCallback;
import com.realmax.smarthomeversion2.tcp.CustomerHandler;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author ayuan
 * 电器
 */
public class ElectricalActivity extends BaseActivity {
    private CheckBox cb_powerSupply;
    private CheckBox cb_mute;
    private GridView gv_numbers;
    private ArrayList<String> numbers;
    private CustomerAdapter customerAdapter;
    private RelativeLayout rl_back;
    private ImageView iv_soundAdd;
    private ImageView iv_soundReduce;
    private ImageView iv_programAdd;
    private ImageView iv_programReduce;
    private TextView tv_currentRoom;
    private String tag = "control_04";
    private AcAndTvAndMusicBean acAndTvAndMusicBean;

    @Override
    protected int getLayout() {
        return R.layout.activity_electrical;
    }

    @Override
    protected void initView() {
        cb_powerSupply = (CheckBox) findViewById(R.id.cb_powerSupply);
        cb_mute = (CheckBox) findViewById(R.id.cb_mute);
        gv_numbers = (GridView) findViewById(R.id.gv_numbers);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        iv_soundAdd = (ImageView) findViewById(R.id.iv_soundAdd);
        iv_soundReduce = (ImageView) findViewById(R.id.iv_soundReduce);
        iv_programAdd = (ImageView) findViewById(R.id.iv_programAdd);
        iv_programReduce = (ImageView) findViewById(R.id.iv_programReduce);
        tv_currentRoom = (TextView) findViewById(R.id.tv_currentRoom);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initEvent() {
        rl_back.setOnClickListener((View v) -> finish());

        cb_powerSupply.setOnClickListener((View v) -> {
            cb_powerSupply.toggle();
            ValueUtil.sendTvCmd(
                    acAndTvAndMusicBean.getTv_S().getTvPower() == 0 ? 1 : 0,
                    acAndTvAndMusicBean.getTv_S().getTvShow(),
                    acAndTvAndMusicBean.getTv_S().getVolume(),
                    tag
            );
        });

        cb_mute.setOnClickListener((View v) -> {
            cb_mute.toggle();
            ValueUtil.sendTvCmd(
                    acAndTvAndMusicBean.getTv_S().getTvPower(),
                    acAndTvAndMusicBean.getTv_S().getTvShow(),
                    acAndTvAndMusicBean.getTv_S().getVolume() == 0 ? 1 : 0,
                    tag
            );
        });

        gv_numbers.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            // TODO: 2020/4/7 GridView 条目点击事件
            // 判断时候在无数字区域
            if (position == 9 || position == 11) {
                return;
            }
            acAndTvAndMusicBean.getTv_S().setTvShow(Integer.parseInt(customerAdapter.getItem(position)));
            ValueUtil.sendTvCmd(
                    acAndTvAndMusicBean.getTv_S().getTvPower(),
                    Integer.parseInt(customerAdapter.getItem(position)),
                    acAndTvAndMusicBean.getTv_S().getVolume(),
                    tag
            );
            L.e("number：" + customerAdapter.getItem(position));
        });

        iv_soundAdd.setOnClickListener((View v) -> {
            // TODO: 2020/4/7 音量加
            int volume = acAndTvAndMusicBean.getTv_S().getVolume();
            if (volume < 100) {
                volume += 1;
            }

            if (volume > 0) {
                cb_mute.setChecked(false);
            }

            acAndTvAndMusicBean.getTv_S().setVolume(volume);
            ValueUtil.sendTvCmd(acAndTvAndMusicBean, tag);
        });

        iv_soundReduce.setOnClickListener((View v) -> {
            // TODO: 2020/4/7 音量减
            int volume = acAndTvAndMusicBean.getTv_S().getVolume();
            if (volume > 0) {
                volume -= 1;
            }
            if (volume <= 0) {
                cb_mute.setChecked(true);
            }
            acAndTvAndMusicBean.getTv_S().setVolume(volume);
            ValueUtil.sendTvCmd(acAndTvAndMusicBean, tag);
        });

        iv_programAdd.setOnClickListener((View v) -> {
            // TODO: 2020/4/7 加
            int tvShow = acAndTvAndMusicBean.getTv_S().getTvShow();
            if (tvShow < 9) {
                tvShow += 1;
            }
            acAndTvAndMusicBean.getTv_S().setTvShow(tvShow);
            ValueUtil.sendTvCmd(acAndTvAndMusicBean, tag);
        });

        iv_programReduce.setOnClickListener((View v) -> {
            // TODO: 2020/4/7 减
            int tvShow = acAndTvAndMusicBean.getTv_S().getTvShow();
            if (tvShow > 0) {
                tvShow -= 1;
            }
            acAndTvAndMusicBean.getTv_S().setTvShow(tvShow);
            ValueUtil.sendTvCmd(acAndTvAndMusicBean, tag);
        });
    }

    @Override
    protected void initData() {
        acAndTvAndMusicBean = new AcAndTvAndMusicBean(new AcAndTvAndMusicBean.TvSBean());
        numbers = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            numbers.add(String.valueOf(i + 1));
        }
        numbers.add("");
        numbers.add("0");
        numbers.add("");

        customerAdapter = new CustomerAdapter();
        gv_numbers.setAdapter(customerAdapter);
        // 去除按钮点击效果
        gv_numbers.setSelector(new ColorDrawable(Color.TRANSPARENT));

        LinkBean customerHandlerBase = Constant.getLinkBeanByTag(tag);
        if (customerHandlerBase != null) {
            customerHandlerBase.setCustomerCallback(new CustomerCallback() {

                @Override
                public void disConnected() {
                }

                @Override
                public void getResultData(String msg) {
                    try {
                        if (!TextUtils.isEmpty(msg)) {
                            /*L.e("接收到的消息" + msg);*/
                            JSONObject jsonObject = new JSONObject(msg);
                            if (jsonObject.has("tv_S")) {
                                acAndTvAndMusicBean = new Gson().fromJson(msg, AcAndTvAndMusicBean.class);
                                // 设置状态
                                setWidgetStatus(acAndTvAndMusicBean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 设置界面中控件的状态
     *
     * @param acAndTvAndMusicBean 数据
     */
    private void setWidgetStatus(AcAndTvAndMusicBean acAndTvAndMusicBean) {
        cb_powerSupply.setChecked(acAndTvAndMusicBean.getTv_S().getTvPower() == 1);
        cb_mute.setChecked(acAndTvAndMusicBean.getTv_S().getVolume() == 0);
    }

    class CustomerAdapter extends BaseAdapter {

        private ImageView ivImage;

        @Override
        public int getCount() {
            return numbers.size();
        }

        @Override
        public String getItem(int position) {
            return numbers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(ElectricalActivity.this, R.layout.item_number, null);
            } else {
                view = convertView;
            }
            initView(view);
            if (position != 9 && position != 11) {
                int drawable = getResources().getIdentifier("pic_number" + getItem(position), "drawable", getPackageName());
                ivImage.setBackgroundResource(R.drawable.xml_cycle_white);
                ivImage.setImageResource(drawable);
            }
            return view;
        }

        private void initView(View view) {
            ivImage = (ImageView) view.findViewById(R.id.iv_image);
        }
    }

}
