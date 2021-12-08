/**
 *
 */
package com.smarthome.magic.config;

import android.content.Context;
import android.util.Log;

import com.smarthome.magic.activity.LoginActivity;
import com.smarthome.magic.model.LoginUser;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;


/**
 * 登陆用户信息管理类
 */
public class UserManager {

    private static UserManager mUserManager;
    private static Context mContext;

    private UserManager(Context ctx) {
        mContext = ctx;
    }

    public static UserManager getManager(Context ctx) {
        if (mUserManager == null) {
            mUserManager = new UserManager(ctx);
        }
        return mUserManager;
    }


    public String getUserId() {
        return PreferenceHelper.getInstance(mContext).getString("of_user_id", "");
    }

    public String getUserIdKey() {
        return PreferenceHelper.getInstance(mContext).getString("user_id_key", "");
    }

    public String getPowerState() {
        return PreferenceHelper.getInstance(mContext).getString("power_state", "");
    }

    public String getAppToken() {
        return PreferenceHelper.getInstance(mContext).getString("app_token", "");
    }


    public String getUserName() {
        return PreferenceHelper.getInstance(mContext).getString("user_name", "");
    }

    public String getRongYun() {
        return PreferenceHelper.getInstance(mContext).getString("token_rong", "");
    }

    public String getIsManage() {
        return PreferenceHelper.getInstance(mContext).getString("is_manage", "");
    }

    //保存用户信息
    public void saveUser(LoginUser.DataBean user) {
        if (user != null) {
            PreferenceHelper.getInstance(mContext).putString("of_user_id", user.getOf_user_id());
            PreferenceHelper.getInstance(mContext).putString("app_token", user.getApp_token());
            PreferenceHelper.getInstance(mContext).putString("user_name", user.getUser_name());
            PreferenceHelper.getInstance(mContext).putString("power_state_name", user.getPower_state());
            PreferenceHelper.getInstance(mContext).putString("token_rong", user.getToken_rong());
            PreferenceHelper.getInstance(mContext).putString("accid", user.getAccid());
            PreferenceHelper.getInstance(mContext).putString("user_id_key", user.getUser_id_key());
            PreferenceHelper.getInstance(mContext).putString("server_id", user.getServer_id());
            PreferenceHelper.getInstance(mContext).putString("power_state", user.getPower_state());
            PreferenceHelper.getInstance(mContext).putString("is_manage", user.getIs_manage());
        }
    }

    /**
     * 删除用户信息
     */
    public void removeUser() {
        String accid = PreferenceHelper.getInstance(mContext).getString("accid", "");
        JPushInterface.deleteAlias(mContext, 0);
        Set<String> tags = new HashSet<>();
        tags.add(accid);
        JPushInterface.deleteTags(mContext, 0, tags);

        PreferenceHelper.getInstance(mContext).removeKey("of_user_id");
        PreferenceHelper.getInstance(mContext).removeKey("app_token");
        PreferenceHelper.getInstance(mContext).removeKey("user_name");
        PreferenceHelper.getInstance(mContext).removeKey("power_state_name");
        PreferenceHelper.getInstance(mContext).removeKey("token_rong");
        PreferenceHelper.getInstance(mContext).removeKey("accid");
        PreferenceHelper.getInstance(mContext).removeKey("user_id_key");
        PreferenceHelper.getInstance(mContext).removeKey("server_id");
        PreferenceHelper.getInstance(mContext).removeKey("power_state");
        PreferenceHelper.getInstance(mContext).removeKey("car_server_id");
    }

}