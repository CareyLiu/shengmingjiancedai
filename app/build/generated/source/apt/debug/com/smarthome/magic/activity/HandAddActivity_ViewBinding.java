// Generated code from Butter Knife. Do not modify!
package com.smarthome.magic.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.smarthome.magic.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HandAddActivity_ViewBinding implements Unbinder {
  private HandAddActivity target;

  private View view7f09009d;

  @UiThread
  public HandAddActivity_ViewBinding(HandAddActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HandAddActivity_ViewBinding(final HandAddActivity target, View source) {
    this.target = target;

    View view;
    target.mEtNumber = Utils.findRequiredViewAsType(source, R.id.et_number, "field 'mEtNumber'", EditText.class);
    view = Utils.findRequiredView(source, R.id.bt_submit, "field 'mBtSubmit' and method 'onClick'");
    target.mBtSubmit = Utils.castView(view, R.id.bt_submit, "field 'mBtSubmit'", Button.class);
    view7f09009d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    HandAddActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mEtNumber = null;
    target.mBtSubmit = null;

    view7f09009d.setOnClickListener(null);
    view7f09009d = null;
  }
}
