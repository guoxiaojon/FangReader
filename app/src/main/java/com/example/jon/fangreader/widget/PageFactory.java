package com.example.jon.fangreader.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.jon.fangreader.R;
import com.example.jon.fangreader.component.ACache;
import com.example.jon.fangreader.model.bean.BookTocBean;
import com.example.jon.fangreader.utils.ReaderUtil;
import com.example.jon.fangreader.utils.SharePreferenceUtil;
import com.example.jon.fangreader.utils.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jon on 2017/1/6.
 */

public class PageFactory {
    public static final int OPEN_BOOK_SUCCESSFUL = 0x1;
    public static final int OPEN_BOOK_FAIL = 0x2;

    private MappedByteBuffer mBuffer;//内存映射
    private int mEndIndex;//当前页Byte结束指针
    private int mBeginIndex;//当前页Byte起始指针
    //临时指针
    private int mTempBeginIndex;
    private int mTempEndIndex;
    //临时内容保存
    private List<String> mTempLines;
    private long mBufferLenght;
    private Paint mPaint;
    /**
     * 行间距
     */
    private int mLineSpace;
    /**
     * 字间距
     */
    //private int mMarginWidth;
    /**
     * 字体大小
     */
    private int mFontSize;
    /**
     * 页面边沿留白
     */
    private int mPageTopPadding;
    private int mPageBottomPadding;
    private int mPageLeftPadding;
    private int mPageRightPadding;
    private int mVisiableWidth;
    private int mVisiableHeight;
    private int mLinesOfPerPage;//一页多少行
    private Bitmap mBackground;
    private List<BookTocBean.MixToc.Chapter> mChapters;




    private Context mContext;
    private int mHeight;//页面高度
    private int mWidth;//页面宽度
    private String mBookId;
    private String mTitle;//小说本章节题目
    private String mContent;//小说本章节内容
    private int mCurrChapter;
    private int mThemeId = R.color.readThemeBrown;

    public List<String> mLines;//将小说内容分割成若干行
    /**
     * "title":"074章 你还真变态"
     * ,"body":"“哥们！不用打电话了，我就是警察。”司机是一名四十多岁的中年人，听见杨洛的话疑惑的看向他。\n
     * 杨洛拿出证件扔给他，然后上了重卡。司机拿着杨洛的证件没有看，而是瞪着惊恐的眼睛看着自己的卡车开始慢慢向前行驶，轮胎碾压物体的“咔咔”声传入耳中。鲜红的血液在车轮下缓缓流出，有如溪水顺着路面微微的坡度流到他的脚下。随着最后两个轮胎碾压过后，那两辆别克已经成了铁饼。\n
     * "
     */



    public PageFactory(Context context, int width, int height, String bookId, List<BookTocBean.MixToc.Chapter> chapters) {
        this.mContext = context;
        this.mWidth = width;
        this.mHeight = height;
        this.mBookId = bookId;
        this.mChapters = chapters;
        this.mLineSpace = 8;
        this.mPaint = new Paint();
        this.mPaint.setColor(mContext.getResources().getColor(R.color.black));
        this.mPaint.setAntiAlias(true);
        this.mLines = new ArrayList<>();
        this.mTempLines = new ArrayList<>();
        this.mPageLeftPadding = 20;
        this.mPageRightPadding = 20;
        this.mPageTopPadding = 50;
        this.mPageBottomPadding = 20;
        this.mFontSize = SystemUtils.sp2px(mContext,16);


        initConfig();
    }

    public void initConfig(){
        this.mVisiableWidth = mWidth - mPageLeftPadding - mPageRightPadding;
        this.mVisiableHeight = mHeight - mPageTopPadding - mPageBottomPadding;
        this.mLinesOfPerPage = (mVisiableHeight-mLineSpace) / (mFontSize + mLineSpace);
        this.mPaint.setTextSize(mFontSize);
        Drawable drawable = mContext.getResources().getDrawable(R.mipmap.theme_leather_bg);
        BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
        this.mBackground = Bitmap.createBitmap(bitmapDrawable.getBitmap(),0,0,mWidth,mHeight);
//        mBackground.eraseColor(mContext.getResources().getColor(mThemeId));
        mBackground = ReaderUtil.getBackground(SharePreferenceUtil.getMode());
    }
    public void setBackground(int id){
        //this.mBackground.eraseColor(mContext.getResources().getColor(R.color.readThemeBrown));
        this.mBackground = ReaderUtil.getBackground(id);

    }
    public void setFontSize(int fontSize){
        this.mFontSize = fontSize;
        initConfig();

    }
    public void setTextColor(int color){
        mPaint.setColor(mContext.getResources().getColor(color));
    }
    public void setWidthAndHeight(int width,int height){
        this.mWidth = width;
        this.mHeight = height;
        initConfig();
    }



