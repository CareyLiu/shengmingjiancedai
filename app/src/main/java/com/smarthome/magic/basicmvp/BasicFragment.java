package com.smarthome.magic.basicmvp;

import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.gyf.barlibrary.ImmersionBar;
import com.smarthome.magic.R;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.app.RxUtils;
import com.smarthome.magic.util.TUtil;

import butterknife.ButterKnife;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

import static android.content.Context.INPUT_METHOD_SERVICE;



public abstract class BasicFragment<T extends BasicPresenter, E extends BasicModel> extends BasicSupportFragment {

    protected ImmersionBar mImmersionBar;
    protected View rootView;//在使用自定义toolbar时候的根布局 =toolBarView+childView
    protected Toolbar toolbar;
    protected TextView tv_title, tv_rightTitle,tv_leftTitle;
    protected ImageView iv_rightTitle,iv_leftTitle;
    protected LinearLayout ll_base_root;
    public T mPresenter;
    public E mModel;
    private boolean isMvp = false;
    protected View emptyView;

    public LinearLayout getLl_base_root() {
        return ll_base_root;
    }
    protected CompositeSubscription _subscriptions = new CompositeSubscription();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
    }

    /**
     * 注册事件通知
     */
    public Observable<Notice> toObservable() {
        return RxBus.getDefault().toObservable(Notice.class);
    }

    /**
     * 发送消息
     */
    public void sendRx(Notice msg) {
        RxBus.getDefault().sendRx(msg);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (null != getArguments()) {
            getBundleExtras(getArguments());
        }
        if (rootView == null) {
            //为空时初始化。
            if (showToolBar() && getToolBarResId() != 0) {
                //如果需要显示自定义toolbar,并且资源id存在的情况下，实例化baseView;
                rootView = inflater.inflate(toolbarCover() ? R.layout.basic_toolbar_cover :
                        R.layout.basic_layout, container, false);//根布局
                ll_base_root = (LinearLayout) rootView.findViewById(R.id.ll_base_root);
                ViewStub mVs_toolbar = (ViewStub) rootView.findViewById(R.id.vs_toolbar);//toolbar容器
                FrameLayout fl_container = (FrameLayout) rootView.findViewById(R.id.fl_container);//子布局容器
                mVs_toolbar.setLayoutResource(getToolBarResId());//toolbar资源id
                mVs_toolbar.inflate();//填充toolbar
                inflater.inflate(getLayoutRes(), fl_container, true);//子布局
            } else {
                //不显示通用toolbar
                rootView = inflater.inflate(getLayoutRes(), container, false);

            }
        }
        ButterKnife.bind(this, rootView);
        initToolBar(rootView);
        initView(rootView);
        mvpCreate();
        return rootView;
    }

    /**
     * 初始化toolbar可重写覆盖自定的toolbar,base中实现的是通用的toolbar
     */
    protected void initToolBar(View rootView) {
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
       if (toolbar == null) {
            return;
        }
        toolbar.setTitle("");
        tv_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tv_rightTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_right);
        iv_rightTitle = (ImageView) toolbar.findViewById(R.id.iv_toolbar_right);
        tv_leftTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_left);
        iv_leftTitle = (ImageView) toolbar.findViewById(R.id.iv_toolbar_left);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initLogic();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (immersionEnabled()) {
            mImmersionBar = ImmersionBar.with(this);
            immersionInit(mImmersionBar);
        }
        hideInput();
    }


    /**
     * 逻辑内容初始化，懒加载模式
     */
    protected abstract void initLogic();

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    /**
     * 关闭软键盘
     */
    public void hideInput() {
        if (getActivity().getCurrentFocus() == null) return;
        ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null){

        }
           // mImmersionBar.on;
    }

    /**
     * 当前页面Fragment支持沉浸式初始化。默认返回false，可设置支持沉浸式初始化
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean immersionEnabled() {
        return false;
    }

    /**
     * 状态栏初始化（immersionEnabled默认为false时不走该方法）
     */
    protected void immersionInit(ImmersionBar mImmersionBar) {
    }

    /**
     * 是否显示通用toolBar
     */
    public boolean showToolBar() {
        return false;
    }

    /**
     * 获取自定义toolbarview 资源id 默认为0，showToolBar()方法必须返回true才有效
     */
    public int getToolBarResId() {
        return R.layout.basic_common_toolbar;
    }

    /**
     * toolbar是否覆盖在内容区上方
     *
     * @return false 不覆盖  true 覆盖
     */
    protected boolean toolbarCover() {
        return false;
    }


    /**
     * 获取布局文件
     */
    protected abstract int getLayoutRes();

    /**
     * 初始化view
     */
    protected abstract void initView(View rootView);

    /**
     * 获取bundle信息
     *
     * @param bundle
     */
    protected void getBundleExtras(Bundle bundle) {
    }

    /**
     * 设置界面是否使用mvp模式（不用关注这个方法，这个方法是专门处理列表类界面的）
     *
     * @param mvp
     */
    public void setMvp(boolean mvp) {
        isMvp = mvp;
    }

    /**
     * mvp架构的初始化
     */
    public void mvpCreate() {
        if (isMvp) {
            mPresenter = TUtil.getT(this, 0);
            mModel = TUtil.getT(this, 1);
        }

        if (mPresenter != null) {
            mPresenter.mContext = this.getActivity();
        }

        initPresenter();
    }

    /**
     * 简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
     */
    public void initPresenter() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }


    public View getEmptyView(String str, @DrawableRes int drawRes) {
        if (emptyView != null) {
            return emptyView;
        }
        emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.layout_empty_view, null, false);
        if (!TextUtils.isEmpty(str)) {
            TextView textView = (TextView) emptyView.findViewById(R.id.tv_text);
            ImageView imageView = (ImageView) emptyView.findViewById(R.id.iv_empty);
            imageView.setImageResource(drawRes);
            textView.setText(str);
        }
        return emptyView;
    }
}
