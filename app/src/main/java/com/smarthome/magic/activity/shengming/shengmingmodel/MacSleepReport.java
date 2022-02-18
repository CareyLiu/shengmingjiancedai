package com.smarthome.magic.activity.shengming.shengmingmodel;

import java.util.List;

public class MacSleepReport {


    /**
     * code : 0000
     * msg : null
     * data : {"sleepData":{"sleepData":[{"value":0,"s":"20220113140333","e":"20220113140333"},{"value":0,"s":"20220113140406","e":"20220113140439"}],"wakeDuraSec":0,"remDuraMin":0,"lightDuraMin":0,"deepDuraMin":0,"sleepTime":null,"wakeTime":null,"remRange":"72 - 135分钟","wakeRange":"18 - 27分钟","lightRange":"170 - 324分钟","deepRange":"100 - 124分钟","score":0},"hrData":{"hrData":[[{"value":86,"time":"20220113140333"},{"value":83,"time":"20220113140406"},{"value":80,"time":"20220113140409"},{"value":79,"time":"20220113140412"},{"value":77,"time":"20220113140415"},{"value":76,"time":"20220113140418"},{"value":73,"time":"20220113140421"},{"value":70,"time":"20220113140424"},{"value":68,"time":"20220113140427"},{"value":68,"time":"20220113140430"},{"value":65,"time":"20220113140433"},{"value":65,"time":"20220113140436"},{"value":64,"time":"20220113140439"},{"value":63,"time":"20220113140442"}]],"slowList":[],"quickList":[],"max":83,"min":83,"avg":83,"slowNum":0,"quickNum":0,"startTime":"20220113140333","longHr":null,"currentHr":null,"hrScore":-1,"deathScore":-1,"hrDc":100},"rrData":{"rrData":[[{"value":22,"time":"20220113140303"},{"value":0,"time":"20220113140306"},{"value":0,"time":"20220113140309"},{"value":0,"time":"20220113140312"},{"value":28,"time":"20220113140315"},{"value":0,"time":"20220113140318"},{"value":0,"time":"20220113140321"},{"value":0,"time":"20220113140324"},{"value":10,"time":"20220113140327"},{"value":11,"time":"20220113140330"},{"value":11,"time":"20220113140333"},{"value":0,"time":"20220113140336"},{"value":0,"time":"20220113140339"},{"value":0,"time":"20220113140342"},{"value":0,"time":"20220113140345"},{"value":0,"time":"20220113140348"},{"value":0,"time":"20220113140351"},{"value":0,"time":"20220113140354"},{"value":0,"time":"20220113140357"},{"value":0,"time":"20220113140400"},{"value":0,"time":"20220113140403"},{"value":0,"time":"20220113140406"},{"value":0,"time":"20220113140409"},{"value":0,"time":"20220113140412"},{"value":0,"time":"20220113140415"},{"value":0,"time":"20220113140418"},{"value":0,"time":"20220113140421"},{"value":16,"time":"20220113140424"},{"value":16,"time":"20220113140427"},{"value":16,"time":"20220113140430"},{"value":16,"time":"20220113140433"},{"value":0,"time":"20220113140436"},{"value":0,"time":"20220113140439"},{"value":0,"time":"20220113140442"}]],"slowList":[],"quickList":[],"stopList":[],"rrStopList":[],"heatGraph":null,"max":28,"min":28,"avg":28,"slowNum":0,"quickNum":0,"stopNum":0,"startTime":"20220113140303","ahi":0,"lastAhi":null,"rrStopCheck":-1,"rrScore":0,"langScore":0,"rrPulseRatio":0,"lastRrPulseRatio":null},"statusData":{"monitorData":[{"status":1,"s":"20220113140330","e":"20220113140412"},{"status":1,"s":"20220113140415","e":"20220113140439"}],"moveData":[{"status":2,"s":"20220113140412","e":"20220113140415"},{"status":2,"s":"20220113140439","e":"20220113140442"}],"leaveData":[{"status":0,"s":"20220113140211","e":"20220113140330"}],"errData":[],"leaveExData":[],"moveTimes":0,"moveMaxDura":0,"moveMinDura":0,"moveDuraAvg":0,"leaveTimes":0,"leaveMaxDura":0,"leaveMinDura":0,"leaveExceptionTime":0,"startTime":"20220113140211","normalMoveDura":3,"actualMoveDura":6,"moveAndSleepRatio":16,"statusScore":0,"asleepTime":"0","wakeTime":"0","lastTime":"20220113140442"},"pressureData":{"data":[],"max":-1,"min":-1,"avg":-1,"avgLf":0,"avgHf":0,"mood":-1,"ans":0,"lastAns":null,"mentalScore":-1,"avgHr":-1,"lastAvgHr":null}}
     */

