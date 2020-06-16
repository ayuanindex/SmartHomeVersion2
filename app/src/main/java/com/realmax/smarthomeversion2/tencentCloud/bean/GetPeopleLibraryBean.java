package com.realmax.smarthomeversion2.tencentCloud.bean;


import com.realmax.smarthomeversion2.network.HttpUtil;

import java.util.List;

@HttpUtil.POST("https://iai.tencentcloudapi.com/")
public class GetPeopleLibraryBean {

    /**
     * Response : {"GroupInfos":[{"GroupName":"腾讯深圳员工","GroupId":"TencentShenZhenEmployee","GroupExDescriptions":["事业群","部门名","组名"],"Tag":"不含实习生"},{"GroupName":"某某大学竹园宿舍楼1号楼","GroupId":"ZhuYuanDormitoryNo1","GroupExDescriptions":["学院名","专业","年级","学号"],"Tag":"全是女生哦"}],"GroupNum":2,"RequestId":"72102087-a18d-4225-9ae9-87ef49e9f63e","Error":{"Code":"InvalidParameterValue.LimitExceed","Message":"返回数量不在合法范围内。"}}
     */

    private ResponseBean Response;

    public ResponseBean getResponse() {
        return Response;
    }

    public void setResponse(ResponseBean Response) {
        this.Response = Response;
    }

    public static class ResponseBean {
        /**
         * GroupInfos : [{"GroupName":"腾讯深圳员工","GroupId":"TencentShenZhenEmployee","GroupExDescriptions":["事业群","部门名","组名"],"Tag":"不含实习生"},{"GroupName":"某某大学竹园宿舍楼1号楼","GroupId":"ZhuYuanDormitoryNo1","GroupExDescriptions":["学院名","专业","年级","学号"],"Tag":"全是女生哦"}]
         * GroupNum : 2
         * RequestId : 72102087-a18d-4225-9ae9-87ef49e9f63e
         * Error : {"Code":"InvalidParameterValue.LimitExceed","Message":"返回数量不在合法范围内。"}
         */

        private int GroupNum;
        private String RequestId;
        private ErrorBean Error;
        private List<GroupInfosBean> GroupInfos;

        public int getGroupNum() {
            return GroupNum;
        }

        public void setGroupNum(int GroupNum) {
            this.GroupNum = GroupNum;
        }

        public String getRequestId() {
            return RequestId;
        }

        public void setRequestId(String RequestId) {
            this.RequestId = RequestId;
        }

        public ErrorBean getError() {
            return Error;
        }

        public void setError(ErrorBean Error) {
            this.Error = Error;
        }

        public List<GroupInfosBean> getGroupInfos() {
            return GroupInfos;
        }

        public void setGroupInfos(List<GroupInfosBean> GroupInfos) {
            this.GroupInfos = GroupInfos;
        }

        public static class ErrorBean {
            /**
             * Code : InvalidParameterValue.LimitExceed
             * Message : 返回数量不在合法范围内。
             */

            private String Code;
            private String Message;

            public String getCode() {
                return Code;
            }

            public void setCode(String Code) {
                this.Code = Code;
            }

            public String getMessage() {
                return Message;
            }

            public void setMessage(String Message) {
                this.Message = Message;
            }

            @Override
            public String toString() {
                return "ErrorBean{" +
                        "Code='" + Code + '\'' +
                        ", Message='" + Message + '\'' +
                        '}';
            }
        }

        public static class GroupInfosBean {
            /**
             * GroupName : 腾讯深圳员工
             * GroupId : TencentShenZhenEmployee
             * GroupExDescriptions : ["事业群","部门名","组名"]
             * Tag : 不含实习生
             */

            private String GroupName;
            private String GroupId;
            private String Tag;
            private List<String> GroupExDescriptions;

            public String getGroupName() {
                return GroupName;
            }

            public void setGroupName(String GroupName) {
                this.GroupName = GroupName;
            }

            public String getGroupId() {
                return GroupId;
            }

            public void setGroupId(String GroupId) {
                this.GroupId = GroupId;
            }

            public String getTag() {
                return Tag;
            }

            public void setTag(String Tag) {
                this.Tag = Tag;
            }

            public List<String> getGroupExDescriptions() {
                return GroupExDescriptions;
            }

            public void setGroupExDescriptions(List<String> GroupExDescriptions) {
                this.GroupExDescriptions = GroupExDescriptions;
            }

            @Override
            public String toString() {
                return "GroupInfosBean{" +
                        "GroupName='" + GroupName + '\'' +
                        ", GroupId='" + GroupId + '\'' +
                        ", Tag='" + Tag + '\'' +
                        ", GroupExDescriptions=" + GroupExDescriptions +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ResponseBean{" +
                    "GroupNum=" + GroupNum +
                    ", RequestId='" + RequestId + '\'' +
                    ", Error=" + Error +
                    ", GroupInfos=" + GroupInfos +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetPeopleLibraryBean{" +
                "Response=" + Response +
                '}';
    }
}
