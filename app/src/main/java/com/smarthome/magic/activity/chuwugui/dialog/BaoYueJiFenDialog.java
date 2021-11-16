package com.smarthome.magic.activity.chuwugui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.smarthome.magic.R;
import com.smarthome.magic.app.Notice;

import butterknife.BindView;

public class BaoYueJiFenDialog extends Dialog {
    @BindView(R.id.tv_baoyuejifei)
    TextView tvBaoyuejifei;
    @BindView(R.id.tv_jiage)
    TextView tvJiage;
    @BindView(R.id.tv_quding)
    TextView tvQuding;

    public BaoYueJiFenDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.chuwugui_baoyue_dialog);
        tvQuding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Notice notice = new Notice();
                dismiss();
            }
        });
    }
}