    private String code;
    private Object msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sleepData : {"sleepData":[{"value":0,"s":"20220113140333","e":"20220113140333"},{"value":0,"s":"20220113140406","e":"20220113140439"}],"wakeDuraSec":0,"remDuraMin":0,"lightDuraMin":0,"deepDuraMin":0,"sleepTime":null,"wakeTime":null,"remRange":"72 - 135分钟","wakeRange":"18 - 27分钟","lightRange":"170 - 324分钟","deepRange":"100 - 124分钟","score":0}
         * hrData : {"hrData":[[{"value":86,"time":"20220113140333"},{"value":83,"time":"20220113140406"},{"value":80,"time":"20220113140409"},{"value":79,"time":"20220113140412"},{"value":77,"time":"20220113140415"},{"value":76,"time":"20220113140418"},{"value":73,"time":"20220113140421"},{"value":70,"time":"20220113140424"},{"value":68,"time":"20220113140427"},{"value":68,"time":"20220113140430"},{"value":65,"time":"20220113140433"},{"value":65,"time":"20220113140436"},{"value":64,"time":"20220113140439"},{"value":63,"time":"20220113140442"}]],"slowList":[],"quickList":[],"max":83,"min":83,"avg":83,"slowNum":0,"quickNum":0,"startTime":"20220113140333","longHr":null,"currentHr":null,"hrScore":-1,"deathScore":-1,"hrDc":100}
         * rrData : {"rrData":[[{"value":22,"time":"20220113140303"},{"value":0,"time":"20220113140306"},{"value":0,"time":"20220113140309"},{"value":0,"time":"20220113140312"},{"value":28,"time":"20220113140315"},{"value":0,"time":"20220113140318"},{"value":0,"time":"20220113140321"},{"value":0,"time":"20220113140324"},{"value":10,"time":"20220113140327"},{"value":11,"time":"20220113140330"},{"value":11,"time":"20220113140333"},{"value":0,"time":"20220113140336"},{"value":0,"time":"20220113140339"},{"value":0,"time":"20220113140342"},{"value":0,"time":"20220113140345"},{"value":0,"time":"20220113140348"},{"value":0,"time":"20220113140351"},{"value":0,"time":"20220113140354"},{"value":0,"time":"20220113140357"},{"value":0,"time":"20220113140400"},{"value":0,"time":"20220113140403"},{"value":0,"time":"20220113140406"},{"value":0,"time":"20220113140409"},{"value":0,"time":"20220113140412"},{"value":0,"time":"20220113140415"},{"value":0,"time":"20220113140418"},{"value":0,"time":"20220113140421"},{"value":16,"time":"20220113140424"},{"value":16,"time":"20220113140427"},{"value":16,"time":"20220113140430"},{"value":16,"time":"20220113140433"},{"value":0,"time":"20220113140436"},{"value":0,"time":"20220113140439"},{"value":0,"time":"20220113140442"}]],"slowList":[],"quickList":[],"stopList":[],"rrStopList":[],"heatGraph":null,"max":28,"min":28,"avg":28,"slowNum":0,"quickNum":0,"stopNum":0,"startTime":"20220113140303","ahi":0,"lastAhi":null,"rrStopCheck":-1,"rrScore":0,"langScore":0,"rrPulseRatio":0,"lastRrPulseRatio":null}
         * statusData : {"monitorData":[{"status":1,"s":"20220113140330","e":"20220113140412"},{"status":1,"s":"20220113140415","e":"20220113140439"}],"moveData":[{"status":2,"s":"20220113140412","e":"20220113140415"},{"status":2,"s":"20220113140439","e":"20220113140442"}],"leaveData":[{"status":0,"s":"20220113140211","e":"20220113140330"}],"errData":[],"leaveExData":[],"moveTimes":0,"moveMaxDura":0,"moveMinDura":0,"moveDuraAvg":0,"leaveTimes":0,"leaveMaxDura":0,"leaveMinDura":0,"leaveExceptionTime":0,"startTime":"20220113140211","normalMoveDura":3,"actualMoveDura":6,"moveAndSleepRatio":16,"statusScore":0,"asleepTime":"0","wakeTime":"0","lastTime":"20220113140442"}
         * pressureData : {"data":[],"max":-1,"min":-1,"avg":-1,"avgLf":0,"avgHf":0,"mood":-1,"ans":0,"lastAns":null,"mentalScore":-1,"avgHr":-1,"lastAvgHr":null}
         */

