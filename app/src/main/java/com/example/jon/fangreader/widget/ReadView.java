package com.example.jon.fangreader.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.Toast;

import com.example.jon.fangreader.model.bean.BookTocBean;
import com.example.jon.fangreader.utils.SharePreferenceUtil;
import com.example.jon.fangreader.utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 2017/1/6.
 */

public abstract class ReadView extends View{

    protected Context mContext;
    protected int mScreenWidth;
    protected int mScreenHeight;

    private PageFactory mPageFactory;
    protected List<BookTocBean.MixToc.Chapter> mChapters;
    private OnReadStateChangedListener mOnReadStateChangedListener;

    protected Scroller mScroller;

    protected Bitmap mCurrPageBitmap;
    protected Bitmap mNextPageBitmap;
    private Canvas mCurrPageCanvas;
    private Canvas mNextPageCanvas;

    protected float mTouchPointX;//当前触摸点X坐标
    protected float mTouchPointY;//当前触摸点的Y坐标
    protected PointF mActionDownPoint;//第一次按下的出触摸点

    protected float mShiftOFDownToTouch;//记录当前点到初始触摸点的横向位移

    private boolean mIsCenter;//点击处是否为中部
    private boolean mIsInvalidOperate;

    protected String mBookId;

    private Region mCenterRegion;

    protected int mHalfWidth;
    protected int mHalfHeight;


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenWidth = SystemUtils.getScreenWidth();
        mScreenHeight = SystemUtils.getScreenHeight();
        mCurrPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mCurrPageCanvas = new Canvas(mCurrPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);
        mCenterRegion  = initCenterRegion();
        mHalfWidth = mScreenWidth/2;
        mHalfHeight = mScreenHeight/2;
        mActionDownPoint = new PointF(w,h);
        mTouchPointX = w;
        mTouchPointY = h;

