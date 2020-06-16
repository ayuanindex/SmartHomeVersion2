package com.realmax.smarthomeversion2.tencentCloud.bean;


import com.realmax.smarthomeversion2.network.HttpUtil;

@HttpUtil.POST("https://iai.tencentcloudapi.com/")
public class DeleteGroupResultBean {

    /**
     * Response : {"RequestId":"2ff7181c-ef20-4f02-b4ed-13924b381368","Error":{"Code":"InvalidParameterValue.GroupIdNotExist","Message":"人员库ID不存在。"}}
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
         * RequestId : 2ff7181c-ef20-4f02-b4ed-13924b381368
         * Error : {"Code":"InvalidParameterValue.GroupIdNotExist","Message":"人员库ID不存在。"}
         */

        private String RequestId;
        private ErrorBean Error;

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

        @Override
        public String toString() {
            return "ResponseBean{" +
                    "RequestId='" + RequestId + '\'' +
                    ", Error=" + Error +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DeleteGroupResultBean{" +
                "Response=" + Response +
                '}';
    }
}
