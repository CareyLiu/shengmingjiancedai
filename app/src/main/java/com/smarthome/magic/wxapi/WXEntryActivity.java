package com.smarthome.magic.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.AppConfig;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.common.StringUtils;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.config.Wetch_S;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.MineModel;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Wetch_S.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {


        if (baseResp instanceof SendAuth.Resp) {
            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
            Log.i("sendAuth_code", resp.code);
            getNet(resp.code);
//??????????????????
        } else if (baseResp instanceof SendMessageToWX.Resp) {
//????????????????????????
            SendMessageToWX.Resp resp = (SendMessageToWX.Resp) baseResp;

            UIHelper.ToastMessage(WXEntryActivity.this, resp.openId);

            Log.i("FenXiang-Resp", String.valueOf(resp.errCode));

            String result;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    result = "????????????";
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "????????????";
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = "????????????";
                    break;
                default:
                    result = "????????????";
                    break;
            }
            UIHelper.ToastMessage(WXEntryActivity.this, result);
            finish();
        }

    }

    private void getNet(String sendAuth_code) {
        Map<String, String> map = new HashMap<>();
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(WXEntryActivity.this).getAppToken());
        map.put("code", "04183");
        map.put("js_code", sendAuth_code);
        String str = PreferenceHelper.getInstance(WXEntryActivity.this).getString(AppConfig.SMS_ID, "");
        if (StringUtils.isEmpty(str)) {
            UIHelper.ToastMessage(WXEntryActivity.this, "????????????????????????????????????");
            return;
        }
        map.put("sms_id", PreferenceHelper.getInstance(WXEntryActivity.this).getString(AppConfig.SMS_ID, ""));
        map.put("sms_code", PreferenceHelper.getInstance(WXEntryActivity.this).getString(AppConfig.SMS_CODE, ""));

        Gson gson = new Gson();
        OkGo.<AppResponse>post(Urls.HOME_PICTURE_HOME)
                .tag(WXEntryActivity.this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse>() {
                    @Override
                    public void onSuccess(Response<AppResponse> response) {
                        UIHelper.ToastMessage(WXEntryActivity.this, response.body().msg);
                        UIHelper.ToastMessage(WXEntryActivity.this, "????????????????????????");
                        PreferenceHelper.getInstance(WXEntryActivity.this).putString(App.CUNCHUBIND_WEIXINPAY, "1");
                        finish();
                    }

                    @Override
                    public void onError(Response<AppResponse> response) {
                        super.onError(response);
                    }
                });
    }

}