        private SleepDataBeanX sleepData;
        private HrDataBeanX hrData;
        private RrDataBeanX rrData;
        private StatusDataBean statusData;
        private PressureDataBean pressureData;

        public SleepDataBeanX getSleepData() {
            return sleepData;
        }

        public void setSleepData(SleepDataBeanX sleepData) {
            this.sleepData = sleepData;
        }

        public HrDataBeanX getHrData() {
            return hrData;
        }

        public void setHrData(HrDataBeanX hrData) {
            this.hrData = hrData;
        }

        public RrDataBeanX getRrData() {
            return rrData;
        }

        public void setRrData(RrDataBeanX rrData) {
            this.rrData = rrData;
        }

        public StatusDataBean getStatusData() {
            return statusData;
        }

        public void setStatusData(StatusDataBean statusData) {
            this.statusData = statusData;
        }

        public PressureDataBean getPressureData() {
            return pressureData;
        }

        public void setPressureData(PressureDataBean pressureData) {
            this.pressureData = pressureData;
        }

        public static class SleepDataBeanX {

            /**
             * sleepData : [{"value":0,"s":"20220113140333","e":"20220113140333"},{"value":0,"s":"20220113140406","e":"20220113140439"}]
             * wakeDuraSec : 0
             * remDuraMin : 0
             * lightDuraMin : 0
             * deepDuraMin : 0
             * sleepTime : null
             * wakeTime : null
             * remRange : 72 - 135分钟
             * wakeRange : 18 - 27分钟
             * lightRange : 170 - 324分钟
             * deepRange : 100 - 124分钟
             * score : 0
             */

            private int wakeDuraSec;
            private int remDuraMin;
            private int lightDuraMin;
            private int deepDuraMin;
            private String sleepTime;
            private String wakeTime;
            private String remRange;
            private String wakeRange;
            private String lightRange;
            private String deepRange;
            private int score;
            private List<SleepDataBean> sleepData;

            public int getWakeDuraSec() {
                return wakeDuraSec;
            }

            public void setWakeDuraSec(int wakeDuraSec) {
                this.wakeDuraSec = wakeDuraSec;
            }

            public int getRemDuraMin() {
                return remDuraMin;
            }

            public void setRemDuraMin(int remDuraMin) {
                this.remDuraMin = remDuraMin;
            }

            public int getLightDuraMin() {
                return lightDuraMin;
            }

            public void setLightDuraMin(int lightDuraMin) {
                this.lightDuraMin = lightDuraMin;
            }

            public int getDeepDuraMin() {
                return deepDuraMin;
            }

            public void setDeepDuraMin(int deepDuraMin) {
                this.deepDuraMin = deepDuraMin;
            }

            public String getSleepTime() {
                return sleepTime;
            }

            public void setSleepTime(String sleepTime) {
                this.sleepTime = sleepTime;
            }

