package com.realmax.smarthomeversion2.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.realmax.smarthomeversion2.App;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.network.HttpUtil;
import com.realmax.smarthomeversion2.tencentCloud.FaceManager;
import com.realmax.smarthomeversion2.tencentCloud.bean.CreatePersonResultBean;
import com.realmax.smarthomeversion2.util.CameraUtil;
import com.realmax.smarthomeversion2.util.L;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class AddFamilyPersonActivity extends BaseActivity {
    private android.widget.RelativeLayout rlBack;
    private android.widget.ImageView ivIcon;
    private androidx.cardview.widget.CardView cardAddPicture;
    private androidx.cardview.widget.CardView cardSubmit;
    private android.widget.EditText etName;
    private android.widget.EditText etId;
    private android.widget.RadioGroup radioSelectGender;
    private android.widget.RadioButton radioMan;
    private android.widget.RadioButton radioWoman;
    private Bitmap bmp = null;

    @Override
    protected int getLayout() {
        return R.layout.activity_addpeople;
    }

    @Override
    protected void initView() {
        rlBack = (RelativeLayout) findViewById(R.id.rl_back);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        cardAddPicture = (CardView) findViewById(R.id.cardAddPicture);
        cardSubmit = (CardView) findViewById(R.id.cardSubmit);
        etName = (EditText) findViewById(R.id.etName);
        etId = (EditText) findViewById(R.id.etId);
        radioSelectGender = (RadioGroup) findViewById(R.id.radioSelectGender);
        radioMan = (RadioButton) findViewById(R.id.radioMan);
        radioWoman = (RadioButton) findViewById(R.id.radioWoman);
    }

    @Override
    protected void initEvent() {
        rlBack.setOnClickListener((View view) -> finish());

        cardAddPicture.setOnClickListener((View view) -> {
            CameraUtil.startCamera(this);
        });

        cardSubmit.setOnClickListener((View view) -> {
            String name = etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                App.showToast("请输入人员名称");
                return;
            }

            String id = etId.getText().toString().trim();
            if (TextUtils.isEmpty(id)) {
                App.showToast("请输入人员ID");
                return;
            }

            int checkedRadioButtonId = radioSelectGender.getCheckedRadioButtonId();

            FaceManager.createPerson(bmp, name, "1", id, (checkedRadioButtonId == R.id.radioMan ? 1 : 2), new HttpUtil.Result<CreatePersonResultBean>() {
                @Override
                public void getData(CreatePersonResultBean createPersonResultBean, Call call, Response response) {
                    uiHandler.post(() -> {
                        if (createPersonResultBean.getResponse().getError() != null) {
                            App.showToast(createPersonResultBean.getResponse().getError().getMessage());
                        } else {
                            if ((createPersonResultBean.getResponse().getSimilarPersonId().equals("") ? 1 : 0) != 0) {
                                App.showToast("该人员已存在");
                            } else {
                                App.showToast("添加成功");
                            }
                            finish();
                        }
                        L.e(createPersonResultBean.toString());
                    });
                }

                @Override
                public void error(Call call, IOException e) {
                    e.printStackTrace();
                    L.e("请求添加新的家庭成员出现异常-----" + e.getMessage());
                }
            });
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CameraUtil.getImageData(requestCode, resultCode, (Bitmap bmp) -> {
            this.bmp = bmp;
            ivIcon.setImageBitmap(bmp);
        });
    }
}
