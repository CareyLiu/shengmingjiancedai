package com.smarthome.magic.activity.chuwugui_two.model;

import java.util.List;

public class GuanliyuanGuiziModel {


    /**
     * msg_code : 0000
     * msg : ok
     * data : [{"device_version":"","create_time":"2021-09-01","device_ccid":"00000500160028","lc_state":"1","sub_inst_id":"460","lms_id":"","device_no":"22","lccs_id":"22","lcs_id":"17","box_list":[{"device_box_type":"1","device_box_id":"275","buy_service_state":"2","create_time":"2021-09-02","device_ccid":"00000500160028","device_box_lock_addr":"0101","device_box_name":"1","device_box_size":"30*30*30","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"276","buy_service_state":"2","create_time":"2021-09-02","device_ccid":"00000500160028","device_box_lock_addr":"0102","device_box_name":"2","device_box_size":"30*30*30","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"1","device_box_id":"279","buy_service_state":"2","create_time":"2021-09-02","device_ccid":"00000500160028","device_box_lock_addr":"0103","device_box_name":"3","device_box_size":"30*30*30","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"280","buy_service_state":"2","create_time":"2021-09-02","device_ccid":"00000500160028","device_box_lock_addr":"0104","device_box_name":"4","device_box_size":"30*30*30","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"303","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0201","device_box_name":"5","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"304","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0202","device_box_name":"6","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"305","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0203","device_box_name":"7","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"306","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0204","device_box_name":"8","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"307","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0205","device_box_name":"9","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"308","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0206","device_box_name":"10","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"309","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0207","device_box_name":"11","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"310","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0208","device_box_name":"12","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"311","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0209","device_box_name":"13","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"}]}]
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
         * box_list : [{"device_box_type":"1","device_box_id":"275","buy_service_state":"2","create_time":"2021-09-02","device_ccid":"00000500160028","device_box_lock_addr":"0101","device_box_name":"1","device_box_size":"30*30*30","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"276","buy_service_state":"2","create_time":"2021-09-02","device_ccid":"00000500160028","device_box_lock_addr":"0102","device_box_name":"2","device_box_size":"30*30*30","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"1","device_box_id":"279","buy_service_state":"2","create_time":"2021-09-02","device_ccid":"00000500160028","device_box_lock_addr":"0103","device_box_name":"3","device_box_size":"30*30*30","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"280","buy_service_state":"2","create_time":"2021-09-02","device_ccid":"00000500160028","device_box_lock_addr":"0104","device_box_name":"4","device_box_size":"30*30*30","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"303","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0201","device_box_name":"5","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"304","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0202","device_box_name":"6","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"305","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0203","device_box_name":"7","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"306","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0204","device_box_name":"8","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"307","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0205","device_box_name":"9","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"308","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0206","device_box_name":"10","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"309","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0207","device_box_name":"11","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"310","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0208","device_box_name":"12","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"},{"device_box_type":"2","device_box_id":"311","buy_service_state":"2","create_time":"2021-11-13","device_ccid":"00000500160028","device_box_lock_addr":"0209","device_box_name":"13","device_box_size":"20*20*20","box_state":"1","device_box_use_state":"1","device_box_state":"1"}]
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
        private List<BoxListBean> box_list;

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

        public List<BoxListBean> getBox_list() {
            return box_list;
        }

        public void setBox_list(List<BoxListBean> box_list) {
            this.box_list = box_list;
        }

        public static class BoxListBean {
            /**
             * device_box_type : 1
             * device_box_id : 275
             * buy_service_state : 2
             * create_time : 2021-09-02
             * device_ccid : 00000500160028
             * device_box_lock_addr : 0101
             * device_box_name : 1
             * device_box_size : 30*30*30
             * box_state : 1
             * device_box_use_state : 1
             * device_box_state : 1
             */

            private String device_box_type;
            private String device_box_id;
            private String buy_service_state;
            private String create_time;
            private String device_ccid;
            private String device_box_lock_addr;
            private String device_box_name;
            private String device_box_size;
            private String box_state;
            private String device_box_use_state;
            private String device_box_state;

            public String getDevice_box_type() {
                return device_box_type;
            }

            public void setDevice_box_type(String device_box_type) {
                this.device_box_type = device_box_type;
            }

            public String getDevice_box_id() {
                return device_box_id;
            }

            public void setDevice_box_id(String device_box_id) {
                this.device_box_id = device_box_id;
            }

            public String getBuy_service_state() {
                return buy_service_state;
            }

            public void setBuy_service_state(String buy_service_state) {
                this.buy_service_state = buy_service_state;
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

            public String getDevice_box_lock_addr() {
                return device_box_lock_addr;
            }

            public void setDevice_box_lock_addr(String device_box_lock_addr) {
                this.device_box_lock_addr = device_box_lock_addr;
            }

            public String getDevice_box_name() {
                return device_box_name;
            }

            public void setDevice_box_name(String device_box_name) {
                this.device_box_name = device_box_name;
            }

            public String getDevice_box_size() {
                return device_box_size;
            }

            public void setDevice_box_size(String device_box_size) {
                this.device_box_size = device_box_size;
            }

            public String getBox_state() {
                return box_state;
            }

            public void setBox_state(String box_state) {
                this.box_state = box_state;
            }

            public String getDevice_box_use_state() {
                return device_box_use_state;
            }

            public void setDevice_box_use_state(String device_box_use_state) {
                this.device_box_use_state = device_box_use_state;
            }

            public String getDevice_box_state() {
                return device_box_state;
            }

            public void setDevice_box_state(String device_box_state) {
                this.device_box_state = device_box_state;
            }
        }
    }
}
