package com.smarthome.magic.activity.chuwugui.frag;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui_two.GouWuGuiScanActivity;
import com.smarthome.magic.activity.chuwugui.GuanliLiebiaoActivity;
import com.smarthome.magic.activity.chuwugui_two.ShiyongjiluActivity;
import com.smarthome.magic.activity.chuwugui.config.ChuwuguiValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.basicmvp.BaseFragment;
import com.smarthome.magic.config.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

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
        initBanner();
    }

    private void initBanner() {
        List<Integer> items = new ArrayList<>();
        items.add(R.mipmap.chuwugui_banner1);
        items.add(R.mipmap.chuwugui_banner2);
        items.add(R.mipmap.chuwugui_banner3);
        items.add(R.mipmap.chuwugui_banner4);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(items);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @OnClick({R.id.rl_back, R.id.ll_woyaocunbao, R.id.ll_woyaoqubao, R.id.ll_cunqujilu, R.id.ll_guanlizhongxin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                clickBack();
                break;
            case R.id.ll_woyaocunbao:
                GouWuGuiScanActivity.actionStart(getContext());
                break;
            case R.id.ll_woyaoqubao:
                break;
            case R.id.ll_cunqujilu:
                ShiyongjiluActivity.actionStart(getContext());
                break;
            case R.id.ll_guanlizhongxin:
                GuanliLiebiaoActivity.actionStart(getContext());
                break;
        }
    }

    private void clickBack() {
        Notice notice = new Notice();
        notice.type = ChuwuguiValue.MSG_CWG_BACK_SHOUYE;
        sendRx(notice);
    }
}