            public String getWakeTime() {
                return wakeTime;
            }

            public void setWakeTime(String wakeTime) {
                this.wakeTime = wakeTime;
            }

            public String getRemRange() {
                return remRange;
            }

            public void setRemRange(String remRange) {
                this.remRange = remRange;
            }

            public String getWakeRange() {
                return wakeRange;
            }

            public void setWakeRange(String wakeRange) {
                this.wakeRange = wakeRange;
            }

            public String getLightRange() {
                return lightRange;
            }

            public void setLightRange(String lightRange) {
                this.lightRange = lightRange;
            }

            public String getDeepRange() {
                return deepRange;
            }

            public void setDeepRange(String deepRange) {
                this.deepRange = deepRange;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public List<SleepDataBean> getSleepData() {
                return sleepData;
            }

            public void setSleepData(List<SleepDataBean> sleepData) {
                this.sleepData = sleepData;
            }

            public static class SleepDataBean {
                /**
                 * value : 0
                 * s : 20220113140333
                 * e : 20220113140333
                 */

                private int value;
                private String s;
                private String e;

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                public String getS() {
                    return s;
                }

                public void setS(String s) {
                    this.s = s;
                }

                public String getE() {
                    return e;
                }

                public void setE(String e) {
                    this.e = e;
                }
            }

            @Override
            public String toString() {
                return "SleepDataBeanX{" +
                        "wakeDuraSec(清醒时长)=" + wakeDuraSec +
                        ", remDuraMin(rem时长)=" + remDuraMin +
                        ", lightDuraMin(浅睡时长)=" + lightDuraMin +
                        ", deepDuraMin(深睡时长)=" + deepDuraMin +
                        ", sleepTime='" + sleepTime + '\'' +
                        ", wakeTime='" + wakeTime + '\'' +
                        ", remRange='" + remRange + '\'' +
                        ", wakeRange='" + wakeRange + '\'' +
                        ", lightRange='" + lightRange + '\'' +
                        ", deepRange='" + deepRange + '\'' +
                        ", score=" + score +
                        ", sleepData=" + sleepData +
                        '}';
            }
        }

        public static class HrDataBeanX {
            /**
             * hrData : [[{"value":86,"time":"20220113140333"},{"value":83,"time":"20220113140406"},{"value":80,"time":"20220113140409"},{"value":79,"time":"20220113140412"},{"value":77,"time":"20220113140415"},{"value":76,"time":"20220113140418"},{"value":73,"time":"20220113140421"},{"value":70,"time":"20220113140424"},{"value":68,"time":"20220113140427"},{"value":68,"time":"20220113140430"},{"value":65,"time":"20220113140433"},{"value":65,"time":"20220113140436"},{"value":64,"time":"20220113140439"},{"value":63,"time":"20220113140442"}]]
             * slowList : []
             * quickList : []
             * max : 83
             * min : 83
             * avg : 83
             * slowNum : 0
             * quickNum : 0
             * startTime : 20220113140333
             * longHr : null
             * currentHr : null
             * hrScore : -1
             * deathScore : -1
             * hrDc : 100.0
             */

            private int max;
            private int min;
            private int avg;
            private int slowNum;
            private int quickNum;
            private String startTime;
            private Object longHr;
            private Object currentHr;
            private int hrScore;
            private int deathScore;
            private double hrDc;
            private List<List<HrDataBean>> hrData;
            private List<?> slowList;
            private List<?> quickList;

            public int getMax() {
                return max;
            }

            public void setMax(int max) {
                this.max = max;
            }

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }

            public int getAvg() {
                return avg;
            }

            public void setAvg(int avg) {
                this.avg = avg;
            }

            public int getSlowNum() {
                return slowNum;
            }

            public void setSlowNum(int slowNum) {
                this.slowNum = slowNum;
            }

            public int getQuickNum() {
                return quickNum;
            }

