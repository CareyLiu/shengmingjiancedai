package com.smarthome.magic.activity.wode_page;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.DefaultX5WebView_HaveNameActivity;
import com.smarthome.magic.activity.fenxiang_tuisong.ShouYeFenXiang_Url_Activity;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.BuTianOrFenXiangDialog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.TuiGuangMaModel;
import com.smarthome.magic.util.AlertUtil;
import com.smarthome.magic.util.Tools;
import com.smarthome.magic.util.x5.utils.AndroidForJs;
import com.smarthome.magic.util.x5.utils.X5WebView;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class TuiGuangMaActivity extends BaseActivity {


    @BindView(R.id.view_1)
    View view1;
    @BindView(R.id.x5_webView)
    X5WebView x5WebView;
    private ProgressDialog progressDialog;
    private String publicMethod;
    List<String> mGoBackUrlList = new ArrayList<>();
    boolean isLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_tui_guang_ma);
        //    initToolbar();
        //  getNet();

        String url = getIntent().getStringExtra("url");
        init(url);
        getNet();
    }


    @Override
    public int getContentViewResId() {
        return R.layout.activity_tui_guang_ma;
    }

    Response<AppResponse<TuiGuangMaModel.DataBean>> response;

    private void getNet() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04341");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(TuiGuangMaActivity.this).getAppToken());
        // map.put("shop_product_id", productId);
        //map.put("wares_id", warseId);

        Log.i("taoken_gg", UserManager.getManager(TuiGuangMaActivity.this).getAppToken());
        Gson gson = new Gson();
        OkGo.<AppResponse<TuiGuangMaModel.DataBean>>post(Urls.SERVER_URL + "shop_new/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<TuiGuangMaModel.DataBean>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<TuiGuangMaModel.DataBean>> response) {
                        TuiGuangMaActivity.this.response = response;
//                        Bitmap b = BitmapFactory.decodeResource(TuiGuangMaActivity.this.getResources(), R.drawable.juyijia_logo);
//                        Bitmap bitmap = Tools.createQRImage(TuiGuangMaActivity.this, response.body().data.get(0).getReferral_code_url(), b);
//                        ivErweima.setImageBitmap(bitmap);
//
//                        tvYonghuYaoqing.setText("????????????" + response.body().data.get(0).getInvitation_code() + "(????????????)");
//
//                        tvYonghuYaoqing.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                ClipboardManager copy = (ClipboardManager) TuiGuangMaActivity.this
//                                        .getSystemService(Context.CLIPBOARD_SERVICE);
//                                copy.setText(response.body().data.get(0).getInvitation_code());
//                                UIHelper.ToastMessage(TuiGuangMaActivity.this, "????????????");
//                            }
//                        });


//                        if (response.body().data.get(0).getDisplay().equals("0")) {
//                            tv_rightTitle.setVisibility(View.VISIBLE);
//                            tv_rightTitle.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    showInput();
//                                }
//                            });
//                        } else {
//                            tv_rightTitle.setVisibility(View.GONE);
//                        }
//                        String url = response.body().data.get(0).getReferral_code_url();
//                        init(url);
                    }

                    @Override
                    public void onError(Response<AppResponse<TuiGuangMaModel.DataBean>> response) {
                        AlertUtil.t(TuiGuangMaActivity.this, response.getException().getMessage());
                    }
                });
    }


    private void init(String url) {
        x5WebView.setWebViewClient(new TuiGuangMaActivity.MyWebViewClient());
        x5WebView.setWebChromeClient(new WebChromeClient());
        x5WebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
//        tvLoad.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadUrl(url);
//            }
//        });

        WebSettings webSetting = x5WebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(x5WebView.getContext().getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(x5WebView.getContext().getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(x5WebView.getContext().getDir("geolocation", 0).getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //x5WebView.addJavascriptInterface(new AndroidForJs(DefaultX5WebView_HaveNameActivity.this), "android");
        long time = System.currentTimeMillis();
        // Log.e(tag, "DefaultX5WebViewActivity : " + url);
        if (url != null) {
            loadUrl(url);
        }
        TbsLog.d("time-cost", "cost time: " + (System.currentTimeMillis() - time));
//        CookieSyncManager.createInstance(x5WebView.getContext());
//        CookieSyncManager.getInstance().sync();
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        //???????????????????????????????????????????????????????????????
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(mContext);//?????????????????????????????????dialog??????
                progressDialog.setMessage("?????????????????????...");
                progressDialog.show();
                x5WebView.setEnabled(false);// ?????????????????????????????????????????????
            }
            super.onPageStarted(view, url, favicon);
        }

        //???????????????????????????????????????????????????????????????
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
                x5WebView.setEnabled(true);
            }
            if (tv_title != null && view != null && view.getTitle() != null) {
                tv_title.setText(view.getTitle());
            }
            publicMethod = "javascript:publicMethod('operatingSystem','" + new Gson().toJson("android") + "')";
            if (x5WebView != null) {
                x5WebView.loadUrl(publicMethod);
            }
            mGoBackUrlList.add(url);
            isLoad = false;

        }

        //???????????????????????????????????????????????????????????????
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed(); // ????????????
        }
    }

    private void getNet_butian(String et) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04343");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(TuiGuangMaActivity.this).getAppToken());
        // map.put("shop_product_id", productId);
        //map.put("wares_id", warseId);
        map.put("invitation_code", et);

        Log.i("taoken_gg", UserManager.getManager(TuiGuangMaActivity.this).getAppToken());
        Gson gson = new Gson();
        OkGo.<AppResponse<Object>>post(Urls.SERVER_URL + "shop_new/app/user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Object>>() {
                    @Override
                    public void onSuccess(final Response<AppResponse<Object>> response) {
                        UIHelper.ToastMessage(TuiGuangMaActivity.this, "????????????");

                        tv_rightTitle.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError(Response<AppResponse<Object>> response) {
                        AlertUtil.t(TuiGuangMaActivity.this, response.getException().getMessage());
                    }
                });
    }

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String url) {
        Intent intent = new Intent(context, TuiGuangMaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    private AlertDialog.Builder builder;

    /**
     * ?????????????????? dialog
     */
    private void showInput() {
        final EditText editText = new EditText(this);
        builder = new AlertDialog.Builder(this).setTitle("???????????????").setView(editText)
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getNet_butian(editText.getText().toString());
                    }
                });
        builder.create().show();
    }

    @Override
    public boolean showToolBar() {
        return true;
    }


    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("?????????");
        tv_title.setTextSize(17);

        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        tv_rightTitle.setText("??????");

