package com.smarthome.magic.fragment.znjj;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttSubscribe;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.ZhiNengFamilyManageDetailActivity;
import com.smarthome.magic.activity.ZhiNengHomeListActivity;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.tuya_device.add.TuyaDeviceAddActivity;
import com.smarthome.magic.activity.tuya_device.changjing.TuyaTianqiActivity;
import com.smarthome.magic.activity.tuya_device.utils.manager.TuyaHomeManager;
import com.smarthome.magic.activity.zhinengjiaju.peinet.PeiWangYinDaoPageActivity;
import com.smarthome.magic.adapter.NewsFragmentPagerAdapter;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.AppConfig;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.basicmvp.BaseFragment;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.common.StringUtils;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.fragment.znjj.model.ZhiNengModel;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.ZhiNengFamilyManageBean;
import com.smarthome.magic.util.DensityUtils;
import com.smarthome.magic.util.DoMqttValue;
import com.smarthome.magic.util.GlideShowImageUtils;
import com.smarthome.magic.view.NoSlidingViewPager;
import com.smarthome.magic.view.magicindicator.MagicIndicator;
import com.smarthome.magic.view.magicindicator.ViewPagerHelper;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.bean.MemberBean;
import com.tuya.smart.home.sdk.bean.WeatherBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaGetMemberListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.api.IResultCallback;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;


/**
 * Created by Administrator on 2018/3/29 0029.
 */