            public void setQuickNum(int quickNum) {
                this.quickNum = quickNum;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public Object getLongHr() {
                return longHr;
            }

            public void setLongHr(Object longHr) {
                this.longHr = longHr;
            }

            public Object getCurrentHr() {
                return currentHr;
            }

            public void setCurrentHr(Object currentHr) {
                this.currentHr = currentHr;
            }

            public int getHrScore() {
                return hrScore;
            }

            public void setHrScore(int hrScore) {
                this.hrScore = hrScore;
            }

            public int getDeathScore() {
                return deathScore;
            }

            public void setDeathScore(int deathScore) {
                this.deathScore = deathScore;
            }

            public double getHrDc() {
                return hrDc;
            }

            public void setHrDc(double hrDc) {
                this.hrDc = hrDc;
            }

            public List<List<HrDataBean>> getHrData() {
                return hrData;
            }

            public void setHrData(List<List<HrDataBean>> hrData) {
                this.hrData = hrData;
            }

            public List<?> getSlowList() {
                return slowList;
            }

            public void setSlowList(List<?> slowList) {
                this.slowList = slowList;
            }

            public List<?> getQuickList() {
                return quickList;
            }

            public void setQuickList(List<?> quickList) {
                this.quickList = quickList;
            }

            public static class HrDataBean {
                /**
                 * value : 86
                 * time : 20220113140333
                 */

                private int value;
                private String time;

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }
            }


            @Override
            public String toString() {
                return "HrDataBeanX{" +
                        "max(最高心率)=" + max +
                        ", min(最低心率)=" + min +
                        ", avg(平均心率)=" + avg +
                        ", slowNum(异常心率过缓次数)=" + slowNum +
                        ", quickNum(异常心率过快次数)=" + quickNum +
                        ", startTime='" + startTime + '\'' +
                        ", longHr=" + longHr +
                        ", currentHr=" + currentHr +
                        ", hrScore=" + hrScore +
                        ", deathScore=" + deathScore +
                        ", hrDc=" + hrDc +
                        ", hrData=" + hrData +
                        ", slowList=" + slowList +
                        ", quickList=" + quickList +
                        '}';
            }
        }

        public static class RrDataBeanX {
            /**
             * rrData : [[{"value":22,"time":"20220113140303"},{"value":0,"time":"20220113140306"},{"value":0,"time":"20220113140309"},{"value":0,"time":"20220113140312"},{"value":28,"time":"20220113140315"},{"value":0,"time":"20220113140318"},{"value":0,"time":"20220113140321"},{"value":0,"time":"20220113140324"},{"value":10,"time":"20220113140327"},{"value":11,"time":"20220113140330"},{"value":11,"time":"20220113140333"},{"value":0,"time":"20220113140336"},{"value":0,"time":"20220113140339"},{"value":0,"time":"20220113140342"},{"value":0,"time":"20220113140345"},{"value":0,"time":"20220113140348"},{"value":0,"time":"20220113140351"},{"value":0,"time":"20220113140354"},{"value":0,"time":"20220113140357"},{"value":0,"time":"20220113140400"},{"value":0,"time":"20220113140403"},{"value":0,"time":"20220113140406"},{"value":0,"time":"20220113140409"},{"value":0,"time":"20220113140412"},{"value":0,"time":"20220113140415"},{"value":0,"time":"20220113140418"},{"value":0,"time":"20220113140421"},{"value":16,"time":"20220113140424"},{"value":16,"time":"20220113140427"},{"value":16,"time":"20220113140430"},{"value":16,"time":"20220113140433"},{"value":0,"time":"20220113140436"},{"value":0,"time":"20220113140439"},{"value":0,"time":"20220113140442"}]]
             * slowList : []
             * quickList : []
             * stopList : []
             * rrStopList : []
             * heatGraph : null
             * max : 28
             * min : 28
             * avg : 28
             * slowNum : 0
             * quickNum : 0
             * stopNum : 0
             * startTime : 20220113140303
             * ahi : 0.0
             * lastAhi : null
             * rrStopCheck : -1
             * rrScore : 0
             * langScore : 0
             * rrPulseRatio : 0.0
             * lastRrPulseRatio : null
             */

