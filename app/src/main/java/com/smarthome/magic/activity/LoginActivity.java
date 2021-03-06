package com.smarthome.magic.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.AppConfig;
import com.smarthome.magic.app.AppManager;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.DialogCallback;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.common.StringUtils;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.BuTianYaoQingMaDialog;
import com.smarthome.magic.dialog.FuWuDialog;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.LoginUser;
import com.smarthome.magic.model.Message;
import com.smarthome.magic.util.TimeCount;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IResultCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import pub.devrel.easypermissions.EasyPermissions;
import rx.functions.Action1;

import static com.smarthome.magic.get_net.Urls.SERVER_URL;


public class LoginActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ed_phone)
    EditText mEtPhone;
    @BindView(R.id.ed_pwd)
    EditText mEtPwdCode;
    @BindView(R.id.get_code)
    TextView mTvGetCode;
    @BindView(R.id.ll_pwd)
    LinearLayout llPwd;
    @BindView(R.id.tv_switch)
    TextView tv_switch;
    @BindView(R.id.tv_zhaohui)
    TextView tvZhaohui;
    @BindView(R.id.ll_qiehuan)
    LinearLayout llQiehuan;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.tv_yinsi)
    TextView tvYinsi;
    @BindView(R.id.tv_yonghu)
    TextView tvYonghushiyong;
    @BindView(R.id.iv_tongyi)
    ImageView ivTongyi;
    @BindView(R.id.ll_tongyi)
    LinearLayout llTongyi;

    private boolean isExit;
    private TimeCount timeCount;
    private String req_type = "2";
    private String smsId;
    FuWuDialog fuWuDialog;

    public static List<LoginUser.DataBean> userlist = new ArrayList<>();
    Response<AppResponse<LoginUser.DataBean>> response;

    @Override
    public int getContentViewResId() {
        return R.layout.act_login;
    }

    @Override
    public void initImmersion() {
        mImmersionBar.with(this).statusBarColor(R.color.white).init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        StatusBarUtil.setLightMode(this);
        timeCount = new TimeCount(60000, 1000, mTvGetCode);
        mEtPhone.setText(PreferenceHelper.getInstance(this).getString("user_phone", ""));
        String strToken = getIntent().getStringExtra("token_guoqi");
        if (strToken != null) {
            //??????????????????
            TishiDialog tishiDialog = new TishiDialog(LoginActivity.this, 3, new TishiDialog.TishiDialogListener() {
                @Override
                public void onClickCancel(View v, TishiDialog dialog) {

                }

                @Override
                public void onClickConfirm(View v, TishiDialog dialog) {

                }

                @Override
                public void onDismiss(TishiDialog dialog) {

                }
            });
            tishiDialog.setTextCancel("");
            tishiDialog.setTextConfirm("?????????");
            tishiDialog.setTextContent("????????????????????????????????????????????????????????????????????????????????????");
            tishiDialog.show();
        }


        RxView.clicks(mTvGetCode)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        get_code();
                    }
                });


        tvYonghushiyong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DefaultX5WebViewActivity.actionStart(LoginActivity.this, "https://shop.hljsdkj.com/shop_new/user_agreement");
            }
        });
        tvYinsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DefaultX5WebViewActivity.actionStart(LoginActivity.this, "https://shop.hljsdkj.com/shop_new/privacy_clause");
            }
        });

        String str = PreferenceHelper.getInstance(this).getString(AppConfig.TANCHUFUWUTANKUANG, "");

        if (StringUtils.isEmpty(str)) {
            fuWuDialog = new FuWuDialog(mContext, new FuWuDialog.FuWuDiaLogClikListener() {
                @Override
                public void onClickCancel() {
                    fuWuDialog.dismiss();
                }

                @Override
                public void onClickConfirm() {

                    String[] perms = {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    EasyPermissions.requestPermissions(LoginActivity.this, "????????????app???????????????", 0, perms);
                    fuWuDialog.dismiss();
                }

                @Override
                public void onDismiss(FuWuDialog dialog) {

                }

                @Override
                public void fuwu() {
                    DefaultX5WebViewActivity.actionStart(LoginActivity.this, "https://shop.hljsdkj.com/shop_new/user_agreement");
                }

                @Override
                public void yinsixieyi() {
                    DefaultX5WebViewActivity.actionStart(LoginActivity.this, "https://shop.hljsdkj.com/shop_new/privacy_clause");
                }
            });

            fuWuDialog.setCancelable(false);
            fuWuDialog.show();
            PreferenceHelper.getInstance(this).putString(AppConfig.TANCHUFUWUTANKUANG, "1");
        }

        ivTongyi.setBackgroundResource(R.mipmap.kaquan_select_n);
        llTongyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shifoutongyi.equals("0")) {
                    ivTongyi.setBackgroundResource(R.mipmap.kaquan_select_s);
                    shifoutongyi = "1";
                } else {
                    ivTongyi.setBackgroundResource(R.mipmap.kaquan_select_n);
                    shifoutongyi = "0";
                }

            }
        });
    }

    private String shifoutongyi = "0";//???????????????
    // ??????????????????????????????????????????
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                // showToast("???????????????????????????");
                UIHelper.ToastMessage(this, "???????????????????????????");
                isExit = true;
                new Thread() {
                    public void run() {
                        SystemClock.sleep(3000);
                        isExit = false;
                    }

                }.start();
                return true;
            }
            // finish();
            AppManager.getAppManager().finishAllActivity();
        }
        return super.onKeyDown(keyCode, event);

    }

    @OnClick({R.id.tv_switch, R.id.bt_login, R.id.tv_zhaohui})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_zhaohui:
                LoginYzmActivity.actionStart(this);
                break;
            case R.id.tv_switch:
                String items[] = {getString(R.string.sms_login), getString(R.string.pwd_login)};
                final ActionSheetDialog dialog = new ActionSheetDialog(this, items, null);
                dialog.isTitleShow(false).show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mTvGetCode.setVisibility(View.VISIBLE);
                                mEtPwdCode.setText("");
                                mEtPwdCode.setHint("??????????????????");
                                mEtPwdCode.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                mEtPwdCode.setInputType(InputType.TYPE_CLASS_NUMBER);
                                req_type = "2";
                                break;
                            case 1:
                                mTvGetCode.setVisibility(View.GONE);
                                mEtPwdCode.setText("");
                                mEtPwdCode.setHint("?????????????????????");
                                mEtPwdCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                mEtPwdCode.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                req_type = "1";
                                break;
                        }
                        dialog.dismiss();

                    }
                });

                break;
            case R.id.bt_login:
                if (shifoutongyi.equals("0")) {
                    UIHelper.ToastMessage(mContext, "?????????????????????????????????????????????????????????");
                    return;
                }
                beforehand_login();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userlist.clear();
    }

    /**
     * ?????????????????????
     */
    private void get_code() {
        if (mEtPhone.getText().equals("") || mEtPhone.getText() == null) {
            UIHelper.ToastMessage(this, "????????????????????????");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("code", "00001");
            map.put("key", Urls.key);
            map.put("user_phone", mEtPhone.getText().toString());
            map.put("mod_id", "0315");
            Gson gson = new Gson();
            OkGo.<AppResponse<Message.DataBean>>post(SERVER_URL + "msg")
                    .tag(this)//
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<AppResponse<Message.DataBean>>() {
                        @Override
                        public void onSuccess(Response<AppResponse<Message.DataBean>> response) {
                            Y.t("?????????????????????");
                            timeCount.start();
                            if (response.body().data.size() > 0)
                                smsId = response.body().data.get(0).getSms_id();
                        }

                        @Override
                        public void onError(Response<AppResponse<Message.DataBean>> response) {
                            Y.tError(response);
                            timeCount.cancel();
                            timeCount.onFinish();
                        }
                    });
        }
    }

    /**
     * ?????????
     */
    private void beforehand_login() {
        if (mEtPhone.getText().equals("")) {
            UIHelper.ToastMessage(this, "????????????????????????");
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("code", "00050");
            map.put("key", Urls.key);
            map.put("req_type", req_type);
            map.put("user_phone", mEtPhone.getText().toString());
            map.put("log_type", "1");
            switch (req_type) {
                case "1"://????????????
                    map.put("user_phone", mEtPhone.getText().toString());
                    map.put("user_pwd", mEtPwdCode.getText().toString());
                    break;
                case "2"://?????????????????????
                    map.put("sms_id", smsId);
                    map.put("sms_code", mEtPwdCode.getText().toString());
                    break;
            }

            Gson gson = new Gson();
            OkGo.<AppResponse<LoginUser.DataBean>>post(SERVER_URL + "index/login")
                    .tag(this)//
                    .upJson(gson.toJson(map))
                    .execute(new DialogCallback<AppResponse<LoginUser.DataBean>>(this) {
                        @Override
                        public void onSuccess(Response<AppResponse<LoginUser.DataBean>> response) {
                            userlist.clear();
                            LoginActivity.this.response = response;
                            loginTuya(mEtPhone.getText().toString());
                        }

                        @Override
                        public void onError(Response<AppResponse<LoginUser.DataBean>> response) {
                            Y.tError(response);
                        }
                    });
        }
    }

    private void loginTuya(String phone) {
        TuyaHomeSdk.getUserInstance().loginOrRegisterWithUid("86", phone, "123456", new ILoginCallback() {
            @Override
            public void onSuccess(User user) {
                loginXiayibu();
                loginYoumeng(phone);
            }

            @Override
            public void onError(String code, String error) {
                Y.t("????????????:" + error);
            }
        });
    }

    private void loginYoumeng(String phone) {
        PushAgent.getInstance(this).setAlias(phone, "TUYA_SMART", new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {
                TuyaHomeSdk.getPushInstance().registerDevice(phone, "umeng", new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {

                    }

                    @Override
                    public void onSuccess() {

                    }
                });
            }
        });
    }

    private void loginXiayibu() {
        //????????????????????????
        PreferenceHelper.getInstance(LoginActivity.this).putString("user_phone", mEtPhone.getText().toString() + "");

        String accid = LoginActivity.this.response.body().data.get(0).getAccid();
        JPushInterface.setAlias(mContext, 0, accid);
        Set<String> tags = new HashSet<>();
        tags.add(accid);
        JPushInterface.setTags(mContext, 0, tags);

        if (response.body().data.size() == 1) {
            //????????????????????????<=1???????????????
            // response.body().data.get(0).invitation_code_state = "2";
            UserManager.getManager(LoginActivity.this).saveUser(LoginActivity.this.response.body().data.get(0));
            if (response.body().data.get(0).getPower_state().equals("1")) {

                if (response.body().data.get(0).invitation_code_state.equals("2")) {
                    BuTianYaoQingMaDialog buTianYaoQingMaDialog = new BuTianYaoQingMaDialog(mContext, new BuTianYaoQingMaDialog.OnDialogItemClickListener() {
                        @Override
                        public void qudingclick(String str) {

                            if (StringUtils.isEmpty(str)) {
                                UIHelper.ToastMessage(mContext, "????????????????????????????????????");
                            } else {
                                getNet_butian(str);
                            }
                        }

                        @Override
                        public void quxiaoclick() {

                            UserManager.getManager(LoginActivity.this).saveUser(response.body().data.get(0));
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                            //??????mqtt
                            Notice n = new Notice();
                            n.type = ConstanceValue.MSG_CONNET_MQTT;
                            RxBus.getDefault().sendRx(n);
                            finish();

                        }
                    });
                    buTianYaoQingMaDialog.show();
                } else {
                    UserManager.getManager(LoginActivity.this).saveUser(response.body().data.get(0));
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                    //??????mqtt
                    Notice n = new Notice();
                    n.type = ConstanceValue.MSG_CONNET_MQTT;
                    RxBus.getDefault().sendRx(n);
                    finish();
                }

            } else {
                UserManager.getManager(LoginActivity.this).saveUser(response.body().data.get(0));
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                //??????mqtt
                Notice n = new Notice();
                n.type = ConstanceValue.MSG_CONNET_MQTT;
                RxBus.getDefault().sendRx(n);
                finish();
            }

            String rongYunTouken = UserManager.getManager(mContext).getRongYun();
            if (!StringUtils.isEmpty(rongYunTouken)) {
                Notice notice = new Notice();
                notice.type = ConstanceValue.MSG_RONGYUN_CHONGZHI;
                RxBus.getDefault().sendRx(notice);
            }
        } else {
            //???????????? >1 ???????????????????????????????????????
            userlist.addAll(response.body().data);
            startActivity(new Intent(LoginActivity.this, SelectLoginActivity.class));
        }

        PreferenceHelper.getInstance(mContext).putString(AppConfig.FIRSTINSTALLDONGTAISHITI, "1");//??????????????????????????????????????????
    }

    public void connectRongYun(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallbackEx() {
            /**
             * ???????????????.
             * @param code ?????????????????????. DATABASE_OPEN_SUCCESS ?????????????????????; DATABASE_OPEN_ERROR ?????????????????????
             */
            @Override
            public void OnDatabaseOpened(RongIMClient.DatabaseOpenStatus code) {
                Log.i("rongYun", "?????????????????????");
            }

            /**
             * token ??????
             */
            @Override
            public void onTokenIncorrect() {
                Log.i("rongYun", "token ??????");
            }

            /**
             * ????????????
             * @param userId ???????????? ID
             */
            @Override
            public void onSuccess(String userId) {
                //UIHelper.ToastMessage(mContext, "??????????????????");
                Log.i("rongYun", "??????????????????");
                PreferenceHelper.getInstance(mContext).putString(AppConfig.RONGYUN_TOKEN, token);
            }

            /**
             * ????????????
             * @param errorCode ?????????
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void getNet_butian(String et) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04343");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("invitation_code", et);
        Gson gson = new Gson();
        OkGo.<AppResponse<Object>>post(SERVER_URL + "shop_new/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Object>>() {
                    @Override

                    public void onSuccess(final Response<AppResponse<Object>> response) {

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                        //??????mqtt
                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_CONNET_MQTT;
                        RxBus.getDefault().sendRx(n);
                        finish();

                    }

                    @Override
                    public void onError(Response<AppResponse<Object>> response) {
                        Y.tError(response);

                        UserManager.getManager(LoginActivity.this).saveUser(LoginActivity.this.response.body().data.get(0));
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                        //??????mqtt
                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_CONNET_MQTT;
                        RxBus.getDefault().sendRx(n);
                        finish();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.i("LoginActivity_xx", "?????????......");
        if (fuWuDialog.isShowing()) {
            fuWuDialog.dismiss();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //UIHelper.ToastMessage(mContext, "?????????");
        Log.i("LoginActivity_xx", "?????????......");

    }
}
