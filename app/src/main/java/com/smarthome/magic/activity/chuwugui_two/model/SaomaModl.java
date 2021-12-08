package com.smarthome.magic.activity.chuwugui_two.model;

import java.io.Serializable;
import java.util.List;

public class SaomaModl implements Serializable {


    /**
     * msg_code : 0000
     * msg : ok
     * data : [{"charging_method":"3","device_name":"12313","device_ccid":"00000500160028","pre_storage_duration":"2","sub_strategy_list":[{"create_time":"2021-09-01","lcb_hour":"1","lccs_id":"22","lcb_specification_id":"1","device_box_type_name":"小箱","lcb_unit_price":"1.00","lcb_deposit_money":"10.00","lccss_id":"65"},{"create_time":"2021-09-01","lcb_hour":"1","lccs_id":"22","lcb_specification_id":"2","device_box_type_name":"中箱","lcb_unit_price":"1.00","lcb_deposit_money":"20.00","lccss_id":"66"},{"create_time":"2021-09-01","lcb_hour":"1","lccs_id":"22","lcb_specification_id":"3","device_box_type_name":"大箱","lcb_unit_price":"1.00","lcb_deposit_money":"30.00","lccss_id":"67"},{"create_time":"2021-09-01","lcb_hour":"1","lccs_id":"22","lcb_specification_id":"6","device_box_type_name":"标准箱","lcb_unit_price":"1.00","lcb_deposit_money":"1.00","lccss_id":"68"}],"device_addr":"南岗区"}]
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

    public static class DataBean implements Serializable{
        /**
         * charging_method : 3
         * device_name : 12313
         * device_ccid : 00000500160028
         * pre_storage_duration : 2
         * sub_strategy_list : [{"create_time":"2021-09-01","lcb_hour":"1","lccs_id":"22","lcb_specification_id":"1","device_box_type_name":"小箱","lcb_unit_price":"1.00","lcb_deposit_money":"10.00","lccss_id":"65"},{"create_time":"2021-09-01","lcb_hour":"1","lccs_id":"22","lcb_specification_id":"2","device_box_type_name":"中箱","lcb_unit_price":"1.00","lcb_deposit_money":"20.00","lccss_id":"66"},{"create_time":"2021-09-01","lcb_hour":"1","lccs_id":"22","lcb_specification_id":"3","device_box_type_name":"大箱","lcb_unit_price":"1.00","lcb_deposit_money":"30.00","lccss_id":"67"},{"create_time":"2021-09-01","lcb_hour":"1","lccs_id":"22","lcb_specification_id":"6","device_box_type_name":"标准箱","lcb_unit_price":"1.00","lcb_deposit_money":"1.00","lccss_id":"68"}]
         * device_addr : 南岗区
         */

        private String charging_method;
        private String device_name;
        private String device_ccid;
        private String pre_storage_duration;
        private String device_addr;
        private List<SubStrategyListBean> sub_strategy_list;

        public String getCharging_method() {
            return charging_method;
        }

        public void setCharging_method(String charging_method) {
            this.charging_method = charging_method;
        }

        public String getDevice_name() {
            return device_name;
        }

        public void setDevice_name(String device_name) {
            this.device_name = device_name;
        }

        public String getDevice_ccid() {
            return device_ccid;
        }

        public void setDevice_ccid(String device_ccid) {
            this.device_ccid = device_ccid;
        }

        public String getPre_storage_duration() {
            return pre_storage_duration;
        }

        public void setPre_storage_duration(String pre_storage_duration) {
            this.pre_storage_duration = pre_storage_duration;
        }

        public String getDevice_addr() {
            return device_addr;
        }

        public void setDevice_addr(String device_addr) {
            this.device_addr = device_addr;
        }

        public List<SubStrategyListBean> getSub_strategy_list() {
            return sub_strategy_list;
        }

        public void setSub_strategy_list(List<SubStrategyListBean> sub_strategy_list) {
            this.sub_strategy_list = sub_strategy_list;
        }

        public static class SubStrategyListBean implements Serializable{
            /**
             * create_time : 2021-09-01
             * lcb_hour : 1
             * lccs_id : 22
             * lcb_specification_id : 1
             * device_box_type_name : 小箱
             * lcb_unit_price : 1.00
             * lcb_deposit_money : 10.00
             * lccss_id : 65
             */

            private String create_time;
            private String lcb_hour;
            private String lccs_id;
            private String lcb_specification_id;
            private String device_box_type_name;
            private String lcb_unit_price;
            private String lcb_deposit_money;
            private String lccss_id;
            private boolean isSelect;

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getLcb_hour() {
                return lcb_hour;
            }

            public void setLcb_hour(String lcb_hour) {
                this.lcb_hour = lcb_hour;
            }

            public String getLccs_id() {
                return lccs_id;
            }

            public void setLccs_id(String lccs_id) {
                this.lccs_id = lccs_id;
            }

            public String getLcb_specification_id() {
                return lcb_specification_id;
            }

            public void setLcb_specification_id(String lcb_specification_id) {
                this.lcb_specification_id = lcb_specification_id;
            }

            public String getDevice_box_type_name() {
                return device_box_type_name;
            }

            public void setDevice_box_type_name(String device_box_type_name) {
                this.device_box_type_name = device_box_type_name;
            }

            public String getLcb_unit_price() {
                return lcb_unit_price;
            }

            public void setLcb_unit_price(String lcb_unit_price) {
                this.lcb_unit_price = lcb_unit_price;
            }

            public String getLcb_deposit_money() {
                return lcb_deposit_money;
            }

            public void setLcb_deposit_money(String lcb_deposit_money) {
                this.lcb_deposit_money = lcb_deposit_money;
            }

            public String getLccss_id() {
                return lccss_id;
            }

            public void setLccss_id(String lccss_id) {
                this.lccss_id = lccss_id;
            }
        }
    }
}