            private Object heatGraph;
            private int max;
            private int min;
            private int avg;
            private int slowNum;
            private int quickNum;
            private int stopNum;
            private String startTime;
            private double ahi;
            private Object lastAhi;
            private int rrStopCheck;
            private int rrScore;
            private int langScore;
            private double rrPulseRatio;
            private Object lastRrPulseRatio;
            private List<List<RrDataBean>> rrData;
            private List<?> slowList;
            private List<?> quickList;
            private List<?> stopList;
            private List<?> rrStopList;

            public Object getHeatGraph() {
                return heatGraph;
            }

            public void setHeatGraph(Object heatGraph) {
                this.heatGraph = heatGraph;
            }

            public int getMax() {
                return max;
            }

            public void setMax(int max) {
                this.max = max;
            }

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }

            public int getAvg() {
                return avg;
            }

            public void setAvg(int avg) {
                this.avg = avg;
            }

            public int getSlowNum() {
                return slowNum;
            }

            public void setSlowNum(int slowNum) {
                this.slowNum = slowNum;
            }

            public int getQuickNum() {
                return quickNum;
            }

            public void setQuickNum(int quickNum) {
                this.quickNum = quickNum;
            }

            public int getStopNum() {
                return stopNum;
            }

            public void setStopNum(int stopNum) {
                this.stopNum = stopNum;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public double getAhi() {
                return ahi;
            }

            public void setAhi(double ahi) {
                this.ahi = ahi;
            }

            public Object getLastAhi() {
                return lastAhi;
            }

            public void setLastAhi(Object lastAhi) {
                this.lastAhi = lastAhi;
            }

            public int getRrStopCheck() {
                return rrStopCheck;
            }

            public void setRrStopCheck(int rrStopCheck) {
                this.rrStopCheck = rrStopCheck;
            }

            public int getRrScore() {
                return rrScore;
            }

            public void setRrScore(int rrScore) {
                this.rrScore = rrScore;
            }

            public int getLangScore() {
                return langScore;
            }

            public void setLangScore(int langScore) {
                this.langScore = langScore;
            }

            public double getRrPulseRatio() {
                return rrPulseRatio;
            }

            public void setRrPulseRatio(double rrPulseRatio) {
                this.rrPulseRatio = rrPulseRatio;
            }

            public Object getLastRrPulseRatio() {
                return lastRrPulseRatio;
            }

            public void setLastRrPulseRatio(Object lastRrPulseRatio) {
                this.lastRrPulseRatio = lastRrPulseRatio;
            }

            public List<List<RrDataBean>> getRrData() {
                return rrData;
            }

            public void setRrData(List<List<RrDataBean>> rrData) {
                this.rrData = rrData;
            }

            public List<?> getSlowList() {
                return slowList;
            }

            public void setSlowList(List<?> slowList) {
                this.slowList = slowList;
            }

            public List<?> getQuickList() {
                return quickList;
            }

            public void setQuickList(List<?> quickList) {
                this.quickList = quickList;
            }

            public List<?> getStopList() {
                return stopList;
            }

            public void setStopList(List<?> stopList) {
                this.stopList = stopList;
            }

            public List<?> getRrStopList() {
                return rrStopList;
            }

            public void setRrStopList(List<?> rrStopList) {
                this.rrStopList = rrStopList;
            }

            public static class RrDataBean {
                /**
                 * value : 22
                 * time : 20220113140303
                 */

                private int value;
                private String time;

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }
            }
        }

        public static class StatusDataBean {
            /**
             * monitorData : [{"status":1,"s":"20220113140330","e":"20220113140412"},{"status":1,"s":"20220113140415","e":"20220113140439"}]
             * moveData : [{"status":2,"s":"20220113140412","e":"20220113140415"},{"status":2,"s":"20220113140439","e":"20220113140442"}]
             * leaveData : [{"status":0,"s":"20220113140211","e":"20220113140330"}]
             * errData : []
             * leaveExData : []
             * moveTimes : 0
             * moveMaxDura : 0
             * moveMinDura : 0
             * moveDuraAvg : 0
             * leaveTimes : 0
             * leaveMaxDura : 0
             * leaveMinDura : 0
             * leaveExceptionTime : 0
             * startTime : 20220113140211
             * normalMoveDura : 3
             * actualMoveDura : 6
             * moveAndSleepRatio : 16
             * statusScore : 0
             * asleepTime : 0
             * wakeTime : 0
             * lastTime : 20220113140442
             */

            private int moveTimes;
            private int moveMaxDura;
            private int moveMinDura;
            private int moveDuraAvg;
            private int leaveTimes;
            private int leaveMaxDura;
            private int leaveMinDura;
            private int leaveExceptionTime;
            private String startTime;
            private int normalMoveDura;
            private int actualMoveDura;
            private int moveAndSleepRatio;
            private int statusScore;
            private String asleepTime;
            private String wakeTime;
            private String lastTime;
            private List<MonitorDataBean> monitorData;
            private List<MoveDataBean> moveData;
            private List<LeaveDataBean> leaveData;
            private List<?> errData;
            private List<?> leaveExData;

            public int getMoveTimes() {
                return moveTimes;
            }

            public void setMoveTimes(int moveTimes) {
                this.moveTimes = moveTimes;
            }

            public int getMoveMaxDura() {
                return moveMaxDura;
            }

            public void setMoveMaxDura(int moveMaxDura) {
                this.moveMaxDura = moveMaxDura;
            }

            public int getMoveMinDura() {
                return moveMinDura;
            }

            public void setMoveMinDura(int moveMinDura) {
                this.moveMinDura = moveMinDura;
            }

            public int getMoveDuraAvg() {
                return moveDuraAvg;
            }

            public void setMoveDuraAvg(int moveDuraAvg) {
                this.moveDuraAvg = moveDuraAvg;
            }

            public int getLeaveTimes() {
                return leaveTimes;
            }

            public void setLeaveTimes(int leaveTimes) {
                this.leaveTimes = leaveTimes;
            }

            public int getLeaveMaxDura() {
                return leaveMaxDura;
            }

            public void setLeaveMaxDura(int leaveMaxDura) {
                this.leaveMaxDura = leaveMaxDura;
            }

            public int getLeaveMinDura() {
                return leaveMinDura;
            }

            public void setLeaveMinDura(int leaveMinDura) {
                this.leaveMinDura = leaveMinDura;
            }

            public int getLeaveExceptionTime() {
                return leaveExceptionTime;
            }

            public void setLeaveExceptionTime(int leaveExceptionTime) {
                this.leaveExceptionTime = leaveExceptionTime;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public int getNormalMoveDura() {
                return normalMoveDura;
            }

            public void setNormalMoveDura(int normalMoveDura) {
                this.normalMoveDura = normalMoveDura;
            }

            public int getActualMoveDura() {
                return actualMoveDura;
            }

            public void setActualMoveDura(int actualMoveDura) {
                this.actualMoveDura = actualMoveDura;
            }

            public int getMoveAndSleepRatio() {
                return moveAndSleepRatio;
            }

            public void setMoveAndSleepRatio(int moveAndSleepRatio) {
                this.moveAndSleepRatio = moveAndSleepRatio;
            }

            public int getStatusScore() {
                return statusScore;
            }

            public void setStatusScore(int statusScore) {
                this.statusScore = statusScore;
            }

            public String getAsleepTime() {
                return asleepTime;
            }

            public void setAsleepTime(String asleepTime) {
                this.asleepTime = asleepTime;
            }

            public String getWakeTime() {
                return wakeTime;
            }

            public void setWakeTime(String wakeTime) {
                this.wakeTime = wakeTime;
            }

            public String getLastTime() {
                return lastTime;
            }

            public void setLastTime(String lastTime) {
                this.lastTime = lastTime;
            }

            public List<MonitorDataBean> getMonitorData() {
                return monitorData;
            }

            public void setMonitorData(List<MonitorDataBean> monitorData) {
                this.monitorData = monitorData;
            }

            public List<MoveDataBean> getMoveData() {
                return moveData;
            }

            public void setMoveData(List<MoveDataBean> moveData) {
                this.moveData = moveData;
            }

            public List<LeaveDataBean> getLeaveData() {
                return leaveData;
            }

            public void setLeaveData(List<LeaveDataBean> leaveData) {
                this.leaveData = leaveData;
            }

            public List<?> getErrData() {
                return errData;
            }

            public void setErrData(List<?> errData) {
                this.errData = errData;
            }

            public List<?> getLeaveExData() {
                return leaveExData;
            }

            public void setLeaveExData(List<?> leaveExData) {
                this.leaveExData = leaveExData;
            }

            public static class MonitorDataBean {
                /**
                 * status : 1
                 * s : 20220113140330
                 * e : 20220113140412
                 */

                private int status;
                private String s;
                private String e;

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public String getS() {
                    return s;
                }

                public void setS(String s) {
                    this.s = s;
                }

                public String getE() {
                    return e;
                }

                public void setE(String e) {
                    this.e = e;
                }
            }

            public static class MoveDataBean {
                /**
                 * status : 2
                 * s : 20220113140412
                 * e : 20220113140415
                 */

                private int status;
                private String s;
                private String e;

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public String getS() {
                    return s;
                }

                public void setS(String s) {
                    this.s = s;
                }

                public String getE() {
                    return e;
                }

                public void setE(String e) {
                    this.e = e;
                }
            }

            public static class LeaveDataBean {
                /**
                 * status : 0
                 * s : 20220113140211
                 * e : 20220113140330
                 */

                private int status;
                private String s;
                private String e;

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public String getS() {
                    return s;
                }

                public void setS(String s) {
                    this.s = s;
                }

                public String getE() {
                    return e;
                }

                public void setE(String e) {
                    this.e = e;
                }
            }
        }

        public static class PressureDataBean {
            /**
             * data : []
             * max : -1
             * min : -1
             * avg : -1
             * avgLf : 0.0
             * avgHf : 0.0
             * mood : -1.0
             * ans : 0
             * lastAns : null
             * mentalScore : -1
             * avgHr : -1
             * lastAvgHr : null
             */

            private int max;
            private int min;
            private int avg;
            private double avgLf;
            private double avgHf;
            private double mood;
            private int ans;
            private Object lastAns;
            private int mentalScore;
            private int avgHr;
            private Object lastAvgHr;
            private List<?> data;

            public int getMax() {
                return max;
            }

            public void setMax(int max) {
                this.max = max;
            }

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }

            public int getAvg() {
                return avg;
            }

            public void setAvg(int avg) {
                this.avg = avg;
            }

            public double getAvgLf() {
                return avgLf;
            }

            public void setAvgLf(double avgLf) {
                this.avgLf = avgLf;
            }

            public double getAvgHf() {
                return avgHf;
            }

            public void setAvgHf(double avgHf) {
                this.avgHf = avgHf;
            }

            public double getMood() {
                return mood;
            }

            public void setMood(double mood) {
                this.mood = mood;
            }

            public int getAns() {
                return ans;
            }

            public void setAns(int ans) {
                this.ans = ans;
            }

            public Object getLastAns() {
                return lastAns;
            }

            public void setLastAns(Object lastAns) {
                this.lastAns = lastAns;
            }

            public int getMentalScore() {
                return mentalScore;
            }

            public void setMentalScore(int mentalScore) {
                this.mentalScore = mentalScore;
            }

            public int getAvgHr() {
                return avgHr;
            }

            public void setAvgHr(int avgHr) {
                this.avgHr = avgHr;
            }

            public Object getLastAvgHr() {
                return lastAvgHr;
            }

            public void setLastAvgHr(Object lastAvgHr) {
                this.lastAvgHr = lastAvgHr;
            }

            public List<?> getData() {
                return data;
            }

            public void setData(List<?> data) {
                this.data = data;
            }
        }
    }
}
