package com.smarthome.magic.activity.chuwugui_two;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui_two.model.SaomaModl;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

public class GouWuGuiScanActivity extends BaseActivity implements QRCodeView.Delegate {
    private static final String tag = GouWuGuiScanActivity.class.getSimpleName();
    @BindView(R.id.zxingview)
    ZBarView mQRCodeView;
    @BindView(R.id.capture_flash)
    ImageView captureFlash;

    private boolean flag = true;
    private ProgressDialog waitdialog;


    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GouWuGuiScanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        mQRCodeView.startSpot();
        mQRCodeView.setDelegate(this);
    }

    @Override
    public int getContentViewResId() {
        return R.layout.gouwugui_activity_scan1;
    }

    private void light() {
        if (flag) {
            flag = false;
            // 开闪光灯
            mQRCodeView.openFlashlight();
            captureFlash.setTag(null);
            captureFlash.setBackgroundResource(R.drawable.flash_open);
        } else {
            flag = true;
            // 关闪光灯
            mQRCodeView.closeFlashlight();
            captureFlash.setTag("1");
            captureFlash.setBackgroundResource(R.drawable.flash_default);
        }
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.e(tag, result);
        waitdialog = ProgressDialog.show(GouWuGuiScanActivity.this, null, "已扫描，正在处理···", true, true);
        waitdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                mQRCodeView.stopSpot();
            }
        });

        vibrate();

        saomacunbao(result);
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
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        waitdialog.dismiss();
                    }
                });
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(tag, "打开相机出错");
        mQRCodeView.startCamera();
    }

    @OnClick({R.id.capture_flash})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.capture_flash:
                light();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mQRCodeView.stopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (waitdialog != null) {
            waitdialog.dismiss();
        }
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("扫一扫购物柜码");
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
}
