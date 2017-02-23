package com.example.jon.fangreader.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.ui.activity.SearchActivity;
import com.orhanobut.logger.Logger;

/**
 * Created by jon on 2017/2/15.
 */

public class BookContentTextView extends TextView {

    public BookContentTextView(Context context) {
        super(context);
    }

    public BookContentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookContentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String text){
        setText("",null);
        for(;;){
            int start = text.indexOf("《");
            int end = text.indexOf("》");
            if(start < 0 || end < 0){
                append(text);
                break;
            }
            SpannableString spannableString = new SpannableString(text.substring(start,end+1));
            spannableString.setSpan(new TextClickSpan(spannableString.toString()),0,end-start+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            append(spannableString);
            setMovementMethod(LinkMovementMethod.getInstance());
            text = text.substring(end+1);
            Logger.e("spannableString : "+spannableString);
        }
    }

    class TextClickSpan extends ClickableSpan{
        private String name;

        public TextClickSpan(String name){
            this.name = name;
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(ContextCompat.getColor(getContext(), R.color.light_coffee));
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View view) {
            name.replace("《","");
            name.replace("》","");
            SearchActivity.jumpToMe(name);

        }
    }
}
