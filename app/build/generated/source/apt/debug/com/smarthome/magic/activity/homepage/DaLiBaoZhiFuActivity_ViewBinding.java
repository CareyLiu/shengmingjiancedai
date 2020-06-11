// Generated code from Butter Knife. Do not modify!
package com.smarthome.magic.activity.homepage;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.flyco.roundview.RoundTextView;
import com.smarthome.magic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DaLiBaoZhiFuActivity_ViewBinding implements Unbinder {
  private DaLiBaoZhiFuActivity target;

  @UiThread
  public DaLiBaoZhiFuActivity_ViewBinding(DaLiBaoZhiFuActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DaLiBaoZhiFuActivity_ViewBinding(DaLiBaoZhiFuActivity target, View source) {
    this.target = target;

    target.ivImage = Utils.findRequiredViewAsType(source, R.id.iv_image, "field 'ivImage'", ImageView.class);
    target.tvText = Utils.findRequiredViewAsType(source, R.id.tv_text, "field 'tvText'", TextView.class);
    target.tvPrice = Utils.findRequiredViewAsType(source, R.id.tv_price, "field 'tvPrice'", TextView.class);
    target.tvChooseZhifufangshi = Utils.findRequiredViewAsType(source, R.id.tv_choose_zhifufangshi, "field 'tvChooseZhifufangshi'", TextView.class);
    target.ivIcon1 = Utils.findRequiredViewAsType(source, R.id.iv_icon_1, "field 'ivIcon1'", ImageView.class);
    target.ivWeixinChoose = Utils.findRequiredViewAsType(source, R.id.iv_weixin_choose, "field 'ivWeixinChoose'", ImageView.class);
    target.view = Utils.findRequiredView(source, R.id.view, "field 'view'");
    target.ivIcon2 = Utils.findRequiredViewAsType(source, R.id.iv_icon_2, "field 'ivIcon2'", ImageView.class);
    target.ivZhifubaoChoose = Utils.findRequiredViewAsType(source, R.id.iv_zhifubao_choose, "field 'ivZhifubaoChoose'", ImageView.class);
    target.frtvPay = Utils.findRequiredViewAsType(source, R.id.frtv_pay, "field 'frtvPay'", RoundTextView.class);
    target.viewWeixin = Utils.findRequiredView(source, R.id.view_weixin, "field 'viewWeixin'");
    target.viewZhifubao = Utils.findRequiredView(source, R.id.view_zhifubao, "field 'viewZhifubao'");
  }

  @Override
  @CallSuper
  public void unbind() {
    DaLiBaoZhiFuActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivImage = null;
    target.tvText = null;
    target.tvPrice = null;
    target.tvChooseZhifufangshi = null;
    target.ivIcon1 = null;
    target.ivWeixinChoose = null;
    target.view = null;
    target.ivIcon2 = null;
    target.ivZhifubaoChoose = null;
    target.frtvPay = null;
    target.viewWeixin = null;
    target.viewZhifubao = null;
  }
}
