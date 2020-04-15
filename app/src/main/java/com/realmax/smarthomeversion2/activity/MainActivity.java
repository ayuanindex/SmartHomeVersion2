package com.realmax.smarthomeversion2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.realmax.smarthomeversion2.R;

import java.util.HashMap;

public class MainActivity extends BaseActivity {
    private ImageView iv_home;
    private ImageView iv_linkSetting;
    private GridView gv_view;
    private HashMap<Integer, Integer> integerHashMap;
    private CustomerAdapter customerAdapter;
    private String[] labels;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_linkSetting = (ImageView) findViewById(R.id.iv_linkSetting);
        gv_view = (GridView) findViewById(R.id.gv_view);
    }

    @Override
    protected void initEvent() {
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2020/4/2
            }
        });

        iv_linkSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2020/4/2
                jump(SettingActivity.class);
            }
        });

        gv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                String tag = "";
                // 跳转到指定界面
                switch (position) {
                    case 0:// 灯光
                        intent = new Intent(MainActivity.this, LightActivity.class);
                        intent.putExtra("tag", "light");
                        break;
                    case 1:// 门窗
                        intent = new Intent(MainActivity.this, CurtainActivity.class);
                        /*intent.putExtra("tag", "curtain");*/
                        intent.putExtra("tag", "light");
                        break;
                    case 2:// 电器
                        intent = new Intent(MainActivity.this, ElectricalActivity.class);
                        intent.putExtra("tag", "electrical");
                        break;
                    case 3:// 传感器
                        intent = new Intent(MainActivity.this, TransducerActivity.class);
                        intent.putExtra("tag", "camera");
                        break;
                    case 4:// 监控
                        intent = new Intent(MainActivity.this, CameraActivity.class);
                        intent.putExtra("tag", "camera");
                        break;
                    case 5:// 空调
                        intent = new Intent(MainActivity.this, AirConditioningActivity.class);
                        intent.putExtra("tag", "camera");
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        // 初始化主界面资源
        integerHashMap = new HashMap<>();
        integerHashMap.put(0, R.drawable.pic_light_open);
        integerHashMap.put(1, R.drawable.pic_dor_window_open);
        integerHashMap.put(2, R.drawable.pic_electrical_open);
        integerHashMap.put(3, R.drawable.pic_sensor_open);
        integerHashMap.put(4, R.drawable.pic_monitor_open);
        integerHashMap.put(5, R.drawable.pic_airconditioning_open);

        labels = new String[]{
                "灯光",
                "门窗",
                "电器",
                "传感",
                "监控",
                "空调",
        };

        customerAdapter = new CustomerAdapter();
        gv_view.setAdapter(customerAdapter);
    }

    /**
     * GridView的数据适配器
     */
    private class CustomerAdapter extends BaseAdapter {
        private ImageView ivIcon;
        private TextView tvLabel;

        @Override
        public int getCount() {
            return integerHashMap.size();
        }

        @Override
        public Integer getItem(int position) {
            return integerHashMap.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(MainActivity.this, R.layout.item_home, null);
            } else {
                view = convertView;
            }
            initView(view);
            ivIcon.setImageResource(getItem(position));
            tvLabel.setText(labels[position]);
            return view;
        }

        private void initView(View view) {
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            tvLabel = (TextView) view.findViewById(R.id.tv_label);
        }
    }
}
