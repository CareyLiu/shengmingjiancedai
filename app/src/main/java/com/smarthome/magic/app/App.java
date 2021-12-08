package com.smarthome.magic.app;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;

import android.text.TextUtils;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import androidx.multidex.MultiDexApplication;

import com.smarthome.magic.common.MethodsCompat;
import com.smarthome.magic.common.StringUtils;
import com.smarthome.magic.common.UIHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * /**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 */
public class App extends MultiDexApplication {
    private static App mAppApplication;
    private static final String tag = App.class.getSimpleName();
    private static App instance;

    public static final String JINGDU = "JINGDU";
    public static final String WEIDU = "WEIDU";

    public static final String DALIBAO_PAY = "DALIBAO_PAY";//大礼包支付
    public static final String TUANGOU_PAY = "TUANGOU_PAY";
    public static final String MAIDAN_PAY = "MAIDAN_PAY";

    public static final String XINTUANYOU_PAY = "XinTUanYouPay";
    public static final String ZIYING_PAY = "ziYingPay";

    public static final String SAOMA_PAY = "SAOMAPAY";
    public static final String BAZI_PAY = "BAZI_PAY";


    public static final String KUAIDIFEI = "0X1111";
    public static final String PRODUCTID = "0X1112";//商品id
    public static final String KUAIDITYPE = "0X1113";//商品id
    public static final String SHIFOUYOUSHANGJI = "0X1114";//是否有上级 0 没有 1 有
    public static final String CUNCHUBIND_ALIPAY = "0X1115";//是否绑定过阿里的支付宝账号用于提现
    public static final String CUNCHU_ZHIFUMIMA = "0X1116";//是否设置过支付密码
    public static final String DINGDANZHIFU = "0X1117";//我得订单列表里面调支付
    public static final String CUNCHUBIND_WEIXINPAY = "0X1118";//是否绑定过微信支付用于提现
    public static final String CUN_GEREN_TOUXIANG = "0X1119";//存储个人头像
    public static final String CHOOSE_KONGZHI_XIANGMU = "0X1120";//我选择硬件控制项目
    public static final String WEIYUQUANSHU = "0X1121";//喂鱼圈数
    public static final String HAS_ZHUJI = "0X1122";//是否家庭里有主机

    public static final String ADDRESS = "0X1123";

    //发送通知
    public static final int TOKEN_ERROR = 0x04;
    public static final int NETWORK_ERROR = 0x05;

    public static String ifFinish = "0";//0不返回测试,1返回测试
    public static final int PAGE_SIZE = 20;//默认分页大小
    private static final int CACHE_TIME = 60 * 60000;//缓存失效时间
    public static Long companyid; //店铺的id  传入测试界面
    // 用于存放获取短信倒计时时间
    public static Map<String, Long> time_map;


    public static final int time = 60 * 1000;//缓存失效时间
    public static final int ANIMATION_TIME = 5000;//访问网络间隔
    // 用于存放城市名
    public static String city_name;

    public static String DeviceId = null;
    private boolean login = false;    //登录状态
    private long loginUid = 0;    //登录用户的id
    private Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();

    public String saveImagePath;//保存图片路径


    public int getSCREEN_W() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public int getSCREEN_H() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

//    public int SCREEN_W = 0;
//    public int SCREEN_H = 0;

    //客服SDK是否已经初始化过了
    public static boolean isKFSDK = false;
    public static String KFACCESSID = "f1e35050-1ffe-11e7-9bc9-7df331fd6b9a";
    //网易云  商家端用户accid前缀mfsj
    public static String ACCID_PREFIX = "mfsj";//测试环境 IM前缀   #网易云商家命名前缀
    //    public static String ACCID_PREFIX = "mfjxs";//正式环境 IM前缀  #网易云商家命名前缀

    public static String ACCID_PREFIX_XFZ = "mfhy";//测试环境 IM前缀   #网易云消费者命名前缀
    //    public static String ACCID_PREFIX_XFZ = "mfxfz";//正式环境 IM前缀  #网易云消费者命名前缀


    public boolean isLive = false;  //全局变量isActive = false 记录当前已经进入后台