        mPageFactory.setWidthAndHeight(mScreenWidth,mScreenHeight);
        mPageFactory.onDraw(mCurrPageCanvas);


    }




    private Region initCenterRegion(){
        Path path = new Path();
        path.moveTo(mScreenWidth/3,mScreenHeight/3);
        path.lineTo(mScreenWidth*2/3,mScreenHeight/3);
        path.lineTo(mScreenWidth*2/3,mScreenHeight*2/3);
        path.lineTo(mScreenWidth/3,mScreenHeight*2/3);
        path.close();
        return computeRegion(path);
    }

    public ReadView(Context context, String bookId,OnReadStateChangedListener listener) {
        super(context);
        this.mBookId = bookId;
        this.mContext = context;
        mScroller = new Scroller(context);
        mScreenWidth = SystemUtils.getScreenWidth();
        mScreenHeight = SystemUtils.getScreenHeight();
        mChapters = new ArrayList<>();
        mPageFactory = new PageFactory(mContext,mScreenWidth, mScreenHeight,mBookId,mChapters);
        mOnReadStateChangedListener  = listener;

    }
    public void setChapters(List<BookTocBean.MixToc.Chapter> chapters){
        mChapters.clear();
        mChapters.addAll(chapters);
    }
    public boolean pageNext(){
        return mPageFactory.pageNext();
    }
    public boolean pagePrevious(){
        return mPageFactory.pagePrevious();
    }
    public void pageCurr(){
        mPageFactory.pageCurr();
    }
    public void calcIndexOfLastPage(){
        mPageFactory.calcIndexOfLastPage();
    }
    public boolean hasNext(){
        return mPageFactory.hasNext();
    }
    public boolean hasPrevious(){
        return mPageFactory.hasPre();
    }
    public boolean openBook(int chapterIndex){
        return mPageFactory.openBook(chapterIndex);
    }
    public boolean openBook(int chapterIndex,int begin,int end){
        return mPageFactory.openBook(new int[]{chapterIndex,begin,end});
    }
    public int getCurrChapterIndex(){
        return mPageFactory.getCurrChapterIndex();
    }




    public void refreshCurrCanvas(int[] readStatus){

        if(!mPageFactory.openBook(readStatus)){
            mOnReadStateChangedListener.onLoadChapterOnStart(readStatus[0]);

            mTouchPointX = mScreenWidth;
            mTouchPointY = mScreenHeight;
            Log.d("data5","************^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            //mPageFactory.pageCurr();
            mPageFactory.onDraw(mCurrPageCanvas);

            return;
        }
        mTouchPointX = mScreenWidth;
        mTouchPointY = mScreenHeight;
        Log.d("data5","************^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        mPageFactory.pageCurr();
        mPageFactory.onDraw(mCurrPageCanvas);

    }

    public void refreshNextCanvasForNext(int chapterIndex){
        mPageFactory.openBook(chapterIndex);
        mPageFactory.pageCurr();
        mPageFactory.onDraw(mNextPageCanvas);

    }
    public void refreshNextCanvasForPre(int chapterIndex){
        Log.d("data6","refreshNextCanvasForPre");
        mPageFactory.openBook(chapterIndex);
        mPageFactory.calcIndexOfLastPage();
        mPageFactory.pageCurr();
        mPageFactory.onDraw(mNextPageCanvas);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mTouchPointX = event.getX();
        mTouchPointY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                abortAnim();
                mActionDownPoint = new PointF(event.getX(),event.getY());
                revisePoint();
                if(mCenterRegion.contains((int) event.getX(),(int) event.getY())){
                    mIsCenter = true;
                    mOnReadStateChangedListener.onClickCenter();
                    Log.d("data2","click center");

                    return false;
                }else {

                    mOnReadStateChangedListener.onFlip();
                    mIsCenter = false;
                    mPageFactory.onDraw(mCurrPageCanvas);

                    if(mActionDownPoint.x < mHalfWidth ){
                        Log.d("data2","我要翻上一页");
                        if(mPageFactory.hasPre()){

                            if(!mPageFactory.pagePrevious()){
                                if(!mPageFactory.openBook(mPageFactory.getCurrChapterIndex())){
                                    mOnReadStateChangedListener.onLoadChapter(mPageFactory.getCurrChapterIndex(),false);

                                }else {
                                    mPageFactory.calcIndexOfLastPage();
                                    mPageFactory.pageCurr();
                                }

                            }
                        }else {
                            mTouchPointY = mScreenHeight;
                            mTouchPointX = 0;
                            invalidate();

                            Toast.makeText(mContext,"没有上一页了~",Toast.LENGTH_SHORT).show();
                            return false;
                        }

                    }else {
                        Log.d("data2","我要翻下一页");
                        if(mPageFactory.hasNext()){
                            Log.d("data2","还有下一页");

                            if( !mPageFactory.pageNext()){//翻页失败说明本章已完，需要加载新章节
                                Log.d("data3","进来了、？？？？？？？？？？？？？？？");
                                if(!mPageFactory.openBook(mPageFactory.getCurrChapterIndex())){//打开失败说明本地没有
                                    mOnReadStateChangedListener.onLoadChapter(mPageFactory.getCurrChapterIndex(),true);
                                    Log.d("data3","进来了！！！！下载");
                                }else {
                                    mPageFactory.pageCurr();
                                    Log.d("data3","进来了！！！！pageCurrent");
                                }

                            }
                        }else {
                            Log.d("data","没有下一页了");
                            mTouchPointX = mScreenWidth;
                            mTouchPointY = mScreenHeight;
                            invalidate();
                            Toast.makeText(mContext,"没有下一页了~",Toast.LENGTH_SHORT).show();
                            return false;
                        }

                    }
                    mPageFactory.onDraw(mNextPageCanvas);

                }

                break;
            case MotionEvent.ACTION_MOVE:

                revisePoint();
                mShiftOFDownToTouch = mTouchPointX - mActionDownPoint.x;
                if((mShiftOFDownToTouch < 0 && mActionDownPoint.x < mHalfWidth)
                        || (mShiftOFDownToTouch > 0 && mActionDownPoint.x > mHalfWidth) ){
                    mIsInvalidOperate = true;
                }else {
                    mIsInvalidOperate = false;
                }

                break;
            case MotionEvent.ACTION_UP:

                if(!mIsCenter && !mIsInvalidOperate){
                    autoCompleteAnim();

                }else{

                    Log.d("data1","Action Up");
                    if(mIsInvalidOperate){
                        Log.d("data1","neeed revert");
                        mPageFactory.revert();
                        restoreAnim();
                        mIsInvalidOperate = false;

                    }
                }

                break;
            case MotionEvent.ACTION_CANCEL:

                if(!mIsCenter ){
                    Log.d("data","Action Cancel");
                    mPageFactory.revert();
                    restoreAnim();

                }
                break;
            default:
                break;
        }

        if(!mIsCenter){
            Log.d("data2","inval");

            invalidate();


        }
        return true;


    }

    protected Region computeRegion(Path path){
        Region region = new Region();
        RectF rectF = new RectF();
        path.computeBounds(rectF,true);
        region.setPath(path,new Region((int)rectF.left,(int)rectF.top,(int)rectF.right,(int)rectF.bottom));
        return region;
    }





    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("data2","onDraw");
        if(mIsCenter){
            refreshCurrCanvas(SharePreferenceUtil.getReadStatus(mBookId));
        }



        calcPoint();
        drawNextPage(canvas);
        drawNextPageShadow(canvas);
        drawFoldRegion(canvas);
        drawCurrPage(canvas);

        drawCurrPageShadow(canvas);
        drawFoldRegionShadow(canvas);


    }


    public void changeMode(int id){
        mPageFactory.setBackground(id);
    }
    public void setTextColor(@ColorRes int color){
        mPageFactory.setTextColor(color);
    }

    public void setFontSize(int size){
        mPageFactory.setFontSize(size);
    }
    public int[] getIndex(){
        return mPageFactory.getIndex();
    }
    public String getDescriptionOfThisPage(){
        return mPageFactory.getDescriptionOfThisPage();
    }

    //
    protected abstract void calcPoint();


    protected abstract void drawFoldRegionShadow(Canvas canvas);

    /**
     * 还原动画
     * */
    protected abstract void restoreAnim();
    /**
     * 自动补齐动画
     * */
    protected abstract void autoCompleteAnim();
    /**
     * 取消动画
     * */
    protected abstract void abortAnim();

    /**
     * 动画是否结束
     * */
    protected abstract boolean animFinished();
    /**
     * 修正触摸点Y坐标
     * */
    protected abstract void revisePoint();


    /**
     * 绘制当前页
     * */
    protected abstract void drawCurrPage(Canvas canvas);
    /**
     * 绘制下一页
     * */
    protected abstract void drawNextPage(Canvas canvas);
    /**
     * 绘制折叠区域
     * */
    protected abstract void drawFoldRegion(Canvas canvas);
    /**
     * 绘制下一页的阴影
     * */
    protected abstract void drawNextPageShadow(Canvas canvas);
    /**
     * 绘制当前页的阴影
     * */
    protected abstract void drawCurrPageShadow(Canvas canvas);


    public interface OnReadStateChangedListener {
        void onLoadChapter(int chapterIndex,boolean loadNext);
        void onLoadChapterOnStart(int chapterIndex);

        void onClickCenter();

        void onFlip();

        void onChapterChanged(int chapterIndex);

        void onPageChanged();

        void onLoadPreChapter();

        void onLoadNextChapter();
    }

}
