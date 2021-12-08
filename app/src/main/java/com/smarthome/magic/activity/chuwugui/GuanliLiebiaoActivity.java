package com.smarthome.magic.activity.chuwugui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui.adapter.GuanliLiebiaoAdapter;
import com.smarthome.magic.activity.chuwugui.config.ChuwuguiValue;
import com.smarthome.magic.activity.chuwugui_two.model.GuanliyuanListModel;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class GuanliLiebiaoActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.ed_guizi)
    EditText ed_guizi;
    @BindView(R.id.bt_queding)
    TextView bt_queding;
    @BindView(R.id.rv_content)
    RecyclerView rv_content;
    private String text;
    private List<GuanliyuanListModel.DataBean> data = new ArrayList<>();
    private GuanliLiebiaoAdapter liebiaoAdapter;

    @Override
    public int getContentViewResId() {
        return R.layout.chuwugui_act_guanli_guiziliebiao;
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
        Intent intent = new Intent(context, GuanliLiebiaoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        text = "";
        showProgressDialog();
        initAdapter();
        initHuidiao();
        getData();
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ChuwuguiValue.MSG_CWG_GUANLI_UPDATA) {
                    getData();
                }
            }
        }));
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "120009");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("text", text);
        Gson gson = new Gson();
        OkGo.<AppResponse<GuanliyuanListModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<GuanliyuanListModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<GuanliyuanListModel.DataBean>> response) {
                        data = response.body().data;
                        liebiaoAdapter.setNewData(data);
                        liebiaoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        text="";
                        ed_guizi.setText("");
                        dismissProgressDialog();
                    }
                });
    }

    private void initAdapter() {
        liebiaoAdapter = new GuanliLiebiaoAdapter(R.layout.chuwugui_item_guanli_guiziliebiao, data);
        rv_content.setLayoutManager(new LinearLayoutManager(mContext));
        rv_content.setAdapter(liebiaoAdapter);
        liebiaoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GuanliyuanListModel.DataBean bean = data.get(position);
                String online_state = bean.getOnline_state();
                if (online_state.equals("1")) {
                    String device_ccid = bean.getDevice_ccid();
                    String device_addr = bean.getDevice_addr();
                    String device_name = bean.getDevice_name();
                    GuanliXiangqingActivity.actionStart(mContext, device_ccid, device_addr, device_name);
                } else {
                    Y.t("设备已离线");
                }
            }
        });

        View view = View.inflate(mContext, R.layout.chuwugui_empty_view, null);
        liebiaoAdapter.setEmptyView(view);
    }

    @OnClick({R.id.rl_back, R.id.bt_queding})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.bt_queding:
                search();
                break;
        }
    }

    private void search() {
        text = ed_guizi.getText().toString();
        showProgressDialog();
        getData();
    }
}
