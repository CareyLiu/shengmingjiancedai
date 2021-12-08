package com.smarthome.magic.activity.chuwugui.frag;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui.BaoyueDingdanActivty;
import com.smarthome.magic.activity.chuwugui.LianxikefuActivity;
import com.smarthome.magic.activity.chuwugui.config.ChuwuguiValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.basicmvp.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.ll_baoyuedingdan)
    LinearLayout ll_baoyuedingdan;

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

    @OnClick({R.id.ll_baoyuedingdan, R.id.rl_back, R.id.ll_xiugaimima, R.id.ll_lianxikefu, R.id.ll_daoqixufu, R.id.ll_tuichudenglu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                clickBack();
                break;
            case R.id.ll_xiugaimima:
                break;
            case R.id.ll_lianxikefu:
                LianxikefuActivity.actionStart(getContext());
                break;
            case R.id.ll_daoqixufu:
                break;
            case R.id.ll_baoyuedingdan:
                BaoyueDingdanActivty.actionStart(getContext());
                break;
            case R.id.ll_tuichudenglu:
                break;
        }
    }

    private void clickBack() {
        Notice notice = new Notice();
        notice.type = ChuwuguiValue.MSG_CWG_BACK_WODE;
        sendRx(notice);
    }
}
