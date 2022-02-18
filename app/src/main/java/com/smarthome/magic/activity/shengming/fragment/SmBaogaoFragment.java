package com.smarthome.magic.activity.shengming.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shengming.shengmingmodel.MacSleepReport;
import com.smarthome.magic.activity.shengming.utils.PieChartManagger;
import com.smarthome.magic.activity.shengming.utils.UrlUtils;
import com.smarthome.magic.activity.shengming.view.CircularProgressView;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.basicmvp.BaseFragment;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

public class SmBaogaoFragment extends BaseFragment {


    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.bt_more)
    TextView bt_more;
    @BindView(R.id.tv_xinlv_avg)
    TextView tv_xinlv_avg;
    @BindView(R.id.tv_huxi_avg)
    TextView tv_huxi_avg;
    @BindView(R.id.tv_xinlv_max)
    TextView tv_xinlv_max;
    @BindView(R.id.tv_huxi_max)
    TextView tv_huxi_max;
    @BindView(R.id.tv_xinlv_min)
    TextView tv_xinlv_min;
    @BindView(R.id.tv_huxi_min)
    TextView tv_huxi_min;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.pie_chat_shuimian)
    PieChart pie_chat_shuimian;
    @BindView(R.id.progress_circular)
    CircularProgressView progress_circular;
    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.tv_jianhu_cishu)
    TextView tv_jianhu_cishu;
    @BindView(R.id.tv_tidong_cishu)
    TextView tv_tidong_cishu;
    @BindView(R.id.tv_jianhu_zuichang)
    TextView tv_jianhu_zuichang;
    @BindView(R.id.tv_tidong_zuichang)
    TextView tv_tidong_zuichang;
    @BindView(R.id.tv_jianhu_zuiduan)
    TextView tv_jianhu_zuiduan;
    @BindView(R.id.tv_tidong_zuiduan)
    TextView tv_tidong_zuiduan;

    private String sessionId;

    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.shengming_frag_baogao;
    }

    @Override
    protected void initView(View rootView) {
        sessionId = PreferenceHelper.getInstance(getContext()).getString("sm_sessionId", "");
        showProgressDialog();
        getNet();
        initSM();
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

    private void getNet() {
        String timestamp = System.currentTimeMillis() + "";
        String ltoken = UrlUtils.getLtoken(sessionId, timestamp);
        String date = "20220211";
        OkGo.<MacSleepReport>get(UrlUtils.getMacSleepReport)
                .params("sessionId", sessionId)
                .params("timestamp", timestamp)
                .params("ltoken", ltoken)
                .params("mac", UrlUtils.MAC)
                .params("date", date)
                .tag(this)
                .execute(new JsonCallback<MacSleepReport>() {
                    @Override
                    public void onSuccess(Response<MacSleepReport> response) {
                        MacSleepReport.DataBean data = response.body().getData();
                        MacSleepReport.DataBean.SleepDataBeanX sleepMain = data.getSleepData();
                        List<MacSleepReport.DataBean.SleepDataBeanX.SleepDataBean> sleepList = sleepMain.getSleepData();
//                        int qingxing = 0;
//                        int qianshui = 0;
//                        int shenshui = 0;
//                        int kuaisuzhayan = 0;
//                        for (int i = 0; i < sleepList.size(); i++) {
//                            MacSleepReport.DataBean.SleepDataBeanX.SleepDataBean sleepDataBean = sleepList.get(i);
//                            int value = sleepDataBean.getValue();
//                            String s = sleepDataBean.getS();
//                            String e = sleepDataBean.getE();
//                            if (value == 0) {
//                                qingxing++;
//                                Y.e("我处于清醒状态  " + s + "  到   " + e);
//                            } else if (value == 1 || value == 2) {
//                                qianshui++;
//                                Y.e("我处于浅睡状态  " + s + "  到   " + e);
//                             } else if (value == 3 || value == 4) {
//                                shenshui++;
//                                Y.e("我处于深睡状态  " + s + "  到   " + e);
//                            } else if (value == 5) {
//                                kuaisuzhayan++;
//                                Y.e("快速眼动期  " + s + "  到   " + e);
//                            }
//                        }
                        Y.e(sleepMain.toString());

                        showRingPieChart(sleepMain);

                        MacSleepReport.DataBean.HrDataBeanX hrDataMain = data.getHrData();
                        tv_xinlv_avg.setText(hrDataMain.getAvg() + "次/分");
                        tv_xinlv_max.setText(hrDataMain.getMax() + "次/分");
                        tv_xinlv_min.setText(hrDataMain.getMin() + "次/分");

                        MacSleepReport.DataBean.RrDataBeanX rrDataMain = data.getRrData();
                        tv_huxi_avg.setText(rrDataMain.getAvg() + "次/分");
                        tv_huxi_max.setText(rrDataMain.getMax() + "次/分");
                        tv_huxi_min.setText(rrDataMain.getMin() + "次/分");

                        MacSleepReport.DataBean.StatusDataBean statusDataMain = data.getStatusData();
                        tv_jianhu_cishu.setText(statusDataMain.getLeaveTimes() + "次");
                        tv_jianhu_zuichang.setText(statusDataMain.getLeaveMaxDura() + "秒");
                        tv_jianhu_zuiduan.setText(statusDataMain.getLeaveMinDura() + "秒");

                        tv_tidong_cishu.setText(statusDataMain.getMoveTimes() + "次");
                        tv_tidong_zuichang.setText(statusDataMain.getMoveMaxDura() + "秒");
                        tv_tidong_zuiduan.setText(statusDataMain.getMoveMinDura() + "秒");

                        progress_circular.setProgress(80);
                        tv_progress.setText("85");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                        smartRefreshLayout.finishRefresh();
                    }
                });

    }

    @OnClick({R.id.iv_back, R.id.bt_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.bt_more:
                break;
        }
    }

    private void showRingPieChart(MacSleepReport.DataBean.SleepDataBeanX sleepMain) {
        int wakeDuraSec = sleepMain.getWakeDuraSec();
        int lightDuraMin = sleepMain.getLightDuraMin();
        int deepDuraMin = sleepMain.getDeepDuraMin();

        //设置每份所占数量
        List<PieEntry> yvals = new ArrayList<>();
        yvals.add(new PieEntry(wakeDuraSec, "清醒("+wakeDuraSec+"分)"));
        yvals.add(new PieEntry(lightDuraMin, "浅睡("+lightDuraMin+"分)"));
        yvals.add(new PieEntry(deepDuraMin, "深睡("+deepDuraMin+"分)"));
        // 设置每份的颜色
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#01D3AA"));
        colors.add(Color.parseColor("#0192FE"));
        colors.add(Color.parseColor("#A068E2"));

        PieChartManagger pieChartManagger = new PieChartManagger(pie_chat_shuimian);
        pieChartManagger.showRingPieChart(yvals, colors);
    }
}
