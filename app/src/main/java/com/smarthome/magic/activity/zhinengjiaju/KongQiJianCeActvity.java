package com.smarthome.magic.activity.zhinengjiaju;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.ZhiNengJiaJuZhuangZhiSetting;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.KongQiJianCeModel;
import com.smarthome.magic.model.KongQiJianCeZ;
import com.smarthome.magic.util.icon_util.SplineChart03View;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;

public class KongQiJianCeActvity extends BaseActivity {

    RelativeLayout.LayoutParams layoutParams;
    @BindView(R.id.tv_jiaquan)
    TextView tvJiaquan;
    @BindView(R.id.tv_pm)
    TextView tvPm;
    @BindView(R.id.tv_kongqi_zhiliang)
    TextView tvKongqiZhiliang;
    @BindView(R.id.tv_co2)
    TextView tvCo2;
    @BindView(R.id.rl_jiaquan)
    RelativeLayout rlJiaquan;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.rl_kongqizhiliang)
    RelativeLayout rlKongqizhiliang;
    @BindView(R.id.ll_main2)
    LinearLayout llMain2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device_id = getIntent().getStringExtra("device_id");
        // UIHelper.ToastMessage(mContext, "??????device_id???" + device_id);
        FrameLayout content = new FrameLayout(this);

        //?????????????????????FrameLayout????????????????????????????????????
        FrameLayout.LayoutParams frameParm = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameParm.gravity = Gravity.BOTTOM | Gravity.RIGHT;

		   /*
		  //?????????????????????FrameLayout????????????????????????????????????
	       mZoomControls = new ZoomControls(this);
	       mZoomControls.setIsZoomInEnabled(true);
	       mZoomControls.setIsZoomOutEnabled(true);
		   mZoomControls.setLayoutParams(frameParm);
		   */

        //???????????????????????????????????????90%????????????
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scrWidth = (int) (dm.widthPixels * 0.9);
        int scrHeight = (int) (dm.heightPixels * 0.4);
        layoutParams = new RelativeLayout.LayoutParams(
                scrWidth, scrHeight);

        //????????????
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //??????view???????????????????????????????????????view??????Activity?????????xml?????????
        final RelativeLayout chartLayout = new RelativeLayout(this);


        rlJiaquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KongQiJianCeXiangXiActivity.actionStart(mContext, device_id, "1");
            }
        });

        rlKongqizhiliang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KongQiJianCeXiangXiActivity.actionStart(mContext, device_id, "2");
            }
        });
        getnet();
        getFouData();
    }

    private void getFouData() {
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "16035");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("device_id", device_id);

        Gson gson = new Gson();
        String a = gson.toJson(map);
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<KongQiJianCeZ.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<KongQiJianCeZ.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<KongQiJianCeZ.DataBean>> response) {
                        showLoadSuccess();
                        tvJiaquan.setText(response.body().data.get(0).getGas_detection_list().get(0).getGd_cascophen());
                        tvPm.setText(response.body().data.get(0).getGas_detection_list().get(0).getGd_particulate_matter());
                        tvKongqiZhiliang.setText(response.body().data.get(0).getGas_detection_list().get(0).getGd_air_quality());
                        tvCo2.setText(response.body().data.get(0).getGas_detection_list().get(0).getGd_carbon_dioxide());

                    }

                    @Override
                    public void onError(Response<AppResponse<KongQiJianCeZ.DataBean>> response) {
                        String str = response.getException().getMessage();
                        UIHelper.ToastMessage(mContext, response.getException().getMessage());

                    }

                    @Override
                    public void onStart(Request<AppResponse<KongQiJianCeZ.DataBean>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void getnet() {
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "16074");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("device_id", device_id);
        map.put("date_type", "1");
        Calendar now = Calendar.getInstance();

        int month = now.get(Calendar.MONTH) + 1;
        String month_last;
        if (month < 10) {
            month_last = "0" + month;
        } else {
            month_last = String.valueOf(month);
        }
        String nianYueRi = now.get(Calendar.YEAR) + "-" + month_last + "-" + now.get(Calendar.DAY_OF_MONTH);
        map.put("time", nianYueRi);
        Gson gson = new Gson();
        String a = gson.toJson(map);
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<KongQiJianCeModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<KongQiJianCeModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<KongQiJianCeModel.DataBean>> response) {
                        showLoadSuccess();
                        SplineChart03View splineChart03View = new SplineChart03View(mContext, response.body().data.get(0).getGd_list(), "1");
                        llMain.addView(splineChart03View, layoutParams);
                        SplineChart03View splineChart03View1 = new SplineChart03View(mContext, response.body().data.get(0).getGd_list(), "2");
                        llMain2.addView(splineChart03View1, layoutParams);




                    }

                    @Override
                    public void onError(Response<AppResponse<KongQiJianCeModel.DataBean>> response) {
                        String str = response.getException().getMessage();
                        UIHelper.ToastMessage(mContext, response.getException().getMessage());

                    }

                    @Override
                    public void onStart(Request<AppResponse<KongQiJianCeModel.DataBean>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_kongqijiance_1;
    }


    @Override
    public boolean showToolBar() {
        return true;
    }

    String device_id;

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("????????????");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        iv_rightTitle.setVisibility(View.VISIBLE);
        iv_rightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SuiYiTieSetting.actionStart(mContext, "", "");
                ZhiNengJiaJuZhuangZhiSetting.actionStart(mContext, device_id);
            }
        });


        iv_rightTitle.setBackgroundResource(R.mipmap.fengnuan_icon_shezhi);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String device_id) {
        Intent intent = new Intent(context, KongQiJianCeActvity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("device_id", device_id);
        context.startActivity(intent);
    }
}
