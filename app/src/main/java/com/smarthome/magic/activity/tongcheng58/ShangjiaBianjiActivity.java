package com.smarthome.magic.activity.tongcheng58;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.services.core.LatLonPoint;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.DefaultX5WebViewActivity;
import com.smarthome.magic.activity.ShuRuInterView;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.tongcheng58.adapter.ShangpinBannerAdapter;
import com.smarthome.magic.activity.tongcheng58.adapter.TcSheshiAdapter;
import com.smarthome.magic.activity.tongcheng58.model.ShangjiaDetailModel;
import com.smarthome.magic.activity.tongcheng58.model.TcBannerModel;
import com.smarthome.magic.activity.tongcheng58.model.TcHomeModel;
import com.smarthome.magic.activity.tongcheng58.model.TcLeimuModel;
import com.smarthome.magic.activity.tongcheng58.model.TcUpLoadModel;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.TongYongShuRuDIalog;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.get_net.Urls;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ShangjiaBianjiActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {


    @BindView(R.id.iv_logo_add)
    ImageView ivLogoAdd;
    @BindView(R.id.ed_name)
    TextView edName;
    @BindView(R.id.tv_hangyefenlei)
    TextView tvHangyefenlei;
    @BindView(R.id.ll_hangyefenlei)
    LinearLayout llHangyefenlei;
    @BindView(R.id.tv_yingye_time)
    TextView tvYingyeTime;
    @BindView(R.id.ll_yingye_time)
    LinearLayout llYingyeTime;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_adress)
    LinearLayout llAdress;
    @BindView(R.id.ed_weixinhao)
    TextView edWeixinhao;
    @BindView(R.id.ed_shangjiagonggao)
    TextView edShangjiagonggao;
    @BindView(R.id.ed_shangjiajieshao)
    TextView edShangjiajieshao;
    @BindView(R.id.ed_zhekou)
    TextView edZhekou;
    @BindView(R.id.tv_stop_time)
    TextView tvStopTime;
    @BindView(R.id.ll_qizhi_time)
    LinearLayout llStopTime;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.ll_ruzhu_time)
    LinearLayout llStartTime;
    @BindView(R.id.iv_xieyi)
    ImageView ivXieyi;
    @BindView(R.id.tv_xieyi)
    TextView tvXieyi;
    @BindView(R.id.bt_save)
    TextView btSave;
    @BindView(R.id.ed_phone)
    TextView edPhone;
    @BindView(R.id.ll_shangjiagonggao)
    LinearLayout llShangjiagonggao;
    @BindView(R.id.ll_shangjiajieshao)
    LinearLayout llShangjiajieshao;
    @BindView(R.id.rv_shebei)
    RecyclerView rvShebei;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.ll_weixin)
    LinearLayout llWeixin;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.ll_zhekou)
    LinearLayout llZhekou;
    @BindView(R.id.rv_banner)
    RecyclerView rvBanner;
    @BindView(R.id.tv_yingye_time_bi)
    TextView tvYingyeTimeBi;
    @BindView(R.id.ll_yingye_time_bi)
    LinearLayout llYingyeTimeBi;
    @BindView(R.id.tv_stop_time_end)
    TextView tvStopTimeEnd;
    @BindView(R.id.ll_qizhi_time_end)
    LinearLayout llQizhiTimeEnd;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private String ir_inst_logo;                    //商家logo
    private String ir_inst_logo_id;                 //商家logo的id
    private String ir_inst_name;                    //商家名称
    private String ir_industry_type;                //公司行业类型
    private String ir_industry_type_name;           //公司行业类型名称
    private String ir_industry_category;            //公司行业类别
    private String ir_industry_category_name;       //公司行业类别名称

    private String ir_inst_open_time;               //开业时间
    private String ir_inst_close_time;              //闭业时间
    private String ir_inst_begin_time;              //公司信息发布开始时间
    private String ir_inst_end_time;                //公司信息发布结束时间
    private String ir_contact_phone;                //联系电话
    private String x_weidu;                               //纬度
    private String y_jingdu;                               //经度
    private String addr;                            //地址
    private String ir_wx_number;                    //微信号
    private String ir_inst_notice;                  //公司公告
    private String ir_validity;                     //公司简介
    private String ir_agio;                         //折扣
    private String ir_inst_device;                  //店内设备（多选传1,2,3,4）
    private String ir_inst_settled_time;            //公司入驻时间
    private boolean isXieyi;

    private OptionsPickerView<Object> leimuPicker;
    private List<TcLeimuModel.DataBean> leimuModels;

    final static private String gonggao = "gonggao";
    final static private String jieshao = "jieshao";
    private String ir_id;
    private ShangjiaDetailModel.DataBean dataBean;
    private TcSheshiAdapter adapter;
    private List<ShangjiaDetailModel.DataBean.TypeArrayBean> dianneisheshi = new ArrayList<>();

    private List<TcBannerModel> bannerModels = new ArrayList<>();
    private List<TcUpLoadModel.DataBean> imgText_list = new ArrayList<>();
    private ShangpinBannerAdapter bannerAdapter;

    @Override
    public int getContentViewResId() {
        return R.layout.tongcheng_act_shangjiaruzhu;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("商家入驻");
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

    public static void actionStart(Context context, String ir_id) {
        Intent intent = new Intent(context, ShangjiaBianjiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("ir_id", ir_id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
        initStart();
        initHuidiao();
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_BIANMINFABU_HUICHUANDIZHI) {
                    List<Object> list = (List<Object>) message.content;
                    addr = (String) list.get(0);
                    LatLonPoint latLonPoint = (LatLonPoint) list.get(1);
                    x_weidu = String.valueOf(latLonPoint.getLatitude());
                    y_jingdu = String.valueOf(latLonPoint.getLongitude());
                    tvAddress.setText(addr);
                } else if (message.type == ConstanceValue.MSG_TONGYONG_INPUT) {
                    String content = (String) message.content;
                    String input_type = message.input_type;
                    if (input_type.equals(gonggao)) {
                        edShangjiagonggao.setText(content);
                    } else if (input_type.equals(jieshao)) {
                        edShangjiajieshao.setText(content);
                    }
                } else if (message.type == ConstanceValue.MSG_ADD_BANNER) {
                    imgText_list = (List<TcUpLoadModel.DataBean>) message.content;
                    bannerAdapter.setNewData(imgText_list);
                    bannerAdapter.notifyDataSetChanged();
                }
            }
        }));
    }

    private void initStart() {
        ir_id = getIntent().getStringExtra("ir_id");

        isXieyi = false;

        x_weidu = PreferenceHelper.getInstance(mContext).getString(App.WEIDU, "");
        y_jingdu = PreferenceHelper.getInstance(mContext).getString(App.JINGDU, "");

        initAdapter();
        getNet();
    }

    private void initAdapter() {
        adapter = new TcSheshiAdapter(R.layout.tongcheng_item_shangjia_sheshi, dianneisheshi);
        rvShebei.setLayoutManager(new GridLayoutManager(mContext, 3));
        rvShebei.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShangjiaDetailModel.DataBean.TypeArrayBean bean = dianneisheshi.get(position);
                String defaultX = bean.getDefaultX();
                if (TextUtils.isEmpty(defaultX)) {
                    bean.setDefaultX("1");
                } else {
                    if (defaultX.equals("1")) {
                        bean.setDefaultX("0");
                    } else {
                        bean.setDefaultX("1");
                    }
                }
                dianneisheshi.set(position, bean);
                adapter.notifyDataSetChanged();
            }
        });


        TcUpLoadModel.DataBean addBeen = new TcUpLoadModel.DataBean();
        imgText_list.add(addBeen);
        bannerAdapter = new ShangpinBannerAdapter(R.layout.tongcheng_item_shangpin_addimg, imgText_list);
        rvBanner.setAdapter(bannerAdapter);
        rvBanner.setLayoutManager(new GridLayoutManager(mContext, 4));
        bannerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (imgText_list != null && imgText_list.size() > position) {
                    switch (view.getId()) {
                        case R.id.iv_main:
                            ShangpinBannerActivity.actionStart(mContext, imgText_list);
                            break;
                    }
                }
            }
        });
    }


    private void getNet() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "17007");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("x", x_weidu);
        map.put("y", y_jingdu);
        map.put("ir_id", ir_id);
        map.put("operate_type", "2");
        Gson gson = new Gson();
        OkGo.<AppResponse<ShangjiaDetailModel.DataBean>>post(Urls.TONG_CHENG)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ShangjiaDetailModel.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<ShangjiaDetailModel.DataBean>> response) {
                        if (response.body().msg_code.equals("0000")) {
                            dataBean = response.body().data.get(0);

                            ir_inst_logo = dataBean.getIr_inst_logo();
                            ir_inst_logo_id = dataBean.getIr_inst_logo_id();
                            Glide.with(mContext).load(ir_inst_logo).into(ivLogoAdd);
                            ir_inst_name = dataBean.getIr_inst_name();
                            edName.setText(ir_inst_name);

                            ir_industry_type = dataBean.getIr_industry_type();
                            ir_industry_type_name = dataBean.getIr_industry_type_name();
                            ir_industry_category = dataBean.getIr_industry_category();
                            ir_industry_category_name = dataBean.getIr_industry_category_name();
                            tvHangyefenlei.setText(ir_industry_type_name + " - " + ir_industry_category_name);

                            ir_inst_open_time = dataBean.getIr_inst_open_time();
                            ir_inst_close_time = dataBean.getIr_inst_close_time();
                            tvYingyeTime.setText(ir_inst_open_time);
                            tvYingyeTimeBi.setText(ir_inst_close_time);


                            ir_contact_phone = dataBean.getIr_contact_phone();
                            edPhone.setText(ir_contact_phone);

                            addr = dataBean.getAddr();
                            tvAddress.setText(addr);

                            x_weidu = dataBean.getX();
                            y_jingdu = dataBean.getY();

                            ir_wx_number = dataBean.getIr_wx_number();
                            edWeixinhao.setText(ir_wx_number);

                            ir_inst_notice = dataBean.getIr_inst_notice();
                            edShangjiagonggao.setText(ir_inst_notice);

                            ir_validity = dataBean.getIr_validity();
                            edShangjiajieshao.setText(ir_validity);

                            ir_agio = dataBean.getIr_agio();
                            if (!TextUtils.isEmpty(ir_agio)) {
                                edZhekou.setText(ir_agio + "折");
                                ir_inst_begin_time = dataBean.getIr_inst_begin_time();
                                ir_inst_end_time = dataBean.getIr_inst_end_time();
                                tvStopTime.setText(ir_inst_begin_time);
                                tvStopTimeEnd.setText(ir_inst_end_time);
                            } else {
                                ir_agio = "";
                                ir_inst_begin_time = "";
                                ir_inst_end_time = "";
                                edZhekou.setText(ir_agio);
                                tvStopTime.setText(ir_inst_begin_time);
                                tvStopTimeEnd.setText(ir_inst_end_time);
                            }

                            ir_inst_settled_time = dataBean.getIr_inst_settled_time();
                            tvStartTime.setText(ir_inst_settled_time + "个月");

                            dianneisheshi = dataBean.getType_array();
                            adapter.setNewData(dianneisheshi);
                            adapter.notifyDataSetChanged();

                            List<ShangjiaDetailModel.DataBean.ImgListBean> imgList = dataBean.getImgList();
                            List<TcUpLoadModel.DataBean> imgLists = new ArrayList<>();

                            for (int i = 0; i < imgList.size(); i++) {
                                ShangjiaDetailModel.DataBean.ImgListBean imgListBean = imgList.get(i);
                                TcUpLoadModel.DataBean addBeen = new TcUpLoadModel.DataBean();
                                addBeen.setImg_url(imgListBean.getIr_img_url());
                                addBeen.setImg_id(imgListBean.getIr_img_id());
                                imgLists.add(addBeen);
                            }
                            imgText_list.addAll(0, imgLists);
                            bannerAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


    @OnClick({R.id.ll_yingye_time_bi, R.id.ll_qizhi_time_end, R.id.ll_name, R.id.ll_weixin, R.id.ll_phone, R.id.ll_zhekou, R.id.ll_shangjiagonggao, R.id.ll_shangjiajieshao, R.id.iv_logo_add, R.id.ll_hangyefenlei, R.id.ll_yingye_time, R.id.ll_adress, R.id.ll_qizhi_time, R.id.ll_ruzhu_time, R.id.iv_xieyi, R.id.tv_xieyi, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_logo_add:
                getPicture();
                break;
            case R.id.ll_hangyefenlei:
                clickFenlei();
                break;
            case R.id.ll_adress:
                Intent intent = new Intent(mContext, PoiKeywordSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_ruzhu_time:
                clickRuzhuTime();
                break;
            case R.id.iv_xieyi:
                clickXiyiSelect();
                break;
            case R.id.tv_xieyi:
                DefaultX5WebViewActivity.actionStart(mContext, Urls.SERVER_URL + "ir/gjIssueNotice");
                break;
            case R.id.bt_save:
                save();
                break;
            case R.id.ll_shangjiagonggao:
                TcInputActivity.actionStart(mContext, gonggao, edShangjiagonggao.getText().toString());
                break;
            case R.id.ll_shangjiajieshao:
                TcInputActivity.actionStart(mContext, jieshao, edShangjiajieshao.getText().toString());
                break;
            case R.id.ll_name:
                clickTongyongshuru("name");
                break;
            case R.id.ll_weixin:
                clickTongyongshuru("weixin");
                break;
            case R.id.ll_phone:
                clickTongyongshuru("phone");
                break;
            case R.id.ll_zhekou:
                clickZheko();
                break;
            case R.id.ll_yingye_time:
                clickTime(0);
                break;
            case R.id.ll_yingye_time_bi:
                clickTime(1);
                break;
            case R.id.ll_qizhi_time:
                clickData(0);
                break;
            case R.id.ll_qizhi_time_end:
                clickData(1);
                break;
        }
    }

    private void clickTongyongshuru(String type) {
        String title;
        String content;

        if (type.equals("name")) {
            title = "请输入商家名称";
            content = edName.getText().toString();
        } else if (type.equals("weixin")) {
            title = "请输入老板微信";
            content = edWeixinhao.getText().toString();
        } else if (type.equals("phone")) {
            title = "请输入联系电话";
            content = edPhone.getText().toString();
        } else {
            title = "请输入折扣";
            content = edZhekou.getText().toString();
        }

        TongYongShuRuDIalog tongYongShuRuDIalog = new TongYongShuRuDIalog(mContext, new ShuRuInterView() {
            @Override
            public void cannel() {

            }

            @Override
            public void submit(String str) {
                if (type.equals("name")) {
                    edName.setText(str);
                } else if (type.equals("weixin")) {
                    edWeixinhao.setText(str);
                } else if (type.equals("phone")) {
                    edPhone.setText(str);
                } else {
                    edZhekou.setText(str);
                }
            }
        }, title);
        if (type.equals("name")) {
            tongYongShuRuDIalog.et_name.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (type.equals("weixin")) {
            tongYongShuRuDIalog.et_name.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (type.equals("phone")) {
            tongYongShuRuDIalog.et_name.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            tongYongShuRuDIalog.et_name.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        tongYongShuRuDIalog.setmContext(content);
        tongYongShuRuDIalog.show();
    }

    private void clickRuzhuTime() {
        String[] items = {"3个月", "6个月", "12个月"};
        ActionSheetDialog dialog = new ActionSheetDialog(mContext, items, null);
        dialog.isTitleShow(false);
        dialog.cancelText("完成");
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ir_inst_settled_time = "3";
                        break;
                    case 1:
                        ir_inst_settled_time = "6";
                        break;
                    case 2:
                        ir_inst_settled_time = "12";
                        break;
                }
                tvStartTime.setText(ir_inst_settled_time + "个月");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void clickZheko() {
        String[] items = {"无折扣", "1折", "2折", "3折", "4折", "5折", "6折", "7折", "8折", "9折"};
        ActionSheetDialog dialog = new ActionSheetDialog(mContext, items, null);
        dialog.isTitleShow(false);
        dialog.cancelText("完成");
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ir_agio = "";
                    edZhekou.setText("");
                    tvStopTime.setText("");
                    tvStopTimeEnd.setText("");
                    ir_inst_begin_time = "";
                    ir_inst_end_time = "";
                } else {
                    ir_agio = position + "";
                    edZhekou.setText(ir_agio + "折");
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void save() {
        ir_inst_name = edName.getText().toString();
        ir_wx_number = edWeixinhao.getText().toString();
        ir_contact_phone = edPhone.getText().toString();
        addr = tvAddress.getText().toString();
        ir_inst_notice = edShangjiagonggao.getText().toString();
        ir_validity = edShangjiajieshao.getText().toString();

        if (TextUtils.isEmpty(ir_inst_logo)) {
            Y.t("请上传企业标志");
            return;
        }

        if (TextUtils.isEmpty(ir_inst_name)) {
            Y.t("请输入商家名称");
            return;
        }

        if (TextUtils.isEmpty(ir_industry_type)) {
            Y.t("请选择行业分类");
            return;
        }

        if (TextUtils.isEmpty(ir_inst_open_time) || TextUtils.isEmpty(ir_inst_close_time)) {
            Y.t("请选择营业时间");
            return;
        }

        if (TextUtils.isEmpty(addr)) {
            Y.t("请选择详细地址");
            return;
        }

        if (TextUtils.isEmpty(ir_contact_phone)) {
            Y.t("请输入联系电话");
            return;
        }

        ir_inst_device = "";
        if (dianneisheshi.size() > 0) {
            for (int i = 0; i < dianneisheshi.size(); i++) {
                String defaultX = dianneisheshi.get(i).getDefaultX();
                String id = dianneisheshi.get(i).getId();
                if (!TextUtils.isEmpty(defaultX)) {
                    if (defaultX.equals("1")) {
                        ir_inst_device = ir_inst_device + id + ",";
                    }
                }
            }
        }

        if (TextUtils.isEmpty(ir_inst_device)) {
            Y.t("请选择店内设施");
            return;
        }
        ir_inst_device = ir_inst_device.substring(0, ir_inst_device.length() - 1);

        bannerModels.clear();
        if (imgText_list.size() > 1) {
            for (int i = 0; i < imgText_list.size() - 1; i++) {
                TcUpLoadModel.DataBean dataBean = imgText_list.get(i);
                bannerModels.add(new TcBannerModel(dataBean.getImg_id(), dataBean.getImg_url()));
            }
        }
        if (bannerModels.size() == 0) {
            Y.t("请上传轮播图");
            return;
        }

        if (TextUtils.isEmpty(ir_inst_settled_time)) {
            Y.t("请选择入驻时间");
            return;
        }

        if (!isXieyi) {
            Y.t("请同意商家发布须知");
            return;
        }

        if (!TextUtils.isEmpty(ir_agio)) {
            if (TextUtils.isEmpty(ir_inst_begin_time)) {
                Y.t("请选择折扣开始日期");
                return;
            }

            if (TextUtils.isEmpty(ir_inst_end_time)) {
                Y.t("请选择折扣结束日期");
                return;
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "17011");
        jsonObject.put("key", Urls.key);
        jsonObject.put("token", UserManager.getManager(mContext).getAppToken());
        jsonObject.put("ir_id", ir_id);
        jsonObject.put("ir_type", "2");
        jsonObject.put("ir_inst_logo", ir_inst_logo);
        jsonObject.put("ir_inst_logo_id", ir_inst_logo_id);
        jsonObject.put("ir_inst_name", ir_inst_name);
        jsonObject.put("ir_industry_type", ir_industry_type);
        jsonObject.put("ir_industry_type_name", ir_industry_type_name);
        jsonObject.put("ir_industry_category", ir_industry_category);
        jsonObject.put("ir_industry_category_name", ir_industry_category_name);
        jsonObject.put("ir_contact_phone", ir_contact_phone);
        jsonObject.put("x", x_weidu);
        jsonObject.put("y", y_jingdu);
        jsonObject.put("addr", addr);
        jsonObject.put("ir_wx_number", ir_wx_number);
        jsonObject.put("pro", bannerModels);
        jsonObject.put("ir_validity", ir_validity);
        jsonObject.put("ir_inst_notice", ir_inst_notice);
        jsonObject.put("ir_agio", ir_agio);
        jsonObject.put("ir_inst_device", ir_inst_device);
        jsonObject.put("ir_inst_settled_time", ir_inst_settled_time);
        jsonObject.put("ir_inst_open_time", ir_inst_open_time);
        jsonObject.put("ir_inst_close_time", ir_inst_close_time);

        if (TextUtils.isEmpty(ir_agio)) {
            jsonObject.put("ir_inst_begin_time", "");
            jsonObject.put("ir_inst_end_time", "");
        } else {
            jsonObject.put("ir_inst_begin_time", ir_inst_begin_time);
            jsonObject.put("ir_inst_end_time", ir_inst_end_time);
        }

        showProgressDialog();
        OkGo.<AppResponse<TcHomeModel.DataBean>>post(Urls.TONG_CHENG)
                .tag(this)//
                .upJson(jsonObject.toJSONString())
                .execute(new JsonCallback<AppResponse<TcHomeModel.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<TcHomeModel.DataBean>> response) {
                        TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_SUCESS, new TishiDialog.TishiDialogListener() {
                            @Override
                            public void onClickCancel(View v, TishiDialog dialog) {

                            }

                            @Override
                            public void onClickConfirm(View v, TishiDialog dialog) {
                                finish();

                            }

                            @Override
                            public void onDismiss(TishiDialog dialog) {

                            }
                        });
                        dialog.show();
                    }

                    @Override
                    public void onError(Response<AppResponse<TcHomeModel.DataBean>> response) {
                        super.onError(response);
                        Y.tError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }

    private void clickXiyiSelect() {
        isXieyi = !isXieyi;
        if (isXieyi) {
            ivXieyi.setImageResource(R.mipmap.tuya_faxian_icon_selector_sel_1);
        } else {
            ivXieyi.setImageResource(R.mipmap.tuya_faxian_icon_selector_sel);
        }
    }

    private void clickFenlei() {
        if (leimuPicker == null) {
            showProgressDialog();
            getLeimu();
        } else {
            leimuPicker.show();
        }
    }

    private void getLeimu() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "17003");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("itemtype_id", "5");
        Gson gson = new Gson();
        OkGo.<AppResponse<TcLeimuModel.DataBean>>post(Urls.TONG_CHENG)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<TcLeimuModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<TcLeimuModel.DataBean>> response) {
                        leimuModels = response.body().data;
                        initLeimu();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }

    private void initLeimu() {
        if (leimuModels.size() > 0) {
            List<Object> names1 = new ArrayList<>();
            List<List<Object>> names2 = new ArrayList<>();

            for (int i = 0; i < leimuModels.size(); i++) {
                TcLeimuModel.DataBean dataBean = leimuModels.get(i);
                names1.add(dataBean.getItem_name());
                List<TcLeimuModel.DataBean.ItemBean> item = dataBean.getItem();
                List<Object> names2Beans = new ArrayList<>();
                for (int j = 0; j < item.size(); j++) {
                    TcLeimuModel.DataBean.ItemBean bean = item.get(j);
                    names2Beans.add(bean.getItem_name());
                }
                names2.add(names2Beans);
            }


            //条件选择器
            leimuPicker = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                    if (leimuModels != null && leimuModels.size() > 0) {

                        TcLeimuModel.DataBean dataBean = leimuModels.get(options1);
                        ir_industry_type = dataBean.getItem_id();
                        ir_industry_type_name = dataBean.getItem_name();

                        TcLeimuModel.DataBean.ItemBean itemBean = dataBean.getItem().get(option2);
                        ir_industry_category = itemBean.getItem_id();
                        ir_industry_category_name = itemBean.getItem_name();

                        tvHangyefenlei.setText(ir_industry_type_name + " - " + ir_industry_category_name);
                    }
                }
            }).build();

            leimuPicker.setPicker(names1, names2);
            leimuPicker.show();
        }
    }

    private void clickTime(int pos) {
        boolean[] select = {false, false, false, true, true, false};
        TimePickerView timePicker = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                int hours = date.getHours();
                int minutes = date.getMinutes();
                String time;
                if (hours < 10) {
                    if (minutes < 10) {
                        time = "0" + hours + ":" + "0" + minutes;
                    } else {
                        time = "0" + hours + ":" + minutes;
                    }
                } else {
                    if (minutes < 10) {
                        time = hours + ":" + "0" + minutes;
                    } else {
                        time = hours + ":" + minutes;
                    }
                }
                if (pos == 0) {
                    ir_inst_open_time = time;
                    tvYingyeTime.setText(ir_inst_open_time);
                } else {
                    ir_inst_close_time = time;
                    tvYingyeTimeBi.setText(ir_inst_close_time);
                }
            }
        })
                .setType(select)// 默认全部显示
                .build();
        timePicker.show();
    }

    private void clickData(int pos) {
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(new Date());
        Calendar endTime = Calendar.getInstance();
        endTime.set(2100, 12, 31);
        boolean[] select = {true, true, true, false, false, false};
        TimePickerView timePicker = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String dataS = Y.getDate(date);
                if (pos == 0) {
                    ir_inst_begin_time = dataS;
                    tvStopTime.setText(ir_inst_begin_time);
                } else {
                    ir_inst_end_time = dataS;
                    tvStopTimeEnd.setText(ir_inst_end_time);
                }
            }
        })
                .setType(select)// 默认全部显示
                .setRangDate(startTime, endTime)
                .build();
        timePicker.show();
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(ShangjiaBianjiActivity.this, this));
        }
        return takePhoto;
    }

    private void getPicture() {
        String[] items = {"拍照", "相册"};
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, items, null);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                Uri imageUri = Uri.fromFile(file);
                switch (position) {
                    case 0:
                        takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                        break;
                    case 1:
                        takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void takeSuccess(TResult result) {
        //此处使用原图路径，不压缩
        File file = new File(result.getImage().getOriginalPath());
        OkGo.<AppResponse<TcUpLoadModel.DataBean>>post(Urls.TONG_CHENG_UPLOAD)
                .tag(this)//
                .isMultipart(true)
                .params("key", Urls.key)
                .params("token", UserManager.getManager(mContext).getAppToken())
                .params("type", "1")
                .params("file", file)
                .execute(new JsonCallback<AppResponse<TcUpLoadModel.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<TcUpLoadModel.DataBean>> response) {
                        if (response.body().msg_code.equals("0000")) {
                            Glide.with(mContext).load(file.getPath()).into(ivLogoAdd);
                            TcUpLoadModel.DataBean dataBean = response.body().data.get(0);
                            ir_inst_logo = dataBean.getImg_url();
                            ir_inst_logo_id = dataBean.getImg_id();
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<TcUpLoadModel.DataBean>> response) {
                        super.onError(response);
                        Y.tError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }

                    @Override
                    public void onStart(Request<AppResponse<TcUpLoadModel.DataBean>, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog();
                    }
                });
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 配置takerPhoto参数
     */
    public CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(800).setAspectY(800);
        return builder.create();
    }
}
