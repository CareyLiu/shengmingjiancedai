package com.smarthome.magic.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.makeramen.roundedimageview.RoundedImageView;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.ConsultActiviy;
import com.smarthome.magic.activity.LoginActivity;
import com.smarthome.magic.activity.PersonInfoAcctivity;
import com.smarthome.magic.activity.ServiceAboutActivity;
import com.smarthome.magic.basicmvp.BaseFragment;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppEvent;
import com.smarthome.magic.config.AppResponse;

import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.ServiceInfo;
import com.smarthome.magic.util.AlertUtil;
import com.smarthome.magic.util.CleanMessageUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;

public class ServiceMineFragment extends BaseFragment implements Observer {

    @BindView(R.id.iv_header)
    RoundedImageView ivHeader;
    @BindView(R.id.service_name)
    TextView serviceName;
    @BindView(R.id.service_age)
    TextView serviceAge;
    @BindView(R.id.service_phone)
    TextView servicePhone;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.layout_consult)
    LinearLayout layoutConsult;
    @BindView(R.id.layout_pending)
    LinearLayout layoutPending;
    @BindView(R.id.layout_processed)
    LinearLayout layoutProcessed;
    @BindView(R.id.layout_tobe_evaluated)
    LinearLayout layoutTobeEvaluated;
    @BindView(R.id.layout_closed)
    LinearLayout layoutClosed;
    @BindView(R.id.layout_about_us)
    LinearLayout layoutAboutUs;
    @BindView(R.id.layout_clear_cache)
    LinearLayout layoutClearCache;
    @BindView(R.id.layout_out)
    LinearLayout layoutOut;
    Unbinder unbinder;
    @BindView(R.id.tv_cache)
    TextView tvCache;

    private NormalDialog normalDialog;
    private BaseAnimatorSet mBasIn = new BounceBottomEnter();
    private BaseAnimatorSet mBasOut = new SlideBottomExit();

    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_service_mine;
    }

    @Override
    protected void initView(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
        AppEvent.getClassEvent().addObserver(this);
        requestData("03314");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_header, R.id.layout_consult, R.id.tv_setting, R.id.layout_pending, R.id.layout_processed, R.id.layout_tobe_evaluated, R.id.layout_closed, R.id.layout_about_us, R.id.layout_clear_cache, R.id.layout_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_header:
                startActivity(new Intent(getActivity(), PersonInfoAcctivity.class)
                        .putExtra("nick_name", serviceName.getText())
                        .putExtra("phone", servicePhone.getText()));
                break;
            case R.id.tv_setting:
                startActivity(new Intent(getActivity(), PersonInfoAcctivity.class)
                        .putExtra("nick_name", serviceName.getText())
                        .putExtra("phone", servicePhone.getText()));
                break;
            case R.id.layout_consult://????????????
                startActivity(new Intent(getActivity(), ConsultActiviy.class).putExtra("title", "????????????").putExtra("state", "5"));
                break;
            case R.id.layout_pending://?????????
                startActivity(new Intent(getActivity(), ConsultActiviy.class).putExtra("title", "???????????????").putExtra("state", "1"));
                break;
            case R.id.layout_processed://?????????
                startActivity(new Intent(getActivity(), ConsultActiviy.class).putExtra("title", "???????????????").putExtra("state", "2"));
                break;
            case R.id.layout_tobe_evaluated://?????????
                startActivity(new Intent(getActivity(), ConsultActiviy.class).putExtra("title", "???????????????").putExtra("state", "3"));
                break;
            case R.id.layout_closed://?????????
                startActivity(new Intent(getActivity(), ConsultActiviy.class).putExtra("title", "???????????????").putExtra("state", "4"));
                break;
            case R.id.layout_about_us:
                startActivity(new Intent(getActivity(), ServiceAboutActivity.class));
                break;
            case R.id.layout_clear_cache:
                normalDialog = new NormalDialog(getActivity());
                normalDialog.content("???????????????????????????????????????????").showAnim(mBasIn).dismissAnim(mBasOut).show();
                normalDialog.setOnBtnClickL(
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                normalDialog.dismiss();
                            }
                        },
                        new OnBtnClickL() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onBtnClick() {
                                CleanMessageUtil.clearAllCache(Objects.requireNonNull(getActivity()));
                                normalDialog.dismiss();
                                AlertUtil.t(getActivity(), "???????????????");
                                try {
                                    tvCache.setText(CleanMessageUtil.getTotalCacheSize(getActivity()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                break;
            case R.id.layout_out:
                String[] items = {"??????"};
                final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(), items, null);
                dialog.title("???????????????????????????");
                dialog.itemTextColor(getResources().getColor(R.color.red));
                dialog.titleTextSize_SP(12);
                dialog.isTitleShow(true).show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                requestData("03321");
                                break;
                            case 1:
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    public void requestData(final String code) {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(getActivity()).getAppToken());
        Gson gson = new Gson();
        OkGo.<AppResponse<ServiceInfo.DataBean>>post(Urls.SERVER_URL + "wit/app/car/witAgent")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ServiceInfo.DataBean>>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onSuccess(final Response<AppResponse<ServiceInfo.DataBean>> response) {
                        if (code.equals("03314")) {
                            //??????????????????
                            Glide.with(Objects.requireNonNull(getActivity())).load(response.body().data.get(0).getUser_img_url()).into(ivHeader);
                            serviceName.setText(response.body().data.get(0).getUser_name());
                            serviceAge.setText(response.body().data.get(0).getUser_age() + "???");
                            servicePhone.setText(response.body().data.get(0).getUser_phone());
                        } else {
                            //????????????
                            UserManager.getManager(getActivity()).removeUser();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            RongIM.getInstance().logout();
                        }


                    }

                    @Override
                    public void onError(Response<AppResponse<ServiceInfo.DataBean>> response) {
                        AlertUtil.t(getActivity(), response.getException().getMessage());
                    }
                });
    }


    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals("update_nick"))
            requestData("03314");
    }
}
