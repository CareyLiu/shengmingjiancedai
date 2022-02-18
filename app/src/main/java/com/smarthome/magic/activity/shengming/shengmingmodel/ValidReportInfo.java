package com.smarthome.magic.activity.shengming.shengmingmodel;

import java.util.List;

public class ValidReportInfo {

    /**
     * code : 0000
     * msg : null
     * data : [{"userId":null,"nickname":null,"phone":null,"deviceId":23073,"mac":"149C7656FFB1","date":"20220113"},{"userId":null,"nickname":null,"phone":null,"deviceId":23754,"mac":"CD0BA16E7C60","date":"20220113"},{"userId":null,"nickname":null,"phone":null,"deviceId":23750,"mac":"44113D1C36C7","date":"20220113"}]
     */

    private String code;
    private Object msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userId : null
         * nickname : null
         * phone : null
         * deviceId : 23073
         * mac : 149C7656FFB1
         * date : 20220113
         */

        private Object userId;
        private Object nickname;
        private Object phone;
        private int deviceId;
        private String mac;
        private String date;

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public Object getNickname() {
            return nickname;
        }

        public void setNickname(Object nickname) {
            this.nickname = nickname;
        }

        public Object getPhone() {
            return phone;
        }

        public void setPhone(Object phone) {
            this.phone = phone;
        }

        public int getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(int deviceId) {
            this.deviceId = deviceId;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
