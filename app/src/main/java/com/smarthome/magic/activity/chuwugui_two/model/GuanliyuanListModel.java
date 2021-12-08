package com.smarthome.magic.activity.chuwugui_two.model;

import java.util.List;

public class GuanliyuanListModel  {


    /**
     * msg_code : 0000
     * msg : ok
     * data : [{"device_version":"","create_time":"2021-09-01","device_ccid":"00000500160028","lc_state":"1","sub_inst_id":"460","lms_id":"","device_no":"22","lccs_id":"22","lcs_id":"17","validdate":"2022-09-30","device_name":"12313","iccid":"89860492192070253923","update_time":"2021-11-20","online_state":"2","x":"","go_time":"2020-09-26","y":"","device_addr":"南岗区","update_state":"1","update_times":"1"}]
     */

    private String msg_code;
    private String msg;
    private List<DataBean> data;

    public String getMsg_code() {
        return msg_code;
    }

    public void setMsg_code(String msg_code) {
        this.msg_code = msg_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
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
         * device_version :
         * create_time : 2021-09-01
         * device_ccid : 00000500160028
         * lc_state : 1
         * sub_inst_id : 460
         * lms_id :
         * device_no : 22
         * lccs_id : 22
         * lcs_id : 17
         * validdate : 2022-09-30
         * device_name : 12313
         * iccid : 89860492192070253923
         * update_time : 2021-11-20
         * online_state : 2
         * x :
         * go_time : 2020-09-26
         * y :
         * device_addr : 南岗区
         * update_state : 1
         * update_times : 1
         */

        private String device_version;
        private String create_time;
        private String device_ccid;
        private String lc_state;
        private String sub_inst_id;
        private String lms_id;
        private String device_no;
        private String lccs_id;
        private String lcs_id;
        private String validdate;
        private String device_name;
        private String iccid;
        private String update_time;
        private String online_state;
        private String x;
        private String go_time;
        private String y;
        private String device_addr;
        private String update_state;
        private String update_times;

        public String getDevice_version() {
            return device_version;
        }

        public void setDevice_version(String device_version) {
            this.device_version = device_version;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getDevice_ccid() {
            return device_ccid;
        }

        public void setDevice_ccid(String device_ccid) {
            this.device_ccid = device_ccid;
        }

        public String getLc_state() {
            return lc_state;
        }

        public void setLc_state(String lc_state) {
            this.lc_state = lc_state;
        }

        public String getSub_inst_id() {
            return sub_inst_id;
        }

        public void setSub_inst_id(String sub_inst_id) {
            this.sub_inst_id = sub_inst_id;
        }

        public String getLms_id() {
            return lms_id;
        }

        public void setLms_id(String lms_id) {
            this.lms_id = lms_id;
        }

        public String getDevice_no() {
            return device_no;
        }

        public void setDevice_no(String device_no) {
            this.device_no = device_no;
        }

        public String getLccs_id() {
            return lccs_id;
        }

        public void setLccs_id(String lccs_id) {
            this.lccs_id = lccs_id;
        }

        public String getLcs_id() {
            return lcs_id;
        }

        public void setLcs_id(String lcs_id) {
            this.lcs_id = lcs_id;
        }

        public String getValiddate() {
            return validdate;
        }

        public void setValiddate(String validdate) {
            this.validdate = validdate;
        }

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
        }

        public String getIccid() {
            return iccid;
        }

        public void setIccid(String iccid) {
            this.iccid = iccid;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getOnline_state() {
            return online_state;
        }

        public void setOnline_state(String online_state) {
            this.online_state = online_state;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getGo_time() {
            return go_time;
        }

        public void setGo_time(String go_time) {
            this.go_time = go_time;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public String getDevice_addr() {
            return device_addr;
        }

        public void setDevice_addr(String device_addr) {
            this.device_addr = device_addr;
        }

        public String getUpdate_state() {
            return update_state;
        }

        public void setUpdate_state(String update_state) {
            this.update_state = update_state;
        }

        public String getUpdate_times() {
            return update_times;
        }

        public void setUpdate_times(String update_times) {
            this.update_times = update_times;
        }
    }
}
