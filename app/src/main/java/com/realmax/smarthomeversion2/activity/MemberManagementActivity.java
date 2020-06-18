package com.realmax.smarthomeversion2.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.realmax.smarthomeversion2.App;
import com.realmax.smarthomeversion2.R;
import com.realmax.smarthomeversion2.network.HttpUtil;
import com.realmax.smarthomeversion2.tencentCloud.FaceManager;
import com.realmax.smarthomeversion2.tencentCloud.bean.CreatePersonResultBean;
import com.realmax.smarthomeversion2.tencentCloud.bean.GroupListBean;
import com.realmax.smarthomeversion2.tencentCloud.bean.PersonBaseInfoBean;
import com.realmax.smarthomeversion2.tencentCloud.bean.PersonListBean;
import com.realmax.smarthomeversion2.tencentCloud.bean.SearchPersonResultBean;
import com.realmax.smarthomeversion2.util.CameraUtil;
import com.realmax.smarthomeversion2.util.L;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author ayuan
 * 人员管理
 */
public class MemberManagementActivity extends BaseActivity {
    private GridView gvPeoples;
    private RelativeLayout rlBack;
    private List<PersonListBean.ResponseBean.PersonInfosBean> personInfos;
    private MaterialCardView cardAdd;
    private CustomerAdapter customerAdapter;
    private SwipeRefreshLayout swRefresh;

    @Override
    protected int getLayout() {
        return R.layout.activity_member_management;
    }

    @Override
    protected void initView() {
        gvPeoples = (GridView) findViewById(R.id.gv_peoples);
        rlBack = (RelativeLayout) findViewById(R.id.rl_back);
        cardAdd = (MaterialCardView) findViewById(R.id.cardAdd);
        swRefresh = (SwipeRefreshLayout) findViewById(R.id.swRefresh);
    }

