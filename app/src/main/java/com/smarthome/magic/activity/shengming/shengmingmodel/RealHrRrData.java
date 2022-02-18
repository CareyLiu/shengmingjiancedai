package com.smarthome.magic.activity.shengming.shengmingmodel;

public class RealHrRrData {


    /**
     * code : 0000
     * msg : null
     * data : {"mac":"149C7656FFB1","hr":73,"rr":11,"status":-1,"time":"2022-01-20 12:55:27"}
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
         * mac : 149C7656FFB1
         * hr : 73
         * rr : 11
         * status : -1
         * time : 2022-01-20 12:55:27
         */

        private String mac;
        private int hr;
        private int rr;
        private int status;
        private String time;

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public int getHr() {
            return hr;
        }

        public void setHr(int hr) {
            this.hr = hr;
        }

        public int getRr() {
            return rr;
        }

        public void setRr(int rr) {
            this.rr = rr;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
