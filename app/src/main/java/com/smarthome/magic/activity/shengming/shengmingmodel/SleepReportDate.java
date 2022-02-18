package com.smarthome.magic.activity.shengming.shengmingmodel;

import java.util.List;

public class SleepReportDate {


    /**
     * code : 0000
     * msg : null
     * data : ["20211210","20220106","20220110","20220113","20220114","20220115"]
     */

    private String code;
    private String msg;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
