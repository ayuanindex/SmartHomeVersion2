package com.realmax.smarthomeversion2.tencentCloud.bean;


import com.realmax.smarthomeversion2.network.HttpUtil;

@HttpUtil.POST("https://iai.tencentcloudapi.com/")
public class CreatePersonResultBean {

    /**
     * Response : {"FaceId":"3454816969590585885","SimilarPersonId":"","FaceRect":{"X":172,"Y":122,"Width":178,"Height":228},"FaceModelVersion":"3.0","Error":{"Code":"MissingParameter","Message":"The request is missing a required parameter `Version`."},"RequestId":"051276ba-52aa-4eeb-8227-f0418452e61b"}
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
         * FaceId : 3454816969590585885
         * SimilarPersonId :
         * FaceRect : {"X":172,"Y":122,"Width":178,"Height":228}
         * FaceModelVersion : 3.0
         * Error : {"Code":"MissingParameter","Message":"The request is missing a required parameter `Version`."}
         * RequestId : 051276ba-52aa-4eeb-8227-f0418452e61b
         */

        private String FaceId;
        private String SimilarPersonId;
        private FaceRectBean FaceRect;
        private String FaceModelVersion;
        private ErrorBean Error;
        private String RequestId;

        public String getFaceId() {
            return FaceId;
        }

        public void setFaceId(String FaceId) {
            this.FaceId = FaceId;
        }

        public String getSimilarPersonId() {
            return SimilarPersonId;
        }

        public void setSimilarPersonId(String SimilarPersonId) {
            this.SimilarPersonId = SimilarPersonId;
        }

        public FaceRectBean getFaceRect() {
            return FaceRect;
        }

        public void setFaceRect(FaceRectBean FaceRect) {
            this.FaceRect = FaceRect;
        }

        public String getFaceModelVersion() {
            return FaceModelVersion;
        }

        public void setFaceModelVersion(String FaceModelVersion) {
            this.FaceModelVersion = FaceModelVersion;
        }

        public ErrorBean getError() {
            return Error;
        }

        public void setError(ErrorBean Error) {
            this.Error = Error;
        }

        public String getRequestId() {
            return RequestId;
        }

        public void setRequestId(String RequestId) {
            this.RequestId = RequestId;
        }

        public static class FaceRectBean {
            /**
             * X : 172
             * Y : 122
             * Width : 178
             * Height : 228
             */

            private int X;
            private int Y;
            private int Width;
            private int Height;

            public int getX() {
                return X;
            }

            public void setX(int X) {
                this.X = X;
            }

            public int getY() {
                return Y;
            }

            public void setY(int Y) {
                this.Y = Y;
            }

            public int getWidth() {
                return Width;
            }

            public void setWidth(int Width) {
                this.Width = Width;
            }

            public int getHeight() {
                return Height;
            }

            public void setHeight(int Height) {
                this.Height = Height;
            }

            @Override
            public String toString() {
                return "FaceRectBean{" +
                        "X=" + X +
                        ", Y=" + Y +
                        ", Width=" + Width +
                        ", Height=" + Height +
                        '}';
            }
        }

        public static class ErrorBean {
            /**
             * Code : MissingParameter
             * Message : The request is missing a required parameter `Version`.
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
                    "FaceId='" + FaceId + '\'' +
                    ", SimilarPersonId='" + SimilarPersonId + '\'' +
                    ", FaceRect=" + FaceRect +
                    ", FaceModelVersion='" + FaceModelVersion + '\'' +
                    ", Error=" + Error +
                    ", RequestId='" + RequestId + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CreatePersonResultBean{" +
                "Response=" + Response +
                '}';
    }
}
