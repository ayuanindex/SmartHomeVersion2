package com.realmax.smarthomeversion2.tencentCloud.bean;

import com.realmax.smarthomeversion2.network.HttpUtil;

import java.util.List;

/**
 * @author ayuan
 * 对话
 */
@HttpUtil.POST("https://tbp.tencentcloudapi.com")
public class InterlocutionBean {

    /**
     * Response : {"DialogStatus":"START","BotName":"SmartHome","IntentName":"lightControl","SlotInfoList":[{"SlotName":"control","SlotValue":"开"},{"SlotName":"device","SlotValue":"灯"},{"SlotName":"room","SlotValue":"客厅"}],"InputText":"帮我把客厅的灯打开。","ResponseMessage":{"GroupList":[{"ContentType":"text/plain","Url":"","Content":"好的正在帮您打开客厅的灯"}]},"ResultType":"1","SessionAttributes":"","ResponseText":"好的正在帮您打开客厅的灯","RequestId":"30a546ec-0f9f-4660-8d12-fd5eed25bc1b","Error":{"Code":"InvalidParameterValue.GroupIdAlreadyExist","Message":"人员库ID已经存在。人员库ID不可重复。"}}
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
         * DialogStatus : START
         * BotName : SmartHome
         * IntentName : lightControl
         * SlotInfoList : [{"SlotName":"control","SlotValue":"开"},{"SlotName":"device","SlotValue":"灯"},{"SlotName":"room","SlotValue":"客厅"}]
         * InputText : 帮我把客厅的灯打开。
         * ResponseMessage : {"GroupList":[{"ContentType":"text/plain","Url":"","Content":"好的正在帮您打开客厅的灯"}]}
         * ResultType : 1
         * SessionAttributes :
         * ResponseText : 好的正在帮您打开客厅的灯
         * RequestId : 30a546ec-0f9f-4660-8d12-fd5eed25bc1b
         * Error : {"Code":"InvalidParameterValue.GroupIdAlreadyExist","Message":"人员库ID已经存在。人员库ID不可重复。"}
         */

        private String DialogStatus;
        private String BotName;
        private String IntentName;
        private String InputText;
        private ResponseMessageBean ResponseMessage;
        private String ResultType;
        private String SessionAttributes;
        private String ResponseText;
        private String RequestId;
        private ErrorBean Error;
        private List<SlotInfoListBean> SlotInfoList;

        public String getDialogStatus() {
            return DialogStatus;
        }

        public void setDialogStatus(String DialogStatus) {
            this.DialogStatus = DialogStatus;
        }

        public String getBotName() {
            return BotName;
        }

        public void setBotName(String BotName) {
            this.BotName = BotName;
        }

        public String getIntentName() {
            return IntentName;
        }

        public void setIntentName(String IntentName) {
            this.IntentName = IntentName;
        }

        public String getInputText() {
            return InputText;
        }

        public void setInputText(String InputText) {
            this.InputText = InputText;
        }

        public ResponseMessageBean getResponseMessage() {
            return ResponseMessage;
        }

        public void setResponseMessage(ResponseMessageBean ResponseMessage) {
            this.ResponseMessage = ResponseMessage;
        }

        public String getResultType() {
            return ResultType;
        }

        public void setResultType(String ResultType) {
            this.ResultType = ResultType;
        }

        public String getSessionAttributes() {
            return SessionAttributes;
        }

        public void setSessionAttributes(String SessionAttributes) {
            this.SessionAttributes = SessionAttributes;
        }

        public String getResponseText() {
            return ResponseText;
        }

        public void setResponseText(String ResponseText) {
            this.ResponseText = ResponseText;
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

        public List<SlotInfoListBean> getSlotInfoList() {
            return SlotInfoList;
        }

        public void setSlotInfoList(List<SlotInfoListBean> SlotInfoList) {
            this.SlotInfoList = SlotInfoList;
        }

        public static class ResponseMessageBean {
            private List<GroupListBean> GroupList;

            public List<GroupListBean> getGroupList() {
                return GroupList;
            }

            public void setGroupList(List<GroupListBean> GroupList) {
                this.GroupList = GroupList;
            }

            public static class GroupListBean {
                /**
                 * ContentType : text/plain
                 * Url :
                 * Content : 好的正在帮您打开客厅的灯
                 */

                private String ContentType;
                private String Url;
                private String Content;

                public String getContentType() {
                    return ContentType;
                }

                public void setContentType(String ContentType) {
                    this.ContentType = ContentType;
                }

                public String getUrl() {
                    return Url;
                }

                public void setUrl(String Url) {
                    this.Url = Url;
                }

                public String getContent() {
                    return Content;
                }

                public void setContent(String Content) {
                    this.Content = Content;
                }

                @Override
                public String toString() {
                    return "GroupListBean{" +
                            "ContentType='" + ContentType + '\'' +
                            ", Url='" + Url + '\'' +
                            ", Content='" + Content + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "ResponseMessageBean{" +
                        "GroupList=" + GroupList +
                        '}';
            }
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

        public static class SlotInfoListBean {
            /**
             * SlotName : control
             * SlotValue : 开
             */

            private String SlotName;
            private String SlotValue;

            public String getSlotName() {
                return SlotName;
            }

            public void setSlotName(String SlotName) {
                this.SlotName = SlotName;
            }

            public String getSlotValue() {
                return SlotValue;
            }

            public void setSlotValue(String SlotValue) {
                this.SlotValue = SlotValue;
            }

            @Override
            public String toString() {
                return "SlotInfoListBean{" +
                        "SlotName='" + SlotName + '\'' +
                        ", SlotValue='" + SlotValue + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ResponseBean{" +
                    "DialogStatus='" + DialogStatus + '\'' +
                    ", BotName='" + BotName + '\'' +
                    ", IntentName='" + IntentName + '\'' +
                    ", InputText='" + InputText + '\'' +
                    ", ResponseMessage=" + ResponseMessage +
                    ", ResultType='" + ResultType + '\'' +
                    ", SessionAttributes='" + SessionAttributes + '\'' +
                    ", ResponseText='" + ResponseText + '\'' +
                    ", RequestId='" + RequestId + '\'' +
                    ", Error=" + Error +
                    ", SlotInfoList=" + SlotInfoList +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "InterlocutionBean{" +
                "Response=" + Response +
                '}';
    }
}
