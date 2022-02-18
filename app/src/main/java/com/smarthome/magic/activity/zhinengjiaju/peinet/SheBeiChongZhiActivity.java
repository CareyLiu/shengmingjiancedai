package com.smarthome.magic.activity.zhinengjiaju.peinet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.roundview.RoundRelativeLayout;
import com.google.gson.Gson;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.tuya_device.add.zi.TuyaAddCameraActivity;
import com.smarthome.magic.activity.yaokongqi.KongQiJingHuaPeiActivity;
import com.smarthome.magic.activity.yaokongqi.WanNengYaoKongQiPeiDui;
import com.smarthome.magic.activity.yaokongqi.YaokongKTPei;
import com.smarthome.magic.activity.yaokongqi.ZhenWanNengYaoKongQiPeiDuiZidingyi;
import com.smarthome.magic.activity.zhinengjiaju.TianJiaPuTongSheBeiActivity;
import com.smarthome.magic.app.AppConfig;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.BangdingFailDialog;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.CarBrand;
import com.smarthome.magic.model.FenLeiContentModel;
import com.smarthome.magic.model.TaoKeTitleListModel;

import java.util.HashMap;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.app.ConstanceValue.MSG_PEIWANG_SUCCESS;
import static com.smarthome.magic.get_net.Urls.MSG;
import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;

public class SheBeiChongZhiActivity extends BaseActivity {

    String image;
    String titleName;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.rrl_xiayibu)
    RoundRelativeLayout rrlXiayibu;
    String header;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.rrl_querencaozuo)
    RelativeLayout rrlQuerencaozuo;
    @BindView(R.id.rrl_zhezhao)
    RoundRelativeLayout rrlZhezhao;
    @BindView(R.id.tv_miaoshu)
    TextView tvMiaoshu;
    private String shiGouXuanZhong = "0";
    private String ccid;
    private String serverId;
    private FenLeiContentModel fenLeiContentModel;
    private String cameraType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image = getIntent().getStringExtra("image");
        titleName = getIntent().getStringExtra("titleName");
        header = getIntent().getStringExtra("header");
        fenLeiContentModel = (FenLeiContentModel) getIntent().getSerializableExtra("FenLeiContentModel");
        tvTitleName.setText(fenLeiContentModel.item_name);
        Glide.with(mContext).load(fenLeiContentModel.img_detail_url).into(ivImage);
        tvMiaoshu.setText(fenLeiContentModel.item_detail);
        ivIcon.setBackgroundResource(R.mipmap.peiwang_icon_mima_weixuanze);
        rrlQuerencaozuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shiGouXuanZhong.equals("0")) {
                    ivIcon.setBackgroundResource(R.mipmap.peiwang_icon);
                    rrlZhezhao.setVisibility(View.GONE);
                    shiGouXuanZhong = "1";
                } else {
                    ivIcon.setBackgroundResource(R.mipmap.peiwang_icon_mima_weixuanze);
                    rrlZhezhao.setVisibility(View.VISIBLE);
                    shiGouXuanZhong = "0";
                }
            }
        });
        rrlXiayibu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rrlZhezhao.getVisibility() == View.VISIBLE) {
                    UIHelper.ToastMessage(mContext, "请确认已执行以上操作");
                } else {
                    if (fenLeiContentModel.type.equals("00")) {
                        String strZhuJi = PreferenceHelper.getInstance(mContext).getString(AppConfig.HAVEZHUJI, "");
                        if (strZhuJi.equals("0")) {
                            ZhiNengJiaJuPeiWangActivity.actionStart(mContext);
                        } else {
                            UIHelper.ToastMessage(mContext, "此家庭已有主机,请切换家庭后重新尝试");
                        }

                    } else if (fenLeiContentModel.type.equals("18")) {//摄像头
                        // TODO: 2021/2/3 添加摄像头
                        TuyaAddCameraActivity.actionStart(mContext, cameraType);
                    } else if (fenLeiContentModel.type.equals("28")) {//其实是电视
                        WanNengYaoKongQiPeiDui.actionStart(SheBeiChongZhiActivity.this);
                    } else if (fenLeiContentModel.type.equals("37")) {
                        YaokongKTPei.actionStart(SheBeiChongZhiActivity.this);
                    } else if (fenLeiContentModel.type.equals("38")) {
                        KongQiJingHuaPeiActivity.actionStart(mContext);
                    } else if (fenLeiContentModel.type.equals("39")) {
                        ZhenWanNengYaoKongQiPeiDuiZidingyi.actionStart(mContext);
                    } else if (fenLeiContentModel.type.equals("40")) {//生命体征带
                        loadScanKitBtnClick();
                    } else {
                        TianJiaPuTongSheBeiActivity.actionStart(mContext, fenLeiContentModel);
                    }
                }
            }
        });
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_PEIWANG_SUCCESS) {
                    finish();
                    //配网成功后的后续处理
                } else if (message.type == ConstanceValue.MSG_DEVICE_ADD) {
                    finish();
                }
            }
        }));

        if (fenLeiContentModel.type.equals("18")) {
            cameraType = "0";

            tv_rightTitle.setText("Wi-Fi快连");
            tv_rightTitle.setVisibility(View.VISIBLE);
            iv_rightTitle.setVisibility(View.VISIBLE);
            iv_rightTitle.setImageResource(R.mipmap.peiwang);
            iv_rightTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseType();
                }
            });
            tv_rightTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseType();
                }
            });
        }
    }



    private void addSuccess() {
        TishiDialog    tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {

            }

            @Override
            public void onDismiss(TishiDialog dialog) {
                Notice notice = new Notice();
                notice.type = MSG_PEIWANG_SUCCESS;
                RxBus.getDefault().sendRx(notice);
                finish();
            }
        });
        tishiDialog.setTextContent("成功添加设备");
        tishiDialog.setTextCancel("");
        tishiDialog.setTextConfirm("完成");
        tishiDialog.show();
    }

    private void chooseType() {
        String items[] = {"Wi-Fi快连", "二维码配网"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, items, null);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        cameraType = "0";
                        tv_rightTitle.setText("Wi-Fi快连");
                        break;
                    case 1:
                        cameraType = "1";
                        tv_rightTitle.setText("二维码配网");
                        break;
                }
                dialog.dismiss();

            }
        });
    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_shebeichongzhi;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("添加设备");
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


    /**
     * 用于其他Activty跳转到该Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String titleName, String image, String header, FenLeiContentModel fenLeiContentModel) {
        Intent intent = new Intent(context, SheBeiChongZhiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("titleName", titleName);
        intent.putExtra("image", image);
        intent.putExtra("header", header);
        intent.putExtra("FenLeiContentModel", fenLeiContentModel);
        context.startActivity(intent);
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
                addSheBei(obj.originalValue);
            }
        }
    }

    public void addSheBei(String mac) {
        showProgressDialog();
        String familyId = PreferenceHelper.getInstance(mContext).getString(AppConfig.FAMILY_ID, "0");
        Map<String, String> map = new HashMap<>();
        map.put("code", "16078");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("family_id", familyId);
        map.put("device_type", "40");
        map.put("device_category", "01");
        map.put("mac", mac);
        Gson gson = new Gson();
        OkGo.<AppResponse<TaoKeTitleListModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<TaoKeTitleListModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<TaoKeTitleListModel.DataBean>> response) {
                        addSuccess();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }
}
