package com.smarthome.magic.activity.tongcheng58;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smarthome.magic.R;
import com.smarthome.magic.basicmvp.BaseFragment;

public class TongchengHomeItemFragment extends BaseFragment {
    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.tongcheng_frag_home_item;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }
}
