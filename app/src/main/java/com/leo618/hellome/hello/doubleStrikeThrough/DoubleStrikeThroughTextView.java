package com.leo618.hellome.hello.doubleStrikeThrough;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * function:带删除线TextView
 *
 * <p></p>
 * Created by lzj on 2016/11/16.
 */
@SuppressWarnings("unused")
public class DoubleStrikeThroughTextView extends TextView {
    private boolean mEnable;
    private int mLineNumber = 2;
    private int mLineColor = Color.parseColor("#9E9D9D");
    private int mLineHeigth = dip2px(2);
    private float mLineSpace = -1;

    public DoubleStrikeThroughTextView(Context context) {
        super(context);
    }

    public DoubleStrikeThroughTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DoubleStrikeThroughTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DoubleStrikeThroughTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 设置删除线条目数量
     *
     * @param number 线条数量
     */
    public DoubleStrikeThroughTextView setStrikeThroughNumber(int number) {
        this.mLineNumber = number;
        return this;
    }

    /**
     * 设置删除线条高度
     *
     * @param heigth 线条高度
     */
    public DoubleStrikeThroughTextView setStrikeThroughHeigth(int heigth) {
        this.mLineHeigth = heigth;
        return this;
    }

    /**
     * 设置删除线条之间的间距
     *
     * @param space 线条之间的间距
     */
    public DoubleStrikeThroughTextView setStrikeThroughSpace(int space) {
        this.mLineSpace = space;
        return this;
    }

    /**
     * 设置删除线条颜色
     *
     * @param color 线条颜色
     */
    public DoubleStrikeThroughTextView setStrikeThroughColor(int color) {
        this.mLineColor = color;
        return this;
    }

    /**
     * 设置文本
     *
     * @param text                      文本内容
     * @param enableDoubleStrikeThrough 是否启用删除线 默认不启用
     */
    public void setText(CharSequence text, boolean enableDoubleStrikeThrough) {
        setText(text);
        this.mEnable = enableDoubleStrikeThrough;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mEnable) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(mLineColor);

            // we give a default value,can be optimized
            if (mLineSpace == -1) {
                mLineSpace = mLineHeigth * 2;
            }

            // half heigth of TextView
            float halfView = getHeight() * 1.0f / 2;
            // half heigth of all lines
            float halfLines = (mLineNumber * (mLineHeigth + mLineSpace) * 1.0f - mLineSpace) / 2;
            // draw lines
            for (int x = 0; x < mLineNumber; x++) {
                float top = halfView - halfLines + (x * (mLineHeigth + mLineSpace));
                canvas.drawRect(new RectF(0, top, getWidth(), top + mLineHeigth), paint);
            }
        }
    }

    private int dip2px(float dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

}