    private Handler unLoginHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == TOKEN_ERROR) {
                UIHelper.showLogin(App.getInstance(), TOKEN_ERROR);
            }
        }
    };

    private final Handler unNetWorkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == NETWORK_ERROR) {
                UIHelper.ToastMessage(App.getInstance(), "网络中断");
            }
        }
    };

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    /**
     * 获取Application
     */
    public static App getApp() {
        return mAppApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppApplication = this;
        instance = this;

    }


    public boolean IsFistRun() {
        String str = getProperty(AppConfig.CONF_FIRST_RUN);
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        return false;
    }

    public boolean isGuide(String version) {
        String str = getProperty(AppConfig.CONF_VERSION_LOOK);
        if (!TextUtils.isEmpty(str)) {
            if (str.equals(version)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public static App getInstance() {
        return instance;
    }


    /**
     * 检测当前系统声音是否为正常模式
     *
     * @return
     */
    public boolean isAudioNormal() {
        AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    /**
     * 应用程序是否发出提示音
     *
     * @return
     */
    public boolean isAppSound() {
        return isAudioNormal() && isVoice();
    }


    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) info = new PackageInfo();
        return info;
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }


    public boolean getCity() {
        String city = getProperty(AppConfig.CONF_CITY);
        if (StringUtils.isEmpty(city)) {
            return false;
        }
        return true;
    }


    /**
     * 用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * 获取登录用户id
     *
     * @return
     */
    public long getLoginUid() {
        return this.loginUid;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        this.cleanCookie();
        this.login = false;
        this.loginUid = 0;
    }


    /**
     * 未登录或修改密码后的处理
     */
    public Handler getUnLoginHandler() {
        return this.unLoginHandler;
    }

    /**
     * 网络中断后的处理
     */
    public Handler getUnNetWorkHandler() {
        return unNetWorkHandler;
    }


    /**
     * 获取用户头像
     *
     * @param key
     * @return
     * @throws AppException
     */
    public Bitmap getUserFace(String key) throws AppException {
        FileInputStream fis = null;
        try {
            fis = openFileInput(key);
            return BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            throw AppException.run(e);
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 是否加载显示文章图片
     *
     * @return
     */
    public boolean isLoadImage() {
        String perf_loadimage = getProperty(AppConfig.CONF_LOAD_IMAGE);
        //默认是加载的
        if (StringUtils.isEmpty(perf_loadimage))
            return true;
        else
            return StringUtils.toBool(perf_loadimage);
    }

    /**
     * 设置是否加载文章图片
     *
     * @param b
     */
    public void setConfigLoadimage(boolean b) {
        setProperty(AppConfig.CONF_LOAD_IMAGE, String.valueOf(b));
    }

    /**
     * 是否发出提示音
     *
     * @return
     */
    public boolean isVoice() {
        String perf_voice = getProperty(AppConfig.CONF_VOICE);
        //默认是开启提示声音
        if (StringUtils.isEmpty(perf_voice))
            return true;
        else
            return StringUtils.toBool(perf_voice);
    }

    /**
     * 设置是否发出提示音
     *
     * @param b
     */
    public void setConfigVoice(boolean b) {
        setProperty(AppConfig.CONF_VOICE, String.valueOf(b));
    }

    /**
     * 是否启动检查更新
     *
     * @return
     */
    public boolean isCheckUp() {
        String perf_checkup = getProperty(AppConfig.CONF_CHECKUP);
        //默认是开启
        if (StringUtils.isEmpty(perf_checkup))
            return true;
        else
            return StringUtils.toBool(perf_checkup);
    }

    /**
     * 设置启动检查更新
     *
     * @param b
     */
    public void setConfigCheckUp(boolean b) {
        setProperty(AppConfig.CONF_CHECKUP, String.valueOf(b));
    }

    /**
     * 是否左右滑动
     *
     * @return
     */
    public boolean isScroll() {
        String perf_scroll = getProperty(AppConfig.CONF_SCROLL);
        //默认是关闭左右滑动
        if (StringUtils.isEmpty(perf_scroll))
            return false;
        else
            return StringUtils.toBool(perf_scroll);
    }

    /**
     * 设置是否左右滑动
     *
     * @param b
     */
    public void setConfigScroll(boolean b) {
        setProperty(AppConfig.CONF_SCROLL, String.valueOf(b));
    }

    /**
     * 是否Https登录
     *
     * @return
     */
    public boolean isHttpsLogin() {
        String perf_httpslogin = getProperty(AppConfig.CONF_HTTPS_LOGIN);
        //默认是http
        if (StringUtils.isEmpty(perf_httpslogin))
            return false;
        else
            return StringUtils.toBool(perf_httpslogin);
    }

    /**
     * 设置是是否Https登录
     *
     * @param b
     */
    public void setConfigHttpsLogin(boolean b) {
        setProperty(AppConfig.CONF_HTTPS_LOGIN, String.valueOf(b));
    }

    /**
     * 清除保存的缓存
     */
    public void cleanCookie() {
        removeProperty(AppConfig.CONF_ACCESSTOKEN);
    }

    /**
     * 判断缓存数据是否可读
     *
     * @param cachefile
     * @return
     */
    private boolean isReadDataCache(String cachefile) {
        return readObject(cachefile) != null;
    }

    /**
     * 判断缓存是否存在
     *
     * @param cachefile
     * @return
     */
    private boolean isExistDataCache(String cachefile) {
        boolean exist = false;
        File data = getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 判断缓存是否失效
     *
     * @param cachefile
     * @return
     */
    public boolean isCacheDataFailure(String cachefile) {
        boolean failure = false;
        File data = getFileStreamPath(cachefile);
        if (data.exists() && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
            failure = true;
        else if (!data.exists())
            failure = true;
        return failure;
    }


    public static void clearCookies(Context context) {
// Edge case: an illegal state exception is thrown if an instance of
// CookieSyncManager has not be created. CookieSyncManager is normally
// created by SettingActivity_back WebKit view, but this might happen if you activity_start the
// app, restore saved state, and click logout before running SettingActivity_back UI
// dialog in SettingActivity_back WebView -- in which case the app crashes
        @SuppressWarnings("unused")
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        //清除webview缓存
        clearCookies(this);
        //清除数据缓存
        clearCacheFolder(getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(getCacheDir(), System.currentTimeMillis());
        //2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            clearCacheFolder(MethodsCompat.getExternalCacheDir(this), System.currentTimeMillis());
        }
        //清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
    }

    /**
     * 清除缓存目录
     *
     * @param dir     目录
     * @param curTime 当前系统时间
     * @return
     */
    private int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    /**
     * 将对象保存到内存缓存中
     *
     * @param key
     * @param value
     */
    public void setMemCache(String key, Object value) {
        memCacheRegion.put(key, value);
    }

    /**
     * 从内存缓存中获取对象
     *
     * @param key
     * @return
     */
    public Object getMemCache(String key) {
        return memCacheRegion.get(key);
    }

    /**
     * 保存磁盘缓存
     *
     * @param key
     * @param value
     * @throws IOException
     */
    public void setDiskCache(String key, String value) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("cache_" + key + ".data", Context.MODE_PRIVATE);
            fos.write(value.getBytes());
            fos.flush();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取磁盘缓存数据
     *
     * @param key
     * @return
     * @throws IOException
     */
    public String getDiskCache(String key) throws IOException {
        FileInputStream fis = null;
        try {
            fis = openFileInput("cache_" + key + ".data");
            byte[] datas = new byte[fis.available()];
            fis.read(datas);
            return new String(datas);
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 保存对象
     *
     * @param ser
     * @param file
     * @throws IOException
     */
    public boolean saveObject(Serializable ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = openFileOutput(file, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     * @throws IOException
     */
    public Serializable readObject(String file) {
        if (!isExistDataCache(file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            //反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key) {
        return AppConfig.getAppConfig(this).get(key);
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取内存中保存图片的路径
     *
     * @return
     */
    public String getSaveImagePath() {
        return saveImagePath;
    }

    /**
     * 设置内存中保存图片的路径
     *
     * @return
     */
    public void setSaveImagePath(String saveImagePath) {
        this.saveImagePath = saveImagePath;
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


//************ IM END***********************

    /**
     * 基本权限管理
     */
    public static final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_SMS,
            Manifest.permission.GET_ACCOUNTS
    };
    /**
     * 基本定位权限管理
     */
    public static final String[] BASIC_PERMISSIONS_LOCATION = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    /**
     * 基本存储权限管理
     */
    public static final String[] BASIC_PERMISSIONS_STORAGE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    public static final String CHUWUGUI_PAY = "CHUWUGUI_PAY";
    public static final String CHUWUGUI_TYPE = "CHUWUGUI_TYPE";

}
