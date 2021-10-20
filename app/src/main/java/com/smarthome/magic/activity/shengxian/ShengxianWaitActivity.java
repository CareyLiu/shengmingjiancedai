package com.smarthome.magic.activity.shengxian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.GouMaiXiangQingActivity;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.CarBrand;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

public class ShengxianWaitActivity extends BaseActivity {


    @BindView(R.id.view)
    Button view;
    private String device_ccid;

    @Override
    public int getContentViewResId() {
        return R.layout.shengxian_act_wait;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("生鲜柜");
        tv_title.setTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.mipmap.back_white);
        mToolbar.setBackgroundColor(Color.parseColor("#F77C28"));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void actionStart(Context context, String device_ccid) {
        Intent intent = new Intent(context, ShengxianWaitActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("device_ccid", device_ccid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device_ccid = getIntent().getStringExtra("device_ccid");
    }

    @Override
    public void initImmersion() {
        super.initImmersion();
        mImmersionBar.with(this).statusBarColor(R.color.shengxian).init();
    }

    @Override
    public boolean isImmersive() {
        return true;
    }

    @OnClick(R.id.view)
    public void onViewClicked() {
        showProgressDialog();
        Map<String, String> map = new HashMap<>();
        map.put("code", "110001");
        map.put("key", Urls.key);
//        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("wx_token", "1628560a06563500g000q000O000t0");
        map.put("device_ccid", device_ccid);

        Gson gson = new Gson();
        OkGo.<AppResponse<CarBrand.DataBean>>post(Urls.SERVER_URL + "lc/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<CarBrand.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<CarBrand.DataBean>> response) {
                        String msg = response.body().msg;
                        TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_XIAOXI, new TishiDialog.TishiDialogListener() {
                            @Override
                            public void onClickCancel(View v, TishiDialog dialog) {

                            }

                            @Override
                            public void onClickConfirm(View v, TishiDialog dialog) {

                            }

                            @Override
                            public void onDismiss(TishiDialog dialog) {
                                finish();
                            }
                        });
                        dialog.setTextConfirm("确定");
                        dialog.setTextCancel("");
                        dialog.setTextContent(msg);
                        dialog.show();
                    }

                    @Override
                    public void onError(Response<AppResponse<CarBrand.DataBean>> response) {
                        String msg = response.getException().getMessage();
                        TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_XIAOXI, new TishiDialog.TishiDialogListener() {
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
                        dialog.setTextConfirm("确定");
                        dialog.setTextCancel("");
                        dialog.setTextContent(msg);
                        dialog.show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });

    }
}