public class ZhiNengJiaJuFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.srL_smart)
    SmartRefreshLayout srLSmart;
    @BindView(R.id.ll_checkhome)
    LinearLayout ll_checkhome;
    @BindView(R.id.tv_family_name)
    TextView tv_family_name;
    @BindView(R.id.tv_device_num)
    TextView tv_device_num;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewPager)
    NoSlidingViewPager viewPager;
    @BindView(R.id.iv_zhuji_zhuangtai)
    ImageView ivZhujiZhuangtai;
    @BindView(R.id.ll_connect_device)
    LinearLayout llConnectDevice;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.tv_zhuji_zhuangtai)
    TextView tvZhujiZhuangtai;
    @BindView(R.id.bt_add_camera)
    ImageView bt_add_camera;
    @BindView(R.id.iv_tianqi)
    ImageView iv_tianqi;
    @BindView(R.id.tv_tianqi)
    TextView tv_tianqi;
    @BindView(R.id.tv_tianqi_wendu)
    TextView tv_tianqi_wendu;
    @BindView(R.id.iv_tianqi_enter)
    ImageView iv_tianqi_enter;
    @BindView(R.id.ll_tianqi_click)
    LinearLayout ll_tianqi_click;

    ImageView ivXiaoChengXu;

    private List<String> tabs = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private NewsFragmentPagerAdapter viewPagerAdapter;

    private ZhiNengDeviceFragment zhiNengDeviceFragment;
    private ZhiNengRoomFragment zhiNengRoomFragment;
    private ZhiNengChangJingFragment zhiNengJiaJuChangJingFragment;

    private List<ZhiNengModel.DataBean> dataBean = new ArrayList<>();
    private String zhuJiWenShiDu;
    private boolean isSettianqi;

    @Override
    protected void immersionInit(ImmersionBar mImmersionBar) {
        mImmersionBar.with(this).statusBarDarkFont(true).fitsSystemWindows(true).statusBarColor(R.color.white).init();
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
        return R.layout.zhinengjiaju;
    }

    private int lastX;
    private int lastY;

    private boolean isIntercept = false;
    /**
     * ??????????????????????????????????????????????????????X
     */
    private int startDownX;
    /**
     * ??????????????????????????????????????????????????????Y
     */
    private int startDownY;
    /**
     * ??????????????????????????????????????????X
     */
    private int lastMoveX;
    /**
     * ??????????????????????????????????????????Y
     */
    private int lastMoveY;

    @Override
    protected void initView(View view) {
        view.setClickable(true);// ??????????????????????????????fragment??????????????????????????????
        ivXiaoChengXu = view.findViewById(R.id.iv_xiaochengxu);
        ivXiaoChengXu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appId = "wxafadbee6859e4228"; // ???????????????(App)??? AppId?????????????????? AppID
                IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);

                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                req.userName = "gh_f4043d63b846"; // ??????????????????id
                //   req.path = path;                  ////??????????????????????????????????????????????????????????????????????????????????????????????????????????????? query ????????????????????????????????????????????? "?foo=bar"???
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// ???????????? ?????????????????????????????????
                api.sendReq(req);
            }
        });

        ivXiaoChengXu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Log.i("ImageButton-00", "x" + String.valueOf(x));
                Log.i("ImageButton-00", "y" + String.valueOf(y));
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = x;
                        lastY = y;

                        startDownX = lastMoveX = (int) event.getRawX();
                        startDownY = lastMoveY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int offsetX = x - lastX;
                        int offsetY = y - lastY;
                        Log.i("view_00", "??????" + (Float.valueOf(pingMuHeight) - v.getTop()) + "");
                        Log.i("view_00", "????????????????????????" + DensityUtils.dp2px(getActivity(), 110) + "");

                        v.layout(v.getLeft() + offsetX,
                                v.getTop() + offsetY,
                                v.getRight() + offsetX,
                                v.getBottom() + offsetY);
                        break;
                    case MotionEvent.ACTION_UP:
                        // adsorbAnim(event.getRawX(), event.getRawY());
                        if (v.getTop() < 0) {
                            RelativeLayout.LayoutParams lpFeedback = new RelativeLayout.LayoutParams(
                                    DensityUtils.dp2px(getActivity(), 60), DensityUtils.dp2px(getActivity(), 60));
                            lpFeedback.leftMargin = v.getLeft();
                            lpFeedback.topMargin = v.getTop();
                            lpFeedback.setMargins(v.getLeft(), 0, 0, 0);
                            v.setLayoutParams(lpFeedback);

                        } else if ((Float.valueOf(pingMuHeight) - v.getTop()) < DensityUtils.dp2px(getActivity(), 110)) {
                            RelativeLayout.LayoutParams lpFeedback = new RelativeLayout.LayoutParams(
                                    DensityUtils.dp2px(getActivity(), 60), DensityUtils.dp2px(getActivity(), 60));
                            lpFeedback.leftMargin = v.getLeft();
                            lpFeedback.topMargin = v.getTop();
                            lpFeedback.setMargins(v.getLeft(), Integer.valueOf(pingMuHeight) - DensityUtils.dp2px(getActivity(), 140), 0, 0);
                            v.setLayoutParams(lpFeedback);

                        } else {
                            RelativeLayout.LayoutParams lpFeedback = new RelativeLayout.LayoutParams(
                                    DensityUtils.dp2px(getActivity(), 60), DensityUtils.dp2px(getActivity(), 60));
                            lpFeedback.leftMargin = v.getLeft();
                            lpFeedback.topMargin = v.getTop();
                            lpFeedback.setMargins(v.getLeft(), v.getTop(), 0, 0);
                            v.setLayoutParams(lpFeedback);
                            int lastMoveDx = Math.abs((int) event.getRawX() - startDownX);
                            int lastMoveDy = Math.abs((int) event.getRawY() - startDownY);
                            if (0 != lastMoveDx || 0 != lastMoveDy) {
                                isIntercept = true;
                            } else {
                                isIntercept = false;
                            }
                        }

                        adsorbAnim(v, event.getRawX(), event.getRawY());

                        break;
                }
                return isIntercept;
            }
        });
    }


    private void adsorbAnim(View view, float rawX, float rawY) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;

        view.animate().setDuration(400)
                .setInterpolator(new OvershootInterpolator())
                .xBy(screenWidth - view.getX() - view.getWidth())
                .start();

    }


    String pingMuHeight;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initSM();
        initHuidiao();

        pingMuHeight = DensityUtils.getScreenHeight(getActivity()) + "";
        Log.i("view_00", pingMuHeight);
    }

    private void initData() {
        ll_checkhome.setOnClickListener(this);
        bt_add_camera.setOnClickListener(this);
        ll_tianqi_click.setOnClickListener(this);
        initViewpager();
        initMagicIndicator();
        getnet("?????????");
    }

    private void initViewpager() {
        viewPager.setScroll(false);
        zhiNengDeviceFragment = new ZhiNengDeviceFragment();
        fragmentList.add(zhiNengDeviceFragment);
        zhiNengRoomFragment = new ZhiNengRoomFragment();
        fragmentList.add(zhiNengRoomFragment);
        zhiNengJiaJuChangJingFragment = new ZhiNengChangJingFragment();
        fragmentList.add(zhiNengJiaJuChangJingFragment);
        tabs.add("??????");
        tabs.add("??????");
        tabs.add("??????");
        viewPagerAdapter = new NewsFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
    }

    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabs == null ? 0 : tabs.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(tabs.get(index));
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.sousuo));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.black_111111));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(getResources().getColor(R.color.car_text_black));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    private void initSM() {
        srLSmart.setEnableLoadMore(false);
        srLSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getnet("??????");
            }
        });
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_PEIWANG_SUCCESS) {
                    ivZhujiZhuangtai.setBackgroundResource(R.mipmap.zhikong_zhujizaixian);
                    tvZhujiZhuangtai.setText("????????????");
                } else if (message.type == ConstanceValue.MSG_ZHINENGJIAJU_SHOUYE_SHUAXIN) {
                    getnet("MSG_ZHINENGJIAJU_SHOUYE_SHUAXIN");
                } else if (message.type == ConstanceValue.MSG_ZHINENGJIAJU_ZHUJI) {
                    getnet("MSG_ZHINENGJIAJU_ZHUJI");
                } else if (message.type == ConstanceValue.MSG_DEVICE_ADD) {
                    getnet("MSG_DEVICE_ADD");
                } else if (message.type == ConstanceValue.MSG_TUYA_TIANQI) {
                    Object content = message.content;
                    if (content == null) {
                        isSettianqi = false;
                        tv_tianqi.setText("????????????");
                        tv_tianqi_wendu.setText("???????????????????????????????????????");
                        iv_tianqi_enter.setVisibility(View.VISIBLE);
                    } else {
                        isSettianqi = true;
                        WeatherBean result = (WeatherBean) message.content;
                        tv_tianqi.setText(result.getCondition());
                        Glide.with(getContext()).applyDefaultRequestOptions(GlideShowImageUtils.showNull()).load(result.getInIconUrl()).into(iv_tianqi);
                        tv_tianqi_wendu.setText(zhuJiWenShiDu + "????????????:" + result.getTemp() + "???");
                        iv_tianqi_enter.setVisibility(View.GONE);
                    }
                } else if (message.type == ConstanceValue.MSG_DEVICE_DELETE) {//????????????
                    getnet("MSG_DEVICE_DELETE");
                } else if (message.type == ConstanceValue.MSG_TIANJIASHEBEI) {//??????????????????
                    getnet("MSG_TIANJIASHEBEI");
                } else if (message.type == ConstanceValue.MSG_DEVICE_ROOM_NAME_CHANGE) {//????????????
                    getnet("MSG_DEVICE_ROOM_NAME_CHANGE");
                } else if (message.type == ConstanceValue.MSG_DELETE_FAMILY) {//??????????????????
                    getnet("MSG_DELETE_FAMILY");
                } else if (message.type == ConstanceValue.MSG_NONEZHINENGJIAJU) {

                }
            }
        }));
    }

    private void getnet(String code) {
        Y.e("????????????????????????  " + code);

        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "16001");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(getActivity()).getAppToken());
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<ZhiNengModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZhiNengModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ZhiNengModel.DataBean>> response) {
                        dataBean = response.body().data;
                        String wendu = dataBean.get(0).getTemperature();//??????
                        String shidu = dataBean.get(0).getHumidity();//??????

//                        Notice notice = new Notice();
//                        notice.type = ConstanceValue.MSG_ZHINENGJIAJU_ZI_SHUAXIN;
//                        notice.content = dataBean;
//                        sendRx(notice);

                        zhiNengDeviceFragment.getData(dataBean);
                        zhiNengRoomFragment.getData(dataBean);
                        zhiNengJiaJuChangJingFragment.getData(dataBean);


                        if (StringUtils.isEmpty(wendu) && StringUtils.isEmpty(shidu)) {
                            zhuJiWenShiDu = "";
                        } else {
                            zhuJiWenShiDu = "????????????" + wendu + "???" + " " + "????????????" + shidu + "???" + " ";
                        }

//                        tv_tianqi_wendu.setText(zhuJiWenShiDu);

                        if (dataBean.get(0).getMember_type().equals("1") || dataBean.get(0).getMember_type().equals("3")) {
                            tv_family_name.setText(dataBean.get(0).getFamily_name());
                        } else {
                            tv_family_name.setText(dataBean.get(0).getFamily_name() + "(????????????)");
                        }
                        tv_device_num.setText(dataBean.get(0).getDevice_num() + "?????????");

                        if (dataBean.get(0).getMember_type().equals("1") || dataBean.get(0).getMember_type().equals("3")) {
                            PreferenceHelper.getInstance(getActivity()).putString(AppConfig.ZHINENGJIAJUGUANLIYUAN, "1");
                        } else {
                            PreferenceHelper.getInstance(getActivity()).putString(AppConfig.ZHINENGJIAJUGUANLIYUAN, "0");
                        }


                        List<ZhiNengModel.DataBean.DeviceBean> device = dataBean.get(0).getDevice();
                        if (device.size() > 0) {
                            ZhiNengModel.DataBean.DeviceBean deviceBean = device.get(0);
                            if (TextUtils.isEmpty(deviceBean.getTy_device_ccid())) {
                                PreferenceHelper.getInstance(getActivity()).putString(App.HAS_ZHUJI, "1");

//                                ????????????ccid
//                                String nowData = "zn/app/" + deviceBean.getServer_id() + "aaaaaaaaaaaaa01000140118";
//                                String hardwareData = "zn/hardware/" + deviceBean.getServer_id() + "aaaaaaaaaaaaa01000140118";

                                String nowData = "zn/app/" + deviceBean.getServer_id() + deviceBean.getDevice_ccid();
                                String hardwareData = "zn/hardware/" + deviceBean.getServer_id() + deviceBean.getDevice_ccid();
                                try {
                                    AndMqtt.getInstance().subscribe(new MqttSubscribe()
                                            .setTopic(nowData)
                                            .setQos(2), new IMqttActionListener() {
                                        @Override
                                        public void onSuccess(IMqttToken asyncActionToken) {

                                        }

                                        @Override
                                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                                        }
                                    });


                                    AndMqtt.getInstance().subscribe(new MqttSubscribe()
                                            .setTopic(hardwareData)
                                            .setQos(2), new IMqttActionListener() {
                                        @Override
                                        public void onSuccess(IMqttToken asyncActionToken) {

                                        }

                                        @Override
                                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                PreferenceHelper.getInstance(getActivity()).putString(App.HAS_ZHUJI, "");
                            }
                        } else {
                            PreferenceHelper.getInstance(getActivity()).putString(App.HAS_ZHUJI, "");
                        }

                        String familyId = dataBean.get(0).getFamily_id();
                        PreferenceHelper.getInstance(getActivity()).putString(AppConfig.PEIWANG_FAMILYID, familyId);
                        String ty_family_id = dataBean.get(0).getTy_family_id();
                        if (TextUtils.isEmpty(ty_family_id)) {
                            if (dataBean.get(0).getMember_type().equals("1") || dataBean.get(0).getMember_type().equals("3")) {
                                List<String> addRooms = new ArrayList<>();
                                TuyaHomeSdk.getHomeManagerInstance().createHome(dataBean.get(0).getFamily_name(), 0, 0, "", addRooms, new ITuyaHomeResultCallback() {
                                    @Override
                                    public void onSuccess(HomeBean bean) {
                                        long tuyaHomeId = bean.getHomeId();
                                        PreferenceHelper.getInstance(getActivity()).putLong(AppConfig.TUYA_HOME_ID, tuyaHomeId);
                                        addTuyaHome(familyId, tuyaHomeId);
                                    }

                                    @Override
                                    public void onError(String errorCode, String errorMsg) {
                                        Y.e("??????????????????:" + errorMsg);
                                    }
                                });
                            }
                        } else {
                            PreferenceHelper.getInstance(getActivity()).putLong(AppConfig.TUYA_HOME_ID, Y.getLong(ty_family_id));
                            TuyaHomeManager.getHomeManager().setHomeId(Y.getLong(ty_family_id));
                        }

                        for (int i = 0; i < dataBean.get(0).getDevice().size(); i++) {
                            if (dataBean.get(0).getDevice().get(i).getDevice_type().equals("00")) {
                                PreferenceHelper.getInstance(getActivity()).putString(AppConfig.HAVEZHUJI, "1");
                                return;
                            }
                            PreferenceHelper.getInstance(getActivity()).putString(AppConfig.HAVEZHUJI, "0");
                        }
                        /**
                         * online_state	???????????????1.?????? 2.??????
                         */
                        if (dataBean.get(0).getDevice().size() == 0) {
                            ivZhujiZhuangtai.setBackgroundResource(R.mipmap.img_connect_device);
                            tvZhujiZhuangtai.setText("????????????");
                            PreferenceHelper.getInstance(getActivity()).putString(AppConfig.HAVEZHUJI, "0");
                            llConnectDevice.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PeiWangYinDaoPageActivity.actionStart(getActivity());
                                }
                            });
                        } else {
                            if (dataBean.get(0).getDevice().get(0).getOnline_state() == null) {
                                ivZhujiZhuangtai.setBackgroundResource(R.mipmap.zhikong_zhujilixian);
                                tvZhujiZhuangtai.setText("????????????");
                            } else if (dataBean.get(0).getDevice().get(0).getOnline_state().equals("1")) {
                                ivZhujiZhuangtai.setBackgroundResource(R.mipmap.zhikong_zhujizaixian);
                                tvZhujiZhuangtai.setText("????????????");
                            } else {
                                ivZhujiZhuangtai.setBackgroundResource(R.mipmap.zhikong_zhujilixian);
                                tvZhujiZhuangtai.setText("????????????");
                            }
                        }


                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        srLSmart.finishRefresh();
                    }
                });
    }

    private void addTuyaHome(String family_id, long ty_family_id) {
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "16045");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(getActivity()).getAppToken());
        map.put("family_id", family_id);
        map.put("ty_family_id", ty_family_id + "");
        map.put("ty_room_id", "0");
        Gson gson = new Gson();
        OkGo.<AppResponse<ZhiNengFamilyManageBean.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZhiNengFamilyManageBean.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<ZhiNengFamilyManageBean.DataBean>> response) {
                        TuyaHomeManager.getHomeManager().setHomeId(ty_family_id);
                    }

                    @Override
                    public void onError(Response<AppResponse<ZhiNengFamilyManageBean.DataBean>> response) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_checkhome:
                startActivity(new Intent(getActivity(), ZhiNengHomeListActivity.class));
                break;
            case R.id.ll_tianqi_click:
                clickSetTianqi();
                break;
            case R.id.bt_add_camera:
                clickAddCamera();
                break;
        }
    }

    private void clickSetTianqi() {
        if (isSettianqi) {
            TuyaTianqiActivity.actionStart(getContext());
//            TuyaChangjingActivity.actionStart(getContext());
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("family_id", dataBean.get(0).getFamily_id());
            bundle.putString("ty_family_id", dataBean.get(0).getTy_family_id());
            startActivity(new Intent(getContext(), ZhiNengFamilyManageDetailActivity.class).putExtras(bundle));
        }
    }

    private TishiDialog tishiDialog;

    private void clickAddCamera() {
        // TODO: 2021/2/19 ????????????????????????
        String str = PreferenceHelper.getInstance(getActivity()).getString(AppConfig.ZHINENGJIAJUGUANLIYUAN, "0");
        if (str.equals("0")) {
            tishiDialog = new TishiDialog(getActivity(), 3, new TishiDialog.TishiDialogListener() {
                @Override
                public void onClickCancel(View v, TishiDialog dialog) {

                }

                @Override
                public void onClickConfirm(View v, TishiDialog dialog) {
                    tishiDialog.dismiss();
                }

                @Override
                public void onDismiss(TishiDialog dialog) {

                }
            });
            tishiDialog.setTextContent("???????????????????????????????????????");
            tishiDialog.show();

        } else if (str.equals("1")) {
            PreferenceHelper.getInstance(getActivity()).putString(AppConfig.MC_DEVICE_CCID, "aaaaaaaaaaaaaaaa80140018");
            TuyaDeviceAddActivity.actionStart(getContext());
        }
//        getHomefff();
//        getHomeList();
    }

    private void getHomefff() {
        TuyaHomeSdk.getMemberInstance().queryMemberList(28846708, new ITuyaGetMemberListCallback() {
            @Override
            public void onSuccess(List<MemberBean> memberBeans) {
                // do something
                Y.e("????????????????????? " + memberBeans.size());
                for (int i = 0; i < memberBeans.size(); i++) {
                    MemberBean bean = memberBeans.get(i);
                    Y.e("?????????????????????  " + bean.getMemberId() + "   " + bean.getAccount() + "   " + bean.getNickName());
                }

                TuyaHomeSdk.getMemberInstance().removeMember(31948111, new IResultCallback() {
                    @Override
                    public void onSuccess() {
                        // do something
                        Y.e("??????????????????");
                    }

                    @Override
                    public void onError(String code, String error) {
                        // do something
                        Y.e("??????????????????  " + error);
                    }
                });
            }

            @Override
            public void onError(String errorCode, String error) {
                // do something
                Y.e("????????????????????????  " + error);
            }
        });
    }

    private void getHomeList() {
        TuyaHomeSdk.getHomeManagerInstance().queryHomeList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> homeBeans) {
                for (int i = 0; i < homeBeans.size(); i++) {
                    HomeBean homeBean = homeBeans.get(i);
                    long homeId = homeBean.getHomeId();
                    String name = homeBean.getName();
                    Y.e("????????????  " + name + "  " + homeId);

                    if (homeId == 31193089 || homeId == 31190326) {

                    } else {
//                        deleteTuyaJiating(homeId + "");
                    }

                }
            }

            @Override
            public void onError(String errorCode, String error) {

            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        getnet("??????");
    }

    private void deleteTuyaJiating(String ty_family_id) {
        Y.e("????????????????????????????????? " + ty_family_id);
        TuyaHomeSdk.newHomeInstance(Y.getLong(ty_family_id)).dismissHome(new IResultCallback() {
            @Override
            public void onSuccess() {
                Y.e("???????????????????????? ");
            }

            @Override
            public void onError(String code, String error) {
                Y.e("??????????????????:" + error);
            }
        });
    }
}
