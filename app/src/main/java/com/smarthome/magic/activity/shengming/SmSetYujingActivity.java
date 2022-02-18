package com.smarthome.magic.activity.shengming;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui.dialog.InputDialog;
import com.smarthome.magic.activity.shengming.shengmingmodel.CreateSession;
import com.smarthome.magic.activity.shengming.utils.UrlUtils;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.PreferenceHelper;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmSetYujingActivity extends BaseActivity {


    @BindView(R.id.iv_switch_hrAlarmEnable)
    ImageView iv_switch_hrAlarmEnable;
    @BindView(R.id.tv_hrAlarmMax)
    TextView tv_hrAlarmMax;
    @BindView(R.id.rl_hrAlarmMax)
    RelativeLayout rl_hrAlarmMax;
    @BindView(R.id.tv_hrAlarmMin)
    TextView tv_hrAlarmMin;
    @BindView(R.id.rl_hrAlarmMin)
    RelativeLayout rl_hrAlarmMin;
    @BindView(R.id.iv_switch_rrAlarmEnable)
    ImageView iv_switch_rrAlarmEnable;
    @BindView(R.id.iv_switch_rrStopEnable)
    ImageView iv_switch_rrStopEnable;
    @BindView(R.id.tv_rrAlarmMax)
    TextView tv_rrAlarmMax;
    @BindView(R.id.rl_rrAlarmMax)
    RelativeLayout rl_rrAlarmMax;
    @BindView(R.id.tv_rrAlarmMin)
    TextView tv_rrAlarmMin;
    @BindView(R.id.rl_rrAlarmMin)
    RelativeLayout rl_rrAlarmMin;
    @BindView(R.id.iv_switch_offBedEnable)
    ImageView iv_switch_offBedEnable;
    @BindView(R.id.tv_leaveDura)
    TextView tv_leaveDura;
    @BindView(R.id.rl_leaveDura)
    RelativeLayout rl_leaveDura;
    @BindView(R.id.tv_sleepDayDuraStart)
    TextView tv_sleepDayDuraStart;
    @BindView(R.id.rl_sleepDayDuraStart)
    RelativeLayout rl_sleepDayDuraStart;
    @BindView(R.id.tv_sleepDayDuraStop)
    TextView tv_sleepDayDuraStop;
    @BindView(R.id.rl_sleepDayDuraStop)
    RelativeLayout rl_sleepDayDuraStop;
    @BindView(R.id.iv_switch_sleepAllDayEnable)
    ImageView iv_switch_sleepAllDayEnable;
    @BindView(R.id.tv_sleepNightDuraStart)
    TextView tv_sleepNightDuraStart;
    @BindView(R.id.rl_sleepNightDuraStart)
    RelativeLayout rl_sleepNightDuraStart;
    @BindView(R.id.tv_sleepNightDuraStop)
    TextView tv_sleepNightDuraStop;
    @BindView(R.id.rl_sleepNightDuraStop)
    RelativeLayout rl_sleepNightDuraStop;
    @BindView(R.id.iv_switch_noonSleepDayEnable)
    ImageView iv_switch_noonSleepDayEnable;
    @BindView(R.id.iv_switch_nightSleepDayEnable)
    ImageView iv_switch_nightSleepDayEnable;
    @BindView(R.id.bt_save)
    TextView bt_save;

    private String hrAlarmEnable;
    private String hrAlarmMax;
    private String hrAlarmMin;
    private String rrAlarmEnable;
    private String rrStopEnable;
    private String rrAlarmMax;
    private String rrAlarmMin;
    private String offBedEnable;
    private String leaveDura;
    private String sleepDayDuraStart;
    private String sleepDayDuraStop;
    private String sleepAllDayEnable;
    private String sleepNightDuraStart;
    private String sleepNightDuraStop;
    private String noonSleepDayEnable;
    private String nightSleepDayEnable;

    private String sessionId;


    private TimePickerView timeDayDuraStart;
    private TimePickerView timeDayDuraStop;
    private TimePickerView timeNightDuraStart;
    private TimePickerView timeNightDuraStop;


    @Override
    public int getContentViewResId() {
        return R.layout.shengming_act_set_yujing;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SmSetYujingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("设备预警参数配置");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        sessionId = PreferenceHelper.getInstance(mContext).getString("sm_sessionId", "");

        hrAlarmEnable = PreferenceHelper.getInstance(mContext).getString("hrAlarmEnable", "0");
        hrAlarmMax = PreferenceHelper.getInstance(mContext).getString("hrAlarmMax", "100");
        hrAlarmMin = PreferenceHelper.getInstance(mContext).getString("hrAlarmMin", "60");

        rrAlarmEnable = PreferenceHelper.getInstance(mContext).getString("rrAlarmEnable", "0");
        rrStopEnable = PreferenceHelper.getInstance(mContext).getString("rrStopEnable", "0");
        rrAlarmMax = PreferenceHelper.getInstance(mContext).getString("rrAlarmMax", "20");
        rrAlarmMin = PreferenceHelper.getInstance(mContext).getString("rrAlarmMin", "10");

        offBedEnable = PreferenceHelper.getInstance(mContext).getString("offBedEnable", "0");
        leaveDura = PreferenceHelper.getInstance(mContext).getString("leaveDura", "60");

        sleepAllDayEnable = PreferenceHelper.getInstance(mContext).getString("sleepAllDayEnable", "0");
        noonSleepDayEnable = PreferenceHelper.getInstance(mContext).getString("noonSleepDayEnable", "0");
        sleepDayDuraStart = PreferenceHelper.getInstance(mContext).getString("sleepDayDuraStart", "12:00");
        sleepDayDuraStop = PreferenceHelper.getInstance(mContext).getString("sleepDayDuraStop", "13:00");
        nightSleepDayEnable = PreferenceHelper.getInstance(mContext).getString("nightSleepDayEnable", "0");
        sleepNightDuraStart = PreferenceHelper.getInstance(mContext).getString("sleepNightDuraStart", "22:00");
        sleepNightDuraStop = PreferenceHelper.getInstance(mContext).getString("sleepNightDuraStop", "08:00");

        if (hrAlarmEnable.equals("1")) {
            iv_switch_hrAlarmEnable.setImageResource(R.mipmap.switch_open);
            rl_hrAlarmMax.setVisibility(View.VISIBLE);
            rl_hrAlarmMin.setVisibility(View.VISIBLE);
        } else {
            iv_switch_hrAlarmEnable.setImageResource(R.mipmap.switch_close);
            rl_hrAlarmMax.setVisibility(View.GONE);
            rl_hrAlarmMin.setVisibility(View.GONE);
        }
        tv_hrAlarmMax.setText(hrAlarmMax + "次/分");
        tv_hrAlarmMin.setText(hrAlarmMin + "次/分");

        if (rrAlarmEnable.equals("1")) {
            iv_switch_rrAlarmEnable.setImageResource(R.mipmap.switch_open);
            rl_rrAlarmMax.setVisibility(View.VISIBLE);
            rl_rrAlarmMin.setVisibility(View.VISIBLE);
        } else {
            iv_switch_rrAlarmEnable.setImageResource(R.mipmap.switch_close);
            rl_rrAlarmMax.setVisibility(View.GONE);
            rl_rrAlarmMin.setVisibility(View.GONE);
        }

        if (rrStopEnable.equals("1")) {
            iv_switch_rrStopEnable.setImageResource(R.mipmap.switch_open);
        } else {
            iv_switch_rrStopEnable.setImageResource(R.mipmap.switch_close);
        }
        tv_rrAlarmMax.setText(rrAlarmMax + "次/分");
        tv_rrAlarmMin.setText(rrAlarmMin + "次/分");

        if (offBedEnable.equals("1")) {
            iv_switch_offBedEnable.setImageResource(R.mipmap.switch_open);
            rl_leaveDura.setVisibility(View.VISIBLE);
        } else {
            iv_switch_offBedEnable.setImageResource(R.mipmap.switch_close);
            rl_leaveDura.setVisibility(View.GONE);
        }
        tv_leaveDura.setText(leaveDura + "分");

        if (sleepAllDayEnable.equals("1")) {
            iv_switch_sleepAllDayEnable.setImageResource(R.mipmap.switch_open);
            iv_switch_noonSleepDayEnable.setImageResource(R.mipmap.switch_close);
            iv_switch_nightSleepDayEnable.setImageResource(R.mipmap.switch_close);

            rl_sleepDayDuraStart.setVisibility(View.GONE);
            rl_sleepDayDuraStop.setVisibility(View.GONE);
            rl_sleepNightDuraStart.setVisibility(View.GONE);
            rl_sleepNightDuraStop.setVisibility(View.GONE);
        } else {
            iv_switch_sleepAllDayEnable.setImageResource(R.mipmap.switch_close);
        }

        if (noonSleepDayEnable.equals("1")) {
            iv_switch_noonSleepDayEnable.setImageResource(R.mipmap.switch_open);
            iv_switch_sleepAllDayEnable.setImageResource(R.mipmap.switch_close);
            rl_sleepDayDuraStart.setVisibility(View.VISIBLE);
            rl_sleepDayDuraStop.setVisibility(View.VISIBLE);
        } else {
            iv_switch_noonSleepDayEnable.setImageResource(R.mipmap.switch_close);
            rl_sleepDayDuraStart.setVisibility(View.GONE);
            rl_sleepDayDuraStop.setVisibility(View.GONE);
        }

        if (nightSleepDayEnable.equals("1")) {
            iv_switch_nightSleepDayEnable.setImageResource(R.mipmap.switch_open);
            iv_switch_sleepAllDayEnable.setImageResource(R.mipmap.switch_close);
            rl_sleepNightDuraStart.setVisibility(View.VISIBLE);
            rl_sleepNightDuraStop.setVisibility(View.VISIBLE);
        } else {
            iv_switch_nightSleepDayEnable.setImageResource(R.mipmap.switch_close);
            rl_sleepNightDuraStart.setVisibility(View.GONE);
            rl_sleepNightDuraStop.setVisibility(View.GONE);
        }

        tv_sleepDayDuraStart.setText(sleepDayDuraStart);
        tv_sleepDayDuraStop.setText(sleepDayDuraStop);
        tv_sleepNightDuraStart.setText(sleepNightDuraStart);
        tv_sleepNightDuraStop.setText(sleepNightDuraStop);
    }

    @OnClick({R.id.bt_save, R.id.iv_switch_hrAlarmEnable, R.id.rl_hrAlarmMax, R.id.rl_hrAlarmMin, R.id.iv_switch_rrAlarmEnable, R.id.iv_switch_rrStopEnable, R.id.rl_rrAlarmMax, R.id.rl_rrAlarmMin, R.id.iv_switch_offBedEnable, R.id.rl_leaveDura, R.id.rl_sleepDayDuraStart, R.id.rl_sleepDayDuraStop, R.id.iv_switch_sleepAllDayEnable, R.id.rl_sleepNightDuraStart, R.id.rl_sleepNightDuraStop, R.id.iv_switch_noonSleepDayEnable, R.id.iv_switch_nightSleepDayEnable})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_switch_hrAlarmEnable:
                clickHrAlarmEnable();
                break;
            case R.id.rl_hrAlarmMax:
                clickHrAlarmMax();
                break;
            case R.id.rl_hrAlarmMin:
                clickHrAlarmMin();
                break;
            case R.id.iv_switch_rrAlarmEnable:
                clickRrAlarmEnable();
                break;
            case R.id.rl_rrAlarmMax:
                clickRrAlarmMax();
                break;
            case R.id.rl_rrAlarmMin:
                clickRrAlarmMin();
                break;
            case R.id.iv_switch_rrStopEnable:
                clickRrStopEnable();
                break;
            case R.id.iv_switch_offBedEnable:
                clickOffBedEnable();
                break;
            case R.id.rl_leaveDura:
                clickLeaveDura();
                break;
            case R.id.iv_switch_sleepAllDayEnable:
                clickSleepAllDayEnable();
                break;
            case R.id.iv_switch_noonSleepDayEnable:
                clickNoonSleepDayEnable();
                break;
            case R.id.rl_sleepDayDuraStart:
                clickDayDuraStart();
                break;
            case R.id.rl_sleepDayDuraStop:
                clickDayDuraStop();
                break;
            case R.id.iv_switch_nightSleepDayEnable:
                clickNightSleepDayEnable();
                break;
            case R.id.rl_sleepNightDuraStart:
                clickNightDuraStart();
                break;
            case R.id.rl_sleepNightDuraStop:
                clickNightDuraStop();
                break;
            case R.id.bt_save:
                clickSave();
                break;
        }
    }

    private void clickHrAlarmEnable() {
        if (hrAlarmEnable.equals("1")) {
            hrAlarmEnable = "0";
            iv_switch_hrAlarmEnable.setImageResource(R.mipmap.switch_close);
            rl_hrAlarmMax.setVisibility(View.GONE);
            rl_hrAlarmMin.setVisibility(View.GONE);
        } else {
            hrAlarmEnable = "1";
            iv_switch_hrAlarmEnable.setImageResource(R.mipmap.switch_open);
            rl_hrAlarmMax.setVisibility(View.VISIBLE);
            rl_hrAlarmMin.setVisibility(View.VISIBLE);
        }
    }

    private void clickHrAlarmMax() {
        InputDialog dialog = new InputDialog(mContext, new InputDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, InputDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, InputDialog dialog) {
                hrAlarmMax = dialog.getTextContent();
                tv_hrAlarmMax.setText(hrAlarmMax + "次/分");
            }

            @Override
            public void onDismiss(InputDialog dialog) {
                Y.hideInputMethod(dialog.tv_content);
            }
        });
        dialog.setTextTitle("请设置心率上限预警值");
        dialog.setTextInput(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dialog.setTextContent(hrAlarmMax);
        dialog.show();
    }

    private void clickHrAlarmMin() {
        InputDialog dialog = new InputDialog(mContext, new InputDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, InputDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, InputDialog dialog) {
                hrAlarmMin = dialog.getTextContent();
                tv_hrAlarmMin.setText(hrAlarmMin + "次/分");
            }

            @Override
            public void onDismiss(InputDialog dialog) {
                Y.hideInputMethod(dialog.tv_content);
            }
        });
        dialog.setTextTitle("请设置心率下限预警值");
        dialog.setTextInput(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dialog.setTextContent(hrAlarmMin);
        dialog.show();
    }

    private void clickRrAlarmEnable() {
        if (rrAlarmEnable.equals("1")) {
            rrAlarmEnable = "0";
            iv_switch_rrAlarmEnable.setImageResource(R.mipmap.switch_close);
            rl_rrAlarmMax.setVisibility(View.GONE);
            rl_rrAlarmMin.setVisibility(View.GONE);
        } else {
            rrAlarmEnable = "1";
            iv_switch_rrAlarmEnable.setImageResource(R.mipmap.switch_open);
            rl_rrAlarmMax.setVisibility(View.VISIBLE);
            rl_rrAlarmMin.setVisibility(View.VISIBLE);
        }
    }

    private void clickRrAlarmMax() {
        InputDialog dialog = new InputDialog(mContext, new InputDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, InputDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, InputDialog dialog) {
                rrAlarmMax = dialog.getTextContent();
                tv_rrAlarmMax.setText(rrAlarmMax + "次/分");
            }

            @Override
            public void onDismiss(InputDialog dialog) {
                Y.hideInputMethod(dialog.tv_content);
            }
        });
        dialog.setTextTitle("请设置呼吸上限预警值");
        dialog.setTextInput(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dialog.setTextContent(rrAlarmMax);
        dialog.show();
    }

    private void clickRrAlarmMin() {
        InputDialog dialog = new InputDialog(mContext, new InputDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, InputDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, InputDialog dialog) {
                rrAlarmMin = dialog.getTextContent();
                tv_rrAlarmMin.setText(rrAlarmMin + "次/分");
            }

            @Override
            public void onDismiss(InputDialog dialog) {
                Y.hideInputMethod(dialog.tv_content);
            }
        });
        dialog.setTextTitle("请设置呼吸下限预警值");
        dialog.setTextInput(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dialog.setTextContent(rrAlarmMin);
        dialog.show();
    }

    private void clickRrStopEnable() {
        if (rrStopEnable.equals("1")) {
            rrStopEnable = "0";
            iv_switch_rrStopEnable.setImageResource(R.mipmap.switch_close);
        } else {
            rrStopEnable = "1";
            iv_switch_rrStopEnable.setImageResource(R.mipmap.switch_open);
        }
    }

    private void clickOffBedEnable() {
        if (offBedEnable.equals("1")) {
            offBedEnable = "0";
            iv_switch_offBedEnable.setImageResource(R.mipmap.switch_close);
            rl_leaveDura.setVisibility(View.GONE);
        } else {
            offBedEnable = "1";
            iv_switch_offBedEnable.setImageResource(R.mipmap.switch_open);
            rl_leaveDura.setVisibility(View.VISIBLE);
        }
    }

    private void clickLeaveDura() {
        InputDialog dialog = new InputDialog(mContext, new InputDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, InputDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, InputDialog dialog) {
                leaveDura = dialog.getTextContent();
                tv_leaveDura.setText(leaveDura + "分");
            }

            @Override
            public void onDismiss(InputDialog dialog) {
                Y.hideInputMethod(dialog.tv_content);
            }
        });
        dialog.setTextTitle("请设置脱离监测在床时间上限");
        dialog.setTextInput(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dialog.setTextContent(leaveDura);
        dialog.show();
    }


    private void clickSleepAllDayEnable() {
        if (sleepAllDayEnable.equals("1")) {
            sleepAllDayEnable = "0";
            iv_switch_sleepAllDayEnable.setImageResource(R.mipmap.switch_close);
        } else {
            sleepAllDayEnable = "1";
            noonSleepDayEnable = "0";
            nightSleepDayEnable = "0";
            iv_switch_sleepAllDayEnable.setImageResource(R.mipmap.switch_open);
            iv_switch_noonSleepDayEnable.setImageResource(R.mipmap.switch_close);
            iv_switch_nightSleepDayEnable.setImageResource(R.mipmap.switch_close);

            rl_sleepDayDuraStart.setVisibility(View.GONE);
            rl_sleepDayDuraStop.setVisibility(View.GONE);
            rl_sleepNightDuraStart.setVisibility(View.GONE);
            rl_sleepNightDuraStop.setVisibility(View.GONE);
        }
    }

    private void clickNoonSleepDayEnable() {
        if (noonSleepDayEnable.equals("1")) {
            noonSleepDayEnable = "0";
            iv_switch_noonSleepDayEnable.setImageResource(R.mipmap.switch_close);
            rl_sleepDayDuraStart.setVisibility(View.GONE);
            rl_sleepDayDuraStop.setVisibility(View.GONE);
        } else {
            noonSleepDayEnable = "1";
            sleepAllDayEnable = "0";
            iv_switch_noonSleepDayEnable.setImageResource(R.mipmap.switch_open);
            iv_switch_sleepAllDayEnable.setImageResource(R.mipmap.switch_close);
            rl_sleepDayDuraStart.setVisibility(View.VISIBLE);
            rl_sleepDayDuraStop.setVisibility(View.VISIBLE);
        }
    }


    private void clickDayDuraStart() {
        if (timeDayDuraStart == null) {
            //时间选择器
            boolean[] select = {false, false, false, true, true, false};
            timeDayDuraStart = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    String chooseHour;
                    String chooseMin;

                    int hours = date.getHours();
                    if (hours < 10) {
                        chooseHour = "0" + hours;
                    } else {
                        chooseHour = "" + hours;
                    }

                    int minutes = date.getMinutes();
                    if (minutes < 10) {
                        chooseMin = "0" + minutes;
                    } else {
                        chooseMin = "" + minutes;
                    }
                    sleepDayDuraStart = chooseHour + ":" + chooseMin;
                    tv_sleepDayDuraStart.setText(sleepDayDuraStart);
                }
            }).setType(select).build();
        }
        timeDayDuraStart.show();
    }

    private void clickDayDuraStop() {
        if (timeDayDuraStop == null) {
            //时间选择器
            boolean[] select = {false, false, false, true, true, false};
            timeDayDuraStop = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    String chooseHour;
                    String chooseMin;

                    int hours = date.getHours();
                    if (hours < 10) {
                        chooseHour = "0" + hours;
                    } else {
                        chooseHour = "" + hours;
                    }

                    int minutes = date.getMinutes();
                    if (minutes < 10) {
                        chooseMin = "0" + minutes;
                    } else {
                        chooseMin = "" + minutes;
                    }
                    sleepDayDuraStop = chooseHour + ":" + chooseMin;
                    tv_sleepDayDuraStop.setText(sleepDayDuraStop);
                }
            }).setType(select).build();
        }
        timeDayDuraStop.show();
    }

    private void clickNightSleepDayEnable() {
        if (nightSleepDayEnable.equals("1")) {
            nightSleepDayEnable = "0";
            iv_switch_nightSleepDayEnable.setImageResource(R.mipmap.switch_close);
            rl_sleepNightDuraStart.setVisibility(View.GONE);
            rl_sleepNightDuraStop.setVisibility(View.GONE);
        } else {
            nightSleepDayEnable = "1";
            sleepAllDayEnable = "0";
            iv_switch_nightSleepDayEnable.setImageResource(R.mipmap.switch_open);
            iv_switch_sleepAllDayEnable.setImageResource(R.mipmap.switch_close);
            rl_sleepNightDuraStart.setVisibility(View.VISIBLE);
            rl_sleepNightDuraStop.setVisibility(View.VISIBLE);
        }
    }

    private void clickNightDuraStart() {
        if (timeNightDuraStart == null) {
            //时间选择器
            boolean[] select = {false, false, false, true, true, false};
            timeNightDuraStart = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    String chooseHour;
                    String chooseMin;

                    int hours = date.getHours();
                    if (hours < 10) {
                        chooseHour = "0" + hours;
                    } else {
                        chooseHour = "" + hours;
                    }

                    int minutes = date.getMinutes();
                    if (minutes < 10) {
                        chooseMin = "0" + minutes;
                    } else {
                        chooseMin = "" + minutes;
                    }
                    sleepNightDuraStart = chooseHour + ":" + chooseMin;
                    tv_sleepNightDuraStart.setText(sleepNightDuraStart);
                }
            }).setType(select).build();
        }
        timeNightDuraStart.show();
    }

    private void clickNightDuraStop() {
        if (timeNightDuraStop == null) {
            //时间选择器
            boolean[] select = {false, false, false, true, true, false};
            timeNightDuraStop = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    String chooseHour;
                    String chooseMin;

                    int hours = date.getHours();
                    if (hours < 10) {
                        chooseHour = "0" + hours;
                    } else {
                        chooseHour = "" + hours;
                    }

                    int minutes = date.getMinutes();
                    if (minutes < 10) {
                        chooseMin = "0" + minutes;
                    } else {
                        chooseMin = "" + minutes;
                    }
                    sleepNightDuraStop = chooseHour + ":" + chooseMin;
                    tv_sleepNightDuraStop.setText(sleepNightDuraStop);
                }
            }).setType(select).build();
        }
        timeNightDuraStop.show();
    }

    private void clickSave() {
        showProgressDialog();
        String timestamp = System.currentTimeMillis() + "";
        String ltoken = UrlUtils.getLtoken(sessionId, timestamp);
        OkGo.<CreateSession>get(UrlUtils.setAlarm)
                .params("sessionId", sessionId)
                .params("timestamp", timestamp)
                .params("ltoken", ltoken)
                .params("mac", UrlUtils.MAC)
                .params("hrAlarmEnable", hrAlarmEnable)
                .params("hrAlarmMax", hrAlarmMax)
                .params("hrAlarmMin", hrAlarmMin)
                .params("rrAlarmEnable", rrAlarmEnable)
                .params("rrStopEnable", rrStopEnable)
                .params("rrAlarmMax", rrAlarmMax)
                .params("rrAlarmMin", rrAlarmMin)
                .params("offBedEnable", offBedEnable)
                .params("leaveDura", leaveDura)
                .params("sleepDayDuraStart", sleepDayDuraStart)
                .params("sleepDayDuraStop", sleepDayDuraStop)
                .params("sleepAllDayEnable", sleepAllDayEnable)
                .params("sleepNightDuraStart", sleepNightDuraStart)
                .params("sleepNightDuraStop", sleepNightDuraStop)
                .params("noonSleepDayEnable", noonSleepDayEnable)
                .params("nightSleepDayEnable", nightSleepDayEnable)
                .tag(this)//
                .execute(new JsonCallback<CreateSession>() {
                    @Override
                    public void onSuccess(Response<CreateSession> response) {
                        String code = response.body().getCode();
                        String msg = response.body().getMsg();
                        if (code.equals("0000")) {
                            Y.t("设置成功");
                             PreferenceHelper.getInstance(mContext).putString("hrAlarmEnable", hrAlarmEnable);
                              PreferenceHelper.getInstance(mContext).putString("hrAlarmMax", hrAlarmMax);
                              PreferenceHelper.getInstance(mContext).putString("hrAlarmMin", hrAlarmMin);

                              PreferenceHelper.getInstance(mContext).putString("rrAlarmEnable", rrAlarmEnable);
                              PreferenceHelper.getInstance(mContext).putString("rrStopEnable", rrStopEnable);
                              PreferenceHelper.getInstance(mContext).putString("rrAlarmMax", rrAlarmMax);
                              PreferenceHelper.getInstance(mContext).putString("rrAlarmMin", rrAlarmMin);

                              PreferenceHelper.getInstance(mContext).putString("offBedEnable", offBedEnable);
                              PreferenceHelper.getInstance(mContext).putString("leaveDura", leaveDura);

                              PreferenceHelper.getInstance(mContext).putString("sleepAllDayEnable", sleepAllDayEnable);
                              PreferenceHelper.getInstance(mContext).putString("noonSleepDayEnable", noonSleepDayEnable);
                              PreferenceHelper.getInstance(mContext).putString("sleepDayDuraStart", sleepDayDuraStart);
                              PreferenceHelper.getInstance(mContext).putString("sleepDayDuraStop", sleepDayDuraStop);
                              PreferenceHelper.getInstance(mContext).putString("nightSleepDayEnable", nightSleepDayEnable);
                              PreferenceHelper.getInstance(mContext).putString("sleepNightDuraStart",sleepNightDuraStart);
                              PreferenceHelper.getInstance(mContext).putString("sleepNightDuraStop", sleepNightDuraStop);
                        } else {
                            Y.t(msg);
                        }
                    }

                    @Override
                    public void onError(Response<CreateSession> response) {
                        super.onError(response);
                        Y.t("网络异常");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }
}
