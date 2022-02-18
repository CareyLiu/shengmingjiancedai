package com.smarthome.magic.activity.tuya_device.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.tuya_device.TuyaBaseCameraDeviceActivity;
import com.smarthome.magic.activity.tuya_device.TuyaBaseDeviceActivity;
import com.smarthome.magic.activity.tuya_device.camera.adapter.TuyaKongzhiAdapter;
import com.smarthome.magic.activity.tuya_device.camera.model.TuyaKongzhiModel;
import com.smarthome.magic.activity.tuya_device.utils.TuyaDialogUtils;
import com.smarthome.magic.activity.tuya_device.utils.manager.TuyaDeviceManagerTwo;
import com.smarthome.magic.activity.tuya_device.utils.manager.TuyaHomeManager;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tuya.smart.api.router.UrlBuilder;
import com.tuya.smart.api.router.UrlRouter;
import com.tuya.smart.camera.camerasdk.typlayer.callback.AbsP2pCameraListener;
import com.tuya.smart.camera.camerasdk.typlayer.callback.OperationDelegateCallBack;
import com.tuya.smart.camera.middleware.p2p.ITuyaSmartCameraP2P;
import com.tuya.smart.camera.middleware.p2p.TuyaSmartCameraP2PFactory;
import com.tuya.smart.camera.middleware.widget.AbsVideoViewCallback;
import com.tuya.smart.camera.middleware.widget.TuyaCameraView;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.api.WifiSignalListener;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class TuyaCameraActivity extends TuyaBaseCameraDeviceActivity implements View.OnClickListener, View.OnTouchListener {

    TuyaCameraView mVideoView;
    @BindView(R.id.rv_kongzhi)
    RecyclerView rv_kongzhi;
    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.bt_up)
    RelativeLayout bt_up;
    @BindView(R.id.bt_down)
    RelativeLayout bt_down;
    @BindView(R.id.bt_left)
    RelativeLayout bt_left;
    @BindView(R.id.bt_right)
    RelativeLayout bt_right;
    @BindView(R.id.ll_fangxiang)
    LinearLayout ll_fangxiang;
    @BindView(R.id.iv_switch_shengyin)
    ImageView iv_switch_shengyin;
    @BindView(R.id.iv_switch_quanping)
    ImageView iv_switch_quanping;
    @BindView(R.id.tv_switch_qingxidu)
    TextView tv_switch_qingxidu;
    @BindView(R.id.tv_xinhao)
    TextView tv_xinhao;
    @BindView(R.id.fl_camera)
    FrameLayout fl_camera;
    @BindView(R.id.ll_main)
    LinearLayout ll_main;
    @BindView(R.id.fl_quanping)
    FrameLayout fl_quanping;
    @BindView(R.id.bt_chonglian)
    TextView bt_chonglian;
    @BindView(R.id.iv_switch_shengyin_qp)
    ImageView iv_switch_shengyin_qp;
    @BindView(R.id.iv_switch_quanping_qp)
    ImageView iv_switch_quanping_qp;
    @BindView(R.id.tv_switch_qingxidu_qp)
    TextView tv_switch_qingxidu_qp;
    @BindView(R.id.rl_quanping)
    RelativeLayout rl_quanping;
    @BindView(R.id.rl_mianban)
    RelativeLayout rl_mianban;
    @BindView(R.id.tv_yinsimoshi)
    TextView tv_yinsimoshi;

    private int p2pType;
    private DeviceBean mDeviceBeen;
    private ITuyaDevice mDevice;
    private ITuyaSmartCameraP2P mCameraP2P;

    private List<TuyaKongzhiModel> kongzhis = new ArrayList<>();
    private TuyaKongzhiAdapter adapter;
    private int kongzhiPositton;

    private String qingxidu;
    private String ty_device_ccid;
    private boolean isKaiqishengyin;
    private boolean isOpenFangxiang;
    private boolean isOnline;
    private boolean isYinsi;

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context, String member_type, String device_id, String ty_device_ccid, String old_name, String room_name) {
        Intent intent = new Intent(context, TuyaCameraActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("member_type", member_type);
        intent.putExtra("device_id", device_id);
        intent.putExtra("ty_device_ccid", ty_device_ccid);
        intent.putExtra("old_name", old_name);
        intent.putExtra("room_name", room_name);
        context.startActivity(intent);
    }

    @Override
    public int getContentViewResId() {
        return R.layout.act_device_camera;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("摄像头");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        iv_rightTitle.setVisibility(View.VISIBLE);
        iv_rightTitle.setImageResource(R.mipmap.mine_shezhi);
        iv_rightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set();
            }
        });
        mToolbar.setNavigationIcon(R.mipmap.back_black);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenFangxiang) {
                    closeFangxiang();
                } else {
                    finish();
                }
            }
        });
    }

    private void set() {//移除设备
        CameraSetActivity.actionStart(mContext,
                getIntent().getStringExtra("member_type"),
                getIntent().getStringExtra("device_id"),
                getIntent().getStringExtra("old_name"),
                getIntent().getStringExtra("room_name")
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
        initAdapter();
        initHuidiao();
    }

    private void init() {
        ty_device_ccid = getIntent().getStringExtra("ty_device_ccid");
        bt_up.setOnClickListener(this);
        bt_down.setOnClickListener(this);
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);
        tv_yinsimoshi.setOnClickListener(this);
        bt_up.setOnTouchListener(this);
        bt_down.setOnTouchListener(this);
        bt_left.setOnTouchListener(this);
        bt_right.setOnTouchListener(this);

        DeviceBean haveDevice = TuyaHomeManager.getHomeManager().isHaveDevice(ty_device_ccid);
        if (haveDevice != null) {
            TuyaDeviceManagerTwo.getDeviceManager().initDevice(haveDevice);
            initCamera();
        } else {
            TuyaDeviceManagerTwo.getDeviceManager().initDevice(null);
            TuyaDialogUtils.t(mContext, "设备已失效!");
        }
    }

    private void initCamera() {
        p2pType = TuyaDeviceManagerTwo.getDeviceManager().getP2pType();
        mDeviceBeen = TuyaDeviceManagerTwo.getDeviceManager().getDeviceBeen();
        mDevice = TuyaDeviceManagerTwo.getDeviceManager().getDevice();
        mCameraP2P = TuyaSmartCameraP2PFactory.createCameraP2P(p2pType, mDeviceBeen.getDevId());
        mVideoView = new TuyaCameraView(mContext);
        mVideoView.setViewCallback(new AbsVideoViewCallback() {
            @Override
            public void onCreated(Object o) {
                super.onCreated(o);
                //渲染视图构造完成时回调
                if (null != mCameraP2P) {
                    mCameraP2P.generateCameraView(o);
                }
            }

            @Override
            public void videoViewClick() {
                super.videoViewClick();
                //点击视图时回调
            }

            @Override
            public void startCameraMove(int cameraDirection) {
                super.startCameraMove(cameraDirection);
                //触发视图滑动操作时回调
                //滑动方向。"0"代表上，"2"代表右，"4"代表下，"6"代表左
            }
        });

        mVideoView.createVideoView(p2pType);
        mCameraP2P.registerP2PCameraListener(new AbsP2pCameraListener() {
            @Override
            public void onSessionStatusChanged(Object o, int i, int i1) {
                super.onSessionStatusChanged(o, i, i1);
            }
        });

        fl_camera.addView(mVideoView);
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_DEVICE_DATA) {
                    if (message.devId.equals(mDeviceBeen.getDevId())) {
                        getData((String) message.content);
                    }
                } else if (message.type == ConstanceValue.MSG_DEVICE_REMOVED) {
                    String devId = TuyaDeviceManagerTwo.getDeviceManager().getDeviceBeen().getDevId();
                    String ccc = message.devId;
                    if (ccc.equals(devId)) {
                        finish();
                    }
                } else if (message.type == ConstanceValue.MSG_DEVICE_STATUSCHANGED) {
                    if (message.devId.equals(mDeviceBeen.getDevId())) {
                        isOnline = (boolean) message.content;
                        if (!isOnline) {
                            dissCameraConnect();
                        }
                    }
                } else if (message.type == ConstanceValue.MSG_DEVICE_NETWORKSTATUSCHANGED) {


                } else if (message.type == ConstanceValue.MSG_DEVICE_DEVINFOUPDATE) {


                } else if (message.type == ConstanceValue.MSG_CAMERA_FAIL) {
                    TuyaDialogUtils.t(mContext, (String) message.content);
                } else if (message.type == ConstanceValue.MSG_DEVICE_DELETE) {
                    finish();
                }
            }
        }));
    }

    public void getData(String dpStr) {
        JSONObject jsonObject = JSON.parseObject(dpStr);
        Set<String> strings = jsonObject.keySet();
        Iterator<String> it = strings.iterator();
        while (it.hasNext()) {
            // 获得key
            String key = it.next();
            String value = jsonObject.getString(key);
            jieData(key, value, jsonObject);
        }

    }

    private void jieData(String key, String value, JSONObject jsonObject) {
        if (key.equals("105")) {
            isYinsi = jsonObject.getBoolean("105");
            if (isYinsi) {
                setAdapterCanClikc(false);
                tv_yinsimoshi.setVisibility(View.VISIBLE);
                mCameraP2P.disconnect(null);
            } else {
                setAdapterCanClikc(true);
                tv_yinsimoshi.setVisibility(View.GONE);
                cameraConnect();
            }
        }
    }

    private void dissCameraConnect() {//离线
        if (mCameraP2P != null) {
            mCameraP2P.disconnect(null);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeFangxiang();
                stopPlayBack();
                rl_mianban.setVisibility(View.GONE);
                bt_chonglian.setVisibility(View.VISIBLE);
            }
        });
        showFailure("设备已离线，请检查设备网络！");
    }

    private void initAdapter() {
        kongzhis.add(new TuyaKongzhiModel(R.mipmap.tuya_shexiangtou_icon_paizhao, "拍照"));
        kongzhis.add(new TuyaKongzhiModel(R.mipmap.tuya_shexiangtou_icon_jianghua, R.mipmap.tuya_shexiangtou_icon_jianghua_sel, "讲话"));
        kongzhis.add(new TuyaKongzhiModel(R.mipmap.tuya_shexiangtou_icon_luzhi, R.mipmap.tuya_shexiangtou_icon_luzhi_sel, "录制"));
        kongzhis.add(new TuyaKongzhiModel(R.mipmap.tuya_shexiangtou_icon_huifang, "回放"));
        kongzhis.add(new TuyaKongzhiModel(R.mipmap.tuya_shexiangtou_icon_yuncunchu, "云存储"));
        kongzhis.add(new TuyaKongzhiModel(R.mipmap.tuya_shexiangtou_icon_fangxiang, "方向"));
        kongzhis.add(new TuyaKongzhiModel(R.mipmap.tuya_shexiangtou_icon_baojing, "报警"));
        kongzhis.add(new TuyaKongzhiModel(R.mipmap.tuya_shexiangtou_icon_xiangce, "相册"));
        adapter = new TuyaKongzhiAdapter(R.layout.item_tuya_camera_kongzhi, kongzhis);
        rv_kongzhi.setLayoutManager(new GridLayoutManager(mContext, 3));
        rv_kongzhi.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                kongzhiPositton = position;
                String name = kongzhis.get(position).getName();
                if (name.equals("拍照")) {
                    clickPaizhao();
                } else if (name.equals("讲话")) {
                    clickJianghua();
                } else if (name.equals("录制")) {
                    clickLuzhi();
                } else if (name.equals("回放")) {
//                    CameraHuifangActivity.actionStart(mContext, p2pType);
                    Bundle bundle = new Bundle();
                    bundle.putString("extra_camera_uuid", ty_device_ccid);
                    UrlBuilder urlBuilder = new UrlBuilder(mContext, "camera_playback_panel").putExtras(bundle);
                    UrlRouter.execute(urlBuilder);
                } else if (name.equals("云存储")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("extra_camera_uuid", ty_device_ccid);
                    UrlBuilder urlBuilder = new UrlBuilder(mContext, "camera_cloud_panel").putExtras(bundle);
                    UrlRouter.execute(urlBuilder);
                } else if (name.equals("方向")) {
                    openFangxiang();
                } else if (name.equals("报警")) {
                    CameraSetBaojingActivity.actionStart(mContext);
                } else if (name.equals("相册")) {
//                    CameraXiangceActivity.actionStart(mContext);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("extra_camera_uuid", ty_device_ccid);
//                    UrlBuilder urlBuilder = new UrlBuilder(mContext, "camera_local_video_photo").putExtras(bundle);
//                    UrlRouter.execute(urlBuilder);
                }
            }
        });
    }

    private void setAdapterCanClikc(boolean canClikc) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < kongzhis.size(); i++) {
                    TuyaKongzhiModel tuyaKongzhiModel = kongzhis.get(i);
                    tuyaKongzhiModel.setCanClick(canClikc);
                    kongzhis.set(i, tuyaKongzhiModel);
                }
                adapter.notifyDataSetChanged();
                rl_mianban.setVisibility(View.VISIBLE);
            }
        });
    }

    private void upData(TuyaKongzhiModel model) {//更新数据
        dismissProgressDialog();
        model.setSelect(!model.isSelect());
        kongzhis.set(kongzhiPositton, model);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    //拍照功能
    private void clickPaizhao() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean granted) {
                if (granted) { // 在android 6.0之前会默认返回true
                    paizhao();
                } else {
                    Y.tLong("拍照功能需要赋予访问本地存储的权限，不开启将无法使用该功能！");
                }
            }
        });
    }

    private void paizhao() {//拍照
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Camera/";
        File file = new File(path);

        if (!file.exists()) {
            file.mkdirs();
        }

        mCameraP2P.snapshot(path, mContext, new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                dismissProgressDialog();
                TuyaDialogUtils.t(mContext, "拍摄成功");
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                showFailure();
            }
        });
    }

    //讲话功能
    private void clickJianghua() {
        TuyaKongzhiModel model = kongzhis.get(kongzhiPositton);
        if (model.isSelect()) {
            stopJianghua(model);
        } else {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.RECORD_AUDIO).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean granted) {
                    if (granted) { // 在android 6.0之前会默认返回true
                        startJianghua(model);
                    } else {
                        Y.tLong("开启对讲功能需要赋予录音的权限，不开启将无法使用该功能！");
                    }
                }
            });
        }
    }

    private void startJianghua(TuyaKongzhiModel model) {//开始讲话
        mCameraP2P.startAudioTalk(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                upData(model);
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                showFailure();
            }
        });
    }

    private void stopJianghua(TuyaKongzhiModel model) {//停止讲话
        mCameraP2P.stopAudioTalk(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                upData(model);
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                showFailure();
                upData(model);
            }
        });
    }

    //录制功能
    private void clickLuzhi() {
        TuyaKongzhiModel model = kongzhis.get(kongzhiPositton);
        if (model.isSelect()) {
            stopLuzhi(model);
        } else {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean granted) {
                    if (granted) { // 在android 6.0之前会默认返回true
                        startLuzhi(model);
                    } else {
                        Y.tLong("录制功能需要赋予访问本地存储的权限，不开启将无法使用该功能！");
                    }
                }
            });
        }
    }

    private void startLuzhi(TuyaKongzhiModel model) {//开始录制
        String picPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Camera/";
        File file = new File(picPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".mp4";
        String videoPath = picPath + fileName;
        mCameraP2P.startRecordLocalMp4(picPath, mContext, new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                upData(model);
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                showFailure();
            }
        });

    }

    private void stopLuzhi(TuyaKongzhiModel model) {//停止录制
        mCameraP2P.stopRecordLocalMp4(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                upData(model);
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                showFailure();
                upData(model);
            }
        });
    }

    private void cameraConnect() {
        if (!mCameraP2P.isConnecting()) {
            showProgressDialog();
            mCameraP2P.connect(mDeviceBeen.getDevId(), new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int sessionId, int requestId, String data) {
                    startPlayBack();
                    getXinhao();
                }

                @Override
                public void onFailure(int sessionId, int requestId, int errCode) {
                    dismissProgressDialog();
                    dissCameraConnect();
                }
            });
        } else {
            showProgressDialog();
            resumePlayBack();
            getXinhao();
        }
    }

    private void getXinhao() {
        mDevice.requestWifiSignal(new WifiSignalListener() {

            @Override
            public void onSignalValueFind(String signal) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_xinhao.setText("信号:" + signal + "%");
                    }
                });
            }

            @Override
            public void onError(String errorCode, String errorMsg) {

            }
        });
    }

    private void startPlayBack() {//开始播放实时视频成功
        if (mCameraP2P != null) {
            mCameraP2P.startPreview(new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int sessionId, int requestId, String data) {
                    dismissProgressDialog();
                    getQingxidu();
                    Map<String, Object> dps = TuyaDeviceManagerTwo.getDeviceManager().getDeviceBeen().getDps();
                    Object o = dps.get("105");
                    if (o == null) {
                        isYinsi = false;
                    } else {
                        isYinsi = (boolean) o;
                    }
                    if (isYinsi) {
                        setAdapterCanClikc(false);
                        mCameraP2P.disconnect(null);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_yinsimoshi.setVisibility(View.VISIBLE);
                                bt_chonglian.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        setAdapterCanClikc(true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_yinsimoshi.setVisibility(View.GONE);
                                bt_chonglian.setVisibility(View.GONE);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(int sessionId, int requestId, int errCode) {
                    dismissProgressDialog();
                    dissCameraConnect();
                }
            });
        }
    }


    private void stopPlayBack() {//停止播放
        if (mCameraP2P != null) {
            mCameraP2P.stopPlayBack(null);
        }
    }

    private void pausePlayBack() {//停止播放
        if (mCameraP2P != null) {
            mCameraP2P.pausePlayBack(null);
        }
    }

    private void resumePlayBack() {//重新开始播放
        if (mCameraP2P != null) {
            mCameraP2P.resumePlayBack(new OperationDelegateCallBack() {
                @Override
                public void onSuccess(int i, int i1, String s) {
                    dismissProgressDialog();
                }

                @Override
                public void onFailure(int i, int i1, int i2) {
                    dismissProgressDialog();
                }
            });
        }
    }

    @OnClick({R.id.bt_chonglian, R.id.iv_switch_shengyin, R.id.iv_switch_shengyin_qp, R.id.iv_switch_quanping, R.id.iv_switch_quanping_qp, R.id.tv_switch_qingxidu, R.id.tv_switch_qingxidu_qp, R.id.rl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_switch_shengyin:
            case R.id.iv_switch_shengyin_qp:
                clickShengyin();
                break;
            case R.id.iv_switch_quanping:
            case R.id.iv_switch_quanping_qp:
                clickQuanping();
                break;
            case R.id.tv_switch_qingxidu:
            case R.id.tv_switch_qingxidu_qp:
                swichQingxidu();
                break;
            case R.id.rl_back:
                closeFangxiang();
                break;
            case R.id.bt_chonglian:
                cameraConnect();
                break;
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void clickQuanping() {
        if (getScreenOrientation(this) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    public int getScreenOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        } else {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }

    private void closeFangxiang() {
        isOpenFangxiang = false;
        ll_fangxiang.setVisibility(View.GONE);
        rv_kongzhi.setVisibility(View.VISIBLE);
    }

    private void openFangxiang() {
        isOpenFangxiang = true;
        rv_kongzhi.setVisibility(View.GONE);
        ll_fangxiang.setVisibility(View.VISIBLE);
    }

    //声音功能
    private void clickShengyin() {
        showProgressDialog();
        int kongzhi = 0;
        if (isKaiqishengyin) {
            kongzhi = 1;//关闭声音
        } else {
            kongzhi = 0;//开启声音
        }
        mCameraP2P.setMute(kongzhi, new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                dismissProgressDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (data.equals("0")) {
                            iv_switch_shengyin.setImageResource(R.mipmap.tuya_shexiangtou_icon_you_shengyin);
                            iv_switch_shengyin_qp.setImageResource(R.mipmap.tuya_shexiangtou_icon_you_shengyin);
                            isKaiqishengyin = true;
                        } else {
                            iv_switch_shengyin.setImageResource(R.mipmap.tuya_shexiangtou_icon_jingyin);
                            iv_switch_shengyin_qp.setImageResource(R.mipmap.tuya_shexiangtou_icon_jingyin);
                            isKaiqishengyin = false;
                        }
                    }
                });
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                showFailure();
            }
        });
    }

    private void getQingxidu() {//获取清晰度
        mCameraP2P.getVideoClarity(new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int sessionId, int requestId, String data) {
                qingxidu = data;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (qingxidu.equals("4")) {
                            tv_switch_qingxidu.setText("高清");
                            tv_switch_qingxidu_qp.setText("高清");
                        } else {
                            tv_switch_qingxidu.setText("标清");
                            tv_switch_qingxidu_qp.setText("标清");
                        }
                    }
                });
            }

            @Override
            public void onFailure(int sessionId, int requestId, int errCode) {
                dismissProgressDialog();
            }
        });
    }

    private void swichQingxidu() {//切换清晰度
        showProgressDialog();
        int qingxiduPos = 0;
        if (qingxidu.equals("2")) {
            qingxiduPos = 4;
        } else if (qingxidu.equals("4")) {
            qingxiduPos = 2;
        }

        qingxidu = qingxiduPos + "";
        mCameraP2P.setVideoClarity(qingxiduPos, new OperationDelegateCallBack() {
            @Override
            public void onSuccess(int i, int i1, String s) {
                dismissProgressDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        qingxidu = s;
                        if (qingxidu.equals("2")) {
                            tv_switch_qingxidu.setText("标清");
                            tv_switch_qingxidu_qp.setText("标清");
                        } else if (qingxidu.equals("4")) {
                            tv_switch_qingxidu.setText("高清");
                            tv_switch_qingxidu_qp.setText("高清");
                        }
                    }
                });
            }

            @Override
            public void onFailure(int i, int i1, int i2) {
                showFailure();
            }
        });
    }

    private void showFailure() {//出错
        Notice notice = new Notice();
        notice.type = ConstanceValue.MSG_CAMERA_FAIL;
        notice.content = "网络异常";
        RxBus.getDefault().sendRx(notice);
    }

    private void showFailure(String msg) {//出错
        Notice notice = new Notice();
        notice.type = ConstanceValue.MSG_CAMERA_FAIL;
        notice.content = msg;
        RxBus.getDefault().sendRx(notice);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (view.getId() == R.id.bt_up) {
                setDp("119", "0");//上
            } else if (view.getId() == R.id.bt_down) {
                setDp("119", "4");//下
            } else if (view.getId() == R.id.bt_left) {
                setDp("119", "6");//左
            } else if (view.getId() == R.id.bt_right) {
                setDp("119", "2");//右
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (view.getId() == R.id.bt_up) {
                setDp("116", false);//停止转动
            } else if (view.getId() == R.id.bt_down) {
                setDp("116", false);//停止转动
            } else if (view.getId() == R.id.bt_left) {
                setDp("116", false);//停止转动
            } else if (view.getId() == R.id.bt_right) {
                setDp("116", false);//停止转动
            }
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            ll_main.setVisibility(View.VISIBLE);
            rl_quanping.setVisibility(View.GONE);
            fl_quanping.removeAllViews();
            fl_camera.addView(mVideoView);
            mToolbar.setVisibility(View.VISIBLE);
            setNotFullScreen();
        } else {
            mToolbar.setVisibility(View.GONE);
            closeFangxiang();
            ll_main.setVisibility(View.GONE);
            rl_quanping.setVisibility(View.VISIBLE);
            fl_camera.removeAllViews();
            fl_quanping.addView(mVideoView);
            setFullScreen();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCameraP2P != null && !isYinsi) {
            cameraConnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlayBack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mCameraP2P) {
            stopPlayBack();
            mCameraP2P.removeOnP2PCameraListener();
            mCameraP2P.destroyP2P();
        }
        TuyaDeviceManagerTwo.getDeviceManager().unRegisterDevListener();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onBackPressedSupport() {
        if (getScreenOrientation(this) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            if (isOpenFangxiang) {
                closeFangxiang();
            } else {
                finish();
            }
        }
    }
}
