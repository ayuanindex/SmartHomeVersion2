package com.realmax.smarthomeversion2.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.realmax.smarthomeversion2.R;

import java.util.HashMap;

/**
 * @author ayuan
 */
public class MainActivity extends BaseActivity {
    private ImageView ivHome;
    private ImageView ivLinkSetting;
    private GridView gvView;
    private HashMap<Integer, Integer> integerHashMap;
    private String[] labels;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        ivHome = findViewById(R.id.iv_home);
        ivLinkSetting = findViewById(R.id.iv_linkSetting);
        gvView = findViewById(R.id.gv_view);
    }


    @Override
    protected void initEvent() {
        ivHome.setOnClickListener((View v) -> {
            // TODO: 2020/4/2
        });

        ivLinkSetting.setOnClickListener((View v) -> jump(SettingActivity.class));

        gvView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            jump(position);
        });
    }

    private void jump(int position) {
        Intent intent = null;
        // 跳转到指定界面
        switch (position) {
            case 0:// 灯光
                intent = new Intent(MainActivity.this, LightActivity.class);
                intent.putExtra("tag", "light");
                break;
            case 1:// 门
                intent = new Intent(MainActivity.this, CurtainActivity.class);
                /*intent.putExtra("tag", "curtain");*/
                intent.putExtra("tag", "light");
                break;
            case 2:// 门
                intent = new Intent(MainActivity.this, DoorActivity.class);
                /*intent.putExtra("tag", "curtain");*/
                intent.putExtra("tag", "door");
                break;
            case 3:// 电器
                intent = new Intent(MainActivity.this, ElectricalActivity.class);
                intent.putExtra("tag", "electrical");
                break;
            case 4:// 传感器
                intent = new Intent(MainActivity.this, TransducerActivity.class);
                intent.putExtra("tag", "camera");
                break;
            case 5:// 监控
                intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra("tag", "camera");
                break;
            case 6:// 空调
                intent = new Intent(MainActivity.this, AirConditioningActivity.class);
                intent.putExtra("tag", "camera");
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void initData() {
        // 初始化主界面资源
        integerHashMap = new HashMap<>(7);
        integerHashMap.put(0, R.drawable.pic_light_open);
        integerHashMap.put(1, R.drawable.pic_dor_window_open);
        integerHashMap.put(2, R.drawable.pic_door);
        integerHashMap.put(3, R.drawable.pic_electrical_open);
        integerHashMap.put(4, R.drawable.pic_sensor_open);
        integerHashMap.put(5, R.drawable.pic_monitor_open);
        integerHashMap.put(6, R.drawable.pic_airconditioning_open);

        labels = new String[]{
                "灯光",
                "窗",
                "门",
                "电器",
                "传感",
                "监控",
                "空调",
        };

        CustomerAdapter customerAdapter = new CustomerAdapter();
        gvView.setAdapter(customerAdapter);
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
            ivIcon = view.findViewById(R.id.iv_icon);
            tvLabel = view.findViewById(R.id.tv_label);
        }
    }
}
