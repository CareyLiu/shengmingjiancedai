package com.smarthome.magic.activity.chuwugui.frag;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smarthome.magic.R;
import com.smarthome.magic.basicmvp.BaseFragment;

import butterknife.BindView;

public class CWodeFragment extends BaseFragment {
    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.iv_head)
    ImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.ll_xiugaimima)
    LinearLayout ll_xiugaimima;
    @BindView(R.id.ll_lianxikefu)
    LinearLayout ll_lianxikefu;
    @BindView(R.id.ll_daoqixufu)
    LinearLayout ll_daoqixufu;
    @BindView(R.id.ll_tuichudenglu)
    LinearLayout ll_tuichudenglu;

    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.chuwugui_frag_wode;
    }

    @Override
    protected void initView(View rootView) {

    }
}
