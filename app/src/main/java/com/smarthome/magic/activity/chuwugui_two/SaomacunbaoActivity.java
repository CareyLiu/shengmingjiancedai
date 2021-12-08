package com.smarthome.magic.activity.chuwugui_two;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui.config.ChuwuguiValue;
import com.smarthome.magic.activity.chuwugui_two.model.SaomaModl;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.BangdingFailDialog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.CarBrand;

import java.util.HashMap;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SaomacunbaoActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.bt_saoma)
    TextView bt_saoma;

    @Override
    public int getContentViewResId() {
        return R.layout.chuwugui_act_saomacunbao;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true);
        mImmersionBar.init();
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SaomacunbaoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initHuidiao();
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ChuwuguiValue.MSG_CWG_PAY_SUCCESS) {
                    finish();
                }
            }
        }));
    }

    @OnClick({R.id.rl_back, R.id.bt_saoma})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.bt_saoma:
//                GouWuGuiScanActivity.actionStart(mContext);
                loadScanKitBtnClick();
                break;
        }
    }




    private static final int REQUEST_CODE_SCAN_ONE = 1001;
    private static final int CAMERA_REQ_CODE = 1002;
    private static final int DECODE = 1003;

    public void loadScanKitBtnClick() {
        requestPermission(CAMERA_REQ_CODE, DECODE);
    }

    private void requestPermission(int requestCode, int mode) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        if (permissions == null || grantResults == null) {
            return;
        }
        if (grantResults.length < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (requestCode == CAMERA_REQ_CODE) {
            ScanUtil.startScan(this, REQUEST_CODE_SCAN_ONE, new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE).create());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == REQUEST_CODE_SCAN_ONE) {
            HmsScan obj = data.getParcelableExtra(ScanUtil.RESULT);
            if (obj != null) {
                saomacunbao(obj.originalValue);
            }
        }
    }

    private void saomacunbao(String device_ccid) {
        String chuwuguiType = PreferenceHelper.getInstance(this).getString(App.CHUWUGUI_TYPE, "");
        Map<String, String> map = new HashMap<>();
        if (chuwuguiType.equals("1")){
            map.put("code", "120025");
        }else {
            map.put("code", "120001");
        }
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("device_ccid", device_ccid);
        map.put("subsystem_id", "tlc");
        Gson gson = new Gson();
        OkGo.<AppResponse<SaomaModl.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<SaomaModl.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<SaomaModl.DataBean>> response) {
                        SaomaModl.DataBean dataBean = response.body().data.get(0);
                        ChuwuguiZhifuActivity.actionStart(mContext, dataBean);
                        finish();
                    }

                    @Override
                    public void onError(Response<AppResponse<SaomaModl.DataBean>> response) {
                        super.onError(response);
                        Y.tError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }
}
