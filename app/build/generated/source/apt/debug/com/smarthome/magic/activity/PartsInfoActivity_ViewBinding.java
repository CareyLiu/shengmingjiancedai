// Generated code from Butter Knife. Do not modify!
package com.smarthome.magic.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.smarthome.magic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PartsInfoActivity_ViewBinding implements Unbinder {
  private PartsInfoActivity target;

  private View view7f090074;

  private View view7f0901af;

  private View view7f0901b5;

  private View view7f0901b0;

  private View view7f0901a1;

  private View view7f09019d;

  private View view7f09019f;

  private View view7f0901b8;

  private View view7f0901b1;

  private View view7f0901a7;

  private View view7f0901ae;

  private View view7f0901b4;

  @UiThread
  public PartsInfoActivity_ViewBinding(PartsInfoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PartsInfoActivity_ViewBinding(final PartsInfoActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.back, "field 'mBack' and method 'onClick'");
    target.mBack = Utils.castView(view, R.id.back, "field 'mBack'", LinearLayout.class);
    view7f090074 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.item_plug, "field 'mItemPlug' and method 'onClick'");
    target.mItemPlug = Utils.castView(view, R.id.item_plug, "field 'mItemPlug'", LinearLayout.class);
    view7f0901af = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.item_sensor, "field 'mItemSensor' and method 'onClick'");
    target.mItemSensor = Utils.castView(view, R.id.item_sensor, "field 'mItemSensor'", LinearLayout.class);
    view7f0901b5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.item_pump, "field 'mItemPump' and method 'onClick'");
    target.mItemPump = Utils.castView(view, R.id.item_pump, "field 'mItemPump'", LinearLayout.class);
    view7f0901b0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.item_fan, "field 'mItemFan' and method 'onClick'");
    target.mItemFan = Utils.castView(view, R.id.item_fan, "field 'mItemFan'", LinearLayout.class);
    view7f0901a1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.item_burner, "field 'mItemBurner' and method 'onClick'");
    target.mItemBurner = Utils.castView(view, R.id.item_burner, "field 'mItemBurner'", LinearLayout.class);
    view7f09019d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.item_control, "field 'mItemControl' and method 'onClick'");
    target.mItemControl = Utils.castView(view, R.id.item_control, "field 'mItemControl'", LinearLayout.class);
    view7f09019f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.item_switch, "field 'mItemSwitch' and method 'onClick'");
    target.mItemSwitch = Utils.castView(view, R.id.item_switch, "field 'mItemSwitch'", LinearLayout.class);
    view7f0901b8 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.item_radiator, "field 'mItemRadiator' and method 'onClick'");
    target.mItemRadiator = Utils.castView(view, R.id.item_radiator, "field 'mItemRadiator'", LinearLayout.class);
    view7f0901b1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.item_line, "field 'mItemLine' and method 'onClick'");
    target.mItemLine = Utils.castView(view, R.id.item_line, "field 'mItemLine'", LinearLayout.class);
    view7f0901a7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.item_plastic, "field 'mItemPlastic' and method 'onClick'");
    target.mItemPlastic = Utils.castView(view, R.id.item_plastic, "field 'mItemPlastic'", LinearLayout.class);
    view7f0901ae = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.item_rubber, "field 'mItemRubber' and method 'onClick'");
    target.mItemRubber = Utils.castView(view, R.id.item_rubber, "field 'mItemRubber'", LinearLayout.class);
    view7f0901b4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.mTvPlug = Utils.findRequiredViewAsType(source, R.id.tv_plug, "field 'mTvPlug'", TextView.class);
    target.mTvSensor = Utils.findRequiredViewAsType(source, R.id.tv_sensor, "field 'mTvSensor'", TextView.class);
    target.mTvPump = Utils.findRequiredViewAsType(source, R.id.tv_pump, "field 'mTvPump'", TextView.class);
    target.mTvFan = Utils.findRequiredViewAsType(source, R.id.tv_fan, "field 'mTvFan'", TextView.class);
    target.mTvBurner = Utils.findRequiredViewAsType(source, R.id.tv_burner, "field 'mTvBurner'", TextView.class);
    target.mTvBoard = Utils.findRequiredViewAsType(source, R.id.tv_board, "field 'mTvBoard'", TextView.class);
    target.mTvSwitch = Utils.findRequiredViewAsType(source, R.id.tv_switch, "field 'mTvSwitch'", TextView.class);
    target.mTvHeat = Utils.findRequiredViewAsType(source, R.id.tv_heat, "field 'mTvHeat'", TextView.class);
    target.mTvLine = Utils.findRequiredViewAsType(source, R.id.tv_line, "field 'mTvLine'", TextView.class);
    target.mTvShell = Utils.findRequiredViewAsType(source, R.id.tv_shell, "field 'mTvShell'", TextView.class);
    target.mTvRubber = Utils.findRequiredViewAsType(source, R.id.tv_rubber, "field 'mTvRubber'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PartsInfoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mBack = null;
    target.mItemPlug = null;
    target.mItemSensor = null;
    target.mItemPump = null;
    target.mItemFan = null;
    target.mItemBurner = null;
    target.mItemControl = null;
    target.mItemSwitch = null;
    target.mItemRadiator = null;
    target.mItemLine = null;
    target.mItemPlastic = null;
    target.mItemRubber = null;
    target.mTvPlug = null;
    target.mTvSensor = null;
    target.mTvPump = null;
    target.mTvFan = null;
    target.mTvBurner = null;
    target.mTvBoard = null;
    target.mTvSwitch = null;
    target.mTvHeat = null;
    target.mTvLine = null;
    target.mTvShell = null;
    target.mTvRubber = null;

    view7f090074.setOnClickListener(null);
    view7f090074 = null;
    view7f0901af.setOnClickListener(null);
    view7f0901af = null;
    view7f0901b5.setOnClickListener(null);
    view7f0901b5 = null;
    view7f0901b0.setOnClickListener(null);
    view7f0901b0 = null;
    view7f0901a1.setOnClickListener(null);
    view7f0901a1 = null;
    view7f09019d.setOnClickListener(null);
    view7f09019d = null;
    view7f09019f.setOnClickListener(null);
    view7f09019f = null;
    view7f0901b8.setOnClickListener(null);
    view7f0901b8 = null;
    view7f0901b1.setOnClickListener(null);
    view7f0901b1 = null;
    view7f0901a7.setOnClickListener(null);
    view7f0901a7 = null;
    view7f0901ae.setOnClickListener(null);
    view7f0901ae = null;
    view7f0901b4.setOnClickListener(null);
    view7f0901b4 = null;
  }
}
