package com.smarthome.magic.activity.shengming.fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shengming.SmJianceLishActivtiy;
import com.smarthome.magic.activity.shengming.shengmingmodel.Device;
import com.smarthome.magic.activity.shengming.shengmingmodel.RealHrRrData;
import com.smarthome.magic.activity.shengming.utils.UrlUtils;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.basicmvp.BaseFragment;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.PreferenceHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

public class SmJianceFragment extends BaseFragment {


    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_xinlv)
    TextView tv_xinlv;
    @BindView(R.id.tv_huxi)
    TextView tv_huxi;
    @BindView(R.id.tv_zhuangtai)
    TextView tv_zhuangtai;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.iv_jilu)
    ImageView iv_jilu;
    @BindView(R.id.iv_xin)
    ImageView iv_xin;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.iv_zhuangtai)
    ImageView iv_zhuangtai;
    @BindView(R.id.iv_huxipinlv)
    ImageView iv_huxipinlv;
    @BindView(R.id.tvv_titlte)
    TextView tvvTitlte;
    @BindView(R.id.tv_xinlv_max)
    TextView tvXinlvMax;
    @BindView(R.id.tv_huxi_max)
    TextView tvHuxiMax;
    @BindView(R.id.tv_dianya)
    TextView tvDianya;
    @BindView(R.id.tv_jinshuikou_wendu)
    TextView tvJinshuikouWendu;
    @BindView(R.id.tv_chushuikou_wendu)
    TextView tvChushuikouWendu;

    private String sessionId;
    private RealHrRrData.DataBean dataBean;
    private boolean isOnFrag;
    private Device deviceModel;

    @Override
    protected void immersionInit(ImmersionBar mImmersionBar) {
        mImmersionBar
                .init();
    }

    @Override
    protected boolean immersionEnabled() {
        return true;
    }

    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.shengming_frag_jiankong;
    }

    @Override
    protected void initView(View rootView) {
        sessionId = PreferenceHelper.getInstance(getContext()).getString("sm_sessionId", "");
        initDonghua();
        initSM();
        initHandler();
        getDevice();

        Glide.with(getContext()).asGif().load(R.drawable.huxinpinlv1).into(iv_huxipinlv);
        iv_huxipinlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getContext()).asGif().load(R.drawable.huxinpinlv1).into(iv_huxipinlv);
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        showProgressDialog();
        isOnFrag = true;
        getNet();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        isOnFrag = false;
    }

    private void initSM() {
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getNet();
            }
        });
    }

    private void initDonghua() {
        ScaleAnimation animation = new ScaleAnimation(1, (float) 0.83, 1, (float) 0.83, Animation.RELATIVE_TO_SELF, 0.5f, 1, 0.5f);
        /**
         * @param fromX 起始x轴位置，0为最小，1为原始，float形
         * @param toX 同上
         * @param fromY 同上T
         * @param toY 同上
         * @param pivotXType 用来约束pivotXValue的取值。取值有三种：Animation.ABSOLUTE，Animation.RELATIVE_TO_SELF，Animation.RELATIVE_TO_PARENT
         * Type：Animation.ABSOLUTE：绝对，如果设置这种类型，后面pivotXValue取值就必须是像素点；比如：控件X方向上的中心点，pivotXValue的取值mIvScale.getWidth() / 2f
         *      Animation.RELATIVE_TO_SELF：相对于控件自己，设置这种类型，后面pivotXValue取值就会去拿这个取值是乘上控件本身的宽度；比如：控件X方向上的中心点，pivotXValue的取值0.5f
         *      Animation.RELATIVE_TO_PARENT：相对于它父容器（这个父容器是指包括这个这个做动画控件的外一层控件）， 原理同上，
         * @param pivotXValue  配合pivotXType使用，原理在上面
         * @param pivotYType 同from/to
         * @param pivotYValue 原理同上
         */
        animation.setDuration(700);
        //设置持续时间
        animation.setFillAfter(false);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        animation.setRepeatCount(99999);
        //设置循环次数
        animation.setRepeatMode(Animation.REVERSE);
        //设置循环方式，REVERSE代表往复循环
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        //设置插值器为先加速再减速
        iv_xin.startAnimation(animation);
        //开始动画，xxx为你要设置动画的控件对象。
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
                            tv_xinlv.setText(dataBean.getHr() + "次/分");
                            tv_huxi.setText(dataBean.getRr() + "");

                            int status = dataBean.getStatus();
                            if (status == 1) {
                                tv_zhuangtai.setText("状态：在床");
                                iv_zhuangtai.setImageResource(R.mipmap.shengming_jc_tidong);
                            } else if (status == 2) {
                                tv_zhuangtai.setText("状态：体动");
                                iv_zhuangtai.setImageResource(R.mipmap.shengming_jc_tidong);
                            } else if (status == 7 || status == 9) {
                                tv_zhuangtai.setText("状态：呼吸暂停");
                                iv_zhuangtai.setImageResource(R.mipmap.shengming_jc_fei);
                            } else {
                                tv_zhuangtai.setText("状态：离床");
                                iv_zhuangtai.setImageResource(R.mipmap.shengming_jc_chuang);
                            }

                            tv_time.setText("最新推送时间:" + dataBean.getTime());
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

    @OnClick({R.id.iv_back, R.id.iv_jilu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                clickBack();
                break;
            case R.id.iv_jilu:
                SmJianceLishActivtiy.actionStart(getContext());
                break;
        }
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    if (isOnFrag) {
                        getNet();
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
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeMessages(1);
    }

    private void clickBack() {
        Notice notice = new Notice();
        notice.type = ConstanceValue.MSG_SHENGMIN_HOME_BACK;
        sendRx(notice);
    }


    private String customerCode = "RSD1389948105";
    private String ytoken = "fca76f960fe5ecd7447edbcc4ea799b2";
    private String mac = "149C7656FFB1";

    private void getDevice() {
        String url = "http://iot.52soft.top/api/getDevice?";
        String timestamp = System.currentTimeMillis() + "";
        String jiami = sessionId + customerCode + "_" + timestamp + ytoken;
        String ltoken = md5(jiami);
        ltoken = ltoken.toLowerCase();
        String urlSet = url + "sessionId=" + sessionId + "&timestamp=" + timestamp + "&ltoken=" + ltoken + "&mac=" + mac;
        OkGo.<Device>get(urlSet)
                .tag(this)//
                .execute(new JsonCallback<Device>() {
                    @Override
                    public void onSuccess(Response<Device> response) {
                        deviceModel = response.body();
                        int online = deviceModel.getData().getOnline();

                        if (online == 1) {
                            tvvTitlte.setText("在线");
                        } else {
                            tvvTitlte.setText("离线");
                        }

                    }
                });
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
