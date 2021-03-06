package com.smarthome.magic.activity.fenxiang_tuisong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.DefaultX5WebViewActivity;
import com.smarthome.magic.activity.DefaultX5WebView_HaveNameActivity;
import com.smarthome.magic.activity.tuangou.TuanGouShangJiaListActivity;
import com.smarthome.magic.activity.zijian_shangcheng.ZiJianShopMallDetailsActivity;
import com.smarthome.magic.app.AppManager;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.HuoDongImageLoader;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.Home;
import com.smarthome.magic.util.AlertUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

import static com.smarthome.magic.config.Wetch_S.APP_ID;


public class HuoDongTanCengActivity extends Activity {

    @BindView(R.id.tv_not_tixing)
    TextView tvNotTixing;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rl_huodong)
    RelativeLayout rlHuodong;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.rl_layout)
    ConstraintLayout rlLayout;
    @BindView(R.id.iv_lijichakan)
    ImageView ivLijichakan;
    @BindView(R.id.iv_fenxiangzhuan)
    ImageView ivFenxiangzhuan;
    @BindView(R.id.ll_chakan_fenxiang)
    LinearLayout llChakanFenxiang;
    private IWXAPI api;
    private int position = 0;

    private List<Home.DataBean.activity> activity;

    /**
     * ????????????activity????????????activity
     *
     * @param context
     */
    public static void actionStart(Context context, List<Home.DataBean.activity> activity) {
        Intent intent = new Intent(context, HuoDongTanCengActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("activity", (Serializable) activity);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);//??????????????????
        api.registerApp(APP_ID);//????????????
        // ???????????????
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shouye_huodong);
        overridePendingTransition(R.anim.anim_bottom_in, R.anim.anim_no);
        ButterKnife.bind(this);
        //       mImmersionBar = ImmersionBar.with(this);
        AppManager.getAppManager().addActivity(this);
        Intent i = getIntent();
        if (i == null) {
            return;
        }

        this.activity = (List<Home.DataBean.activity>) getIntent().getSerializableExtra("activity");
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvNotTixing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.ToastMessage(HuoDongTanCengActivity.this, "????????????");
                getNotTiXing();
            }
        });

        ivLijichakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.get(position).activity_type_id.equals("1")) {
                    ZiJianShopMallDetailsActivity.actionStart(HuoDongTanCengActivity.this, activity.get(position).shop_product_id, activity.get(position).wares_id);
                } else if (activity.get(position).activity_type_id.equals("4")) {
                    TuanGouShangJiaListActivity.actionStart(HuoDongTanCengActivity.this, activity.get(position).img_type);

                } else if (activity.get(position).activity_type_id.equals("2")) {
                    if (null == activity.get(position).html_url) {
                        return;
                    }
                    DefaultX5WebView_HaveNameActivity.actionStart(HuoDongTanCengActivity.this, activity.get(position).html_url, "????????????");
                }
            }

        });
        ivFenxiangzhuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UIHelper.ToastMessage(HuoDongTanCengActivity.this, "?????????");
                if (activity.get(position).is_share.equals("2")) {
                    return;
                }
                if (activity.get(position).activity_type_id.equals("5")) {
                    return;
                }
                ShouYeFenXiangActivity.actionStart(HuoDongTanCengActivity.this, activity.get(position));
                finish();

            }
        });


        //?????????????????????

        List<String> items = new ArrayList<>();
        for (int j = 0; j < activity.size(); j++) {
            items.add(activity.get(j).img_url);
        }

        banner.setImageLoader(new HuoDongImageLoader());

        if (banner != null) {
            //??????????????????
            banner.setDelayTime(3000);//??????????????????
            banner.setImages(items);
            //banner?????????????????????????????????????????????
            banner.start();
            banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    HuoDongTanCengActivity.this.position = position;
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                if (activity.get(position).activity_type_id.equals("1")) {
//                    ZiJianShopMallDetailsActivity.actionStart(HuoDongTanCengActivity.this, activity.get(position).shop_product_id, activity.get(position).wares_id);
//                } else if (activity.get(position).activity_type_id.equals("4")) {
//                    TuanGouShangJiaListActivity.actionStart(HuoDongTanCengActivity.this, activity.get(position).img_type);
//
//                } else if (activity.get(position).activity_type_id.equals("2")) {
//                    if (null == activity.get(position).html_url) {
//                        return;
//                    }
//                    DefaultX5WebViewActivity.actionStart(HuoDongTanCengActivity.this, activity.get(position).html_url);
//                }
            }
        });
    }


    private void getNotTiXing() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04260");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(HuoDongTanCengActivity.this).getAppToken());
        // map.put("shop_product_id", productId);
        //map.put("wares_id", warseId);
        map.put("activity_id", activity.get(position).activity_id);

        Gson gson = new Gson();
        OkGo.<AppResponse<Object>>post(Urls.SERVER_URL + "shop_new/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Object>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<Object>> response) {
                        finish();

                    }

                    @Override
                    public void onError(Response<AppResponse<Object>> response) {
                        AlertUtil.t(HuoDongTanCengActivity.this, response.getException().getMessage());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        Log.d("c", "onActivityResult");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_no, R.anim.anim_bottom_out);
    }

    /**
     * ??????????????????
     */
    public Observable<Notice> toObservable() {
        return RxBus.getDefault().toObservable(Notice.class);
    }

    /**
     * ????????????
     */
    public void sendRx(Notice msg) {
        RxBus.getDefault().sendRx(msg);
    }

}

