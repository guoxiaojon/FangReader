package com.example.jon.fangreader.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by jon on 2017/2/21.
 */

public class DrawableCenterButton extends TextView {
    public DrawableCenterButton(Context context) {
        super(context);
    }

    public DrawableCenterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if(drawables !=null){
            Drawable drawableLeft = drawables[0];
            if(drawableLeft != null){
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawWidth = drawableLeft.getIntrinsicWidth();
                float width = drawWidth + drawablePadding + textWidth;
                float shift = (getWidth()-width)/2;
                canvas.translate(shift,0);
            }
        }

        super.onDraw(canvas);
    }
}
