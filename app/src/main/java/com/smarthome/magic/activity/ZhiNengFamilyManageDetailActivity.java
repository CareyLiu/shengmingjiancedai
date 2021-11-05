package com.smarthome.magic.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.tuya_device.utils.TuyaConfig;
import com.smarthome.magic.activity.tuya_device.utils.manager.TuyaHomeManager;
import com.smarthome.magic.adapter.ZhiNengFamilyManageDetailAdapter;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.MyCarCaoZuoDialog_CaoZuoTIshi;
import com.smarthome.magic.dialog.MyCarCaoZuoDialog_Success;
import com.smarthome.magic.dialog.ZhiNengFamilyAddDIalog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.ChandiModel;
import com.smarthome.magic.model.ZhiNengFamilyEditBean;
import com.smarthome.magic.model.ZhiNengFamilyMAnageDetailBean;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.MemberBean;
import com.tuya.smart.home.sdk.callback.ITuyaGetMemberListCallback;
import com.tuya.smart.sdk.api.IResultCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;

public class ZhiNengFamilyManageDetailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.rl_family_name)
    RelativeLayout rl_family_name;
    @BindView(R.id.tv_family_name)
    TextView tv_family_name;
    @BindView(R.id.rl_family_address)
    RelativeLayout rl_family_address;
    @BindView(R.id.tv_family_address)
    TextView tv_family_address;
    @BindView(R.id.tv_family_device_num)
    TextView tv_family_device_num;
    @BindView(R.id.rl_family_room_num)
    RelativeLayout rl_family_room_num;
    @BindView(R.id.tv_family_room_num)
    TextView tv_family_room_num;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_family_delete)
    TextView tv_family_delete;
    private LinearLayout ll_add_share;
    private Context context = ZhiNengFamilyManageDetailActivity.this;
    private ZhiNengFamilyMAnageDetailBean.DataBean dataBean;
    private List<ZhiNengFamilyMAnageDetailBean.DataBean.MemberBean> memberBeans = new ArrayList<>();
    private ZhiNengFamilyManageDetailAdapter zhiNengFamilyManageDetailAdapter;
    private View footerView;
    private String family_id = "";
    private String ty_family_id;
    private String member_type;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_zhineng_family_manage_detail;
    }

    @Override
    public void initImmersion() {
        mImmersionBar.with(this).statusBarColor(R.color.white).init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setLightMode(this);
        initToolbar();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        family_id = getIntent().getStringExtra("family_id");
        ty_family_id = getIntent().getStringExtra("ty_family_id");
        if (family_id == null) {
            family_id = "";
        }
        getnet();
    }

    private void initView() {
        footerView = LayoutInflater.from(context).inflate(R.layout.activity_zhineng_family_manage_footer, null);
        ll_add_share = footerView.findViewById(R.id.ll_add_share);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        zhiNengFamilyManageDetailAdapter = new ZhiNengFamilyManageDetailAdapter(R.layout.item_zhineng_family_manage_detail, memberBeans);
        recyclerView.setAdapter(zhiNengFamilyManageDetailAdapter);
        rl_family_name.setOnClickListener(this);
        rl_family_address.setOnClickListener(this);
        rl_family_room_num.setOnClickListener(this);
        tv_family_delete.setOnClickListener(this);
        ll_add_share.setOnClickListener(this);
        zhiNengFamilyManageDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ZhiNengFamilyMAnageDetailBean.DataBean.MemberBean memberBean = (ZhiNengFamilyMAnageDetailBean.DataBean.MemberBean) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("member_type", dataBean.getMember_type());
                bundle.putString("family_id", dataBean.getFamily_id());
                bundle.putString("ty_family_id", ty_family_id);
                bundle.putParcelable("member", memberBean);
                startActivity(new Intent(context, ZhiNengFamilyMemberDetailActivity.class).putExtras(bundle));
            }
        });
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_FAMILY_MANAGE_CHANGENAME) {
                    changeFamily(message.content.toString());
                }
            }
        }));
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("家庭管理");
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

    String memberType = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_family_name:
                ZhiNengFamilyAddDIalog zhiNengFamilyAddDIalog = new ZhiNengFamilyAddDIalog(context, ConstanceValue.MSG_FAMILY_MANAGE_CHANGENAME);
                zhiNengFamilyAddDIalog.show();
                break;
            case R.id.rl_family_address:
                showSelectCity();
                break;
            case R.id.rl_family_room_num:
                Bundle bundle1 = new Bundle();
                bundle1.putString("family_id", family_id);
                bundle1.putString("member_type", dataBean.getMember_type());
                startActivity(new Intent(context, ZhiNengRoomManageActivity.class).putExtras(bundle1));
                break;
            case R.id.ll_add_share:

                String[] items = {"选择您要添加的角色", "成员", "管理员"};
                final ActionSheetDialog dialog = new ActionSheetDialog(this, items, null);
                dialog.isTitleShow(false).show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        Bundle bundle = new Bundle();
                        Uri imageUri = Uri.fromFile(file);
                        switch (position) {
                            case 0:

                                break;
                            case 1:
                                memberType = "2";
                                bundle.putString("family_id", family_id);
                                bundle.putString("memberType", memberType);
                                startActivity(new Intent(context, ZhiNengFamilyAddShareActivity.class).putExtras(bundle));

                                break;
                            case 2:
                                memberType = "3";
                                bundle.putString("family_id", family_id);
                                bundle.putString("memberType", memberType);
                                startActivity(new Intent(context, ZhiNengFamilyAddShareActivity.class).putExtras(bundle));
                                break;
                        }
                        dialog.dismiss();

                    }
                });

                break;
            case R.id.tv_family_delete:
                delete();
                break;
        }
    }

    private void delete() {
        String tishi;
        if (member_type.equals("1")) {
            tishi = "确定要删除该家庭吗？";
        } else {
            tishi = "确定要退出该家庭吗？";
        }

        MyCarCaoZuoDialog_CaoZuoTIshi myCarCaoZuoDialog_caoZuoTIshi = new MyCarCaoZuoDialog_CaoZuoTIshi(ZhiNengFamilyManageDetailActivity.this,
                "提示", tishi, "取消", "确定", new MyCarCaoZuoDialog_CaoZuoTIshi.OnDialogItemClickListener() {
            @Override
            public void clickLeft() {

            }

            @Override
            public void clickRight() {
                deleteFamily();
            }
        });
        myCarCaoZuoDialog_caoZuoTIshi.show();
    }

    private void getnet() {
        //访问网络获取数据 下面的列表数据
        Map<String, String> map = new HashMap<>();
        map.put("code", "16015");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(context).getAppToken());
        map.put("family_id", family_id);
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<ZhiNengFamilyMAnageDetailBean.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZhiNengFamilyMAnageDetailBean.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ZhiNengFamilyMAnageDetailBean.DataBean>> response) {
                        dataBean = response.body().data.get(0);
                        member_type = dataBean.getMember_type();
                        if (member_type.equals("1")) {
                            tv_family_delete.setText("删除家庭");
                            if (zhiNengFamilyManageDetailAdapter.getFooterViewsCount() == 0) {
                                zhiNengFamilyManageDetailAdapter.addFooterView(footerView);
                            }
                        } else {
                            tv_family_delete.setText("退出家庭");
                        }

                        province_name_pro = dataBean.getProvince_name();
                        city_name_pro = dataBean.getCity_name();
                        code_name = dataBean.getArea_name();

                        tv_family_name.setText(dataBean.getFamily_name());
                        tv_family_device_num.setText(dataBean.getDevice_num() + "个设备");
                        tv_family_room_num.setText(dataBean.getRoom_num() + "个房间");

                        memberBeans.clear();
                        memberBeans.addAll(dataBean.getMember());
                        zhiNengFamilyManageDetailAdapter.notifyDataSetChanged();

                        String province_name = dataBean.getProvince_name();
                        if (!TextUtils.isEmpty(province_name)) {
                            tv_family_address.setText(province_name + "-" + dataBean.getCity_name() + "-" + dataBean.getArea_name());
                        }

                        initMember();
                    }
                });
    }


    private void initMember() {
        TuyaHomeSdk.getMemberInstance().queryMemberList(Y.getLong(ty_family_id), new ITuyaGetMemberListCallback() {
            @Override
            public void onSuccess(List<MemberBean> tuyaMembers) {
                for (int i = 0; i < tuyaMembers.size(); i++) {
                    MemberBean tuyaMeberBean = tuyaMembers.get(i);
                    String account = tuyaMeberBean.getAccount();
                    long tuyaMemberId = tuyaMeberBean.getMemberId();
                    boolean isDelete = true;
                    for (int j = 0; j < memberBeans.size(); j++) {
                        ZhiNengFamilyMAnageDetailBean.DataBean.MemberBean memberBean = memberBeans.get(j);
                        String ty_member_id = memberBean.getTy_member_id();
                        if (ty_member_id.equals(String.valueOf(tuyaMemberId))) {
                            isDelete = false;
                        }

                        if (memberBean.getMember_type().equals("1")) {
                            if (account.equals(memberBean.getMember_phone())) {
                                isDelete = false;
                            }
                        }
                    }

                    if (isDelete) {
                        Y.e("账号是多少啊啊啊 " + account + "   " + tuyaMemberId + "   应该被删除");
                        removeMember(tuyaMemberId);
                    } else {
                        Y.e("账号是多少啊啊啊 " + account + "   " + tuyaMemberId + "   不删除");
                    }
                }
            }

            @Override
            public void onError(String errorCode, String error) {
                Y.e("我失败了么啊啊啊" + error);
            }
        });
    }

    private void removeMember(long memberId) {
        TuyaHomeSdk.getMemberInstance().removeMember(memberId, new IResultCallback() {
            @Override
            public void onSuccess() {
                Y.e("删除成功");
            }

            @Override
            public void onError(String code, String error) {
                Y.e("删除失败" + code + "   " + error);
            }
        });
    }

    /**
     * 修改家庭名字
     */
    private void changeFamily(String family_name) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "16012");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(context).getAppToken());
        map.put("family_id", family_id);
        map.put("family_name", family_name);
        map.put("old_name", dataBean.getFamily_name());
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<ZhiNengFamilyEditBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZhiNengFamilyEditBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ZhiNengFamilyEditBean>> response) {
                        if (response.body().msg.equals("ok")) {
                            tv_family_name.setText(family_name);
                            dataBean.setFamily_name(family_name);

                            if (TextUtils.isEmpty(province_name_pro)) {
                                TuyaHomeSdk.newHomeInstance(Y.getLong(ty_family_id)).updateHome(family_name, 0, 0, "", new IResultCallback() {
                                    @Override
                                    public void onSuccess() {
                                        Y.e("涂鸦家庭设置成功");
                                    }

                                    @Override
                                    public void onError(String code, String error) {
                                        Y.e("涂鸦家庭设置失败:" + error);
                                    }
                                });
                            } else {
                                changeTuyaCity();
                            }
                            MyCarCaoZuoDialog_Success dialog_success = new MyCarCaoZuoDialog_Success(ZhiNengFamilyManageDetailActivity.this,
                                    "成功", "修改成功", new MyCarCaoZuoDialog_Success.OnDialogItemClickListener() {
                                @Override
                                public void clickLeft() {

                                }

                                @Override
                                public void clickRight() {

                                }
                            });
                            dialog_success.show();
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<ZhiNengFamilyEditBean>> response) {
                        MyCarCaoZuoDialog_CaoZuoTIshi myCarCaoZuoDialog_caoZuoTIshi = new MyCarCaoZuoDialog_CaoZuoTIshi(context,
                                "提示", response.getException().getMessage(), "知道了", new MyCarCaoZuoDialog_CaoZuoTIshi.OnDialogItemClickListener() {
                            @Override
                            public void clickLeft() {

                            }

                            @Override
                            public void clickRight() {

                            }
                        });
                        myCarCaoZuoDialog_caoZuoTIshi.show();
                    }
                });
    }

    /**
     * 退出/删除家庭
     */
    private void deleteFamily() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "16019");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(context).getAppToken());
        map.put("family_id", family_id);
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<ZhiNengFamilyEditBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZhiNengFamilyEditBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ZhiNengFamilyEditBean>> response) {
                        if (response.body().msg.equals("ok")) {
                            Notice notice = new Notice();
                            notice.type = ConstanceValue.MSG_DELETE_FAMILY;
                            RxBus.getDefault().sendRx(notice);

                            String title = "";
                            if (member_type.equals("1")) {
                                title = "删除家庭";
                                deleteTuyaJiating();
                            } else {
                                title = "退出家庭";
                                memberTuichu();
                            }
                            MyCarCaoZuoDialog_Success dialog_success = new MyCarCaoZuoDialog_Success(ZhiNengFamilyManageDetailActivity.this,
                                    "成功", title, new MyCarCaoZuoDialog_Success.OnDialogItemClickListener() {
                                @Override
                                public void clickLeft() {

                                }

                                @Override
                                public void clickRight() {
                                    finish();
                                }
                            });
                            dialog_success.show();
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<ZhiNengFamilyEditBean>> response) {
                        super.onError(response);
                        MyCarCaoZuoDialog_CaoZuoTIshi myCarCaoZuoDialog_caoZuoTIshi = new MyCarCaoZuoDialog_CaoZuoTIshi(context,
                                "提示", response.getException().getMessage(), "知道了", new MyCarCaoZuoDialog_CaoZuoTIshi.OnDialogItemClickListener() {
                            @Override
                            public void clickLeft() {

                            }

                            @Override
                            public void clickRight() {

                            }
                        });
                        myCarCaoZuoDialog_caoZuoTIshi.show();
                    }
                });
    }

    private void memberTuichu() {
        String phone = PreferenceHelper.getInstance(mContext).getString("user_phone", "");
        for (int i = 0; i < memberBeans.size(); i++) {
            ZhiNengFamilyMAnageDetailBean.DataBean.MemberBean memberBean = memberBeans.get(i);
            String member_phone = memberBean.getMember_phone();
            if (phone.equals(member_phone)) {
                TuyaHomeSdk.getMemberInstance().removeMember(Y.getLong(memberBean.getTy_member_id()), new IResultCallback() {
                    @Override
                    public void onSuccess() {
                        // do something
                        Y.e("离开家庭成功");
                    }

                    @Override
                    public void onError(String code, String error) {
                        // do something
                        Y.e("离开家庭失败  " + error);
                    }
                });
            }
        }
    }

    private void deleteTuyaJiating() {
        TuyaHomeSdk.newHomeInstance(Y.getLong(ty_family_id)).dismissHome(new IResultCallback() {
            @Override
            public void onSuccess() {
                Y.e("解散涂鸦家庭成功 ");
            }

            @Override
            public void onError(String code, String error) {
                Y.e("解散家庭失败:" + error);
            }
        });
    }

    private OptionsPickerView<Object> chandiPicker;
    private List<ChandiModel.DataBean> chandiModels;
    private String province_name_pro = "";
    private String province_id_pro = "";
    private String city_name_pro = "";
    private String city_id_pro = "";
    private String code_id = "";
    private String code_name = "";

    private void showSelectCity() {
        if (chandiPicker == null) {
            showProgressDialog();
            getCity();
        } else {
            chandiPicker.show();
        }
    }

    private void getCity() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "00005");
        map.put("key", Urls.key);
        map.put("type_id", "province_city_ch");
        Gson gson = new Gson();
        OkGo.<AppResponse<ChandiModel.DataBean>>post(Urls.MSG)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ChandiModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ChandiModel.DataBean>> response) {
                        chandiModels = response.body().data;
                        initChandi();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }

    private void initChandi() {
        List<Object> names1 = new ArrayList<>();
        List<List<Object>> names2 = new ArrayList<>();
        List<List<List<Object>>> names3 = new ArrayList<>();

        for (int i = 0; i < chandiModels.size(); i++) {
            ChandiModel.DataBean bean = chandiModels.get(i);
            names1.add(bean.getCode_name());
            List<ChandiModel.DataBean.ClBeanX> next2 = bean.getCl();
            List<Object> names2Beans = new ArrayList<>();
            List<List<Object>> names3Beans = new ArrayList<>();
            for (int j = 0; j < next2.size(); j++) {
                ChandiModel.DataBean.ClBeanX nextLevelBeanX = next2.get(j);
                names2Beans.add(nextLevelBeanX.getCode_name());
                List<ChandiModel.DataBean.ClBeanX.ClBean> next3 = nextLevelBeanX.getCl();

                List<Object> names3BeansZi = new ArrayList<>();
                for (int k = 0; k < next3.size(); k++) {
                    names3BeansZi.add(next3.get(k).getCode_name());
                }
                names3Beans.add(names3BeansZi);

            }
            names2.add(names2Beans);
            names3.add(names3Beans);
        }

        //条件选择器
        chandiPicker = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                ChandiModel.DataBean dataBean = chandiModels.get(options1);
                ChandiModel.DataBean.ClBeanX cityBean = dataBean.getCl().get(option2);
                ChandiModel.DataBean.ClBeanX.ClBean clBean = cityBean.getCl().get(options3);

                province_name_pro = dataBean.getCode_name();
                province_id_pro = dataBean.getCode_id();
                city_name_pro = cityBean.getCode_name();
                city_id_pro = cityBean.getCode_id();
                code_id = clBean.getCode_id();
                code_name = clBean.getCode_name();

                tv_family_address.setText(province_name_pro + "-" + city_name_pro + "-" + code_name);
                changeCity();
            }
        }).build();
        chandiPicker.setPicker(names1, names2, names3);
        chandiPicker.show();
    }

    /**
     * 修改城市
     */
    private void changeCity() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "16012");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(context).getAppToken());
        map.put("family_id", family_id);
        map.put("family_name", dataBean.getFamily_name());
        map.put("old_name", dataBean.getFamily_name());
        map.put("province_id", province_id_pro);
        map.put("province_name", province_name_pro);
        map.put("city_id", city_id_pro);
        map.put("city_name", city_name_pro);
        map.put("area_id", code_id);
        map.put("area_name", code_name);
        Gson gson = new Gson();
        OkGo.<AppResponse<ZhiNengFamilyEditBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZhiNengFamilyEditBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ZhiNengFamilyEditBean>> response) {
                        MyCarCaoZuoDialog_Success dialog_success = new MyCarCaoZuoDialog_Success(ZhiNengFamilyManageDetailActivity.this,
                                "成功", "修改成功", new MyCarCaoZuoDialog_Success.OnDialogItemClickListener() {
                            @Override
                            public void clickLeft() {

                            }

                            @Override
                            public void clickRight() {

                            }
                        });
                        dialog_success.show();
                        changeTuyaCity();
                    }

                    @Override
                    public void onError(Response<AppResponse<ZhiNengFamilyEditBean>> response) {
                        MyCarCaoZuoDialog_CaoZuoTIshi myCarCaoZuoDialog_caoZuoTIshi = new MyCarCaoZuoDialog_CaoZuoTIshi(context,
                                "提示", response.getException().getMessage(), "知道了", new MyCarCaoZuoDialog_CaoZuoTIshi.OnDialogItemClickListener() {
                            @Override
                            public void clickLeft() {

                            }

                            @Override
                            public void clickRight() {

                            }
                        });
                        myCarCaoZuoDialog_caoZuoTIshi.show();
                    }
                });
    }

    private void changeTuyaCity() {
        try {
            GeocodeSearch geocodeSearch = new GeocodeSearch(mContext);
            geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                    if (i == 1000) {
                        if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null && geocodeResult.getGeocodeAddressList().size() > 0) {
                            GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
                            double latitude = geocodeAddress.getLatLonPoint().getLatitude();//纬度
                            double longititude = geocodeAddress.getLatLonPoint().getLongitude();//经度
                            Y.e("经纬度获取成功了 " + latitude + "  " + longititude);
                            setTuyaCity(latitude, longititude);
                        } else {
                            Y.e("编码转换失败");
                        }
                    }
                }
            });
            GeocodeQuery geocodeQuery = new GeocodeQuery(province_name_pro + city_name_pro + code_name, "29");
            geocodeSearch.getFromLocationNameAsyn(geocodeQuery);
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    private void setTuyaCity(double latitude, double longititude) {
        TuyaHomeSdk.newHomeInstance(Y.getLong(ty_family_id)).updateHome(dataBean.getFamily_name(), longititude, latitude, province_name_pro + city_name_pro + code_name, new IResultCallback() {
            @Override
            public void onSuccess() {
                Y.e("涂鸦家庭设置成功");
            }

            @Override
            public void onError(String code, String error) {
                Y.e("涂鸦家庭设置失败:" + error);
            }
        });
    }
}
