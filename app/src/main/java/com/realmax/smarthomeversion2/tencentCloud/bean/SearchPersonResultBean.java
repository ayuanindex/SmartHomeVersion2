package com.realmax.smarthomeversion2.tencentCloud.bean;


import com.realmax.smarthomeversion2.network.HttpUtil;

import java.util.List;

@HttpUtil.POST("https://iai.tencentcloudapi.com/")
public class SearchPersonResultBean {

    /**
     * Response : {"Results":[{"RetCode":0,"Candidates":[{"PersonId":"person1","FaceId":"","Score":100,"PersonName":null,"Gender":null,"PersonGroupInfos":null},{"PersonId":"person2","FaceId":"","Score":60,"PersonName":null,"Gender":null,"PersonGroupInfos":null},{"PersonId":"person3","FaceId":"","Score":50,"PersonName":null,"Gender":null,"PersonGroupInfos":null}],"FaceRect":{"X":139,"Y":59,"Width":124,"Height":162}},{"RetCode":0,"Candidates":[{"PersonId":"person2","FaceId":"","Score":100,"PersonName":null,"Gender":null,"PersonGroupInfos":null},{"PersonId":"person1","FaceId":"","Score":60,"PersonName":null,"Gender":null,"PersonGroupInfos":null},{"PersonId":"person3","FaceId":"","Score":20,"PersonName":null,"Gender":null,"PersonGroupInfos":null}],"FaceRect":{"X":328,"Y":70,"Width":119,"Height":162}}],"PersonNum":5,"FaceModelVersion":"3.0","RequestId":"c4608852-ff60-4a01-8dc3-367f6046baaf","Error":{"Code":"FailedOperation.ImageDownloadError","Message":"图片下载错误。"}}
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
         * Results : [{"RetCode":0,"Candidates":[{"PersonId":"person1","FaceId":"","Score":100,"PersonName":null,"Gender":null,"PersonGroupInfos":null},{"PersonId":"person2","FaceId":"","Score":60,"PersonName":null,"Gender":null,"PersonGroupInfos":null},{"PersonId":"person3","FaceId":"","Score":50,"PersonName":null,"Gender":null,"PersonGroupInfos":null}],"FaceRect":{"X":139,"Y":59,"Width":124,"Height":162}},{"RetCode":0,"Candidates":[{"PersonId":"person2","FaceId":"","Score":100,"PersonName":null,"Gender":null,"PersonGroupInfos":null},{"PersonId":"person1","FaceId":"","Score":60,"PersonName":null,"Gender":null,"PersonGroupInfos":null},{"PersonId":"person3","FaceId":"","Score":20,"PersonName":null,"Gender":null,"PersonGroupInfos":null}],"FaceRect":{"X":328,"Y":70,"Width":119,"Height":162}}]
         * PersonNum : 5
         * FaceModelVersion : 3.0
         * RequestId : c4608852-ff60-4a01-8dc3-367f6046baaf
         * Error : {"Code":"FailedOperation.ImageDownloadError","Message":"图片下载错误。"}
         */

        private int PersonNum;
        private String FaceModelVersion;
        private String RequestId;
        private ErrorBean Error;
        private List<ResultsBean> Results;

        public int getPersonNum() {
            return PersonNum;
        }

        public void setPersonNum(int PersonNum) {
            this.PersonNum = PersonNum;
        }

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

        public List<ResultsBean> getResults() {
            return Results;
        }

        public void setResults(List<ResultsBean> Results) {
            this.Results = Results;
        }

        public static class ErrorBean {
            /**
             * Code : FailedOperation.ImageDownloadError
             * Message : 图片下载错误。
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

        public static class ResultsBean {
            /**
             * RetCode : 0
             * Candidates : [{"PersonId":"person1","FaceId":"","Score":100,"PersonName":null,"Gender":null,"PersonGroupInfos":null},{"PersonId":"person2","FaceId":"","Score":60,"PersonName":null,"Gender":null,"PersonGroupInfos":null},{"PersonId":"person3","FaceId":"","Score":50,"PersonName":null,"Gender":null,"PersonGroupInfos":null}]
             * FaceRect : {"X":139,"Y":59,"Width":124,"Height":162}
             */

            private int RetCode;
            private FaceRectBean FaceRect;
            private List<CandidatesBean> Candidates;

            public int getRetCode() {
                return RetCode;
            }

            public void setRetCode(int RetCode) {
                this.RetCode = RetCode;
            }

            public FaceRectBean getFaceRect() {
                return FaceRect;
            }

            public void setFaceRect(FaceRectBean FaceRect) {
                this.FaceRect = FaceRect;
            }

            public List<CandidatesBean> getCandidates() {
                return Candidates;
            }

            public void setCandidates(List<CandidatesBean> Candidates) {
                this.Candidates = Candidates;
            }

            public static class FaceRectBean {
                /**
                 * X : 139
                 * Y : 59
                 * Width : 124
                 * Height : 162
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

            public static class CandidatesBean {
                /**
                 * PersonId : person1
                 * FaceId :
                 * Score : 100
                 * PersonName : null
                 * Gender : null
                 * PersonGroupInfos : null
                 */

                private String PersonId;
                private String FaceId;
                private String Score;
                private String PersonName;
                private String Gender;
                private String PersonGroupInfos;

                public String getPersonId() {
                    return PersonId;
                }

                public void setPersonId(String PersonId) {
                    this.PersonId = PersonId;
                }

                public String getFaceId() {
                    return FaceId;
                }

                public void setFaceId(String FaceId) {
                    this.FaceId = FaceId;
                }

                public String getScore() {
                    return Score;
                }

                public void setScore(String Score) {
                    this.Score = Score;
                }

                public Object getPersonName() {
                    return PersonName;
                }

                public void setPersonName(String PersonName) {
                    this.PersonName = PersonName;
                }

                public String getGender() {
                    return Gender;
                }

                public void setGender(String Gender) {
                    this.Gender = Gender;
                }

                public Object getPersonGroupInfos() {
                    return PersonGroupInfos;
                }

                public void setPersonGroupInfos(String PersonGroupInfos) {
                    this.PersonGroupInfos = PersonGroupInfos;
                }

                @Override
                public String toString() {
                    return "CandidatesBean{" +
                            "PersonId='" + PersonId + '\'' +
                            ", FaceId='" + FaceId + '\'' +
                            ", Score=" + Score +
                            ", PersonName='" + PersonName + '\'' +
                            ", Gender='" + Gender + '\'' +
                            ", PersonGroupInfos='" + PersonGroupInfos + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "ResultsBean{" +
                        "RetCode=" + RetCode +
                        ", FaceRect=" + FaceRect +
                        ", Candidates=" + Candidates +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ResponseBean{" +
                    "PersonNum=" + PersonNum +
                    ", FaceModelVersion='" + FaceModelVersion + '\'' +
                    ", RequestId='" + RequestId + '\'' +
                    ", Error=" + Error +
                    ", Results=" + Results +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SearchPersonResultBean{" +
                "Response=" + Response +
                '}';
    }
}

