package com.realmax.smarthomeversion2.tencentCloud;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.realmax.smarthomeversion2.network.HttpUtil;
import com.realmax.smarthomeversion2.tencentCloud.bean.CreatePersonResultBean;
import com.realmax.smarthomeversion2.tencentCloud.bean.DeleteGroupResultBean;
import com.realmax.smarthomeversion2.tencentCloud.bean.GetPeopleLibraryBean;
import com.realmax.smarthomeversion2.tencentCloud.bean.GroupListBean;
import com.realmax.smarthomeversion2.tencentCloud.bean.PersonBaseInfoBean;
import com.realmax.smarthomeversion2.tencentCloud.bean.PersonListBean;
import com.realmax.smarthomeversion2.tencentCloud.bean.SearchPersonResultBean;
import com.realmax.smarthomeversion2.util.CustomerThread;
import com.realmax.smarthomeversion2.util.EncodeAndDecode;

import java.util.List;
import java.util.TreeMap;

public class FaceManager {
    private static final String TAG = "FaceManager";

    /**
     * 压缩图片到腾讯云可使用大小
     *
     * @param bitmap 需要压缩的图片
     * @return 返回压缩后的图片
     */
    private static Bitmap compressMatrix(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 获取人员库列表
     *
     * @param result 请求回调
     */
    public static void getGroupList(HttpUtil.Result<GroupListBean> result) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                TreeMap<String, Object> params = new TreeMap<>();
                // 公共参数
                params.put("Action", "GetGroupList");
                // 公共参数
                params.put("Region", "ap-shanghai");
                // 公共参数
                params.put("Version", "2020-03-03");

                TencentCloudAPIInitUtil.setIP("iai.tencentcloudapi.com");
                TreeMap<String, Object> init = TencentCloudAPIInitUtil.init(params);
                HttpUtil.doPost(init, GroupListBean.class, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 创建人员
     *
     * @param bitmap     人员照片
     * @param personName 人员姓名
     * @param groupId    人员库ID
     * @param personId   人员ID
     * @param isMan      男性或者女性
     * @param result     网络请求回调
     */
    public static void createPerson(Bitmap bitmap, String personName, String groupId, String personId, int isMan, HttpUtil.Result<CreatePersonResultBean> result) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                TreeMap<String, Object> params = new TreeMap<>();
                // 公共参数
                params.put("Action", "CreatePerson");
                // 公共参数
                params.put("Region", "ap-shanghai");
                // 公共参数
                params.put("Version", "2018-03-01");
                // 业务参数
                params.put("GroupId", groupId);
                params.put("PersonName", personName);
                params.put("PersonId", personId);
                params.put("Gender", isMan);
                params.put("UniquePersonControl", "1");
                params.put("QualityControl", "1");
                params.put("NeedRotateDetection", "1");
                params.put("Image", EncodeAndDecode.bitmapToBase64(compressMatrix(compressMatrix(compressMatrix(bitmap)))));
                TencentCloudAPIInitUtil.setIP("iai.tencentcloudapi.com");
                TreeMap<String, Object> init = TencentCloudAPIInitUtil.init(params);

                HttpUtil.doPost(init, CreatePersonResultBean.class, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 获取人员库信息
     *
     * @param result 请求状态回调
     */
    public static void getPeopleLibrary(HttpUtil.Result<GetPeopleLibraryBean> result) {
        try {
            TreeMap<String, Object> params = new TreeMap<>();
            // 公共参数
            params.put("Action", "GetGroupList");
            // 公共参数
            params.put("Region", "ap-shanghai");
            // 公共参数
            params.put("Version", "2018-03-01");
            TencentCloudAPIInitUtil.setIP("iai.tencentcloudapi.com");
            TreeMap<String, Object> init = TencentCloudAPIInitUtil.init(params);
            HttpUtil.doPost(init, GetPeopleLibraryBean.class, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建人员库
     *
     * @param name   人员库名称
     * @param id     人员库ID
     * @param result 请求数据的回调
     */
    public static void createGroup(String name, String id, HttpUtil.Result<CreatePersonResultBean> result) {
        try {
            TreeMap<String, Object> params = new TreeMap<>();
            // 公共参数
            params.put("Action", "CreateGroup");
            // 公共参数
            params.put("Region", "ap-shanghai");
            // 公共参数
            params.put("Version", "2018-03-01");
            params.put("GroupName", name);
            params.put("GroupId", id);
            TencentCloudAPIInitUtil.setIP("iai.tencentcloudapi.com");
            TreeMap<String, Object> init = TencentCloudAPIInitUtil.init(params);
            HttpUtil.doPost(init, CreatePersonResultBean.class, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取人员列表
     *
     * @param groupId 人员库ID
     * @param result  请求回调
     */
    public static void getPersonList(String groupId, HttpUtil.Result<PersonListBean> result) {
        try {
            TreeMap<String, Object> params = new TreeMap<>();
            // 公共参数
            params.put("Action", "GetPersonList");
            // 公共参数
            params.put("Region", "ap-shanghai");
            // 公共参数
            params.put("Version", "2018-03-01");
            params.put("GroupId", groupId);
            TencentCloudAPIInitUtil.setIP("iai.tencentcloudapi.com");
            TreeMap<String, Object> init = TencentCloudAPIInitUtil.init(params);
            HttpUtil.doPost(init, PersonListBean.class, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除人员库
     *
     * @param groupId 人员库ID
     * @param result  请求回调
     */
    public static void deleteGroup(String groupId, HttpUtil.Result<DeleteGroupResultBean> result) {
        try {
            TreeMap<String, Object> params = new TreeMap<>();
            // 公共参数
            params.put("Action", "DeleteGroup");
            // 公共参数
            params.put("Region", "ap-shanghai");
            // 公共参数
            params.put("Version", "2018-03-01");
            params.put("GroupId", groupId);
            TencentCloudAPIInitUtil.setIP("iai.tencentcloudapi.com");
            TreeMap<String, Object> init = TencentCloudAPIInitUtil.init(params);
            HttpUtil.doPost(init, DeleteGroupResultBean.class, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找人脸信息
     *
     * @param bmp    需要查找的图片
     * @param groups 人员组
     * @param result 请求回调
     */
    public static void searchFaces(Bitmap bmp, List<String> groups, HttpUtil.Result<SearchPersonResultBean> result) {
        try {
            TreeMap<String, Object> params = new TreeMap<>();
            // 公共参数
            params.put("Action", "SearchPersons");
            // 公共参数
            params.put("Region", "ap-shanghai");
            // 公共参数
            params.put("Version", "2020-03-03");
            for (int i = 0; i < groups.size(); i++) {
                params.put("GroupIds." + i, groups.get(i));
            }
            params.put("Image", EncodeAndDecode.bitmapToBase64(compressMatrix(compressMatrix(compressMatrix(bmp)))));
            params.put("NeedRotateDetection", 1);
            TencentCloudAPIInitUtil.setIP("iai.tencentcloudapi.com");
            TreeMap<String, Object> init = TencentCloudAPIInitUtil.init(params);
            HttpUtil.doPost(init, SearchPersonResultBean.class, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取人员基础信息
     *
     * @param personId 人员ID
     * @param result   请求回调
     */
    public static void getPersonBaseInfo(String personId, HttpUtil.Result<PersonBaseInfoBean> result) {
        try {
            TreeMap<String, Object> params = new TreeMap<>();
            // 公共参数
            params.put("Action", "GetPersonBaseInfo");
            // 公共参数
            params.put("Region", "ap-shanghai");
            // 公共参数
            params.put("Version", "2020-03-03");
            params.put("PersonId", personId);
            TencentCloudAPIInitUtil.setIP("iai.tencentcloudapi.com");
            TreeMap<String, Object> init = TencentCloudAPIInitUtil.init(params);
            HttpUtil.doPost(init, PersonBaseInfoBean.class, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除人员
     *
     * @param personId 人员ID
     * @param result   请求回调
     */
    public static void deletePeople(String personId, HttpUtil.Result<PersonBaseInfoBean> result) {
        CustomerThread.poolExecutor.execute(() -> {
            try {
                TreeMap<String, Object> params = new TreeMap<>();
                // 公共参数
                params.put("Action", "DeletePerson");
                // 公共参数
                params.put("Region", "ap-shanghai");
                // 公共参数
                params.put("Version", "2020-03-03");
                params.put("PersonId", personId);
                TencentCloudAPIInitUtil.setIP("iai.tencentcloudapi.com");
                TreeMap<String, Object> init = TencentCloudAPIInitUtil.init(params);
                HttpUtil.doPost(init, PersonBaseInfoBean.class, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
