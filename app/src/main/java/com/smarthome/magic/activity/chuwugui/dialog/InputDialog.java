package com.smarthome.magic.activity.chuwugui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smarthome.magic.R;


public class InputDialog extends Dialog implements View.OnClickListener {

    public EditText tv_content;
    public TextView tv_title;
    public TextView tv_cancel;
    public TextView tv_confirm;
    public View vv_line;

    protected boolean dismissAfterClick = true;
    private TishiDialogListener mListener;

    public InputDialog setmListener(TishiDialogListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public InputDialog(Context context, TishiDialogListener mListener) {
        this(context, R.style.dialogBaseBlur);
        this.mListener = mListener;
    }

    public InputDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.chuwugui_dialog_input);

        vv_line = findViewById(R.id.vv_line);
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_confirm = findViewById(R.id.tv_confirm);


        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        setTextTitle("提示").setTextConfirm("确定").setTextCancel("取消");
    }


    // ---------------------------text
    public InputDialog setTextTitle(String text) {
        tv_title.setText(text);
        tv_content.setHint(text);
        return this;
    }

    public InputDialog setTextCancel(String text) {
        if (TextUtils.isEmpty(text)) {
            tv_cancel.setVisibility(View.GONE);
            vv_line.setVisibility(View.GONE);
        } else {
            tv_cancel.setVisibility(View.VISIBLE);
            vv_line.setVisibility(View.VISIBLE);
            tv_cancel.setText(text);
        }
        return this;
    }

    public InputDialog setTextConfirm(String text) {
        tv_confirm.setText(text);
        return this;
    }

    public InputDialog setTextContent(String text) {
        tv_content.setText(text);
        return this;
    }

    public InputDialog setTextInput(int inputType) {
        tv_content.setInputType(inputType);
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v == tv_cancel) {
            clickCancel(v);
        } else if (v == tv_confirm) {
            clickConfirm(v);
        }
    }


    private void clickCancel(View v) {
        if (mListener != null) {
            mListener.onClickCancel(v, InputDialog.this);
        }
        dismissAfterClick();
    }

    private void clickConfirm(View v) {
        if (mListener != null) {
            mListener.onClickConfirm(v, InputDialog.this);
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
    public InputDialog setDismissAfterClick(boolean dismissAfterClick) {
        this.dismissAfterClick = dismissAfterClick;
        return this;
    }


    public interface TishiDialogListener {
        void onClickCancel(View v, InputDialog dialog);

        void onClickConfirm(View v, InputDialog dialog);

        void onDismiss(InputDialog dialog);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        if (mListener != null) {
            mListener.onDismiss(InputDialog.this);
        }
    }

    public String getTextContent() {
        return tv_content.getText().toString();
    }
}