//        tv_leftTitle.setText("??????");
//        tv_leftTitle.setVisibility(View.VISIBLE);
//        tv_leftTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShouYeFenXiang_Url_Activity.actionStart(TuiGuangMaActivity.this);
//            }
//        });


        iv_rightTitle.setVisibility(View.VISIBLE);
        iv_rightTitle.setBackgroundResource(R.mipmap.def_more);
        iv_rightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BuTianOrFenXiangDialog buTianOrFenXiangDialog = new BuTianOrFenXiangDialog(mContext, new BuTianOrFenXiangDialog.OnDialogItemClickListener() {
                    @Override
                    public void butianClick() {

//                        if (response.body().data.get(0).getDisplay() == null) {
//                            return;
//                        }
                        if (response.body().data.get(0).getDisplay().equals("0")) {
                            showInput();
                        } else {
                            UIHelper.ToastMessage(mContext, "??????????????? ????????????");
                        }
                    }

                    @Override
                    public void fenXiangClick() {
                        ShouYeFenXiang_Url_Activity.actionStart(TuiGuangMaActivity.this);
                    }

                    @Override
                    public void quXiaoClick() {
                        //UIHelper.ToastMessage(mContext, "??????");
                    }
                });


                buTianOrFenXiangDialog.show();
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imm.hideSoftInputFromWindow(findViewById(R.id.cl_layout).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });
    }

    public void loadUrl(String url) {
        if (url == null) {
            return;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        x5WebView.loadUrl(url);
    }


    @Override
    public void onDestroy() {
        if (x5WebView != null) {
            x5WebView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (x5WebView != null) {
            x5WebView.onPause();
            x5WebView.reload();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (x5WebView != null) {
            x5WebView.onResume();
            x5WebView.reload();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (x5WebView != null && x5WebView.canGoBack()) {
                int index = mGoBackUrlList.size();
                if (index == 0 || index == 1) {
                    x5WebView.goBack();
                    mGoBackUrlList.clear();
                } else {
                    if (x5WebView.canGoBackOrForward(-index)) {
                        x5WebView.goBackOrForward(-index);
                        mGoBackUrlList.clear();
                    } else {
                        x5WebView.goBack();
                        mGoBackUrlList.clear();
                    }
                }
                return true;
            } else {
                if (!isLoad) {
                    return super.onKeyDown(keyCode, event);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
