package com.realmax.smarthomeversion2.tencentCloud.bean;


import com.realmax.smarthomeversion2.network.HttpUtil;

@HttpUtil.POST("https://iai.tencentcloudapi.com/")
public class CreateGroupResultBean {

    /**
     * Response : {"FaceModelVersion":"3.0","RequestId":"e53ee4ec-9099-4b35-a129-21dd4820ff85","Error":{"Code":"InvalidParameterValue.GroupIdAlreadyExist","Message":"人员库ID已经存在。人员库ID不可重复。"}}
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
         * FaceModelVersion : 3.0
         * RequestId : e53ee4ec-9099-4b35-a129-21dd4820ff85
         * Error : {"Code":"InvalidParameterValue.GroupIdAlreadyExist","Message":"人员库ID已经存在。人员库ID不可重复。"}
         */

        private String FaceModelVersion;
        private String RequestId;
        private ErrorBean Error;

        public String getFaceModelVersion() {
            return FaceModelVersion;
        }

        public void setFaceModelVersion(String FaceModelVersion) {
            this.FaceModelVersion = FaceModelVersion;
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

        public static class ErrorBean {
            /**
             * Code : InvalidParameterValue.GroupIdAlreadyExist
             * Message : 人员库ID已经存在。人员库ID不可重复。
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
                    "FaceModelVersion='" + FaceModelVersion + '\'' +
                    ", RequestId='" + RequestId + '\'' +
                    ", Error=" + Error +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CreateGroupResultBean{" +
                "Response=" + Response +
                '}';
    }
}
