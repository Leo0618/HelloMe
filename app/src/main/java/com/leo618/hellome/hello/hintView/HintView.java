package com.leo618.hellome.hello.hintView;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.leo618.hellome.libcore.util.Logg;
import com.leo618.hellome.libcore.util.UIUtil;

/**
 * function:提示控件(例如消息红点提示)
 *
 * <p></p>
 * Created by lzj on 2016/11/17.
 */

@SuppressWarnings("unused")
public class HintView {
    private static final String TAG = "HintView";

    private int hintViewSize = UIUtil.dip2px(15);
    private int hintViewTextSize = UIUtil.dip2px(12);
    private int hintViewBgColor = Color.RED;
    private int hintViewTextColor = Color.WHITE;
    private int hintViewGravity = Gravity.TOP | Gravity.END;
    private int hintViewLeftMargin, hintViewTopMargin, hintViewRightMargin, hintViewBottomMargin;
    private String content = "";

    public HintView setHintViewSize(int size) {
        this.hintViewSize = size;
        return this;
    }

    public HintView setHintViewTextSize(int size) {
        this.hintViewTextSize = size;
        return this;
    }

    public HintView setHintViewBgColor(int color) {
        this.hintViewBgColor = color;
        return this;
    }

    public HintView setHintViewTextColor(int color) {
        this.hintViewTextColor = color;
        return this;
    }

    public HintView setHintViewGravity(int gravity) {
        this.hintViewGravity = gravity;
        return this;
    }

    public HintView setHintViewMargin(int margin) {
        this.hintViewLeftMargin = margin;
        this.hintViewTopMargin = margin;
        this.hintViewRightMargin = margin;
        this.hintViewBottomMargin = margin;
        return this;
    }

    public HintView setHintViewMargin(int left, int top, int right, int bottom) {
        this.hintViewLeftMargin = left;
        this.hintViewTopMargin = top;
        this.hintViewRightMargin = right;
        this.hintViewBottomMargin = bottom;
        return this;
    }

    public HintView setContent(String content) {
        this.content = content;
        return this;
    }

    public void hint(final View targetView) {
        if (targetView == null) {
            Logg.w(TAG, "targetView is null");
            return;
        }
        if (targetView.getParent() == null) {
            Logg.w(TAG, "targetView getParent is null");
            return;
        }
        if (!(targetView.getParent() instanceof ViewGroup)) {
            Logg.w(TAG, "targetView parent is not a ViewGroup");
            return;
        }
        final ViewGroup targetViewParentView = (ViewGroup) targetView.getParent();
        targetView.post(new Runnable() {
            @Override
            public void run() {
                int index = targetViewParentView.indexOfChild(targetView);
                targetViewParentView.removeViewAt(index);

                FrameLayout containerLayout = new FrameLayout(targetView.getContext());
                containerLayout.setBackgroundColor(Color.TRANSPARENT);
                containerLayout.setLayoutParams(targetView.getLayoutParams());
                containerLayout.addView(targetView);

                TextView hintContentView = new TextView(targetView.getContext());
                hintContentView.setBackground(hintViewBg());
                hintContentView.setTextColor(hintViewTextColor);
                hintContentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintViewTextSize);
                hintContentView.setGravity(Gravity.CENTER);
                hintContentView.setText(content);
                hintContentView.setPadding(4, 1, 4, 1);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = hintViewGravity;
                layoutParams.leftMargin = hintViewLeftMargin;
                layoutParams.topMargin = hintViewTopMargin;
                layoutParams.rightMargin = hintViewRightMargin;
                layoutParams.bottomMargin = hintViewBottomMargin;
                hintContentView.setLayoutParams(layoutParams);
                containerLayout.addView(hintContentView);

                targetViewParentView.addView(containerLayout, index);
            }
        });
    }

    private GradientDrawable hintViewBg() {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setUseLevel(false);
        shape.setCornerRadius(hintViewTextSize);
        shape.setColor(hintViewBgColor);
        shape.setSize(hintViewSize, hintViewSize);
        return shape;
    }
//    private GradientDrawable hintViewBg() {
//        GradientDrawable shape = new GradientDrawable();
//        shape.setShape(GradientDrawable.OVAL);
//        shape.setUseLevel(false);
//        shape.setColor(hintViewBgColor);
//        shape.setSize(hintViewSize, hintViewSize);
//        return shape;
//    }
}
