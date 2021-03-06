package com.smarthome.magic.activity.dingdan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.flyco.roundview.RoundRelativeLayout;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.common.StringUtils;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.TaoKeDetailList;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.smarthome.magic.get_net.Urls.HOME_PICTURE_HOME;
import static com.smarthome.magic.get_net.Urls.TAOKELIST;

public class DingDanShenQingTuikuanActivity extends BaseActivity {


    @BindView(R.id.tv_shenqingtuikuan_huashu)
    TextView tvShenqingtuikuanHuashu;
    @BindView(R.id.tv_shouhuozhuagntai_huashu)
    TextView tvShouhuozhuagntaiHuashu;
    @BindView(R.id.tv_shenqingyuanyin_huashu)
    TextView tvShenqingyuanyinHuashu;
    @BindView(R.id.tv_tuikuanjine)
    TextView tvTuikuanjine;
    @BindView(R.id.tv_jine)
    TextView tvJine;
    @BindView(R.id.tv_shuoming)
    TextView tvShuoming;
    @BindView(R.id.tv_phone_huashu)
    TextView tvPhoneHuashu;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.rrl_tijiaoshenqing)
    RoundRelativeLayout rrlTijiaoshenqing;
    @BindView(R.id.tv_shenqingtuikuan)
    TextView tvShenqingtuikuan;
    @BindView(R.id.et_content)
    EditText etContent;
    private String str;

    private String shop_form_id, refund_type;
    private String dingDanId;
    private String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        str = getIntent().getStringExtra("str");
        shop_form_id = getIntent().getStringExtra(" ");
        money = getIntent().getStringExtra("money");
        if (!StringUtils.isEmpty(money)) {
            tvJine.setText("??" + money);
        } else {
            tvJine.setText("?? 0");
        }
        tvShenqingtuikuan.setText(str);
        dingDanId = getIntent().getStringExtra("dingDanId");
        if (StringUtils.isEmpty(dingDanId)) {
            UIHelper.ToastMessage(DingDanShenQingTuikuanActivity.this, "??????????????????,?????????app????????????");
            finish();
            return;
        }
        shop_form_id = dingDanId;
        if (str.equals("????????????(????????????)")) {
            refund_type = "1";
        } else if (str.equals("??????????????????")) {
            refund_type = "2";
        }

        rrlTijiaoshenqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StringUtils.isEmpty(etContent.getText().toString())) {
                    UIHelper.ToastMessage(DingDanShenQingTuikuanActivity.this, "?????????????????????");
                    return;
                }

                if (StringUtils.isEmpty(etPhone.getText().toString())) {
                    UIHelper.ToastMessage(DingDanShenQingTuikuanActivity.this, "??????????????????");
                    return;
                }

                Map<String, String> map = new HashMap<>();
                map.put("code", "04154");
                map.put("key", Urls.key);
                map.put("token", UserManager.getManager(DingDanShenQingTuikuanActivity.this).getAppToken());
                map.put("shop_form_id", shop_form_id);
                map.put("refund_type", refund_type);
                map.put("refund_cause", etContent.getText().toString());
                map.put("refund_phone", etPhone.getText().toString());

                Gson gson = new Gson();
                Log.e("map_data", gson.toJson(map));
                OkGo.<AppResponse<TaoKeDetailList.DataBean>>post(HOME_PICTURE_HOME)
                        .tag(this)//
                        .upJson(gson.toJson(map))
                        .execute(new JsonCallback<AppResponse<TaoKeDetailList.DataBean>>() {
                            @Override
                            public void onSuccess(Response<AppResponse<TaoKeDetailList.DataBean>> response) {
                                // Log.i("response_data", new Gson().toJson(response.body()));
                                finish();
                                UIHelper.ToastMessage(DingDanShenQingTuikuanActivity.this, "????????????");
                            }
                        });


            }
        });

    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_ding_dan_shen_qing_tuikuan;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("????????????");
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

    @Override
    public boolean showToolBar() {
        return true;
    }

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String str, String dingDanId, String money) {
        Intent intent = new Intent(context, DingDanShenQingTuikuanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("str", str);
        intent.putExtra("money", money);
        intent.putExtra("dingDanId", dingDanId);
        context.startActivity(intent);
    }


}
