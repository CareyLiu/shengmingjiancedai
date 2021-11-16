package com.smarthome.magic.activity.chuwugui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smarthome.magic.R;
import com.smarthome.magic.adapter.chuwugui.GuiZiLieBiaoAdapter;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GuiZiLieBiaoActivity extends BaseActivity {
    @BindView(R.id.rlv_list)
    RecyclerView rlvList;
    @BindView(R.id.tv_zaicigoumaifuwu)
    TextView tvZaicigoumaifuwu;

    private GuiZiLieBiaoAdapter guiZiLieBiaoAdapter;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        guiZiLieBiaoAdapter = new GuiZiLieBiaoAdapter(R.layout.chuwugui_layout_guiziliebiao, list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rlvList.setLayoutManager(linearLayoutManager);
        rlvList.setAdapter(guiZiLieBiaoAdapter);


        if (list.size() == 0) {
            guiZiLieBiaoAdapter.setEmptyView(R.mipmap.chuwugui_empty);
            guiZiLieBiaoAdapter.notifyDataSetChanged();
        }

        tvZaicigoumaifuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.ToastMessage(mContext, "再此购买服务");


            }
        });

    }

    @Override
    public int getContentViewResId() {
        return R.layout.chuwugui_layout_guiziliebiao;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("柜子列表");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imm.hideSoftInputFromWindow(findViewById(R.id.cl_layout).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });
    }
}
