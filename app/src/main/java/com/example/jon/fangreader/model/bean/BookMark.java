package com.example.jon.fangreader.model.bean;

import java.io.Serializable;

/**
 * Created by jon on 2017/2/12.
 */

public class BookMark implements Serializable{
    public int getmChapterIndex() {
        return mChapterIndex;
    }

    public void setmChapterIndex(int mChapterIndex) {
        this.mChapterIndex = mChapterIndex;
    }

    private int mChapterIndex;
    private int mBeginIndex;
    private int mEndIndex;
    private String mDescription;

    public BookMark(int mChapterIndex,int mBeginIndex, int mEndIndex, String mDescription) {
        this.mChapterIndex = mChapterIndex;
        this.mBeginIndex = mBeginIndex;
        this.mEndIndex = mEndIndex;
        this.mDescription = mDescription;
    }

    public int getmBeginIndex() {
        return mBeginIndex;
    }

    public void setmBeginIndex(int mBeginIndex) {
        this.mBeginIndex = mBeginIndex;
    }

    public int getmEndIndex() {
        return mEndIndex;
    }

    public void setmEndIndex(int mEndIndex) {
        this.mEndIndex = mEndIndex;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BookMark){
            return mBeginIndex == ((BookMark)obj).getmBeginIndex();
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return mDescription;
    }
}
