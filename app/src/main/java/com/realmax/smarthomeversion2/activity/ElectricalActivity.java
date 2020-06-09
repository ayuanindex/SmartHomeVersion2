package com.realmax.smarthomeversion2.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.util.L;

import java.util.ArrayList;

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
    private int currentPosition = 0;

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

    @Override
    protected void initEvent() {
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cb_powerSupply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO: 2020/4/8 电源开关
            }
        });

        cb_mute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO: 2020/4/8 静音开关
            }
        });

        gv_numbers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 2020/4/7 GridView 条目点击事件
                // 判断时候在无数字区域
                if (position == 9 || position == 11) {
                    return;
                }

                L.e("number：" + customerAdapter.getItem(position));
            }
        });

        iv_soundAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2020/4/7 音量加
            }
        });

        iv_soundReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2020/4/7 音量减
            }
        });

        iv_programAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2020/4/7 加
            }
        });

        iv_programReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2020/4/7 减
            }
        });
    }

    @Override
    protected void initData() {
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
