package com.smarthome.magic.activity.chuwugui_two.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui.dialog.InputDialog;
import com.smarthome.magic.activity.chuwugui_two.model.GuanliyuanGuiziModel;

import androidx.annotation.NonNull;

public class GuanliyuanDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout rl_close;
    private TextView tv_tishi;
    private TextView bt_kaixiang;
    private TextView bt_jinyong;
    protected boolean dismissAfterClick = true;
    private GuanliyuanListener mListener;
    private GuanliyuanGuiziModel.DataBean.BoxListBean model;

    public GuanliyuanDialog setmListener(GuanliyuanListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public GuanliyuanDialog(Context context, GuanliyuanGuiziModel.DataBean.BoxListBean model, GuanliyuanListener mListener) {
        this(context, R.style.dialogBaseBlur,model);
        this.mListener = mListener;
    }

    public GuanliyuanDialog(Context context, int theme, GuanliyuanGuiziModel.DataBean.BoxListBean model) {
        super(context, theme);
        this.model = model;
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.chuwugui_dialog_guanliguizi);

        tv_tishi = findViewById(R.id.tv_tishi);
        bt_kaixiang = findViewById(R.id.bt_kaixiang);
        bt_jinyong = findViewById(R.id.bt_jinyong);
        rl_close = findViewById(R.id.rl_close);

        if (model.getBox_state().equals("1")) {
            bt_kaixiang.setVisibility(View.VISIBLE);
            bt_jinyong.setText("禁用");
            tv_tishi.setText(model.getDevice_box_name() + "号柜状态为空闲中，请选择操作类型。");
        } else {
            bt_kaixiang.setVisibility(View.GONE);
            bt_jinyong.setText("解禁");
            tv_tishi.setText(model.getDevice_box_name() + "号柜已被禁用，请选择操作类型。");
        }

        bt_kaixiang.setOnClickListener(this);
        bt_jinyong.setOnClickListener(this);
        rl_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_close:
                dismiss();
                break;
            case R.id.bt_kaixiang:
                clickKaixiang();
                break;
            case R.id.bt_jinyong:
                clickJinyong();
                break;
        }
    }

    private void clickJinyong() {
        if (mListener!=null){
            if (model.getBox_state().equals("1")){
                mListener.onClickJinyong(model);
            }else {
                mListener.onClickJiejin(model);
            }
        }
        dismissAfterClick();
    }

    private void clickKaixiang() {
        if (mListener!=null){
            mListener.onClickKaixiang(model);
        }
        dismissAfterClick();
    }

    protected void dismissAfterClick() {
        if (dismissAfterClick) {
            dismiss();
        }
    }

    /**
     * 设置是否点击按钮后自动关闭窗口,默认true(是)
     *
     * @param dismissAfterClick
     * @return
     */
    public GuanliyuanDialog setDismissAfterClick(boolean dismissAfterClick) {
        this.dismissAfterClick = dismissAfterClick;
        return this;
    }

    public interface GuanliyuanListener {
        void onClickKaixiang(GuanliyuanGuiziModel.DataBean.BoxListBean bean);

        void onClickJinyong(GuanliyuanGuiziModel.DataBean.BoxListBean  bean);

        void onClickJiejin(GuanliyuanGuiziModel.DataBean.BoxListBean  bean);

        void onDismiss(InputDialog dialog);
    }
}
