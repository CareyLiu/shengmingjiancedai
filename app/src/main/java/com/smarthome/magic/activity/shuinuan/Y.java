package com.smarthome.magic.activity.shuinuan;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.lzy.okgo.model.Response;
import com.smarthome.magic.config.MyApplication;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 开发工具类
 */
public class Y {


    private static final boolean open = false;

    public static void i(String str) {
        if (open)
            Log.i("安卓开发", "----------------------------     " + str + "      -------------------------------");
    }

    public static void e(String bodyMsg) {
        if (open) {
            if (bodyMsg.length() > 4000) {
                for (int i = 0; i < bodyMsg.length(); i += 4000) {
                    if (i + 4000 < bodyMsg.length()) {
                        Log.e("安卓长开发", bodyMsg.substring(i, i + 4000));
                    } else {
                        //当前截取的长度已经超过了总长度，则打印出剩下的全部信息
                        Log.e("安卓长开发", bodyMsg.substring(i, bodyMsg.length()));
                    }
                }
            } else {
                //直接打印
                Log.e("安卓开发:", "----------------------------     " + bodyMsg + "      -------------------------------");
            }
        }
    }

    public static void t(String str) {
        Toast.makeText(MyApplication.getInstance().getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    public static void tLong(String str) {
        Toast.makeText(MyApplication.getInstance().getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }

    public static void tError(Response response) {
        tLong(response.getException().getMessage());
    }

    public static Resources getResources() {
        return MyApplication.getInstance().getApplicationContext().getResources();
    }

    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static int getDimen(int resId) {
        return (int) getResources().getDimension(resId);
    }


    public static int getInt(String content) {
        if (!TextUtils.isEmpty(content)) {
            try {
                return (int) getDouble(content);
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static long getLong(String content) {
        if (!TextUtils.isEmpty(content)) {
            try {
                return Long.parseLong(content);
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static double getDouble(String content) {
        if (!TextUtils.isEmpty(content)) {
            try {
                return Double.parseDouble(content);
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static float getFloat(String content) {
        if (!TextUtils.isEmpty(content)) {
            try {
                return Float.parseFloat(content);
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }


    public static String formatNum(float num) {
        String format = new DecimalFormat("#.#").format(num);
        return format;
    }

    /**
     * 获取Money
     */
    public static String getMoney(double money) {
        String format = new DecimalFormat("#.##").format(money);
        return format;
    }

    /**
     * 获取Money
     */
    public static String getMoney(float money) {
        String format = new DecimalFormat("#.##").format(money);
        return format;
    }

    /**
     * 获取Money
     */
    public static BigDecimal getMoneyB(String money) {
        BigDecimal bigDecimal = new BigDecimal(money);
        return bigDecimal;
    }


    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得控件高度
     *
     * @param context
     * @return
     */
    public static int getViewHeght(View view, Context context) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        return height;
    }

    /**
     * 获得控件宽度
     *
     * @param context
     * @return
     */
    public static int getViewWidth(View view, Context context) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int width = view.getMeasuredWidth();
        return width;
    }

    public static void setViewMarginTop(View view, int top) {
        ViewGroup.MarginLayoutParams p = getViewMarginLayoutParams(view);
        if (p != null) {
            p.topMargin = top;
            view.setLayoutParams(p);
        }
    }

    public static void setViewMarginLeft(View view, int left) {
        ViewGroup.MarginLayoutParams p = getViewMarginLayoutParams(view);
        if (p != null) {
            p.leftMargin = left;
            view.setLayoutParams(p);
        }
    }

    public static void setViewMarginBottom(View view, int bottom) {
        ViewGroup.MarginLayoutParams p = getViewMarginLayoutParams(view);
        if (p != null) {
            p.bottomMargin = bottom;
            view.setLayoutParams(p);
        }
    }

    public static void setViewMarginRight(View view, int right) {
        ViewGroup.MarginLayoutParams p = getViewMarginLayoutParams(view);
        if (p != null) {
            p.rightMargin = right;
            view.setLayoutParams(p);
        }
    }

    public static boolean setViewHeight(View view, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            if (params.height != height) {
                params.height = height;
                view.setLayoutParams(params);
            }
            return true;
        }
        return false;
    }

    public static boolean setViewWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            if (params.width != width) {
                params.width = width;
                view.setLayoutParams(params);
            }
            return true;
        }
        return false;
    }


    public static ViewGroup.MarginLayoutParams getViewMarginLayoutParams(View view) {
        ViewGroup.MarginLayoutParams result = null;
        if (view != null) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params != null && params instanceof ViewGroup.MarginLayoutParams) {
                result = (ViewGroup.MarginLayoutParams) params;
            }
        }
        return result;
    }

    /**
     * 隐藏输入法
     *
     * @param view
     */
    public static void hideInputMethod(View view) {
        hideInputMethod(view, MyApplication.getAppContext());
    }

    public static void hideInputMethod(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 获取日期
     */
    public static Date getData(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date parse = format.parse(date);
            return parse;
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    /**
     * 获取日期精确到秒
     */
    public static String getDataS(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return format.format(date);
    }

    /**
     * 获取日期精确
     */
    public static String getData(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return format.format(date);
    }
}
