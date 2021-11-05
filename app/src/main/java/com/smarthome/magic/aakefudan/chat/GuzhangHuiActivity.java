package com.smarthome.magic.aakefudan.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.amap.api.maps2d.model.LatLng;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.MyApplication;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.util.AlertUtil;
import com.smarthome.magic.util.NavigationUtils;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.get_net.Urls.HOME_PICTURE_HOME;

public class GuzhangHuiActivity extends BaseActivity {

    private String sub_user_name;
    private String sub_accid;
    private String sub_user_id;
    private String inst_id;
    private String user_car_id;
    private String zhu_car_stoppage_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 添加会话界面
        ConversationFragment conversationFragment = new ConversationFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, conversationFragment);
        transaction.commit();
        sub_user_name = getIntent().getStringExtra("sub_user_name");
        sub_accid = getIntent().getStringExtra("sub_accid");
        sub_user_id = getIntent().getStringExtra("sub_user_id");
        inst_id = getIntent().getStringExtra("inst_id");
        user_car_id = getIntent().getStringExtra("user_car_id");
        zhu_car_stoppage_no = getIntent().getStringExtra("zhu_car_stoppage_no");
        tv_title.setText(sub_user_name);
        getLiaoTian(mContext);

        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_RONGYUN_STATE) {
                    RongIMClient.ConnectionStatusListener.ConnectionStatus status = (RongIMClient.ConnectionStatusListener.ConnectionStatus) message.content;
                    if (status.getMessage().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONN_USER_BLOCKED)) {
                        //* 用户被开发者后台封禁
                        // notice.content = status.CONN_USER_BLOCKED;
                        UIHelper.ToastMessage(mContext, "该商户已被封禁");
                    } else if (status.getMessage().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                        //连接成功
                        showLoadSuccess();
                    } else if (status.getMessage().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                        //连接中
                        // notice.content = status.CONNECTING;
                        showLoading();
                    } else if (status.getMessage().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                        //断开连接
                        // notice.content = status.DISCONNECTED;
                        showLoadFailed();
                    } else if (status.getMessage().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                        //用户账户在其他设备登录，本机会被踢掉线。
                        UIHelper.ToastMessage(mContext, "用户账户在其他设备登录，本机会被踢掉线。");
                    } else if (status.getMessage().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE)) {
                        //网络不可用
                        showLoadFailed();
                    } else if (status.getMessage().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.SERVER_INVALID)) {
                        //TOKEN_INCORRECT
                        //连接失败
                        UIHelper.ToastMessage(mContext, "连接失败");
                    } else if (status.getMessage().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.TOKEN_INCORRECT)) {
                        //Token 不正确
                        //notice.content = status.TOKEN_INCORRECT;
                    }
                } else if (message.type == ConstanceValue.MSG_SERVICE_CHAT) {
                    MyMessage model = (MyMessage) message.content;
                    clickDaohang(model);
                }
            }
        }));


    }

    private void clickDaohang(MyMessage model) {
        String items[] = {"高德地图导航", "百度地图导航"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, items, null);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        try {
                            String lat_x = model.getLat_x();
                            String lon_y = model.getLon_y();
                            Double x = Double.valueOf(lat_x);
                            Double y = Double.valueOf(lon_y);
                            LatLng latLng = new LatLng(x, y);
                            NavigationUtils.Navigation(latLng);
                        } catch (Exception e) {
                            com.smarthome.magic.app.UIHelper.ToastMessage(MyApplication.getApp().getApplicationContext(), "请下载高德地图后重新尝试", Toast.LENGTH_SHORT);
                        }
                        break;
                    case 1:
                        try {
                            String lat_x = model.getLat_x();
                            String lon_y = model.getLon_y();
                            Double x = Double.valueOf(lat_x);
                            Double y = Double.valueOf(lon_y);
                            LatLng latLng = new LatLng(x, y);
                            NavigationUtils.NavigationBaidu(latLng, model.getCustomRepairName());
                        } catch (Exception e) {
                            com.smarthome.magic.app.UIHelper.ToastMessage(MyApplication.getApp().getApplicationContext(), "请下载百度地图后重新尝试", Toast.LENGTH_SHORT);
                        }


                        break;
                }
                dialog.dismiss();

            }
        });


    }


    @Override
    public int getContentViewResId() {
        return R.layout.conversation;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("关于我们");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imm.hideSoftInputFromWindow(findViewById(R.id.cl_layout).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });
    }

    public void getLiaoTian(Context context) {
        Map<String, String> map = new HashMap<>();
        map.put("token", UserManager.getManager(context).getAppToken());
        map.put("code", "03312");
        map.put("key", Urls.key);
        map.put("user_car_id", user_car_id);
        map.put("sub_user_id", sub_user_id);
        map.put("sub_accid", sub_accid);
        map.put("inst_id", inst_id);
        map.put("sub_user_name", sub_user_name);
        map.put("zhu_car_stoppage_no", zhu_car_stoppage_no);

        Gson gson = new Gson();
        OkGo.<AppResponse<Object>>
                post(Urls.MESSAGE_URL).
                tag(context).
                upJson(gson.toJson(map)).
                execute(new JsonCallback<AppResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Object>> response) {
                        UIHelper.ToastMessage(mContext, "建立聊天成功");
                        showLoadSuccess();
                    }

                    @Override
                    public void onError(Response<AppResponse<Object>> response) {
                        AlertUtil.t(context, response.getException().getMessage());
                        showLoadSuccess();
                    }

                    @Override
                    public void onStart(Request<AppResponse<Object>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }
                });
    }

}