package com.smarthome.magic.app;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public interface ConstanceValue {
    String HIDE_LOADING_STATUS_MSG = "hide_loading_status_msg";
    int MSG_CAR_J_G = 0x10000;//g汽车GPS经纬度
    int MSG_CAR_J_M = 0x10001;//M风暖加热器时时数据
    int MSG_CAR_K = 0x0002;//k车门/车窗/车锁
    int MSG_CAR_h = 0x0003;//胎压
    int MSG_CAR_l = 0x0004;//开关
    int MSG_CAR_m = 0x0005;//m汽车其它数据
    int MSG_CAR_n = 0x0006;//硬件监控报警

    int MSG_CAR_p = 0x0007;//硬件日志
    int MSG_CAR_r = 0x0008;//数据直接存储到数据库，一条一条的存好 维护时候可以导出数据库数据   常看数据分析问题，内部信号量状态等观察区数据
    int MSG_CAR_s = 0x0009;//掉电不消失数据 设备出厂时候是0  一直累加 到产品生命周期终止
    int MSG_CAR_Z = 0x00010;//状态
    int MSG_GUZHANG = 0X00011;//故障状态
    int MSG_CLEARGUZHANGSUCCESS = 0X00012;//清除故障成功
    int MSG_UNSUB_MQTT = 0X00013;//清除application mqtt订阅的信息
    int MSG_CONNET_MQTT = 0X00014;//重新登录的时候，重连mqtt
    int MSG_CAR_I = 0x10015;//获取主机的时时信息
    int MSG_CAR_HUI_FU_CHU_CHAGN = 0x10016;//恢复出厂设置成功
    int MSG_CAR_FEGNYOUBI = 0x10017;//获得风油比参数
    int MSG_WETCHSUCCESS = 0x10018;//微信支付成功
    int MSG_DALIBAO_SUCCESS = 0X10019;

    int MSG_SETFZONGHE = 0x10020;//点击综合显示的项目

    int MSG_TUANGOUPAY = 0x10021;//团购支付
    int MSG_DIYONGQUAN = 0x10022;//返回
    int MSG_SAOMASUCCESS = 0x10023;//支付成功
    int MSG_SAOMAFAILE = 0x10024;//支付失败


    int MSG_XINTUANYOU_PAY = 0x10025;//新团油支付成功
    int MSG_XINTUANYOU_PAY_FAIL = 0x10026;//新团友支付失败


    int MSG_ZIYING_PAY = 0x10027;//自营支付
    int MSG_ZIYING_PAY_FAIL = 0x10028;//自营支付失败
    int MSG_GETADDRESS = 0x10029;//获得收货地址
    int MSG_NONEADDRESS = 0x10030;//没有收货地址
    int MSG_APK_DOWNLOADSUCCESS = 0x10031;//apk更新完毕执行安装
    int MSG_DINGDAN_PAY = 0x10032;//执行订单支付操作

    int MSG_PAY_SUCCESS_REFRESH_WODE = 0x10033;//支付成功刷新接口

    int MSG_KT_DATA = 0x10034;//水暖加热器数据
    int MSG_SN_DATA = 0x10035;//水暖加热器数据
    int MSG_DAILISHANG_TIXIAN = 0x10036;//提现
    int MSG_ADD_CHELIANG_SUCCESS = 0x10037;//添加车辆成功


    /**
     * 智能家居部分
     */
    int MSG_FAMILY_MANAGE_ADD = 0x10038;//家庭管理，创建家庭
    int MSG_FAMILY_MANAGE_CHANGENAME = 0x10039;//家庭管理，更改家庭名字
    int MSG_PEIWANG_SUCCESS = 0x10041;//配网成功
    int MSG_ROOM_MANAGE_ADD = 0x10042;//家庭管理，新建家庭名字
    int MSG_ROOM_MANAGE_CHANGENAME = 0x10043;//家庭管理，更改家庭名字
    int MSG_ROOM_DEVICE_CHANGENAME = 0x10044;//家庭管理，更改家庭名字

    /**
     * MQTT 连接
     */
    int MSG_MQTT_CONNECTCOMPLETE = 0x10045;//连接完成
    int MSG_MQTT_CONNECTLOST = 0x10046;//连接丢失
    int MSG_MQTT_CONNECTARRIVE = 0x10047;//收到连接
    int MSG_MQTT_CONNECT_CHONGLIAN_ONSUCCESS = 0x10048;//重连成功
    int MSG_MQTT_CONNECT_CHONGLIAN_ONFAILE = 0x10049;//重连失败


    int MSG_ZHINENGJIAJU_SHOUYE_SHUAXIN = 0x10050;//智能家居首页刷新
    int MSG_RONGYUN_STATE = 0x10051;//连接状态


    int MSG_BAZI_FSBJ1 = 0x10052;//风水摆件绑定成功
    int MSG_BAZI_FSBJ2 = 0x10053;//风水摆件绑定成功2
    int MSG_SERVICE_CHAT = 0x10054;//服务跳转

    int MSG_RONGYUN_REVICE = 0x10055;//接收聊天消息
    int MSG_GUZHANG_SHOUYE = 0x10056;//故障到首页
    int MSG_GOTOXIAOXI = 0x10057;//跳转到消息页
    int MSG_P = 0x10058;//接收到了p.
    int MSG_ZHINENGJIAJU = 0x10059;//跳转到智能家居


    int MSG_K6111 = 0x10060;//接收到了档位开机指令
    int MSG_K6131 = 0x10061;//接收到关机指令
    int MSG_K6121 = 0x10062;//接收到了空调开机
    int MSG_K6141 = 0x10063;//接收到了水泵模式
    int MSG_K6161 = 0x10064;//接收到了预泵油模式
    int MSG_K6171 = 0x10065;//接收到了预通风模式

    int MSG_ZHILINGMA = 0x10066;//指令码
    int MSG_LOGIN = 0x10067;//登录
    int MSG_JIEBANG = 0x10068;//解绑设备
    int MSG_SHUA = 0x10069;//设备刷新
    int MSG_N9_WEILIANJIE = 0x10070;//实时数据未连接
    int MSG_N9_LIANJIE = 0x10071;//实时数据已连接
    int MSG_GDIAN = 0x10072;//g.
    int MSG_DANGWEIGUANJI = 0x10073;//档位关机
    int MSG_DANGWEIKAIJI = 0x10074;//档位开机

    int MSG_KONGTIAOKAIJI = 0x10075;//空调开机
    int MSG_KONGTIAOGUANJI = 0x10076;//空调关机
    int MSG_C_P = 0x10077;//c_p 获得当前的
    int MSG_YUTONGFENGKAIJI = 0x10078;//预通风开机
    int MSG_YUTONGFENGGUANJI = 0x10079;//预通风关机

    int MSG_BEGNYOUKAIJI = 0x10080;//泵油开机
    int MSG_BENGYOUGUANJI = 0x10081;//泵油关机
    int MSG_RONGYUN_CHONGZHI = 0x10082;//重置融云

    int MSG_GONGXIANG_PEOPLE = 0x10083;//共享成员

    int MSG_ZCKT = 0x10084;//空调
    int MSG_KAQUAN_DUIHUAN = 0x10085;//卡券兑换
    int MSG_SHEBEIZHUANGTAI = 0x10086;//开灯
    int MSG_ZHINENGJIAJUGUANDENG = 0x10087;//关灯
    int MSG_ZHINENGJIAJU_ZHUJI = 0x10088;//主机在线与离线
    int MSG_ZHINENGJIAJU_MENCI = 0x10089;//门磁进入
    int MSG_ZHINENGJIAJU_MENCIPAGE = 0x10090;//门磁页面


    int MSG_WIFI_CLOSE = 0x10091;//wifi关闭
    int MSG_WIFI_INFO = 0x10092;//wifi信息
    int MSG_WIFI_SET = 0x10093;//wifi信息
    int MSG_DEVICE_ADD = 0x10094;//添加设备
    int MSG_DEVICE_DATA = 0x10095;//dp数据更新
    int MSG_DEVICE_REMOVED = 0x10096;//设备移除回调
    int MSG_DEVICE_STATUSCHANGED = 0x10097;//设备上下线回调
    int MSG_DEVICE_NETWORKSTATUSCHANGED = 0x10098;//网络状态发生变动时的回调
    int MSG_DEVICE_DEVINFOUPDATE = 0x10099;//设备信息更新回调
    int MSG_CAMERA_FAIL = 0x10100;//摄像机通知失败
    int MSG_DEVICE_ROOM_NAME_CHANGE = 0x10101;//摄像机名字改变
    int MSG_DEVICE_DELETE = 0x10102;//移除设备
    int MSG_DEVICE_DINGSHI = 0x10104;//定时成功

    int MSG_YUYINHUANXING = 0x100105;//语音唤醒
    int MSG_YUYINKAIQITONGZHI = 0x100106;//开启语音
    int MSG_YUYINGUANBITONGZHI = 0x100107;//关闭语音
    int MSG_YUYINXIAOSHI = 0x100108;//界面消失

    int MSG_DEVICE_DINGSHI_CHONGFU = 0x10109;//定时设置重复

    int MSG_TUYA_TIANQI = 0x10110;//涂鸦天气设置
    int MSG_RIQICHONGFU = 0x10111;//涂鸦天气设置
    int MSG_ZHINENGJIAJU_SHEBEILIEBIAO = 0x10112;//智能家居单个设备列表信息
    int MSG_ZHINENGJIAJU_ICON = 0x10113;//智能家居单个设备列表信息
    int MSG_DINGSHI_NEED = 0x10114;//定时使用
    int MSG_TIANJIASHEBEI = 0x10115;//添加设备基本参数
    int MSG_TIANJIAZHUJI = 0x10116;//添加主机
    int MSG_PEIWNAG_ESPTOUCH = 0x10117;//配网type
    int MSG_PEIWANG_ERROR = 0x10118;//配网错误
    int MSG_TIANJIAZHUJI_ZIDONG = 0x10119;//添加主机自动
    int MSG_TIANJIASHEBEI_ZIDONG = 0x10120;//添加设备自动

    int MSG_DEVICE_DELETE_TUYA = 0x10121;//移除设备完全
    int MSG_SENDCODE_HUIDIAO = 0x10122;//发送验证码给dialog的回调

    int MSG_KAIGUAN_DELETE = 0x10123;//删除开关
    int MSG_ZHUJIBANG_OTHER = 0x10124;//他人绑主机验证

    int MSG_ZHINENGJIAJU_ZI_SHUAXIN = 0x10125;//智能家居子页面刷新
    int MSG_CAOZUODONGTAISHITI = 0x10126;//操作动态实体
    int MSG_XIUGAIDONGTAISHITIFINISH = 0x10127;//修改动态实体成功

    int MSG_ZHUJICHONGZHI = 0x10128;//主机重置
    int MSG_DELETE_FAMILY = 0x10129;//删除家庭


    int MSG_WANNENGYAOKONGQIPEIWANG = 0x10130;//遥控器配网
    int MSG_WANNENGYAOKONGQI_CODE_PEIDUI = 0x10131;//万能遥控器配网码
    int MSG_WANNENGYAOKONGQI_CODE_PEIDUI_ZIDINGYI = 0x10132;//万能遥控器配网码1
    int MSG_WANNENGYAOKONGQI_CODE_DELETE = 0x10133;//万能遥控器配网码

    int MSG_BIANMINFABU_HUICHUANDIZHI = 0x10134;//回传地址
    int MSG_TONGYONG_INPUT = 0x10135;//通用输入
    int MSG_FUWUGONGZHONG = 0x10136;//服务工种

    int MSG_ADD_BANNER = 0x10137;//添加轮播图
    int MSG_NONEZHINENGJIAJU = 0x10138;//刷新不重新创建时，重新走程序
    int MSG_ZHUJIXIUGAIXINXI = 0x10139;//主机修改信息成功或失败

    int MSG_NETWORK_CHANGE = 0x10140;//网络监听发生变化

    int MSG_SHOUYELIEBIAO = 0x10141;//智能家居设备列表

    int MSG_CHUWUGUI_DIANJIJIFEI = 0x10142;//点击确定跳支付

    int MSG_SHENGMIN_HOME_BACK= 0x10143;//生命体征带返回首页

}
