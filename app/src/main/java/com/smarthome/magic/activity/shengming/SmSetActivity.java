package com.smarthome.magic.activity.shengming;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shengming.shengmingmodel.CreateSession;
import com.smarthome.magic.activity.shengming.utils.UrlUtils;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.PreferenceHelper;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmSetActivity extends BaseActivity {

    @BindView(R.id.rl_set_canshu)
    RelativeLayout rl_set_canshu;
    @BindView(R.id.iv_switch_shishishuju)
    ImageView iv_switch_shishishuju;
    @BindView(R.id.iv_switch_yujingshuju)
    ImageView iv_switch_yujingshuju;
    @BindView(R.id.rl_set_jiekou)
    RelativeLayout rl_set_jiekou;

    private String shishishuju;
    private String yujingshuju;
    private String sessionId;

    @Override
    public int getContentViewResId() {
        return R.layout.shengming_act_set;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SmSetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("设置");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        shishishuju = PreferenceHelper.getInstance(mContext).getString("sm_sshishishuju", "");
        yujingshuju = PreferenceHelper.getInstance(mContext).getString("sm_syujingshuju", "");
        sessionId = PreferenceHelper.getInstance(mContext).getString("sm_sessionId", "");

        if (shishishuju.equals("1")) {
            iv_switch_shishishuju.setImageResource(R.mipmap.switch_open);
        } else {
            iv_switch_shishishuju.setImageResource(R.mipmap.switch_close);
        }

        if (yujingshuju.equals("1")) {
            iv_switch_yujingshuju.setImageResource(R.mipmap.switch_open);
        } else {
            iv_switch_yujingshuju.setImageResource(R.mipmap.switch_close);
        }
    }

    @OnClick({R.id.rl_set_canshu, R.id.iv_switch_shishishuju, R.id.iv_switch_yujingshuju, R.id.rl_set_jiekou})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_set_canshu:
                SmSetYujingActivity.actionStart(mContext);
                break;
            case R.id.iv_switch_shishishuju:
                clickShishishuju();
                break;
            case R.id.iv_switch_yujingshuju:
                cilickYujingshuju();
                break;
            case R.id.rl_set_jiekou:
                ShengmingtestActivity.actionStart(mContext);
                break;
        }
    }

    private void clickShishishuju() {
        if (shishishuju.equals("1")) {
            shishishuju = "0";
            iv_switch_shishishuju.setImageResource(R.mipmap.switch_close);
        } else {
            shishishuju = "1";
            iv_switch_shishishuju.setImageResource(R.mipmap.switch_open);
        }

        showProgressDialog();
        String timestamp = System.currentTimeMillis() + "";
        String ltoken = UrlUtils.getLtoken(sessionId, timestamp);

        String urls = UrlUtils.registerPushMultiAddress + "?sessionId=" + sessionId
                + "&timestamp=" + timestamp
                + "&ltoken=" + ltoken
                + "&host=" + "112.124.21.39:10005"
                + "&flag=" + shishishuju;

        OkGo.<CreateSession>get(urls)
//                .params("sessionId", sessionId)
//                .params("timestamp", timestamp)
//                .params("ltoken", ltoken)
//                .params("host", "47.96.234.171:123")
//                .params("flag", shishishuju)
                .tag(this)//
                .execute(new JsonCallback<CreateSession>() {
                    @Override
                    public void onSuccess(Response<CreateSession> response) {
                        String code = response.body().getCode();
                        if (code.equals("0000")) {
                            Y.t("设置成功");
                            PreferenceHelper.getInstance(mContext).putString("sm_sshishishuju", shishishuju);
                        } else {
                            Y.t("设置失败");
                        }
                    }

                    @Override
                    public void onError(Response<CreateSession> response) {
                        super.onError(response);
                        Y.t("网络异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }

    private void cilickYujingshuju() {
        if (yujingshuju.equals("1")) {
            yujingshuju = "0";
            iv_switch_yujingshuju.setImageResource(R.mipmap.switch_close);
        } else {
            yujingshuju = "1";
            iv_switch_yujingshuju.setImageResource(R.mipmap.switch_open);
        }

        showProgressDialog();
        String timestamp = System.currentTimeMillis() + "";
        String ltoken = UrlUtils.getLtoken(sessionId, timestamp);


        String urls = UrlUtils.registerAlarmPushAddress + "?sessionId=" + sessionId
                + "&timestamp=" + timestamp
                + "&ltoken=" + ltoken
                + "&host=" + "112.124.21.39:10005"
                + "&flag=" + yujingshuju;

        OkGo.<CreateSession>get(urls)
//                .params("sessionId", sessionId)
//                .params("timestamp", timestamp)
//                .params("ltoken", ltoken)
//                .params("host", "117.74.21.11:2001")
//                .params("flag", yujingshuju)
                .tag(this)//
                .execute(new JsonCallback<CreateSession>() {
                    @Override
                    public void onSuccess(Response<CreateSession> response) {
                        String code = response.body().getCode();
                        if (code.equals("0000")) {
                            Y.t("设置成功");
                            PreferenceHelper.getInstance(mContext).putString("sm_syujingshuju", yujingshuju);
                        } else {
                            Y.t("设置失败");
                        }
                    }

                    @Override
                    public void onError(Response<CreateSession> response) {
                        super.onError(response);
                        Y.t("网络异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }
}
