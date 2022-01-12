package com.smarthome.magic.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;

import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.OwnerInfo;
import com.smarthome.magic.util.AlertUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivity {


    @BindView(R.id.back)
    LinearLayout mBack;
    @BindView(R.id.item_pic)
    LinearLayout mItemPic;
    @BindView(R.id.item_brand)
    LinearLayout mItemBrand;
    @BindView(R.id.item_region)
    LinearLayout mItemRegion;
    @BindView(R.id.item_fuel)
    LinearLayout mItemFuel;
    @BindView(R.id.item_number)
    LinearLayout mItemNumber;
    @BindView(R.id.item_type)
    LinearLayout mItemType;
    @BindView(R.id.item_name)
    LinearLayout mItemName;
    @BindView(R.id.item_contact)
    LinearLayout mItemContact;
    @BindView(R.id.item_insurance)
    LinearLayout mItemInsurance;
    @BindView(R.id.item_date)
    LinearLayout mItemDate;
    @BindView(R.id.item_service)
    LinearLayout mItemService;
    @BindView(R.id.iv_pic)
    ImageView mIvPic;
    @BindView(R.id.tv_brand)
    TextView mTvBrand;
    @BindView(R.id.tv_region)
    TextView mTvRegion;
    @BindView(R.id.tv_fule_type)
    TextView mTvFuleType;
    @BindView(R.id.tv_model)
    TextView mTvNumber;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_safe)
    TextView mTvSafe;
    @BindView(R.id.tv_date)
    TextView mTvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        requestData();
    }

    @OnClick({R.id.back, R.id.item_pic, R.id.item_brand, R.id.item_region, R.id.item_fuel, R.id.item_number, R.id.item_type, R.id.item_name, R.id.item_contact, R.id.item_insurance, R.id.item_date, R.id.item_service})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.back:
                finish();
                break;
            case R.id.item_pic:
                break;
            case R.id.item_brand:
                break;
            case R.id.item_region:
                break;
            case R.id.item_fuel:
                break;
            case R.id.item_number:
                break;
            case R.id.item_type:
                break;
            case R.id.item_name:
                break;
            case R.id.item_contact:
                break;
            case R.id.item_insurance:
                break;
            case R.id.item_date:
                break;
            case R.id.item_service:
                break;
        }
    }

    public void requestData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03205");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("user_car_id", PreferenceHelper.getInstance(this).getString("car_id", ""));
        Gson gson = new Gson();
        OkGo.<AppResponse<OwnerInfo.DataBean>>post(Urls.SERVER_URL + "wit/app/zhu")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<OwnerInfo.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<OwnerInfo.DataBean>> response) {
                            Glide.with(UserInfoActivity.this).load(response.body().data.get(0).getCar_brand_url_one()).into(mIvPic);
                            mTvBrand.setText(response.body().data.get(0).getCar_brand_name_one());
                            mTvDate.setText(response.body().data.get(0).getInstall_time());
                            mTvFuleType.setText(response.body().data.get(0).getFuel_type_name());
                            mTvName.setText(response.body().data.get(0).getUser_name());
                            mTvNumber.setText(response.body().data.get(0).getUser_phone());
                            mTvPhone.setText(response.body().data.get(0).getSell_phone());
                            mTvRegion.setText(response.body().data.get(0).getArea_name());
                            mTvSafe.setText(response.body().data.get(0).getSell_phone());
                            mTvType.setText(response.body().data.get(0).getCar_brand_name_two());
                    }

                    @Override
                    public void onError(Response<AppResponse<OwnerInfo.DataBean>> response) {
                        AlertUtil.t(UserInfoActivity.this,response.getException().getMessage());                    }
                });
    }

}
