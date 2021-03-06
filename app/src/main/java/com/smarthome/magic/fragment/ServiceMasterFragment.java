package com.smarthome.magic.fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.adapter.MasterListAdapter;
import com.smarthome.magic.basicmvp.BaseFragment;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.MasterModel;
import com.smarthome.magic.util.AlertUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.smarthome.magic.activity.taokeshagncheng.Custom5SearchThingActivity.hideKeyboard;

public class ServiceMasterFragment extends BaseFragment {

    @BindView(R.id.list)
    LRecyclerView list;
    Unbinder unbinder;
    List<MasterModel.DataBean> modelList = new ArrayList<>();
    MasterListAdapter masterListAdapter;
    LRecyclerViewAdapter lRecyclerViewAdapter;
    String servicefromId, userPhone, plateNumber = "";

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.layout_query)
    LinearLayout layoutQuery;


    @Override
    protected void initLogic() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_service_master;
    }

    @Override
    protected void initView(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
        masterListAdapter = new MasterListAdapter(getActivity());
        masterListAdapter.setDataList(modelList);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(masterListAdapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(lRecyclerViewAdapter);
        plateNumber = "";
        etPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //????????????????????????????????????
                    hideKeyboard(etPhone);
                    // ???????????????????????????,??????????????????????????????
                    userPhone = etPhone.getText().toString();
                    modelList.clear();
                    etPhone.setText("");
                    requestData("", plateNumber, userPhone);
                    return true;
                }
                return false;
            }
        });

        layoutQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPhone = etPhone.getText().toString();
                modelList.clear();
                hideKeyboard(etPhone);
                etPhone.setText("");
                requestData("", plateNumber, userPhone);
            }
        });
        //????????????????????????
        list.setHeaderViewColor(R.color.black_666666, R.color.black_666666, R.color.transparent);
        //????????????????????????
        list.setFooterViewColor(R.color.black_666666, R.color.black_666666, R.color.transparent);
        //??????????????????????????????
        list.setFooterViewHint("????????????????????????", "??????????????????", "??????????????????????????????????????????");

        list.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                requestData(servicefromId, plateNumber, userPhone);
            }
        });
        list.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                modelList.clear();
                requestData("", plateNumber, userPhone);
            }
        });
        list.refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void requestData(final String fromId, String plate_number, String user_phone) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "03319");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(getActivity()).getAppToken());
        map.put("plate_number", plate_number);
        map.put("user_phone", user_phone);
        map.put("of_user_id", fromId);
        Gson gson = new Gson();
        OkGo.<AppResponse<MasterModel.DataBean>>post(Urls.SERVER_URL + "wit/app/car/witAgent")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<MasterModel.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<MasterModel.DataBean>> response) {
                        modelList.addAll(response.body().data);
                        masterListAdapter.setDataList(modelList);
                        list.refreshComplete(10);
                        lRecyclerViewAdapter.notifyDataSetChanged();
                        if (response.body().data.size() > 0) {
                            servicefromId = modelList.get(modelList.size() - 1).getOf_user_id();
                        } else {
                            if (!fromId.equals(""))
                                list.setNoMore(true);
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<MasterModel.DataBean>> response) {
                        AlertUtil.t(getActivity(), response.getException().getMessage());
                    }
                });
    }
}
