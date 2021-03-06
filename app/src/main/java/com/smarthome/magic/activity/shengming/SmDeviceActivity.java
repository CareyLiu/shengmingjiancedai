package com.smarthome.magic.activity.shengming;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shengming.shengmingmodel.Device;
import com.smarthome.magic.activity.shengming.shengmingmodel.RealHrRrData;
import com.smarthome.magic.activity.shengming.utils.UrlUtils;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.PreferenceHelper;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmDeviceActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_zaixian)
    TextView tv_zaixian;
    @BindView(R.id.iv_jilu)
    ImageView iv_jilu;
    @BindView(R.id.iv_xin)
    ImageView iv_xin;
    @BindView(R.id.tv_xinlv)
    TextView tv_xinlv;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.iv_zhuangtai)
    ImageView iv_zhuangtai;
    @BindView(R.id.tv_zhuangtai)
    TextView tv_zhuangtai;
    @BindView(R.id.iv_huxipinlv)
    ImageView iv_huxipinlv;
    @BindView(R.id.tv_huxi)
    TextView tv_huxi;
    @BindView(R.id.tv_time_kongqizhiliang)
    TextView tv_time_kongqizhiliang;
    @BindView(R.id.bt_kongqizhiliang_xiangqing)
    RelativeLayout bt_kongqizhiliang_xiangqing;
    @BindView(R.id.bt_tizhengbaogao)
    RelativeLayout bt_tizhengbaogao;
    @BindView(R.id.tv_cuimian_zhuangtai)
    TextView tv_cuimian_zhuangtai;
    @BindView(R.id.iv_cuimian_switch)
    ImageView iv_cuimian_switch;
    @BindView(R.id.iv_cuimian_zhuangta)
    ImageView iv_cuimian_zhuangta;
    @BindView(R.id.ll_cuimian_zhuangta)
    LinearLayout ll_cuimian_zhuangta;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private String sessionId;
    private String cuimianType;
    private boolean isOnFrag;
    private RealHrRrData.DataBean dataBean;
    private Device deviceModel;


    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.shengming_act_device;
    }

    /**
     * ????????????Activty????????????Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SmDeviceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        getData();
        initDonghua();
        initSM();
        initHandler();
        showProgressDialog();
        getNet();
        getDevice();
    }

    private void getData() {
        sessionId = PreferenceHelper.getInstance(mContext).getString("sm_sessionId", "");
        cuimianType = PreferenceHelper.getInstance(mContext).getString("cuimianType", "0");
        Glide.with(mContext).asGif().load(R.drawable.huxinpinlv1).into(iv_huxipinlv);

        if (cuimianType.equals("1")) {
            tv_cuimian_zhuangtai.setText("???????????????");
            tv_cuimian_zhuangtai.setTextColor(Y.getColor(R.color.white));
            iv_cuimian_switch.setImageResource(R.mipmap.shengming_cuimian_switch_on);
            iv_cuimian_zhuangta.setImageResource(R.mipmap.shuimian_icon);
            ll_cuimian_zhuangta.setBackgroundResource(R.mipmap.cuimian_bg);
        } else {
            tv_cuimian_zhuangtai.setText("???????????????");
            tv_cuimian_zhuangtai.setTextColor(Y.getColor(R.color.color_9));
            iv_cuimian_switch.setImageResource(R.mipmap.shengming_cuimian_switch_off);
            iv_cuimian_zhuangta.setImageResource(R.mipmap.shuimiannor_icon);
            ll_cuimian_zhuangta.setBackgroundResource(R.mipmap.cuimianguanbi);
        }
    }

    private void initDonghua() {
        ScaleAnimation animation = new ScaleAnimation(1, (float) 0.83, 1, (float) 0.83, Animation.RELATIVE_TO_SELF, 0.5f, 1, 0.5f);
        /**
         * @param fromX ??????x????????????0????????????1????????????float???
         * @param toX ??????
         * @param fromY ??????T
         * @param toY ??????
         * @param pivotXType ????????????pivotXValue??????????????????????????????Animation.ABSOLUTE???Animation.RELATIVE_TO_SELF???Animation.RELATIVE_TO_PARENT
         * Type???Animation.ABSOLUTE?????????????????????????????????????????????pivotXValue?????????????????????????????????????????????X????????????????????????pivotXValue?????????mIvScale.getWidth() / 2f
         *      Animation.RELATIVE_TO_SELF??????????????????????????????????????????????????????pivotXValue??????????????????????????????????????????????????????????????????????????????X????????????????????????pivotXValue?????????0.5f
         *      Animation.RELATIVE_TO_PARENT????????????????????????????????????????????????????????????????????????????????????????????????????????? ???????????????
         * @param pivotXValue  ??????pivotXType????????????????????????
         * @param pivotYType ???from/to
         * @param pivotYValue ????????????
         */
        animation.setDuration(700);
        //??????????????????
        animation.setFillAfter(false);
        //??????????????????????????????????????????????????????????????????true????????????????????????????????????????????????
        animation.setRepeatCount(99999);
        //??????????????????
        animation.setRepeatMode(Animation.REVERSE);
        //?????????????????????REVERSE??????????????????
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        //????????????????????????????????????
        iv_xin.startAnimation(animation);
        //???????????????xxx???????????????????????????????????????
    }

    private void getNet() {
        String timestamp = System.currentTimeMillis() + "";
        String ltoken = UrlUtils.getLtoken(sessionId, timestamp);
        OkGo.<RealHrRrData>get(UrlUtils.getRealHrRrData)
                .params("sessionId", sessionId)
                .params("timestamp", timestamp)
                .params("ltoken", ltoken)
                .params("mac", UrlUtils.MAC)
                .tag(this)//
                .execute(new JsonCallback<RealHrRrData>() {
                    @Override
                    public void onSuccess(Response<RealHrRrData> response) {
                        dataBean = response.body().getData();
                        if (dataBean != null) {
                            tv_xinlv.setText(dataBean.getHr() + "???/???");
                            tv_huxi.setText(dataBean.getRr() + "");

                            int status = dataBean.getStatus();
                            if (status == 1) {
                                tv_zhuangtai.setText("???????????????");
                                iv_zhuangtai.setImageResource(R.mipmap.shengming_jc_tidong);
                            } else if (status == 2) {
                                tv_zhuangtai.setText("???????????????");
                                iv_zhuangtai.setImageResource(R.mipmap.shengming_jc_tidong);
                            } else if (status == 7 || status == 9) {
                                tv_zhuangtai.setText("?????????????????????");
                                iv_zhuangtai.setImageResource(R.mipmap.shengming_jc_fei);
                            } else {
                                tv_zhuangtai.setText("???????????????");
                                iv_zhuangtai.setImageResource(R.mipmap.shengming_jc_chuang);
                            }

                            tv_time.setText("??????????????????:" + dataBean.getTime());
                            tv_time_kongqizhiliang.setText(Y.getDate(Y.getDate(dataBean.getTime())));
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                        smartRefreshLayout.finishRefresh();
                    }
                });
    }

    private void getDevice() {
        String timestamp = System.currentTimeMillis() + "";
        String ltoken = UrlUtils.getLtoken(sessionId, timestamp);
        OkGo.<Device>get(UrlUtils.getDevice)
                .params("sessionId", sessionId)
                .params("timestamp", timestamp)
                .params("ltoken", ltoken)
                .params("mac", UrlUtils.MAC)
                .tag(this)//
                .execute(new JsonCallback<Device>() {
                    @Override
                    public void onSuccess(Response<Device> response) {
                        deviceModel = response.body();
                        int online = deviceModel.getData().getOnline();
                        if (online == 1) {
                            tv_zaixian.setText("????????????(??????)");
                        } else {
                            tv_zaixian.setText("????????????(??????)");
                        }

                    }
                });
    }

    private void initSM() {
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getNet();
                getDevice();
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.iv_jilu, R.id.bt_kongqizhiliang_xiangqing, R.id.bt_tizhengbaogao, R.id.iv_cuimian_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_jilu:
                SmSetActivity.actionStart(mContext);
                break;
            case R.id.bt_kongqizhiliang_xiangqing:
                SmKongqizhiliangActivity.actionStart(mContext);
                break;
            case R.id.bt_tizhengbaogao:
                SmHomeActivity.actionStart(mContext);
                break;
            case R.id.iv_cuimian_switch:
                clickSwitch();
                break;
        }
    }

    private void clickSwitch() {
        if (cuimianType.equals("1")) {
            cuimianType = "0";
            tv_cuimian_zhuangtai.setText("???????????????");
            tv_cuimian_zhuangtai.setTextColor(Y.getColor(R.color.color_9));
            iv_cuimian_switch.setImageResource(R.mipmap.shengming_cuimian_switch_off);
            iv_cuimian_zhuangta.setImageResource(R.mipmap.shuimiannor_icon);
            ll_cuimian_zhuangta.setBackgroundResource(R.mipmap.cuimianguanbi);
        } else {
            cuimianType = "1";
            tv_cuimian_zhuangtai.setText("???????????????");
            tv_cuimian_zhuangtai.setTextColor(Y.getColor(R.color.white));
            iv_cuimian_switch.setImageResource(R.mipmap.shengming_cuimian_switch_on);
            iv_cuimian_zhuangta.setImageResource(R.mipmap.shuimian_icon);
            ll_cuimian_zhuangta.setBackgroundResource(R.mipmap.cuimian_bg);
        }
        PreferenceHelper.getInstance(mContext).putString("cuimianType", cuimianType);
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    if (isOnFrag) {
                        getNet();
                        getDevice();
                    }
                    initHandler();
                    break;
            }
            return false;
        }
    });

    private void initHandler() {
        Message message = handler.obtainMessage(1);
        handler.sendMessageDelayed(message, 60000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnFrag = true;
        getNet();
        getDevice();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnFrag = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
    }
}