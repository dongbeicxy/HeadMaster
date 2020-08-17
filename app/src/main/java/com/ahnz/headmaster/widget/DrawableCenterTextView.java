package com.ahnz.headmaster.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author xzb
 * @description: drawableLeft与文本一起居中显示
 * @date :2020/7/10 15:21
 * //https://blog.csdn.net/shenshibaoma/article/details/53405524
 */
@SuppressLint("AppCompatCustomView")
public class DrawableCenterTextView extends TextView {

    public DrawableCenterTextView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context) {
        super(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (null != drawables) {
            Drawable drawableLeft = drawables[0];
            Drawable drawableRight = drawables[2];
            float textWidth = getPaint().measureText(getText().toString());
            if (null != drawableLeft) {
                setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                float contentWidth = textWidth + getCompoundDrawablePadding() + drawableLeft.getIntrinsicWidth();
                if (getWidth() - contentWidth > 0) {
                    canvas.translate((getWidth() - contentWidth - getPaddingRight() - getPaddingLeft()) / 2, 0);
                }
            }
            if (null != drawableRight) {
                setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                float contentWidth = textWidth + getCompoundDrawablePadding() + drawableRight.getIntrinsicWidth();
                if (getWidth() - contentWidth > 0) {
                    canvas.translate(-(getWidth() - contentWidth - getPaddingRight() - getPaddingLeft()) / 2, 0);
                }
            }
            if (null == drawableRight && null == drawableLeft) {
                setGravity(Gravity.CENTER);
            }
        }
        super.onDraw(canvas);
    }
}