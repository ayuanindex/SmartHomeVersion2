package com.realmax.smarthomeversion2.activity;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.realmax.smarthomeversion2.App;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.audio.CommendActivity;
import com.realmax.smarthomeversion2.mqtt.CurtainControl;
import com.realmax.smarthomeversion2.mqtt.LightControl;
import com.realmax.smarthomeversion2.mqtt.MqttControl;
import com.realmax.smarthomeversion2.util.L;
import com.realmax.smarthomeversion2.util.ValueUtil;

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
    private boolean isFirst = true;

    /**
     * BaseActivity获取布局资源ID
     *
     * @return 布局资源ID
     */
    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        ivHome = findViewById(R.id.iv_home);
        ivLinkSetting = findViewById(R.id.iv_linkSetting);
        gvView = findViewById(R.id.gv_view);
    }

    /**
     * 初始化控件的监听（点击监听、触摸监听等等）
     */
    @Override
    protected void initEvent() {
        // 跳转到语音控制界面
        ivHome.setOnClickListener((View v) -> jump(CommendActivity.class));

        // 跳转到连接设置界面
        ivLinkSetting.setOnClickListener((View v) -> jump(SettingActivity.class));

        // 跳转到设备控制界面
        gvView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> jumpToActivity(position));
    }

    /**
     * 初始化数据
     */
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
        integerHashMap.put(7, R.drawable.pic_face);

        labels = new String[]{
                "灯光",
                "窗",
                "门",
                "电器",
                "传感",
                "监控",
                "空调",
                "家庭成员管理"
        };

        // 促使物联网开发平台中使用的设备连接（存储所有的MQTT连接）
        HashMap<String, MqttControl> mqttControllerHashMap = new HashMap<>(1);
        // 将灯和窗帘等设备的MQTT控制类存放到一个静态集合中
        // LightControl和CurtainControl都是继承于MqttControl类
        mqttControllerHashMap.put("light", new LightControl(this, "Light.json", "ZJIJA6UHXP", "light", "L6yOvzW0qCbHG8pr0iKGYA=="));
        mqttControllerHashMap.put("Curtain", new CurtainControl(this, "Curtain.json", "5KEF6G3TQW", "curtain", "0Xu8+JQPrVVljvdN29jV/Q=="));
        // 将填充完成的map集合存入到设置到ValueUtil静态类中，并进行统一连接
        ValueUtil.setMqttControlHashMap(mqttControllerHashMap);

        // 列表适配器设置
        CustomerAdapter customerAdapter = new CustomerAdapter();
        gvView.setAdapter(customerAdapter);
    }

    /**
     * 跳转到新的Activity
     *
     * @param position 选择的数字
     */
    private void jumpToActivity(int position) {
        Intent intent = null;
        // 跳转到指定界面
        switch (position) {
            case 0:// 灯光
                intent = new Intent(MainActivity.this, LightActivity.class);
                break;
            case 1:// 窗
                intent = new Intent(MainActivity.this, CurtainActivity.class);
                break;
            case 2:// 门
                intent = new Intent(MainActivity.this, DoorActivity.class);
                break;
            case 3:// 电器
                intent = new Intent(MainActivity.this, ElectricalActivity.class);
                break;
            case 4:// 传感器
                intent = new Intent(MainActivity.this, TransducerActivity.class);
                break;
            case 5:// 监控
                intent = new Intent(MainActivity.this, CameraActivity.class);
                break;
            case 6:// 空调
                intent = new Intent(MainActivity.this, AirConditioningActivity.class);
                break;
            case 7:// 空调
                intent = new Intent(MainActivity.this, MemberManagementActivity.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
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

    @Override
    public void onBackPressed() {
        // 注释掉返回按钮
        if (isFirst) {
            isFirst = false;
            App.showToast("再次出发返回键退出");
            Runnable r = () -> isFirst = true;
            uiHandler.postDelayed(r, 2000);
        } else {
            super.onBackPressed();
            uiHandler.postDelayed(() -> System.exit(0), 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
