package com.smarthome.magic.model;

import android.util.Log;

public class ChuLiGuZhangMa {


    //返回转化后的故障码
    public static String getGuZhangMa(String ccid, String codeMa) {

        String code = ccid.substring(17, 20);
        String newCode = codeMa;
        switch (code) {
            case "001":
            case "002":

                newCode = zhuanCode(codeMa);

                break;
            case "003":
                break;
            case "004":
                break;
            case "005":
                break;
            case "006":
                break;
            case "007":
                break;
            case "008":
                break;
            case "009":
                break;
            case "010":
                break;
            case "011":
                break;
            case "017":
                newCode = zhunCodeLaona(codeMa);
                break;
            default:
                return codeMa;
        }

        return newCode;
    }

    private static String zhunCodeLaona(String codeMa) {
        String newCode = null;

        if (codeMa.equals("3")) {
            newCode = "5";
        } else if (codeMa.equals("6")) {
            newCode = "8";
        } else if (codeMa.equals("4")) {
            newCode = "2";
        } else if (codeMa.equals("2") || codeMa.equals("1")) {
            newCode = "1";
        } else if (codeMa.equals("7")) {
            newCode = "3";
        } else if (codeMa.equals("8")) {
            newCode = "4";
        } else if (codeMa.equals("9")) {
            newCode = "6";
        } else if (codeMa.equals("5")) {
            newCode = "11";
        } else if (codeMa.equals("11")) {
            newCode = "9";
        } else if (codeMa.equals("10")) {
            newCode = "10";
        } else if (codeMa.equals("18")) {
            newCode = "18";
        } else if (codeMa.equals("15") || codeMa.equals("16")) {
            newCode = "12";
        }

        Log.i("ChuLiGuZhangLaona", "codeMa" + codeMa);
        Log.i("ChuLiGuZhangLaona", "newCode" + newCode);
        return newCode;
    }

    public static String zhuanCode(String codeMa) {
        String newCode = null;

        if (codeMa.equals("1")) {
            newCode = "5";
        } else if (codeMa.equals("2") || codeMa.equals("3")) {
            newCode = "8";
        } else if (codeMa.equals("5") || codeMa.equals("6")) {
            newCode = "2";
        } else if (codeMa.equals("7") || codeMa.equals("8")) {
            newCode = "1";
        } else if (codeMa.equals("13") || codeMa.equals("14")) {
            newCode = "3";
        } else if (codeMa.equals("10") || codeMa.equals("11")) {
            newCode = "4";
        } else if (codeMa.equals("9")) {
            newCode = "6";
        } else if (codeMa.equals("12")) {
            newCode = "11";
        } else if (codeMa.equals("19")) {
            newCode = "9";
        } else if (codeMa.equals("18")) {
            newCode = "18";
        } else if (codeMa.equals("15") || codeMa.equals("16")) {
            newCode = "12";
        }

        Log.i("ChuLiGuZhang", "codeMa" + codeMa);
        Log.i("ChuLiGuZhang", "newCode" + newCode);
        return newCode;
    }

    public static String codeXinXiShow(String ccid, String newCode) {
        String pinPaiCode = ccid.substring(17, 20);
        String info = null;

            /*
        1.电压过低或过高
        2.油泵开路或短路
        3.壳体传感器开路或短路
        4.进气传感器开路或短路
        5.加热赛开路或短路
        6.入风口温度高
        8.风机开路或短路
        9.熄火
        10.点火失败
        11.壳体温度高
        12.水泵开路或短路
        18.脱机
        */

        switch (newCode) {
            case "1":
                info = "电压过低或过高";
                break;
            case "2":
                info = "油泵开路或短路";
                break;
            case "3":
                info = "壳体传感器开路或短路";
                break;
            case "4":
                info = "进气传感器开路或短路";
                break;
            case "5":
                info = "加热赛开路或短路";
                break;
            case "6":
                info = "入风口温度高";
                break;
            case "8":
                info = "风机开路或短路";
                break;
            case "9":
                info = "熄火";
                if (pinPaiCode.equals("001") || pinPaiCode.equals("002")) {
                    info = "熄火或点火失败";
                }
                break;
            case "10":
                info = "点火失败";
                if (pinPaiCode.equals("001") || pinPaiCode.equals("002")) {
                    info = "熄火或点火失败";
                }
                break;
            case "11":
                info = "壳体温度高";
                break;
            case "12":
                info = "水泵开路或短路";
                break;
            case "18":
                info = "脱机";
                break;
            default:
                info = "未知故障信息";
                break;


        }
        return info;
    }


}
