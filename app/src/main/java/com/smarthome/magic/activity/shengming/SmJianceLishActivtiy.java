package com.smarthome.magic.activity.shengming;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shengming.adapter.LishiHuxiAdapter;
import com.smarthome.magic.activity.shengming.adapter.LishiXinlvAdapter;
import com.smarthome.magic.activity.shengming.shengmingmodel.HistoryHrRrData;
import com.smarthome.magic.activity.shengming.utils.UrlUtils;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.PreferenceHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmJianceLishActivtiy extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_select_time)
    TextView tv_select_time;
    @BindView(R.id.ll_select_time)
    LinearLayout ll_select_time;
    @BindView(R.id.bt_xinlv)
    TextView bt_xinlv;
    @BindView(R.id.bt_huxi)
    TextView bt_huxi;
    @BindView(R.id.rv_xinlv)
    RecyclerView rv_xinlv;
    @BindView(R.id.rv_huxi)
    RecyclerView rv_huxi;
    @BindView(R.id.ll_no_data)
    LinearLayout ll_no_data;

    private String sessionId;

    private List<HistoryHrRrData.DataBean.HrDataBean> zongHrList = new ArrayList<>();
    private LishiXinlvAdapter xinlvAdapter;

    private List<HistoryHrRrData.DataBean.RrDataBean> zongRrList = new ArrayList<>();
    private LishiHuxiAdapter huxiAdapter;
    private String riqiDate;
    private TimePickerView selectTime;


    @Override
    public int getContentViewResId() {
        return R.layout.shengming_act_lishi;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SmJianceLishActivtiy.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public boolean showToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        init();
        getNet();
        initAdapter();
        selectXinlv();
    }

    private void init() {
        sessionId = PreferenceHelper.getInstance(mContext).getString("sm_sessionId", "");
        Date date = new Date();
        tv_select_time.setText(Y.getDate(date));
        riqiDate = Y.getDateWu(date);
    }

    private void initAdapter() {
        xinlvAdapter = new LishiXinlvAdapter(R.layout.shengming_item_lishi, zongHrList);
        rv_xinlv.setLayoutManager(new LinearLayoutManager(mContext));
        rv_xinlv.setAdapter(xinlvAdapter);

        huxiAdapter = new LishiHuxiAdapter(R.layout.shengming_item_lishi, zongRrList);
        rv_huxi.setLayoutManager(new LinearLayoutManager(mContext));
        rv_huxi.setAdapter(huxiAdapter);
    }

    private void getNet() {
        showProgressDialog();
        String timestamp = System.currentTimeMillis() + "";
        String ltoken = UrlUtils.getLtoken(sessionId, timestamp);
        OkGo.<HistoryHrRrData>get(UrlUtils.getHistoryHrRrData)
                .params("sessionId", sessionId)
                .params("timestamp", timestamp)
                .params("ltoken", ltoken)
                .params("mac", UrlUtils.MAC)
                .params("date", riqiDate)
                .tag(this)//
                .execute(new JsonCallback<HistoryHrRrData>() {
                    @Override
                    public void onSuccess(Response<HistoryHrRrData> response) {
                        if (response != null) {
                            zongHrList.clear();
                            zongRrList.clear();

                            HistoryHrRrData.DataBean data = response.body().getData();

                            List<List<HistoryHrRrData.DataBean.HrDataBean>> hrData = data.getHrData();
                            for (int i = 0; i < hrData.size(); i++) {
                                List<HistoryHrRrData.DataBean.HrDataBean> hrDataBeans = hrData.get(i);
                                for (int j = 0; j < hrDataBeans.size(); j++) {
                                    zongHrList.add(hrDataBeans.get(j));
                                }
                            }

                            List<List<HistoryHrRrData.DataBean.RrDataBean>> rrData = data.getRrData();
                            for (int i = 0; i < rrData.size(); i++) {
                                List<HistoryHrRrData.DataBean.RrDataBean> rrDataBeans = rrData.get(i);
                                for (int j = 0; j < rrDataBeans.size(); j++) {
                                    zongRrList.add(rrDataBeans.get(j));
                                }
                            }

                            xinlvAdapter.setNewData(zongHrList);
                            xinlvAdapter.notifyDataSetChanged();

                            huxiAdapter.setNewData(zongRrList);
                            huxiAdapter.notifyDataSetChanged();

                            if (zongHrList.size() > 0) {
                                ll_no_data.setVisibility(View.GONE);
                            } else {
                                ll_no_data.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }

    @OnClick({R.id.iv_back, R.id.ll_select_time, R.id.bt_xinlv, R.id.bt_huxi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_select_time:
                selectTime();
                break;
            case R.id.bt_xinlv:
                selectXinlv();
                break;
            case R.id.bt_huxi:
                selectHuxi();
                break;
        }
    }

    private void selectTime() {
        if (selectTime == null) {
            //时间选择器
            boolean[] select = {true, true, true, false, false, false};
            selectTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    tv_select_time.setText(Y.getDate(date));
                    riqiDate = Y.getDateWu(date);
                    getNet();
                }
            }).setType(select)
                    .build();
        }
        selectTime.show();
    }

    private void selectXinlv() {
        bt_xinlv.setBackgroundResource(R.drawable.shengming_lishi_bg_bt);
        bt_huxi.setBackground(null);
        bt_xinlv.setTextColor(Y.getColor(R.color.white));
        bt_huxi.setTextColor(Y.getColor(R.color.color_3));

        rv_xinlv.setVisibility(View.VISIBLE);
        rv_huxi.setVisibility(View.GONE);
    }

    private void selectHuxi() {
        bt_huxi.setBackgroundResource(R.drawable.shengming_lishi_bg_bt);
        bt_xinlv.setBackground(null);
        bt_huxi.setTextColor(Y.getColor(R.color.white));
        bt_xinlv.setTextColor(Y.getColor(R.color.color_3));

        rv_huxi.setVisibility(View.VISIBLE);
        rv_xinlv.setVisibility(View.GONE);
    }
}
