/**
 *
 */
package com.smarthome.magic.model;

import java.util.List;

/**
 * 登陆用户信息
 * @author yantong
 * 下午3:14:17
 */
public class LoginUser {


    /**
     * msg_code : 0000
     * msg : ok
     * row_num : 1
     * data : [{"of_user_id":"673","app_token":"1560151A87045300I000H000R000P0","user_name":"未设置","power_state_name":"车主","token_rong":"","accid":"wit_673","user_id_key":"104n105x106D125g139c112o107D114n111g","server_id":"01/","power_state":"1"}]
     */

    private String msg_code;
    private String msg;
    private String row_num;
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

    public String getRow_num() {
        return row_num;
    }

    public void setRow_num(String row_num) {
        this.row_num = row_num;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * of_user_id : 673
         * app_token : 1560151A87045300I000H000R000P0
         * user_name : 未设置
         * power_state_name : 车主
         * token_rong :
         * accid : wit_673
         * user_id_key : 104n105x106D125g139c112o107D114n111g
         * server_id : 01/
         * power_state : 1
         * is_manage : 储物柜管理员 1是  2不是
         */

        private String of_user_id;
        private String app_token;
        private String user_name;
        private String power_state_name;
        private String token_rong;
        private String accid;
        private String user_id_key;
        private String server_id;
        private String power_state;
        private String subsystem_id;
        public String invitation_code_state;
        public String is_manage;

        public String getIs_manage() {
            return is_manage;
        }

        public void setIs_manage(String is_manage) {
            this.is_manage = is_manage;
        }

        public String getSubsystem_id() {
            return subsystem_id;
        }

        public void setSubsystem_id(String subsystem_id) {
            this.subsystem_id = subsystem_id;
        }

        public String getOf_user_id() {
            return of_user_id;
        }

        public void setOf_user_id(String of_user_id) {
            this.of_user_id = of_user_id;
        }

        public String getApp_token() {
            return app_token;
        }

        public void setApp_token(String app_token) {
            this.app_token = app_token;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getPower_state_name() {
            return power_state_name;
        }

        public void setPower_state_name(String power_state_name) {
            this.power_state_name = power_state_name;
        }

        public String getToken_rong() {
            return token_rong;
        }

        public void setToken_rong(String token_rong) {
            this.token_rong = token_rong;
        }

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getUser_id_key() {
            return user_id_key;
        }

        public void setUser_id_key(String user_id_key) {
            this.user_id_key = user_id_key;
        }

        public String getServer_id() {
            return server_id;
        }

        public void setServer_id(String server_id) {
            this.server_id = server_id;
        }

        public String getPower_state() {
            return power_state;
        }

        public void setPower_state(String power_state) {
            this.power_state = power_state;
        }
    }
}