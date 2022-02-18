package com.smarthome.magic.activity.shengming.fragment;

import android.Manifest;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chelianwang.ScanAddCarActivity;
import com.smarthome.magic.activity.shengming.SmSetActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.basicmvp.BaseFragment;


import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class SmWodeFragment extends BaseFragment {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_set)
    ImageView iv_set;
    @BindView(R.id.iv_head)
    ImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.bt_saoma)
    TextView bt_saoma;

    @Override
    protected void immersionInit(ImmersionBar mImmersionBar) {
        mImmersionBar
                .init();
    }

    @Override
    protected boolean immersionEnabled() {
        return true;
    }


    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.shengming_frag_wode;
    }

    @Override
    protected void initView(View rootView) {

    }

    @OnClick({R.id.iv_back, R.id.bt_saoma, R.id.iv_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                clickBack();
                break;
            case R.id.bt_saoma:
                clickSaoma();
                break;
            case R.id.iv_set:
                clickSet();
                break;
        }
    }

    private void clickSaoma() {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.CAMERA)) {
            ScanAddCarActivity.actionStart(getContext());
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.xjqx), 0, Manifest.permission.CAMERA);
        }
    }

    private void clickBack() {
        Notice notice = new Notice();
        notice.type = ConstanceValue.MSG_SHENGMIN_HOME_BACK;
        sendRx(notice);
    }

    private void clickSet() {
        SmSetActivity.actionStart(getContext());
    }


}
