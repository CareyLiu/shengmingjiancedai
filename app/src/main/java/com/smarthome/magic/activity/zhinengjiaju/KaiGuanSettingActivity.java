package com.smarthome.magic.activity.zhinengjiaju;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.ZhiNengRoomManageActivity;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.MyCarCaoZuoDialog_CaoZuoTIshi;
import com.smarthome.magic.dialog.MyCarCaoZuoDialog_Success;
import com.smarthome.magic.dialog.ZhiNengFamilyAddDIalog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.SuiYiTieModel;
import com.smarthome.magic.model.ZhiNengJiaJuKaiGuanModel;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;

public class KaiGuanSettingActivity extends BaseActivity {
    @BindView(R.id.tv_family_name)
    TextView tvFamilyName;
    @BindView(R.id.tv_shebeiming)
    TextView tvShebeiming;
    @BindView(R.id.iv_shebeimingcheng)
    ImageView ivShebeimingcheng;
    @BindView(R.id.tv_shebeileixing)
    TextView tvShebeileixing;
    @BindView(R.id.tv_guanlianshebei)
    TextView tvGuanlianshebei;
    @BindView(R.id.tv_room_delete)
    TextView tvRoomDelete;
    @BindView(R.id.rl_shebeimingcheng)
    RelativeLayout rlShebeimingcheng;
    @BindView(R.id.iv_guanlian_icon)
    ImageView ivGuanlianIcon;
    @BindView(R.id.rl_guanlian)
    RelativeLayout rlGuanlian;
    @BindView(R.id.tv_fangjian_ming)
    TextView tvFangjianMing;
    @BindView(R.id.rl_fangjian)
    RelativeLayout rlFangjian;
    private String device_ccid;
    private String device_ccidup;
    private String device_id;
    private String member_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device_ccid = getIntent().getStringExtra("device_ccid");
        device_ccidup = getIntent().getStringExtra("device_ccidup");
        member_type = getIntent().getStringExtra("member_type");
        tvRoomDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCarCaoZuoDialog_CaoZuoTIshi myCarCaoZuoDialog_caoZuoTIshi = new MyCarCaoZuoDialog_CaoZuoTIshi(mContext,
                        "??????", "???????????????????????????", "??????", "??????", new MyCarCaoZuoDialog_CaoZuoTIshi.OnDialogItemClickListener() {
                    @Override
                    public void clickLeft() {
                    }

                    @Override
                    public void clickRight() {
                        //????????????
                        shanChuSuiYiTie();
                    }

                });
                myCarCaoZuoDialog_caoZuoTIshi.show();
            }
        });
        rlShebeimingcheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhiNengFamilyAddDIalog zhiNengFamilyAddDIalog = new ZhiNengFamilyAddDIalog(mContext, ConstanceValue.MSG_ROOM_DEVICE_CHANGENAME);
                zhiNengFamilyAddDIalog.show();
            }
        });

        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_ROOM_DEVICE_CHANGENAME) {
                    xiuGaiMing(String.valueOf(message.content));
                }
            }
        }));

        rlFangjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("device_id", device_id);
                bundle.putString("family_id", family_id);
                bundle.putString("member_type", member_type);
                startActivity(new Intent(mContext, ZhiNengRoomManageActivity.class).putExtras(bundle));
            }
        });


    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_suiyitie_setting;
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
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imm.hideSoftInputFromWindow(findViewById(R.id.cl_layout).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });
    }


    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String device_ccid, String device_ccidup, String member_type) {
        Intent intent = new Intent(context, KaiGuanSettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("device_ccid", device_ccid);
        intent.putExtra("device_ccidup", device_ccidup);
        intent.putExtra("member_type", member_type);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getnet();
    }

    private String family_id = "";

    private void getnet() {
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "16068");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("device_ccid", device_ccid);
        map.put("device_ccid_up", device_ccidup);
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<ZhiNengJiaJuKaiGuanModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZhiNengJiaJuKaiGuanModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ZhiNengJiaJuKaiGuanModel.DataBean>> response) {
                        showLoadSuccess();
                        family_id = response.body().data.get(0).getFamily_id();
                        device_id = response.body().data.get(0).getDevice_id();
                        tvFamilyName.setText(response.body().data.get(0).getFamily_name());
                        tvShebeiming.setText(response.body().data.get(0).getDevice_name());
                        tvShebeileixing.setText(response.body().data.get(0).getDevice_type_name());
                        tvFangjianMing.setText(response.body().data.get(0).getRoom_name());
                    }

                    @Override
                    public void onError(Response<AppResponse<ZhiNengJiaJuKaiGuanModel.DataBean>> response) {
                        UIHelper.ToastMessage(mContext, response.getException().getMessage());
                    }

                    @Override
                    public void onStart(Request<AppResponse<ZhiNengJiaJuKaiGuanModel.DataBean>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void xiuGaiMing(String suiYiTieName) {
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "16054");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("device_ccid", device_ccid);
        map.put("device_ccid_up", device_ccidup);
        map.put("device_name", suiYiTieName);
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<SuiYiTieModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<SuiYiTieModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<SuiYiTieModel.DataBean>> response) {
                        UIHelper.ToastMessage(mContext, "??????????????????");
                        tvShebeiming.setText(suiYiTieName);
                    }

                    @Override
                    public void onError(Response<AppResponse<SuiYiTieModel.DataBean>> response) {
                        UIHelper.ToastMessage(mContext, response.getException().getMessage());
                    }
                });
    }

    private void shanChuSuiYiTie() {
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "16055");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("device_ccid", device_ccid);
        map.put("device_ccid_up", device_ccidup);
        map.put("family_id", family_id);
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<SuiYiTieModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<SuiYiTieModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<SuiYiTieModel.DataBean>> response) {
                        if (response.body().msg_code.equals("0000")) {
                            Notice notice = new Notice();
                            notice.type = ConstanceValue.MSG_ZHINENGJIAJU_SHOUYE_SHUAXIN;
                            sendRx(notice);

                            MyCarCaoZuoDialog_Success myCarCaoZuoDialog_success = new MyCarCaoZuoDialog_Success(KaiGuanSettingActivity.this,
                                    "??????", "??????????????????", "??????", new MyCarCaoZuoDialog_Success.OnDialogItemClickListener() {
                                @Override
                                public void clickLeft() {

                                }

                                @Override
                                public void clickRight() {
                                    Notice notice = new Notice();
                                    notice.type = ConstanceValue.MSG_KAIGUAN_DELETE;
                                    sendRx(notice);
                                    finish();
                                }
                            });
                            myCarCaoZuoDialog_success.show();
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<SuiYiTieModel.DataBean>> response) {
                        UIHelper.ToastMessage(mContext, response.getException().getMessage());
                    }
                });
    }
}