    @Override
    protected void initEvent() {
        swRefresh.setOnRefreshListener(this::getPeopleList);

        gvPeoples.setOnItemClickListener((AdapterView<?> adapterView, View view, int i, long l) -> {
            // TODO: 2020/6/18
            Snackbar snackbar = Snackbar.make(view, "是否删除人员\"" + customerAdapter.getItem(i).getPersonName() + "\"", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.setAction("删除", (View delete) -> {
                FaceManager.deletePeople(customerAdapter.getItem(i).getPersonId(), new HttpUtil.Result<PersonBaseInfoBean>() {
                    @Override
                    public void getData(PersonBaseInfoBean personBaseInfoBean, Call call, Response response) {
                        if (personBaseInfoBean.getResponse().getError() == null) {
                            getPeopleList();
                        }
                    }

                    @Override
                    public void error(Call call, IOException e) {

                    }
                });
            });
            snackbar.show();
        });

        rlBack.setOnClickListener((View view) -> {
            finish();
        });

        cardAdd.setOnClickListener((View view) -> {
            // 打开家庭成员界面
            startActivityForResult(new Intent(this, AddFamilyPersonActivity.class), 100);
        });
    }

    @Override
    protected void initData() {
        CameraUtil.startCamera(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            getPeopleList();
        } else {
            CameraUtil.getImageData(requestCode, resultCode, (Bitmap bmp) -> {
                ArrayList<String> groups = new ArrayList<>();
                groups.add("1");
                FaceManager.searchFaces(bmp, groups, new HttpUtil.Result<SearchPersonResultBean>() {
                    @Override
                    public void getData(SearchPersonResultBean searchPersonResultBean, Call call, Response response) {
                        if (searchPersonResultBean.getResponse().getError() == null) {
                            List<SearchPersonResultBean.ResponseBean.ResultsBean> results = searchPersonResultBean.getResponse().getResults();
                            if (results.size() > 0) {
                                // 将拍照后的人脸比对的结果按照可信度进行排序，选择最高的，并且可信度要大于9
                                Collections.sort(results.get(0).getCandidates(), new Comparator<SearchPersonResultBean.ResponseBean.ResultsBean.CandidatesBean>() {
                                    @Override
                                    public int compare(SearchPersonResultBean.ResponseBean.ResultsBean.CandidatesBean candidatesBean, SearchPersonResultBean.ResponseBean.ResultsBean.CandidatesBean t1) {
                                        return (int) ((Double.parseDouble(t1.getScore()) * Integer.MAX_VALUE) - (Double.parseDouble(candidatesBean.getScore()) * Integer.MAX_VALUE));
                                    }
                                });

                                SearchPersonResultBean.ResponseBean.ResultsBean.CandidatesBean candidatesBean = results.get(0).getCandidates().get(0);
                                if (Double.parseDouble(candidatesBean.getScore()) > 95) {
                                    FaceManager.getPersonBaseInfo(candidatesBean.getPersonId(), new HttpUtil.Result<PersonBaseInfoBean>() {
                                        @Override
                                        public void getData(PersonBaseInfoBean personBaseInfoBean, Call call, Response response) {
                                            if (personBaseInfoBean.getResponse().getError() == null) {
                                                L.e("获取成功----" + personBaseInfoBean.toString());
                                                uiHandler.post(() -> {
                                                    App.showToast("识别成功");
                                                    MemberManagementActivity.this.init();
                                                });
                                            } else {
                                                uiHandler.post(() -> {
                                                    App.showToast("您不是家庭成员");
                                                    finish();
                                                });
                                            }
                                        }

                                        @Override
                                        public void error(Call call, IOException e) {
                                            e.printStackTrace();
                                            L.e("请求人员信息发生异常---------" + e.getMessage());
                                        }
                                    });
                                } else {
                                    uiHandler.post(() -> {
                                        App.showToast("您不是家庭成员");
                                        finish();
                                    });
                                }
                            } else {
                                uiHandler.post(() -> {
                                    App.showToast("您不是家庭成员");
                                    finish();
                                });
                            }
                        } else {
                            uiHandler.post(() -> {
                                App.showToast(searchPersonResultBean.getResponse().getError().getMessage());
                            });
                            finish();
                        }
                    }

                    @Override
                    public void error(Call call, IOException e) {

                    }
                });
            });
        }
    }

    private void init() {

        personInfos = new ArrayList<>();

        customerAdapter = new CustomerAdapter();
        gvPeoples.setAdapter(customerAdapter);
        getGrouList();
    }

    private void getGrouList() {
        // 获取人员库信息
        FaceManager.getGroupList(new HttpUtil.Result<GroupListBean>() {
            @Override
            public void getData(GroupListBean groupListBean, Call call, Response response) {
                // 查看有没有制定的人员库
                if (groupListBean.getResponse().getError() != null) {
                    L.e("请求人员库列表返回的的错误信息-----------" + groupListBean.getResponse().getError().toString());
                } else {
                    if (groupListBean.getResponse().getGroupNum() == 0) {
                        // 没有人员库，现在创建
                        createPeopleGroup();
                    } else {
                        for (GroupListBean.ResponseBean.GroupInfosBean groupInfo : groupListBean.getResponse().getGroupInfos()) {
                            if (groupInfo.getGroupName().equals("家庭") || groupInfo.getGroupId().equals("1")) {
                                getPeopleList();
                                break;
                            }
                        }
                    }
                    L.e("请求人员库列表成功----------" + groupListBean.toString());
                }
            }

            @Override
            public void error(Call call, IOException e) {
                e.printStackTrace();
                L.e("请求人员库列表发生异常------------" + e.getMessage());
            }
        });
    }

    /**
     * 创建人员库
     */
    private void createPeopleGroup() {
        FaceManager.createGroup("家庭", "1", new HttpUtil.Result<CreatePersonResultBean>() {
            @Override
            public void getData(CreatePersonResultBean createPersonResultBean, Call call, Response response) {
                if (createPersonResultBean.getResponse().getError() != null) {
                    L.e("创建人员库请求返回的错误信息----------------" + createPersonResultBean.getResponse().getError().toString());
                } else {
                    L.e("创建成功");
                }
            }

            @Override
            public void error(Call call, IOException e) {
                e.printStackTrace();
                L.e("请求创建人员库出现异常-----------" + e.getMessage());
            }
        });
    }

    /**
     * 获取人员列表
     */
    private void getPeopleList() {
        FaceManager.getPersonList("1", new HttpUtil.Result<PersonListBean>() {
            @Override
            public void getData(PersonListBean personListBean, Call call, Response response) {
                if (personListBean.getResponse().getError() != null) {
                    L.e("请求人员列表返回的额错误信息--------" + personListBean.getResponse().getError().toString());
                } else {
                    personInfos.clear();
                    personInfos.addAll(personListBean.getResponse().getPersonInfos());
                    uiHandler.post(() -> {
                        customerAdapter.notifyDataSetChanged();
                        swRefresh.setRefreshing(false);
                    });
                }
            }

            @Override
            public void error(Call call, IOException e) {
                e.printStackTrace();
                L.e("请求获取人员列表----- " + e.getMessage());
            }
        });
    }

    class CustomerAdapter extends BaseAdapter {
        private TextView tvText;

        @Override
        public int getCount() {
            return personInfos.size();
        }

        @Override
        public PersonListBean.ResponseBean.PersonInfosBean getItem(int i) {
            return personInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View customerView;
            if (view == null) {
                customerView = View.inflate(MemberManagementActivity.this, R.layout.item_member, null);
            } else {
                customerView = view;
            }
            initView(customerView);
            tvText.setText(getItem(i).getPersonName());
            return customerView;
        }

        private void initView(View customerView) {
            tvText = (TextView) customerView.findViewById(R.id.tv_text);
        }
    }

}
