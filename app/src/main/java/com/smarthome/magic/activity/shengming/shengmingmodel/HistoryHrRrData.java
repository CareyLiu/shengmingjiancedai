package com.smarthome.magic.activity.shengming.shengmingmodel;

import java.util.List;

public class HistoryHrRrData {


    /**
     * code : 0000
     * msg : null
     * data : {"rrData":[[{"value":0,"time":"20220120120101"},{"value":0,"time":"20220120120140"}],[{"value":0,"time":"20220120121358"},{"value":23,"time":"20220120121600"},{"value":8,"time":"20220120121800"},{"value":12,"time":"20220120121803"}],[{"value":0,"time":"20220120122132"},{"value":0,"time":"20220120122141"}],[{"value":0,"time":"20220120123725"},{"value":0,"time":"20220120123737"}],[{"value":0,"time":"20220120124938"},{"value":8,"time":"20220120125138"},{"value":23,"time":"20220120125338"},{"value":10,"time":"20220120125502"}]],"hrData":[[{"value":86,"time":"20220120121412"},{"value":71,"time":"20220120121612"},{"value":72,"time":"20220120121803"}],[{"value":69,"time":"20220120124944"},{"value":71,"time":"20220120125144"},{"value":73,"time":"20220120125344"},{"value":72,"time":"20220120125502"}]]}
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
        private List<List<RrDataBean>> rrData;
        private List<List<HrDataBean>> hrData;

        public List<List<RrDataBean>> getRrData() {
            return rrData;
        }

        public void setRrData(List<List<RrDataBean>> rrData) {
            this.rrData = rrData;
        }

        public List<List<HrDataBean>> getHrData() {
            return hrData;
        }

        public void setHrData(List<List<HrDataBean>> hrData) {
            this.hrData = hrData;
        }

        public static class RrDataBean {
            /**
             * value : 0
             * time : 20220120120101
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

        public static class HrDataBean {
            /**
             * value : 86
             * time : 20220120121412
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
}
