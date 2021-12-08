package com.smarthome.magic.activity.chuwugui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui.adapter.BaoyueDingdanAdapter;
import com.smarthome.magic.app.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaoyueDingdanActivty extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.rv_content)
    RecyclerView rv_content;

    @Override
    public int getContentViewResId() {
        return R.layout.chuwugui_act_baoyue_dingdan;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, BaoyueDingdanActivty.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initAdapter();
    }

    private void initAdapter() {
        List<String>list=new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");

        BaoyueDingdanAdapter adapter=new BaoyueDingdanAdapter(R.layout.chuwugui_item_baoyue_dingdan,list);
        rv_content.setLayoutManager(new LinearLayoutManager(mContext));
        rv_content.setAdapter(adapter);
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }
}
