package com.realmax.smarthomeversion2.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.activity.bean.RoomBean;
import com.realmax.smarthomeversion2.util.L;

/**
 * @author ayuan
 * 人员管理
 */
public class MemberManagementActivity extends BaseActivity {
    private GridView gvPeoples;
    private RelativeLayout rlBack;

    @Override
    protected int getLayout() {
        return R.layout.activity_member_management;
    }

    @Override
    protected void initView() {
        gvPeoples = (GridView) findViewById(R.id.gv_peoples);
        rlBack = (RelativeLayout) findViewById(R.id.rl_back);
    }

    @Override
    protected void initEvent() {
        gvPeoples.setOnItemClickListener((AdapterView<?> adapterView, View view, int i, long l) -> {
            // TODO: 2020/6/18
        });

        rlBack.setOnClickListener((View view) -> {
            finish();
        });
    }

    @Override
    protected void initData() {
        for (RoomBean roomBean : AirConditioningActivity.roomBeans) {
            L.e(roomBean.toString());
        }
    }
}
