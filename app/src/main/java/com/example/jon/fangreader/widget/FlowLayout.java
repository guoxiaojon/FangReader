package com.example.jon.fangreader.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 2017/2/20.
 */

public class FlowLayout extends ViewGroup {
    private List<List<View>> mAllViews = new ArrayList<>();
    private List<Integer> mLineHeight = new ArrayList<>();

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //每行的宽度
        int lineWidth = 0;
        //每行的高度
        int lineHeight = 0;

        //测量的
        int width = 0;
        int height = 0;

        int childSize = getChildCount();
        for(int i = 0;i < childSize;i++){

            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth()+ lp.leftMargin+ lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if( childWidth + lineWidth > sizeWidth){
                //一行容不下了
                width = Math.max(width,lineWidth);
                height += lineHeight;
                lineHeight = childHeight;
                lineWidth = childWidth;

            }else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight,childHeight);
            }

            if(i == childSize - 1){
                width = Math.max(width,lineWidth);
                height += lineHeight;
            }



        }
        Log.d("data4",">>>>"+height);
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth:width,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight: height);




    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        mAllViews.clear();
        mLineHeight.clear();

        int childSize = getChildCount();
        int lineWidth = 0;
        int lineHeight = 0;
        int width = getWidth();

        List<View> lineViews = new ArrayList<>();
        for(int i = 0; i<childSize; i++){

            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if(lineWidth + childWidth > width){
                //换行
                mAllViews.add(lineViews);
                mLineHeight.add(lineHeight);
                lineViews = new ArrayList<>();
                lineHeight = childHeight;
                lineWidth = childWidth;

            }else {
                lineViews.add(child);
                lineHeight = Math.max(lineHeight,childHeight);
                lineWidth += childWidth;
            }

            if(i == childSize - 1){
                mAllViews.add(lineViews);
                mLineHeight.add(lineHeight);
            }

        }
        int topShift = 0;
        int leftShift = 0;
        for(int i = 0;i<mAllViews.size();i++ ){
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);
            for(int j = 0;j<lineViews.size();j++){
                View child = lineViews.get(j);
                MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
                child.layout(leftShift,topShift,leftShift + child.getMeasuredWidth(),topShift + child.getMeasuredHeight());
                leftShift += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            }
            leftShift = 0 ;
            topShift += lineHeight;


        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }


}
