package com.smarthome.magic.activity.chuwugui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.smarthome.magic.R;
import com.smarthome.magic.app.BaseActivity;

import butterknife.BindView;

public class ChuWuGuiSheZhiMiMaActivity extends BaseActivity {
    @BindView(R.id.tv_zaicigoumaifuwu)
    TextView tvZaicigoumaifuwu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvZaicigoumaifuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getContentViewResId() {
        return R.layout.chuwugui_shehzikaibao_mima;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();

        tv_title.setText("设置此箱子密码");
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
