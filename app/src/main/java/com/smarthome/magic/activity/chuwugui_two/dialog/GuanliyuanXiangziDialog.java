package com.smarthome.magic.activity.chuwugui_two.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui.dialog.InputDialog;
import com.smarthome.magic.activity.chuwugui_two.model.GuanliyuanGuiziModel;
import com.smarthome.magic.activity.chuwugui_two.model.GuanliyuanXiangziModel;

public class GuanliyuanXiangziDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout rl_close;
    private TextView tv_box_name;
    private TextView tv_time;
    private TextView bt_jieshu;
    private TextView bt_dianhua;
    protected boolean dismissAfterClick = true;
    private GuanliyuanXiangziListener mListener;
    private GuanliyuanXiangziModel.DataBean model;

    public GuanliyuanXiangziDialog setmListener(GuanliyuanXiangziListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public GuanliyuanXiangziDialog(Context context, GuanliyuanXiangziModel.DataBean model, GuanliyuanXiangziListener mListener) {
        this(context, R.style.dialogBaseBlur,model);
        this.mListener = mListener;
    }

    public GuanliyuanXiangziDialog(Context context, int theme, GuanliyuanXiangziModel.DataBean model) {
        super(context, theme);
        this.model = model;
        init();
    }

    private void init() {
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.chuwugui_dialog_guizixiangqing);

        tv_box_name = findViewById(R.id.tv_box_name);
        tv_time = findViewById(R.id.tv_time);
        bt_jieshu = findViewById(R.id.bt_jieshu);
        bt_dianhua = findViewById(R.id.bt_dianhua);
        rl_close = findViewById(R.id.rl_close);

        bt_jieshu.setOnClickListener(this);
        bt_dianhua.setOnClickListener(this);
        rl_close.setOnClickListener(this);

        tv_box_name.setText(model.getDevice_box_name());

        String time=model.getLc_day()+model.getLc_hour()+model.getLc_minute()+model.getLc_second();
        tv_time.setText(time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_close:
                dismiss();
                break;
            case R.id.bt_jieshu:
                clickJieshu();
                break;
            case R.id.bt_dianhua:
                clickDianhua();
                break;
        }
    }

    private void clickJieshu() {
        if (mListener!=null){
            mListener.onClickJieshu(model);
        }
        dismissAfterClick();
    }

    private void clickDianhua() {
        if (mListener!=null){
            mListener.onClickDianhua(model);
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
    public GuanliyuanXiangziDialog setDismissAfterClick(boolean dismissAfterClick) {
        this.dismissAfterClick = dismissAfterClick;
        return this;
    }

    public interface GuanliyuanXiangziListener  {
        void onClickJieshu(GuanliyuanXiangziModel.DataBean bean);

        void onClickDianhua(GuanliyuanXiangziModel.DataBean bean);

        void onDismiss(InputDialog dialog);
    }
}
