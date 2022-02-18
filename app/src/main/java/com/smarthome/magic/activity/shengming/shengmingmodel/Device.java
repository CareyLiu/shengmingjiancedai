package com.smarthome.magic.activity.shengming.shengmingmodel;

public class Device {


    /**
     * code : 0000
     * msg : null
     * data : {"id":23073,"mac":"149C7656FFB1","alias":"149C7656FFB1","softVer":"1.0","online":1,"remark":null,"version":"1.0E","orgId":165,"srcHost":"221.178.127.67"}
     */

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
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
         * id : 23073
         * mac : 149C7656FFB1
         * alias : 149C7656FFB1
         * softVer : 1.0
         * online : 1
         * remark : null
         * version : 1.0E
         * orgId : 165
         * srcHost : 221.178.127.67
         */

        private int id;
        private String mac;
        private String alias;
        private String softVer;
        private int online;
        private Object remark;
        private String version;
        private int orgId;
        private String srcHost;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getSoftVer() {
            return softVer;
        }

        public void setSoftVer(String softVer) {
            this.softVer = softVer;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getOrgId() {
            return orgId;
        }

        public void setOrgId(int orgId) {
            this.orgId = orgId;
        }

        public String getSrcHost() {
            return srcHost;
        }

        public void setSrcHost(String srcHost) {
            this.srcHost = srcHost;
        }
    }
}
