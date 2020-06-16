package com.realmax.smarthomeversion2.tencentCloud.bean;


import com.realmax.smarthomeversion2.network.HttpUtil;

import java.util.List;

@HttpUtil.POST("https://iai.tencentcloudapi.com/")
public class PersonListBean {

    /**
     * Response : {"PersonInfos":[{"PersonName":"evanliao","PersonId":"1001","Gender":1,"PersonExDescriptions":["云与智慧产业事业群","大数据及人工智能产品中心","人脸识别产品组"],"FaceIds":["2877242150180891493"]}],"PersonNum":1,"FaceNum":1,"RequestId":"aa292f16-27d9-423b-9048-cdd43f6e4156","Error":{"Code":"InvalidParameterValue.GroupIdNotExist","Message":"人员库ID不存在。"}}
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
         * PersonInfos : [{"PersonName":"evanliao","PersonId":"1001","Gender":1,"PersonExDescriptions":["云与智慧产业事业群","大数据及人工智能产品中心","人脸识别产品组"],"FaceIds":["2877242150180891493"]}]
         * PersonNum : 1
         * FaceNum : 1
         * RequestId : aa292f16-27d9-423b-9048-cdd43f6e4156
         * Error : {"Code":"InvalidParameterValue.GroupIdNotExist","Message":"人员库ID不存在。"}
         */

        private int PersonNum;
        private int FaceNum;
        private String RequestId;
        private ErrorBean Error;
        private List<PersonInfosBean> PersonInfos;

        public int getPersonNum() {
            return PersonNum;
        }

        public void setPersonNum(int PersonNum) {
            this.PersonNum = PersonNum;
        }

        public int getFaceNum() {
            return FaceNum;
        }

        public void setFaceNum(int FaceNum) {
            this.FaceNum = FaceNum;
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

        public List<PersonInfosBean> getPersonInfos() {
            return PersonInfos;
        }

        public void setPersonInfos(List<PersonInfosBean> PersonInfos) {
            this.PersonInfos = PersonInfos;
        }

        public static class ErrorBean {
            /**
             * Code : InvalidParameterValue.GroupIdNotExist
             * Message : 人员库ID不存在。
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

        public static class PersonInfosBean {
            /**
             * PersonName : evanliao
             * PersonId : 1001
             * Gender : 1
             * PersonExDescriptions : ["云与智慧产业事业群","大数据及人工智能产品中心","人脸识别产品组"]
             * FaceIds : ["2877242150180891493"]
             */

            private String PersonName;
            private String PersonId;
            private int Gender;
            private List<String> PersonExDescriptions;
            private List<String> FaceIds;

            public String getPersonName() {
                return PersonName;
            }

            public void setPersonName(String PersonName) {
                this.PersonName = PersonName;
            }

            public String getPersonId() {
                return PersonId;
            }

            public void setPersonId(String PersonId) {
                this.PersonId = PersonId;
            }

            public int getGender() {
                return Gender;
            }

            public void setGender(int Gender) {
                this.Gender = Gender;
            }

            public List<String> getPersonExDescriptions() {
                return PersonExDescriptions;
            }

            public void setPersonExDescriptions(List<String> PersonExDescriptions) {
                this.PersonExDescriptions = PersonExDescriptions;
            }

            public List<String> getFaceIds() {
                return FaceIds;
            }

            public void setFaceIds(List<String> FaceIds) {
                this.FaceIds = FaceIds;
            }

            @Override
            public String toString() {
                return "PersonInfosBean{" +
                        "PersonName='" + PersonName + '\'' +
                        ", PersonId='" + PersonId + '\'' +
                        ", Gender=" + Gender +
                        ", PersonExDescriptions=" + PersonExDescriptions +
                        ", FaceIds=" + FaceIds +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ResponseBean{" +
                    "PersonNum=" + PersonNum +
                    ", FaceNum=" + FaceNum +
                    ", RequestId='" + RequestId + '\'' +
                    ", Error=" + Error +
                    ", PersonInfos=" + PersonInfos +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PersonListBean{" +
                "Response=" + Response +
                '}';
    }
}
