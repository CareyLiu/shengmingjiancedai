package com.smarthome.magic.activity.chuwugui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui.adapter.GuanliXiangqingAdapter;
import com.smarthome.magic.activity.chuwugui.config.ChuwuguiValue;
import com.smarthome.magic.activity.chuwugui.dialog.InputDialog;
import com.smarthome.magic.activity.chuwugui_two.dialog.GuanliyuanDialog;
import com.smarthome.magic.activity.chuwugui_two.dialog.GuanliyuanXiangziDialog;
import com.smarthome.magic.activity.chuwugui_two.model.GuanliyuanGuiziModel;
import com.smarthome.magic.activity.chuwugui_two.model.CunbaoModel;
import com.smarthome.magic.activity.chuwugui_two.model.GuanliyuanXiangziModel;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuanliXiangqingActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.bt_quankai)
    TextView bt_quankai;
    @BindView(R.id.bt_xiugai_dizhi)
    TextView bt_xiugai_dizhi;
    @BindView(R.id.bt_xiugai_name)
    TextView bt_xiugai_name;
    @BindView(R.id.rv_content)
    RecyclerView rv_content;

    private String dizhi;
    private String name;
    private String device_ccid;
    private GuanliyuanGuiziModel.DataBean dataBean;
    private List<GuanliyuanGuiziModel.DataBean.BoxListBean> box_list = new ArrayList<>();
    private GuanliXiangqingAdapter xiangqingAdapter;
    private GuanliyuanGuiziModel.DataBean.BoxListBean selectBeen;
    private int selectPosition;

    @Override
    public int getContentViewResId() {
        return R.layout.chuwugui_act_guanli_guizi;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context, String device_ccid, String dizhi, String name) {
        Intent intent = new Intent(context, GuanliXiangqingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("device_ccid", device_ccid);
        intent.putExtra("dizhi", dizhi);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initData();
        initAdapter();
        showProgressDialog();
        getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "120010");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("device_ccid", device_ccid);
        Gson gson = new Gson();
        OkGo.<AppResponse<GuanliyuanGuiziModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<GuanliyuanGuiziModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<GuanliyuanGuiziModel.DataBean>> response) {
                        dataBean = response.body().data.get(0);
                        box_list = dataBean.getBox_list();
                        xiangqingAdapter.setNewData(box_list);
                        xiangqingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }

    private void initData() {
        device_ccid = getIntent().getStringExtra("device_ccid");
        dizhi = getIntent().getStringExtra("dizhi");
        name = getIntent().getStringExtra("name");
    }

    private void initAdapter() {
        xiangqingAdapter = new GuanliXiangqingAdapter(R.layout.chuwugui_item_guanli_guizi, box_list);
        rv_content.setLayoutManager(new GridLayoutManager(mContext, 3));
        rv_content.setAdapter(xiangqingAdapter);
        xiangqingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                selectPosition = position;
                selectBeen = box_list.get(position);
                String box_state = selectBeen.getBox_state();
                if (box_state.equals("1")) {//空闲中
                    showGuanliyuanDialog();
                } else if (box_state.equals("2")) {//使用中
                    getXiangzixiangqing();
                } else {//禁用
                    showGuanliyuanDialog();
                }
            }
        });

        View view = View.inflate(mContext, R.layout.chuwugui_empty_view, null);
        xiangqingAdapter.setEmptyView(view);
    }

    private void getXiangzixiangqing() {
        showProgressDialog();
        Map<String, String> map = new HashMap<>();
        map.put("code", "120012");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("device_ccid", device_ccid);
        map.put("device_box_id", selectBeen.getDevice_box_id());
        Gson gson = new Gson();
        OkGo.<AppResponse<GuanliyuanXiangziModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<GuanliyuanXiangziModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<GuanliyuanXiangziModel.DataBean>> response) {
                        List<GuanliyuanXiangziModel.DataBean> data = response.body().data;
                        if (data.size() > 0) {
                            GuanliyuanXiangziModel.DataBean dataBean = data.get(0);
                            showXiangziDialog(dataBean);
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<GuanliyuanXiangziModel.DataBean>> response) {
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

    private void showXiangziDialog(GuanliyuanXiangziModel.DataBean dataBean) {
        GuanliyuanXiangziDialog xiangziDialog = new GuanliyuanXiangziDialog(mContext, dataBean, new GuanliyuanXiangziDialog.GuanliyuanXiangziListener() {
            @Override
            public void onClickJieshu(GuanliyuanXiangziModel.DataBean bean) {
                clickKaixiang(bean.getLc_use_id());
            }

            @Override
            public void onClickDianhua(GuanliyuanXiangziModel.DataBean bean) {
                String user_phone = bean.getUser_phone();
                callPhone(user_phone);
            }

            @Override
            public void onDismiss(InputDialog dialog) {

            }
        });

        xiangziDialog.show();
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打） * * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }


    private void showGuanliyuanDialog() {
        GuanliyuanDialog dialog = new GuanliyuanDialog(mContext, selectBeen, new GuanliyuanDialog.GuanliyuanListener() {
            @Override
            public void onClickKaixiang(GuanliyuanGuiziModel.DataBean.BoxListBean bean) {
                clickKaixiangKong();
            }

            @Override
            public void onClickJinyong(GuanliyuanGuiziModel.DataBean.BoxListBean bean) {
                clickJinyong();
            }

            @Override
            public void onClickJiejin(GuanliyuanGuiziModel.DataBean.BoxListBean bean) {
                clickJiejin();
            }

            @Override
            public void onDismiss(InputDialog dialog) {

            }
        });
        dialog.show();
    }

    private void clickJiejin() {
        showProgressDialog();
        Map<String, String> map = new HashMap<>();
        map.put("code", "120014");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("device_box_id", selectBeen.getDevice_box_id());
        map.put("device_box_state", "1");
        Gson gson = new Gson();
        OkGo.<AppResponse<GuanliyuanGuiziModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<GuanliyuanGuiziModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<GuanliyuanGuiziModel.DataBean>> response) {
                        selectBeen.setBox_state("1");
                        box_list.set(selectPosition, selectBeen);
                        xiangqingAdapter.notifyDataSetChanged();
                        Y.t("解禁成功");
                    }

                    @Override
                    public void onError(Response<AppResponse<GuanliyuanGuiziModel.DataBean>> response) {
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

    private void clickJinyong() {
        showProgressDialog();
        Map<String, String> map = new HashMap<>();
        map.put("code", "120014");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("device_box_id", selectBeen.getDevice_box_id());
        map.put("device_box_state", "2");
        Gson gson = new Gson();
        OkGo.<AppResponse<GuanliyuanGuiziModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<GuanliyuanGuiziModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<GuanliyuanGuiziModel.DataBean>> response) {
                        selectBeen.setBox_state("3");
                        box_list.set(selectPosition, selectBeen);
                        xiangqingAdapter.notifyDataSetChanged();
                        Y.t("禁用成功");
                    }

                    @Override
                    public void onError(Response<AppResponse<GuanliyuanGuiziModel.DataBean>> response) {
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

    private void clickKaixiang(String lc_use_id) {
        showProgressDialog();
        Map<String, String> map = new HashMap<>();
        map.put("code", "120013");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("lc_use_id", lc_use_id);
        Gson gson = new Gson();
        OkGo.<AppResponse<GuanliyuanGuiziModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<GuanliyuanGuiziModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<GuanliyuanGuiziModel.DataBean>> response) {
                        Y.t("开箱成功");
                        getData();
                    }

                    @Override
                    public void onError(Response<AppResponse<GuanliyuanGuiziModel.DataBean>> response) {
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

    private void clickKaixiangKong() {
        showProgressDialog();
        Map<String, String> map = new HashMap<>();
        map.put("code", "120022");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("type", "3");
        map.put("ccid", device_ccid);
        map.put("device_box_id", selectBeen.getDevice_box_id());
        Gson gson = new Gson();
        OkGo.<AppResponse<CunbaoModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<CunbaoModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<CunbaoModel.DataBean>> response) {
                        Y.t("开箱成功");
                    }

                    @Override
                    public void onError(Response<AppResponse<CunbaoModel.DataBean>> response) {
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

    @OnClick({R.id.rl_back, R.id.bt_quankai, R.id.bt_xiugai_dizhi, R.id.bt_xiugai_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.bt_quankai:
                quankai();
                break;
            case R.id.bt_xiugai_dizhi:
                clickDizhi();
                break;
            case R.id.bt_xiugai_name:
                clickName();
                break;
        }
    }

    private void clickDizhi() {
        InputDialog dialog = new InputDialog(mContext, new InputDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, InputDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, InputDialog dialog) {
                dizhi = dialog.getTextContent();
                xiugaiDizhi(dizhi);
            }

            @Override
            public void onDismiss(InputDialog dialog) {
                Y.hideInputMethod(dialog.tv_content);
            }
        });

        dialog.setTextTitle("修改柜子地址");
        dialog.setTextContent(dizhi);
        dialog.show();
    }

    private void clickName() {
        InputDialog dialog = new InputDialog(mContext, new InputDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, InputDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, InputDialog dialog) {
                name = dialog.getTextContent();
                xiugaiName(name);
            }

            @Override
            public void onDismiss(InputDialog dialog) {
                Y.hideInputMethod(dialog.tv_content);
            }
        });

        dialog.setTextTitle("修改柜子名称");
        dialog.setTextContent(name);
        dialog.show();
    }


    private void quankai() {
        showProgressDialog();
        Map<String, String> map = new HashMap<>();
        map.put("code", "120022");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("type", "4");
        map.put("ccid", device_ccid);
        Gson gson = new Gson();
        OkGo.<AppResponse<CunbaoModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<CunbaoModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<CunbaoModel.DataBean>> response) {
                        Y.t("开箱成功");
                    }

                    @Override
                    public void onError(Response<AppResponse<CunbaoModel.DataBean>> response) {
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


    private void xiugaiDizhi(String device_addr) {
        showProgressDialog();
        Map<String, String> map = new HashMap<>();
        map.put("code", "120011");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("device_ccid", device_ccid);
        map.put("device_addr", device_addr);
        Gson gson = new Gson();
        OkGo.<AppResponse<CunbaoModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<CunbaoModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<CunbaoModel.DataBean>> response) {
                        Y.t("修改成功");
                        Notice n = new Notice();
                        n.type = ChuwuguiValue.MSG_CWG_GUANLI_UPDATA;
                        RxBus.getDefault().sendRx(n);
                    }

                    @Override
                    public void onError(Response<AppResponse<CunbaoModel.DataBean>> response) {
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


    private void xiugaiName(String device_name) {
        showProgressDialog();
        Map<String, String> map = new HashMap<>();
        map.put("code", "120011");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("device_ccid", device_ccid);
        map.put("device_name", device_name);
        Gson gson = new Gson();
        OkGo.<AppResponse<CunbaoModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<CunbaoModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<CunbaoModel.DataBean>> response) {
                        Y.t("修改成功");
                        Notice n = new Notice();
                        n.type = ChuwuguiValue.MSG_CWG_GUANLI_UPDATA;
                        RxBus.getDefault().sendRx(n);
                    }

                    @Override
                    public void onError(Response<AppResponse<CunbaoModel.DataBean>> response) {
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

}
