package com.smarthome.magic.activity.wode_page.bazinew;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.wode_page.bazinew.base.BaziBaseActivity;
import com.smarthome.magic.activity.wode_page.bazinew.dialog.JiesuoDialog;
import com.smarthome.magic.activity.wode_page.bazinew.model.DanganModel;
import com.smarthome.magic.activity.wode_page.bazinew.model.YunshiModel;
import com.smarthome.magic.activity.wode_page.bazinew.utils.BaziCode;
import com.smarthome.magic.activity.wode_page.bazinew.utils.TimeUtils;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YunshiActivity extends BaziBaseActivity {

    @BindView(R.id.tv_name_sex)
    TextView tv_name_sex;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
    @BindView(R.id.tv_data_jiexi)
    TextView tv_data_jiexi;
    @BindView(R.id.tv_yunshi)
    TextView tv_yunshi;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.ll_jiesuo)
    LinearLayout ll_jiesuo;
    @BindView(R.id.iv_left)
    ImageView iv_left;
    @BindView(R.id.iv_right)
    ImageView iv_right;
    @BindView(R.id.tv_select_data)
    TextView tv_select_data;

    private int code;
    private String mingpan_id;
    private String ex_type;
    private String ex_factor;
    private int year;
    private int month;
    private int day;
    private TimePickerView timePicker;

    @Override
    public int getContentViewResId() {
        return R.layout.bazi_activity_yunshi;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("运势");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        code = getIntent().getIntExtra("code", 0);
        mingpan_id = getIntent().getStringExtra("mingpan_id");

        if (code == BaziCode.ST_nian) {
            tv_title.setText("流年运势");
            tv_yunshi.setText("流年运势");
            ex_type = "2";
            year = TimeUtils.getYear();
            ex_factor = "" + year;

            tv_select_data.setText(year + "");
        } else if (code == BaziCode.ST_yue) {
            tv_title.setText("流月运势");
            tv_yunshi.setText("流月运势");
            ex_type = "3";
            year = TimeUtils.getYear();
            month = TimeUtils.getMonth();

            String monthS = "";
            if (month >= 10) {
                monthS = "" + month;
            } else {
                monthS = "0" + month;
            }
            ex_factor = "" + year + monthS;
            tv_select_data.setText(year + "-" + monthS);
        } else if (code == BaziCode.ST_ri) {
            tv_title.setText("流日运势");
            tv_yunshi.setText("流日运势");
            ex_type = "4";
            year = TimeUtils.getYear();
            month = TimeUtils.getMonth();
            day = TimeUtils.getDay();

            String monthS = "";
            String dayS = "";
            if (month >= 10) {
                monthS = "" + month;
            } else {
                monthS = "0" + month;
            }
            if (day >= 10) {
                dayS = "" + day;
            } else {
                dayS = "0" + day;
            }
            ex_factor = "" + year + monthS + dayS;
            tv_select_data.setText(year + "-" + monthS + "-" + dayS);
        } else if (code == BaziCode.ST_chuanyi) {

        }

        getNet();
    }


    public void getNet() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "11018");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("mingpan_id", mingpan_id);
        map.put("ex_type", ex_type);
        map.put("ex_factor", ex_factor);
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<YunshiModel.DataBean>>post(Urls.BAZIAPP)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<YunshiModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<YunshiModel.DataBean>> response) {
                        List<YunshiModel.DataBean> data = response.body().data;
                        if (data != null && data.size() > 0) {
                            YunshiModel.DataBean bean = data.get(0);
                            tv_name_sex.setText(bean.getName() + "  " + bean.getSex());
                            tv_birthday.setText(bean.getBirthday());
                            tv_data_jiexi.setText(bean.getTime_text());

                            String lock = bean.getLock();
                            if ("1".equals(lock)) {
                                tv_content.setVisibility(View.VISIBLE);
                                ll_jiesuo.setVisibility(View.GONE);
                                tv_content.setText(bean.getEx_text());
                            } else {
                                tv_content.setVisibility(View.GONE);
                                ll_jiesuo.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<YunshiModel.DataBean>> response) {
                        super.onError(response);
                        tv_content.setVisibility(View.GONE);
                        ll_jiesuo.setVisibility(View.GONE);
                        String msg = response.getException().getMessage();
                        String[] msgToast = msg.split("：");
                        if (msgToast.length == 3) {
                            t(msgToast[2]);
                        }
                    }
                });
    }

    @OnClick({R.id.ll_jiesuo, R.id.iv_left, R.id.iv_right, R.id.tv_select_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_jiesuo:
                clickJiesuo();
                break;
            case R.id.iv_left:
                clickLeft();
                break;
            case R.id.iv_right:
                clickRight();
                break;
            case R.id.tv_select_data:
                selectData();
                break;
        }
    }

    private void clickJiesuo() {
        if (code == BaziCode.ST_nian || code == BaziCode.ST_yue) {
            JiesuoDialog dialog = new JiesuoDialog(this, 0);
            dialog.setPayClick(new JiesuoDialog.JieSuoPayClick() {
                @Override
                public void payCi() {

                }

                @Override
                public void payNian() {

                }
            });
            dialog.show();
        } else if (code == BaziCode.ST_ri) {
            JiesuoDialog dialog = new JiesuoDialog(this, 1);
            dialog.setPayClick(new JiesuoDialog.JieSuoPayClick() {
                @Override
                public void payCi() {

                }

                @Override
                public void payNian() {

                }
            });
            dialog.show();
        }
    }

    private void selectData() {
        if (timePicker == null) {
            boolean[] time = {false, false, false, false, false, false};
            if (code == BaziCode.ST_nian) {
                time[0] = true;
            } else if (code == BaziCode.ST_yue) {
                time[0] = true;
                time[1] = true;
            } else if (code == BaziCode.ST_ri) {
                time[0] = true;
                time[1] = true;
                time[2] = true;
            }

            //时间选择器
            timePicker = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {

                    Calendar instance = Calendar.getInstance();
                    instance.setTime(date);
                    if (code == BaziCode.ST_nian) {
                        year = instance.get(Calendar.YEAR);

                        tv_select_data.setText(TimeUtils.getData(date, "yyyy"));
                        ex_factor = TimeUtils.getData(date, "yyyy");
                    } else if (code == BaziCode.ST_yue) {
                        year = instance.get(Calendar.YEAR);
                        month = instance.get(Calendar.MONTH) + 1;

                        tv_select_data.setText(TimeUtils.getData(date, "yyyy-MM"));
                        ex_factor = TimeUtils.getData(date, "yyyyMM");
                    } else if (code == BaziCode.ST_ri) {
                        year = instance.get(Calendar.YEAR);
                        month = instance.get(Calendar.MONTH) + 1;
                        day = instance.get(Calendar.DATE);

                        tv_select_data.setText(TimeUtils.getData(date));
                        ex_factor = TimeUtils.getData(date, "yyyyMMdd");
                    }
                    getNet();
                }
            }).setType(time).build();
        }
        timePicker.show();
    }

    private void clickRight() {
        if (code == BaziCode.ST_nian) {
            year++;
            ex_factor = "" + year;
            tv_select_data.setText("" + year);
        } else if (code == BaziCode.ST_yue) {
            if (month < 12) {
                month++;
            } else {
                month = 1;
                year++;
            }

            String monthS = "";
            if (month >= 10) {
                monthS = "" + month;
            } else {
                monthS = "0" + month;
            }
            ex_factor = "" + year + monthS;
            tv_select_data.setText(year + "-" + monthS);
        } else if (code == BaziCode.ST_ri) {
            int nowDay = TimeUtils.getMonthLastDay(year, month);
            if (day < nowDay) {
                day++;
            } else {
                if (month < 12) {
                    month++;
                } else {
                    year++;
                    month = 1;
                }
                day = 1;
            }

            String monthS = "";
            String dayS = "";
            if (month >= 10) {
                monthS = "" + month;
            } else {
                monthS = "0" + month;
            }
            if (day >= 10) {
                dayS = "" + day;
            } else {
                dayS = "0" + day;
            }
            ex_factor = "" + year + monthS + dayS;
            tv_select_data.setText(year + "-" + monthS + "-" + dayS);
        }

        getNet();
    }

    private void clickLeft() {
        if (code == BaziCode.ST_nian) {
            year--;
            ex_factor = "" + year;
            tv_select_data.setText("" + year);
        } else if (code == BaziCode.ST_yue) {
            if (month > 1) {
                month--;
            } else {
                month = 12;
                year--;
            }

            String monthS = "";
            if (month >= 10) {
                monthS = "" + month;
            } else {
                monthS = "0" + month;
            }
            ex_factor = "" + year + monthS;
            tv_select_data.setText(year + "-" + monthS);
        } else if (code == BaziCode.ST_ri) {
            if (day > 1) {
                day--;
            } else {
                if (month > 1) {
                    month--;
                } else {
                    year--;
                    month = 12;
                }
                day = TimeUtils.getMonthLastDay(year, month);
            }

            String monthS = "";
            String dayS = "";
            if (month >= 10) {
                monthS = "" + month;
            } else {
                monthS = "0" + month;
            }
            if (day >= 10) {
                dayS = "" + day;
            } else {
                dayS = "0" + day;
            }
            ex_factor = "" + year + monthS + dayS;
            tv_select_data.setText(year + "-" + monthS + "-" + dayS);
        }

        getNet();
    }
}
