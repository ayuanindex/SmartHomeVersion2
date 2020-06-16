package com.realmax.smarthomeversion2.tencentCloud.bean;


import com.realmax.smarthomeversion2.network.HttpUtil;

import java.util.List;

@HttpUtil.POST("https://iai.tencentcloudapi.com/")
public class PersonBaseInfoBean {

    /**
     * Response : {"PersonName":"EvanLiao","Gender":1,"FaceIds":["2873640802022644880","2875186538564559728"],"RequestId":"9568a077-0710-40d2-9d6a-b9483d3f2051","Error":{"Code":"InvalidParameterValue.PersonIdNotExist","Message":"人员ID不存在。"}}
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
         * PersonName : EvanLiao
         * Gender : 1
         * FaceIds : ["2873640802022644880","2875186538564559728"]
         * RequestId : 9568a077-0710-40d2-9d6a-b9483d3f2051
         * Error : {"Code":"InvalidParameterValue.PersonIdNotExist","Message":"人员ID不存在。"}
         */

        private String PersonName;
        private int Gender;
        private String RequestId;
        private ErrorBean Error;
        private List<String> FaceIds;

        public String getPersonName() {
            return PersonName;
        }

        public void setPersonName(String PersonName) {
            this.PersonName = PersonName;
        }

        public int getGender() {
            return Gender;
        }

        public void setGender(int Gender) {
            this.Gender = Gender;
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

        public List<String> getFaceIds() {
            return FaceIds;
        }

        public void setFaceIds(List<String> FaceIds) {
            this.FaceIds = FaceIds;
        }

        public static class ErrorBean {
            /**
             * Code : InvalidParameterValue.PersonIdNotExist
             * Message : 人员ID不存在。
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

        @Override
        public String toString() {
            return "ResponseBean{" +
                    "PersonName='" + PersonName + '\'' +
                    ", Gender=" + Gender +
                    ", RequestId='" + RequestId + '\'' +
                    ", Error=" + Error +
                    ", FaceIds=" + FaceIds +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PersonBaseInfoBean{" +
                "Response=" + Response +
                '}';
    }
}
