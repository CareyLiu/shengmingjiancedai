package com.smarthome.magic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import androidx.recyclerview.widget.RecyclerView;

import com.smarthome.magic.R;
import com.smarthome.magic.adapter.GouMaiXiangQingItemAdapter;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GouMaiXiangQingActivity extends BaseActivity {
    GouMaiXiangQingItemAdapter gouMaiXiangQingItemAdapter;

    List<String> mDatas = new ArrayList<>();
    @BindView(R.id.iv_left_line)
    ImageView ivLeftLine;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mDatas.add("0");
//        mDatas.add("1");
//
//        gouMaiXiangQingItemAdapter = new GouMaiXiangQingItemAdapter(R.layout.goumaixiangqing_item, mDatas);
//        rlvList.setAdapter(gouMaiXiangQingItemAdapter);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rlvList.setLayoutManager(linearLayoutManager);
//        gouMaiXiangQingItemAdapter.setNewData(mDatas);

        showProgressDialog("正在结算，请稍后......");

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(3000);
dismissProgressDialog();
                    UIHelper.ToastMessage(mContext, "扣款成功", Toast.LENGTH_LONG);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_goumaixiangqing;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }


    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("购买详情");
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

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GouMaiXiangQingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
