package com.smarthome.magic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;

import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.util.AlertUtil;
import com.smarthome.magic.view.Keyboard;
import com.smarthome.magic.view.PayEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RevisePayActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.PayEditText_pay)
    PayEditText PayEditTextPay;
    @BindView(R.id.KeyboardView_pay)
    Keyboard KeyboardViewPay;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    private Boolean isFirst = true;
    private String pwd;
    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "", "0", "<<"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_pay_password);
        ButterKnife.bind(this);
        StatusBarUtil.setLightMode(this);
        KeyboardViewPay.setKeyboardKeys(KEY);
        KeyboardViewPay.setOnClickKeyboardListener(new Keyboard.OnClickKeyboardListener() {
            @Override
            public void onKeyClick(int position, String value) {
                if (position < 11) {
                    PayEditTextPay.add(value);
                } else if (position == 11) {
                    PayEditTextPay.remove();
                }
            }
        });

        PayEditTextPay.setOnInputFinishedListener(new PayEditText.OnInputFinishedListener() {
            @Override
            public void onInputFinished(String password) {
                if (isFirst) {
                    pwd = password;
                    PayEditTextPay.cleanPassWord();
                    tvTips.setText("????????????????????????");
                    isFirst = false;
                } else {
                    if (pwd.equals(password)) {
                        //????????????????????????,??????????????????
                        requestData();

                    } else {
                        tvTips.setText("?????????????????????????????????????????????");
                        PayEditTextPay.cleanPassWord();
                    }
                }
            }
        });
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * ??????????????????
     */
    private void requestData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04239");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(getApplication()).getAppToken());
        map.put("sms_id", getIntent().getStringExtra("sms_id"));
        map.put("sms_code", getIntent().getStringExtra("sms_code"));
        map.put("pay_password", PayEditTextPay.getText().toString().trim());
        Gson gson = new Gson();
        OkGo.<AppResponse>post(Urls.SERVER_URL + "shop_new/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse>() {
                    @Override
                    public void onSuccess(Response<AppResponse> response) {
                        AlertUtil.t(getApplicationContext(), response.body().msg);
                        finish();
                    }

                    @Override
                    public void onError(Response<AppResponse> response) {
                        AlertUtil.t(getApplicationContext(), response.getException().getMessage());

                    }
                });

    }


}
