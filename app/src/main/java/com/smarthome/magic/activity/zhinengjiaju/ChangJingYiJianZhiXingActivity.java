package com.smarthome.magic.activity.zhinengjiaju;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flyco.roundview.RoundRelativeLayout;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.ShuRuInterView;
import com.smarthome.magic.adapter.ChuangJianZhiNengTiaoJianListAdapter;
import com.smarthome.magic.adapter.ZhiNengChangJingSheBeiAdapter1;
import com.smarthome.magic.app.AppConfig;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.common.StringUtils;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.ShuRuDIalog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.ChangJingWhereModel;
import com.smarthome.magic.model.ChangJingXiangQingModel;
import com.smarthome.magic.model.ChangJingZhiXingModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.app.ConstanceValue.MSG_ZHINENGJIAJU_SHOUYE_SHUAXIN;
import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;

public class ChangJingYiJianZhiXingActivity extends BaseActivity {
    @BindView(R.id.rl_yijianzhixing_tianjiatiaojian)
    RoundRelativeLayout rlYijianzhixingTianjiatiaojian;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.rl_yijianzhixing)
    RoundRelativeLayout rlYijianzhixing;
    @BindView(R.id.ll_tianjiashebei)
    LinearLayout llTianjiashebei;
    @BindView(R.id.rl_changjing)
    RoundRelativeLayout rlChangjing;
    @BindView(R.id.ll_yijian_zhixing)
    LinearLayout llYijianZhixing;
    @BindView(R.id.rll_changjingmingcheng)
    RoundRelativeLayout rllChangjingmingcheng;
    ShuRuDIalog shuRuDIalog;
    @BindView(R.id.tv_changjingmingcheng)
    TextView tvChangjingmingcheng;
    @BindView(R.id.ll_tianjiatiaojian)
    LinearLayout llTianjiatiaojian;
    @BindView(R.id.rlv_list)
    RecyclerView rlvList;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_tiaoJianMing)
    TextView tvTiaoJianMing;
    @BindView(R.id.tv_tiaojianxihua)
    TextView tvTiaojianxihua;
    @BindView(R.id.iv_icon_1)
    ImageView ivIcon1;
    @BindView(R.id.tiaojian_list)
    RecyclerView tiaojianList;
    private String familyId;
    private String zhiXingLeiXing;
    ZhiNengChangJingSheBeiAdapter1 zhiNengChangJingSheBeiAdapter1;
    private List<ChangJingZhiXingModel> mDatas = new ArrayList<>();
    String iconStr;

    private String kaiShiSHiJian;
    private String chongFuFangShi;

    private List<String> xingQiList = new ArrayList<>();
    ChangJingZhiXingModel changJingZhiXingModel;
    ChuangJianZhiNengTiaoJianListAdapter chuangJianZhiNengTiaoJianListAdapter;

    List<ChangJingZhiXingModel> changJingZhiXingModels = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        familyId = PreferenceHelper.getInstance(mContext).getString(AppConfig.FAMILY_ID, "");
        zhiXingLeiXing = getIntent().getStringExtra("zhiXingLeiXing");
        kaiShiSHiJian = getIntent().getStringExtra("kaiShiShiJian");
        chongFuFangShi = getIntent().getStringExtra("chongFuFangShi");
        changJingZhiXingModel = (ChangJingZhiXingModel) getIntent().getSerializableExtra("changJingZhiXingModel");
        mDatas = new ArrayList<>();
        xingQiList = (List<String>) getIntent().getSerializableExtra("chongFuJuTiXingQi");

        String str = "";
        if (xingQiList != null) {
            if (xingQiList.get(0).equals("1")) {
                str = str + "??????";
            }
            if (xingQiList.get(1).equals("1")) {
                str = str + "??????";
            }
            if (xingQiList.get(2).equals("1")) {
                str = str + "??????";
            }
            if (xingQiList.get(3).equals("1")) {
                str = str + "??????";
            }
            if (xingQiList.get(4).equals("1")) {
                str = str + "??????";
            }
            if (xingQiList.get(5).equals("1")) {
                str = str + "??????";
            }
            if (xingQiList.get(6).equals("1")) {
                str = str + "??????";
            }
        }

        if (zhiXingLeiXing.equals("1")) {
            llTianjiatiaojian.setVisibility(View.GONE);
            tvTiaojianxihua.setVisibility(View.GONE);
            tvTiaoJianMing.setText("????????????");
            Glide.with(mContext).load(R.mipmap.addchangjing_icon_yijianzhixing).into(ivImage);

            tiaojianList.setVisibility(View.GONE);
        } else if (zhiXingLeiXing.equals("2")) {
            tvTiaoJianMing.setText("??????");
            tvTiaojianxihua.setVisibility(View.VISIBLE);
            tvTiaojianxihua.setText(str + "  " + kaiShiSHiJian);
            Glide.with(mContext).load(R.mipmap.addchangjing_icon_time).into(ivImage);
            llTianjiatiaojian.setVisibility(View.GONE);

            tiaojianList.setVisibility(View.GONE);
        } else if (zhiXingLeiXing.equals("3")) {

            rlYijianzhixing.setVisibility(View.VISIBLE);
            tiaojianList.setVisibility(View.GONE);

            Glide.with(mContext).load(changJingZhiXingModel.img_url).into(ivImage);
            tvTiaoJianMing.setText(changJingZhiXingModel.device_name);

            if (changJingZhiXingModel.pro_go_one.equals("1")) {
                tvTiaojianxihua.setText("??????");
            } else {
                tvTiaojianxihua.setText("??????");
            }

            initThreeLeiXingAdapter();

        }
        rlChangjing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangJingTuBiaoActivity.actionStart(mContext);
            }
        });

        rllChangjingmingcheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuRuDIalog = new ShuRuDIalog(mContext, new ShuRuInterView() {
                    @Override
                    public void cannel() {
                        shuRuDIalog.dismiss();
                    }

                    @Override
                    public void submit(String str) {
                        tvChangjingmingcheng.setText(str);
                        //   bianJiChangJingMingCheng(str);
                    }
                });
                shuRuDIalog.show();
            }
        });

        llTianjiashebei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SheBeiBianHuaActivity.actionStart(mContext, familyId, "");
            }
        });
        //initAdapter();

        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_ZHINENGJIAJU_SHEBEILIEBIAO) {
                    zhiNengChangJingSheBeiAdapter1 = new ZhiNengChangJingSheBeiAdapter1(R.layout.item_zhinengchangjing_shebei_1, mDatas);
                    ChangJingZhiXingModel changJingZhiXingModel = (ChangJingZhiXingModel) message.content;
                    mDatas.add(changJingZhiXingModel);
                    initAdapter();
                    zhiNengChangJingSheBeiAdapter1.notifyDataSetChanged();

                } else if (message.type == ConstanceValue.MSG_ZHINENGJIAJU_ICON) {
                    iconStr = (String) message.content;
                    Glide.with(mContext).load(iconStr).into(ivIcon);
                }
            }
        }));
    }

    private void initThreeLeiXingAdapter() {


        changJingZhiXingModels.add(changJingZhiXingModel);
        chuangJianZhiNengTiaoJianListAdapter = new ChuangJianZhiNengTiaoJianListAdapter(R.layout.item_chuang_jian_zhi_neng_tiao_jian, changJingZhiXingModels);
        tiaojianList.setAdapter(chuangJianZhiNengTiaoJianListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        tiaojianList.setLayoutManager(linearLayoutManager);
        chuangJianZhiNengTiaoJianListAdapter.setNewData(changJingZhiXingModels);

    }

    private void initAdapter() {
        rlvList.setAdapter(zhiNengChangJingSheBeiAdapter1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        rlvList.setLayoutManager(linearLayoutManager);
        zhiNengChangJingSheBeiAdapter1.setNewData(mDatas);
    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_changjing_yijianzhixing;
    }


    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("??????");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        tv_rightTitle.setText("??????");
        tv_rightTitle.setVisibility(View.VISIBLE);
        tv_rightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  UIHelper.ToastMessage(mContext, "???????????????");
                baoCun();
            }
        });
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imm.hideSoftInputFromWindow(findViewById(R.id.cl_layout).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });
    }

    private void baoCun() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "16056");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        if (tvChangjingmingcheng.getText().toString().equals("????????????")) {
            UIHelper.ToastMessage(mContext, "?????????????????????????????????");
            return;
        } else {
            map.put("scene_title", tvChangjingmingcheng.getText().toString());
        }
        if (!StringUtils.isEmpty(iconStr)) {
            map.put("scene_pic", iconStr);
        } else {
            UIHelper.ToastMessage(mContext, "?????????????????????");
            return;
        }
        if (mDatas.size() == 0) {
            UIHelper.ToastMessage(mContext, "??????????????????????????????");
            return;
        }
        map.put("scene_type", zhiXingLeiXing);
        map.put("family_id", familyId);

        if (zhiXingLeiXing.equals("1")) {
            map.put("go", mDatas);

        } else if (zhiXingLeiXing.equals("2")) {
            ChangJingWhereModel changJingWhereModel = new ChangJingWhereModel();
            changJingWhereModel.week_time_begin = kaiShiSHiJian;
            changJingWhereModel.week_time_oper = chongFuFangShi;
            List<ChangJingWhereModel> changJingWhereModels = new ArrayList<>();
            if (chongFuFangShi.equals("4")) {
                if (xingQiList != null && xingQiList.size() > 0) {
                    if (xingQiList.get(0).equals("1")) {
                        changJingWhereModel.week_1 = "1";
                    } else {
                        changJingWhereModel.week_1 = "2";
                    }

                    if (xingQiList.get(1).equals("1")) {
                        changJingWhereModel.week_2 = "1";
                    } else {
                        changJingWhereModel.week_2 = "2";
                    }

                    if (xingQiList.get(2).equals("1")) {
                        changJingWhereModel.week_3 = "1";
                    } else {
                        changJingWhereModel.week_3 = "2";
                    }

                    if (xingQiList.get(3).equals("1")) {
                        changJingWhereModel.week_4 = "1";
                    } else {
                        changJingWhereModel.week_4 = "2";
                    }
                    if (xingQiList.get(4).equals("1")) {
                        changJingWhereModel.week_5 = "1";
                    } else {
                        changJingWhereModel.week_5 = "2";
                    }
                    if (xingQiList.get(5).equals("1")) {
                        changJingWhereModel.week_6 = "1";
                    } else {
                        changJingWhereModel.week_6 = "2";
                    }
                    if (xingQiList.get(6).equals("1")) {
                        changJingWhereModel.week_7 = "1";
                    } else {
                        changJingWhereModel.week_7 = "2";
                    }
                }
            }
            changJingWhereModels.add(changJingWhereModel);
            map.put("where", changJingWhereModels);
            map.put("go", mDatas);
        } else if (zhiXingLeiXing.equals("3")) {

            map.put("where", changJingZhiXingModels);
            map.put("go", mDatas);
        }
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<ChangJingXiangQingModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ChangJingXiangQingModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ChangJingXiangQingModel.DataBean>> response) {
                        UIHelper.ToastMessage(mContext, "??????????????????");

                        Notice notice = new Notice();
                        notice.type = MSG_ZHINENGJIAJU_SHOUYE_SHUAXIN;
                        RxBus.getDefault().sendRx(notice);
                        finish();
                    }

                    @Override
                    public void onError(Response<AppResponse<ChangJingXiangQingModel.DataBean>> response) {
                        String str = response.getException().getMessage();
                        UIHelper.ToastMessage(mContext, str);
                    }
                });

    }


    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String zhiXingLeiXing, String kaiShiShiJian, String chongFuFangShi, List<String> strings) {
        Intent intent = new Intent(context, ChangJingYiJianZhiXingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("zhiXingLeiXing", zhiXingLeiXing);
        intent.putExtra("kaiShiShiJian", kaiShiShiJian);
        intent.putExtra("chongFuFangShi", chongFuFangShi);
        intent.putExtra("chongFuJuTiXingQi", (Serializable) strings);
        context.startActivity(intent);
    }

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String zhiXingLeiXing, ChangJingZhiXingModel changJingZhiXingModel) {
        Intent intent = new Intent(context, ChangJingYiJianZhiXingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("zhiXingLeiXing", zhiXingLeiXing);
        intent.putExtra("changJingZhiXingModel", changJingZhiXingModel);
        context.startActivity(intent);
    }

}
