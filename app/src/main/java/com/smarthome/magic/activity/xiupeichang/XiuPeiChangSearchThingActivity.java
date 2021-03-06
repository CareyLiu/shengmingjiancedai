package com.smarthome.magic.activity.xiupeichang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.roundview.RoundLinearLayout;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.tuangou.TuanGouShangJiaDetailsActivity;
import com.smarthome.magic.adapter.Custom5HistoryAdapter;
import com.smarthome.magic.adapter.tuangou.TuanGouShangJiaListAdapterTwo;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.db.DBManager;
import com.smarthome.magic.db.HistoryRecordDao;
import com.smarthome.magic.db.table.HistoryRecord;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.TuanGouShangJiaListBeanNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.smarthome.magic.app.App.JINGDU;
import static com.smarthome.magic.app.App.WEIDU;
import static com.smarthome.magic.db.DbField.XIUPEICHANGSEARCH;
import static com.smarthome.magic.get_net.Urls.LIBAOLIST;


public class XiuPeiChangSearchThingActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_serarchKey)
    EditText etSerarchKey;
    @BindView(R.id.rl_search)
    RoundLinearLayout rlSearch;
    @BindView(R.id.iv_cancel)
    TextView ivCancel;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ll_tou)
    LinearLayout llTou;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.srL_smart)
    SmartRefreshLayout srLSmart;
    @BindView(R.id.ll_none)
    LinearLayout llNone;
    @BindView(R.id.activity_custom3_search)
    RelativeLayout activityCustom3Search;

    private List<HistoryRecord> searchKeywords1 = new ArrayList<>();
    private Custom5HistoryAdapter historyAdapter;
    public DBManager dbManager;

    private List<TuanGouShangJiaListBeanNew.DataBean> storeListBeans = new ArrayList<>();
    private TuanGouShangJiaListAdapterTwo tuanGouShangJiaListAdapter;

    private String strTitle;
    private String inst_id = "";
    private String type = "7";//?????????????????? 1.?????? 2.??????/?????? 3.???????????? 4.???????????? 5.??????

    /**
     * ????????????activity????????????activity
     */
    public static void actionStart(Context context, String string) {
        Intent intent = new Intent(context, XiuPeiChangSearchThingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("string", string);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strTitle = getIntent().getStringExtra("string");
        etSerarchKey.setText(strTitle);
        dbManager = DBManager.getInstance(this);
        dbManager.setDebug();

        getNet();
        initSM();
        historyAdapter();

        etSerarchKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                //????????????????????????????????????
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            insertKeyword(textView.getText().toString().trim(), 1);
                            strTitle = textView.getText().toString();
                            hideInput();
                            getNet();
                            return true;
                        default:
                            return true;
                    }
                }
                return false;
            }
        });

        searchKeywords1 = queryKeywordList(XIUPEICHANGSEARCH);
        tuanGouShangJiaListAdapter = new TuanGouShangJiaListAdapterTwo(R.layout.item_shangjia, storeListBeans);
        tuanGouShangJiaListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                type = storeListBeans.get(position).getValue_1();
                switch (view.getId()) {
                    case R.id.constrain:
                        if (type.equals("7")) {
                            XiupeichangShangActivity.actionStart(mContext, tuanGouShangJiaListAdapter.getData().get(position).getInst_id());
                        } else {
                            TuanGouShangJiaDetailsActivity.actionStart(mContext, tuanGouShangJiaListAdapter.getData().get(position).getInst_id(), type);
                        }
                        break;
                }
            }
        });

        LinearLayoutManager gridLayoutManager1 = new LinearLayoutManager(this);
        swipeTarget.setLayoutManager(gridLayoutManager1);
        tuanGouShangJiaListAdapter.openLoadAnimation();//?????????????????????
        swipeTarget.setAdapter(tuanGouShangJiaListAdapter);
    }

    private void initSM() {
        srLSmart.setEnableAutoLoadMore(true);
        srLSmart.setEnableRefresh(true);
        srLSmart.setEnableLoadMore(true);
        srLSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getNet();
            }
        });
        srLSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                lordNet();
            }
        });
    }

    /**
     * ??????
     */
    public void insertKeyword(String keyword, int type) {
        if (keyword == null || keyword.isEmpty()) {
            return;
        }
        HistoryRecordDao historyRecordDao = dbManager.getDaoSession().getHistoryRecordDao();
        List<HistoryRecord> searchKeywordList = queryKeywordList(type);
        if (searchKeywordList.size() > 9) {
            historyRecordDao.queryBuilder().where(HistoryRecordDao.Properties.Date.eq(searchKeywordList.get(0).getDate())).buildDelete().executeDeleteWithoutDetachingEntities();
        }

        Iterator<HistoryRecord> iterator = searchKeywordList.iterator();
        while (iterator.hasNext()) {
            HistoryRecord next = iterator.next();
            if (keyword.equals(next.getName())) {
                iterator.remove();
                historyRecordDao.queryBuilder().where(HistoryRecordDao.Properties.Name.eq(next.getName())).buildDelete().executeDeleteWithoutDetachingEntities();
            }
        }

        HistoryRecord sk = new HistoryRecord();
        sk.setName(keyword);
        sk.setDate(System.currentTimeMillis());
        sk.setType(type);
        historyRecordDao.insertInTx(sk);

        if (historyAdapter != null) {
            searchKeywords1.clear();
            searchKeywords1 = queryKeywordList(1);
            historyAdapter.setNewData(searchKeywords1);
        }
    }

    /**
     * ??????
     */
    public List<HistoryRecord> queryKeywordList(int type) {
        HistoryRecordDao historyRecordDao = dbManager.getDaoSession().getHistoryRecordDao();
        List<HistoryRecord> searchKeywordList = historyRecordDao.queryBuilder().where(HistoryRecordDao.Properties.Type.eq(type)).orderAsc(HistoryRecordDao.Properties.Date).build().list();
        return searchKeywordList;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // ????????????????????????????????????????????????TouchEvent???
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //????????????????????????location??????
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // ??????????????????????????????????????????EditText?????????
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_custom5_search_thing;
    }

    @OnClick({R.id.iv_cancel, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cancel:
                etSerarchKey.setText("");
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    //????????????
    private void historyAdapter() {
        historyAdapter = new Custom5HistoryAdapter(searchKeywords1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //  private String
    public void getNet() {
        Y.e("??????????????????????????????");
        inst_id = "";
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "08041");
        map.put("key", Urls.key);
        map.put("y", PreferenceHelper.getInstance(mContext).getString(JINGDU, "0X11"));
        map.put("x", PreferenceHelper.getInstance(mContext).getString(WEIDU, "0X11"));
        map.put("text", strTitle);
        map.put("inst_id", inst_id);
        Gson gson = new Gson();
        OkGo.<AppResponse<TuanGouShangJiaListBeanNew.DataBean>>post(LIBAOLIST)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<TuanGouShangJiaListBeanNew.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<TuanGouShangJiaListBeanNew.DataBean>> response) {
                        storeListBeans.clear();
                        storeListBeans = response.body().data;
                        tuanGouShangJiaListAdapter.setNewData(storeListBeans);
                        tuanGouShangJiaListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        srLSmart.finishRefresh();
                    }
                });
    }

    //  private String
    public void lordNet() {
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "08041");
        map.put("key", Urls.key);
        map.put("y", PreferenceHelper.getInstance(mContext).getString(JINGDU, "0X11"));
        map.put("x", PreferenceHelper.getInstance(mContext).getString(WEIDU, "0X11"));
        map.put("text", strTitle);
        map.put("inst_id", inst_id);
        Gson gson = new Gson();
        OkGo.<AppResponse<TuanGouShangJiaListBeanNew.DataBean>>post(LIBAOLIST)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<TuanGouShangJiaListBeanNew.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<TuanGouShangJiaListBeanNew.DataBean>> response) {
                        List<TuanGouShangJiaListBeanNew.DataBean> data = response.body().data;
                        storeListBeans.addAll(data);
                        if (storeListBeans.size() > 0) {
                            inst_id = storeListBeans.get(storeListBeans.size() - 1).getInst_id();
                        } else {
                            inst_id = "";
                        }
                        tuanGouShangJiaListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        srLSmart.finishLoadMore();
                    }
                });
    }
}
