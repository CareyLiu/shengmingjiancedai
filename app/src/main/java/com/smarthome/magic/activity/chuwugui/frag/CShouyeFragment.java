package com.smarthome.magic.activity.chuwugui.frag;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.smarthome.magic.R;
import com.smarthome.magic.basicmvp.BaseFragment;
import com.youth.banner.Banner;

import butterknife.BindView;
import butterknife.OnClick;

public class CShouyeFragment extends BaseFragment {
    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.ll_woyaocunbao)
    LinearLayout ll_woyaocunbao;
    @BindView(R.id.ll_woyaoqubao)
    LinearLayout ll_woyaoqubao;
    @BindView(R.id.ll_cunqujilu)
    LinearLayout ll_cunqujilu;
    @BindView(R.id.ll_guanlizhongxin)
    LinearLayout ll_guanlizhongxin;

    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.chuwugui_frag_shouye;
    }

    @Override
    protected void initView(View rootView) {

    }

    @OnClick({R.id.rl_back, R.id.ll_woyaocunbao, R.id.ll_woyaoqubao, R.id.ll_cunqujilu, R.id.ll_guanlizhongxin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                break;
            case R.id.ll_woyaocunbao:
                break;
            case R.id.ll_woyaoqubao:
                break;
            case R.id.ll_cunqujilu:
                break;
            case R.id.ll_guanlizhongxin:
                break;
        }
    }
}