    /**
     * 是否有下一页
     */
    public boolean hasNext() {
        if(mEndIndex < mBufferLenght ||  mCurrChapter < mChapters.size()-1){
            return true;
        }
        return false;
    }

    /***
     * 是否有上一页
     */
    public boolean hasPre() {
        Log.d("data","mBeginIndex :"+mBeginIndex + "mCurrChapter "+mCurrChapter);
        if(mBeginIndex > 0 || mCurrChapter > 0){
            return true;
        }
        return false;
    }



    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBackground, 0, 0, null);

        float shiftOfY = mPageTopPadding;
        for (int i = 0; i < mLines.size(); i++) {
            canvas.drawText(mLines.get(i), mPageLeftPadding, shiftOfY, mPaint);
            shiftOfY = shiftOfY + mFontSize + mLineSpace;

        }

    }
    boolean mOpenNext = false;

    /**
     * 状态回滚
     */
    public void revert() {
        if(openNewChapter){
            Log.d("data","revert openNewChapter");
            if(mOpenNext){
                openBook(--mCurrChapter);
            }else {
                openBook(++mCurrChapter);
            }

        }
        Log.d("data","恢复的索引 "+ mTempBeginIndex + " : "+mTempEndIndex);
        mBeginIndex = mTempBeginIndex;
        mEndIndex = mTempEndIndex;
        pageCurr();

    }


    boolean openNewChapter = false;

    public boolean openBook(int[] readStatus){

        if(readStatus.length < 3)
            return false ;
        this.mCurrChapter = readStatus[0];
        mBeginIndex = readStatus[1];
        mEndIndex = readStatus[2];
        File chapterFile = null;
        if(mChapters.size() > 0){
            chapterFile = ACache.getChapterForRead(mBookId,mChapters.get(mCurrChapter).getLink());

        }

        if(chapterFile != null){
            try {
                mBuffer = new RandomAccessFile(chapterFile,"r")
                        .getChannel()
                        .map(FileChannel.MapMode.READ_ONLY,0,chapterFile.length());
                mBufferLenght = chapterFile.length();
                Log.d("data6","mBUfferLen :" +mBufferLenght);

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;


        }else {
            //本书无缓存
            Log.d("data6","本书无缓存"+mCurrChapter);
            return false;
        }


    }
    public boolean openBook(int chapterIndex){
        return openBook(new int[]{chapterIndex,0,0});

    }
    //读取当前页
    public void pageCurr(){
        mEndIndex = mBeginIndex;
        Log.d("data3","pageCurr()++"+mEndIndex);
        pageNext();
        Log.d("data3","pageCurr()--");

    }




    /**
     * xxxxx ... x\nxx...xxx  \n   xxxx...
     * |            |
     * end         begin
     */

    //后翻一页
    public boolean pageNext() {

        Log.d("data3","PageNext()");
        if(mEndIndex != mBeginIndex ){
            Log.d("data","保存");
            mTempBeginIndex = mBeginIndex;
            mTempEndIndex = mEndIndex;
            openNewChapter = false;
        }


        List<String> lines = new ArrayList<>();
        if(mEndIndex >= mBufferLenght){
            mLines.clear();
            Log.d("data","打开下一章节~~~~~了"+mBufferLenght);
            mCurrChapter++;
            openNewChapter = true;
            mOpenNext = true;
            return false;

        }
        mBeginIndex = mEndIndex;
        int currLine = 0;
        String string = null;
        while (currLine < mLinesOfPerPage && mEndIndex < mBufferLenght) {

            byte[] retVal = readNextParagraph();
            Log.d("data","下一页数组长度："+retVal.length
                    +"\n mEndIndex :" + mEndIndex
                    + "\n mBeginIndex :" + mBeginIndex
                    + "\n BufferLength : "+mBufferLenght);

            try {
                string = new String(retVal, "UTF-8");
                Log.d("data", string);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            while (string.length() != 0 && currLine < mLinesOfPerPage) {
                int wordOfEachLine = mPaint.breakText(string, true, mVisiableWidth, null);
                lines.add(string.substring(0, wordOfEachLine));
                currLine++;
                string = string.substring(wordOfEachLine);
            }
            if (string.length() != 0) {
                //指针回退
                try {
                    byte[] temp = string.getBytes("UTF-8");
                    mEndIndex -= temp.length;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }


        }
        if(mLines != null){
            mLines.clear();
            mLines.addAll(lines);
        }
        SharePreferenceUtil.saveReadStatus(mBookId,new int[]{mCurrChapter,mBeginIndex,mEndIndex});
        return true;

    }
    public int getCurrChapterIndex(){
        return mCurrChapter;
    }

    //前翻一页
    public boolean pagePrevious() {
        openNewChapter = false;
        Log.d("data6","previ一次");
        mTempBeginIndex = mBeginIndex;
        mTempEndIndex = mEndIndex;

        Log.d("data","保存的 索引" + mTempBeginIndex +" : "+ mTempEndIndex + " : :" +mBufferLenght);


        if(mBeginIndex == 0){
            --mCurrChapter;
            openNewChapter = true;
            mOpenNext = false;
            Log.d("data6","pre  新章节"+mCurrChapter);
            return false;
        }
        if(mLines != null){
            mLines.clear();
        }
        List<String> lines = new ArrayList<>();
        int currLine = 0;
        mEndIndex = mBeginIndex;
        while (currLine < mLinesOfPerPage && mBeginIndex > 0) {
            byte[] retVal = readPreParagraph();
            String string = null;
            try {
                string = new String(retVal, "UTF-8");
                Log.d("data", string);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            lines.clear();
            while (string.length() != 0 /*&& currLine < mLinesOfPerPage*/) {
                int wordOfEachLine = mPaint.breakText(string, true, mVisiableWidth, null);
                lines.add(string.substring(0, wordOfEachLine ));
                currLine++;
                string = string.substring(wordOfEachLine, string.length());
            }
            Collections.reverse(lines);
            if(mLines != null){
                mLines.addAll(lines);
            }


//            if (string.length() != 0) {
//                try {
//                    mBeginIndex += string.getBytes("UTF-8").length;
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }

        }
        while(currLine > mLinesOfPerPage){
            try {
                mBeginIndex += mLines.get(currLine - 1).getBytes("UTF-8").length;
                mLines.remove(currLine-1);
                Log.d("data","remove  : "+ currLine +" : mLinesOfPerPage ："+mLinesOfPerPage);
                currLine--;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        Collections.reverse(mLines);
        SharePreferenceUtil.saveReadStatus(mBookId,new int[]{mCurrChapter,mBeginIndex,mEndIndex});
        return true;
    }

    //根据mEndIndex位置读取下一段内容
    private byte[] readNextParagraph() {
        //xx... xx\n  xxxxx....xx
        //         |      or   |
        //        begin       begin

        if (mBuffer == null) {
            return null;
        }
        if (mBuffer.get(mEndIndex) == 0x0A) {
            mEndIndex++;
        }
        int tempIndex = mEndIndex;
        int num = 0;
        for (int i = tempIndex; i < mBufferLenght && mBuffer.get(i) != 0x0A; i++) {
            num++;
            mEndIndex++;

        }

        byte[] retVal = new byte[num];
        for (int i = 0; i < num; i++) {
            retVal[i] = mBuffer.get(tempIndex + i);
        }
        return retVal;
    }

    /**
     * 根据mBeginIndex指针读取上一段内容
     */
    private byte[] readPreParagraph() {
        // xxx...xxxx\n
        //  |  or     |
        // end       end


        if (mBuffer == null) {
            return null;
        }

        //mEndIndex = mBeginIndex;

//        if( mBuffer.get(mEndIndex - 1) == 0x0A ){
//            mEndIndex--;
//            mBeginIndex--;
//        }

        int tempIndex = mBeginIndex;

        if( mBuffer.get(tempIndex - 1) == 0x0A ){
            tempIndex--;
            mBeginIndex--;
        }
        int num = 0;
        for (int i = tempIndex - 1; i >= 0 && mBuffer.get(i) != 0x0A; i--) {
            num++;
            mBeginIndex--;
        }
        byte[] retVal = new byte[num];
        for (int i = 0; i < num; i++) {
            retVal[i] = mBuffer.get(mBeginIndex + i);
        }
        return retVal;

    }

    public void calcIndexOfLastPage(){

        while (mEndIndex < mBufferLenght){
            mBeginIndex = mEndIndex;
            pageNext();
            Log.d("data3","calcIndexOfLastPage");
        }

    }

    public int[] getIndex(){
        return new int[]{mBeginIndex,mEndIndex};
    }
    public String getDescriptionOfThisPage(){
        return mChapters.get(mCurrChapter).getTitle() + "@..."+mLines.get(0)+mLines.get(1)+mLines.get(2);
    }


}
