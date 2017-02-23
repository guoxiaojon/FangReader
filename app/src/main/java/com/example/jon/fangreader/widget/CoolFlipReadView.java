package com.example.jon.fangreader.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.drawable.GradientDrawable;

import com.example.jon.fangreader.utils.SystemUtils;


/**
 * Created by jon on 2017/1/6.
 */

public class CoolFlipReadView extends ReadView {
    private Context mContext;

    private Region mTouchPointYValidRegion;//触摸点Y可用区域
    private Path mFlodAndNextPath;
    private Path mFlodPath;
    private Path mNextPath;
    private int mCahceAreaOfY;//触摸点Y坐标的缓冲区（有意义区域），即Y永远不能大于此值
    private int mCacheAreaOfX;//X坐标临界值，用于修正贝塞尔曲线

    //用于模拟当前页背面的扭曲效果
    private static final int SUB_WIDTH = 19;//横向分19个格
    private static final int SUB_HEIGHT = 19;//纵向分19个格
    private float[] mVerts = new float[(SUB_HEIGHT + 1) * (SUB_WIDTH + 1) * 2];
    private float[] mTempVerts = new float[(SUB_HEIGHT + 1) * (SUB_WIDTH + 1) * 2];


    //计算贝塞尔曲线时的辅助变量
    float K;
    float L;
    float X;
    float Y;

    //贝塞尔曲线A控制点
    float mBezierAControlX;
    float mBezierAControlY;
    //二阶贝塞尔曲线A的终点
    float mBezierAEndX;
    float mBezierAEndY;
    //曲线A的起点
    float mBezierAStartX;
    float mBezierAStartY;
    //过曲线A终点与起点的线段的中点
    float mBezierAMiddleX;
    float mBezierAMiddleY;
    //曲线A的顶点
    float mBezierAVertexX;
    float mBezierAVertexY;

    //贝塞尔曲线B控制点
    float mBezierBControlX;
    float mBezierBControlY;
    //贝塞尔曲线B终点
    float mBezierBEndX;
    float mBezierBEndY;
    //贝塞尔曲线B起点
    float mBezierBStartX;
    float mBezierBStartY;
    //过曲线B的终点和起点的线段的中点
    float mBezierBMiddleX;
    float mBezierBMiddleY;
    //曲线B的顶点
    float mBezierBVertexX;
    float mBezierBVertexY;

    //当贝塞尔曲线的控制点超出屏幕时与屏幕边缘的交点
    float MN;
    float QN;
    float Mx;
    float My;
    float Qx;
    float Qy;

    //页脚
    float mFooterX;
    float mFooterY;
    //页脚上面的叶头

    float mHeaderX;
    float mHeaderY;

    boolean mIsLeft;




    public CoolFlipReadView(Context context, String bookId,OnReadStateChangedListener listener) {
        super(context, bookId,listener);
        mContext = context;
        mFlodAndNextPath = new Path();
        mFlodPath = new Path();
        mNextPath = new Path();
    }


    @Override
    protected void calcPoint() {
        if (mActionDownPoint.x > mHalfWidth /*&& mActionDownPoint.y > mHalfHeight*/) {
            //右下

            mIsLeft = false;
            mFooterX = mScreenWidth;
            mFooterY = mScreenHeight;
            mHeaderX = mFooterX;
            mHeaderY = 0;

            K = mScreenWidth - mTouchPointX;
            L = mScreenHeight - mTouchPointY;
            X = (float) (Math.pow(K, 2) + Math.pow(L, 2)) / (2 * K);
            Y = (float) (Math.pow(L, 2) + Math.pow(K, 2)) / (2 * L);
            mBezierAControlX = mScreenWidth - X;
            mBezierAControlY = mScreenHeight;
            mBezierBControlX = mScreenWidth;
            mBezierBControlY = mScreenHeight - Y;
            //二阶贝塞尔曲线
            mBezierAEndX = mTouchPointX + (mBezierAControlX - mTouchPointX) / 4 * 3;
            mBezierAEndY = mTouchPointY + (mScreenHeight - mTouchPointY) / 4 * 3;
            mBezierAStartX = mBezierAControlX - X / 4 /*< 0 ? 0 : mBezierAControlX - X / 4*/;

            mBezierAStartY = mScreenHeight;
            mBezierAMiddleX = (mBezierAStartX + mBezierAEndX) / 2;
            mBezierAMiddleY = (mBezierAStartY + mBezierAEndY) / 2;

            mBezierBEndX = mTouchPointX + (mScreenWidth - mTouchPointX) / 4 * 3;
            mBezierBEndY = mBezierBControlY + (mTouchPointY - mBezierBControlY) / 4;
            mBezierBStartX = mScreenWidth;
            mBezierBStartY = mBezierBControlY - Y / 4;
            mBezierBMiddleX = (mBezierBStartX + mBezierBEndX) / 2;
            mBezierBMiddleY = (mBezierBStartY + mBezierBEndY) / 2;
            mBezierBVertexX = (mBezierBMiddleX + mBezierBControlX) / 2;
            mBezierBVertexY = (mBezierBMiddleY + mBezierBControlY) / 2;
            mBezierAVertexX = (mBezierAMiddleX + mBezierAControlX) / 2;
            mBezierAVertexY = (mBezierAMiddleY + mBezierAControlY) / 2;
            if (mBezierBControlY < 0) {

                MN = (mScreenWidth - mTouchPointX) * (-mBezierBControlY) / (mTouchPointY - mBezierBControlY);
                QN = (mScreenWidth - mBezierAControlX) * (-mBezierBControlY) / (mScreenHeight - mBezierBControlY);
                Mx = mScreenWidth - MN;
                My = 0;
                Qx = mScreenWidth - QN;
                Qy = 0;
            }

        } else if (mActionDownPoint.x < mHalfWidth /*&& mActionDownPoint.y > mHalfHeight*/) {
            //左下

            mIsLeft = true;
            X = (float) (Math.pow(mTouchPointX, 2) + Math.pow(mScreenHeight - mTouchPointY, 2)) / (2 * mTouchPointX);
            Y = (float) (Math.pow(mTouchPointX, 2) + Math.pow(mTouchPointY - mScreenHeight, 2)) / (2 * (mScreenHeight - mTouchPointY));
            mFooterX = 0;
            mFooterY = mScreenHeight;
            mHeaderX = mFooterX;
            mHeaderY = 0;
            mBezierAControlX = X;
            mBezierAControlY = mScreenHeight;
            mBezierAStartX = X / 4 * 5 /*> mScreenWidth ? mScreenWidth : X / 4 * 5*/;
            mBezierAStartY = mScreenHeight;
            mBezierAEndX = mBezierAControlX + (mTouchPointX - mBezierAControlX) / 4;
            mBezierAEndY = mScreenHeight - (mScreenHeight - mTouchPointY) / 4;
            mBezierAMiddleX = (mBezierAEndX + mBezierAStartX) / 2;
            mBezierAMiddleY = (mBezierAEndY + mBezierAStartY) / 2;
            mBezierAVertexX = (mBezierAMiddleX + mBezierAControlX) / 2;
            mBezierAVertexY = (mBezierAMiddleY + mBezierAControlY) / 2;

            mBezierBStartX = 0;
            mBezierBStartY = mScreenHeight - Y / 4 * 5;
            mBezierBControlX = 0;
            mBezierBControlY = mScreenHeight - Y;
            mBezierBEndX = mTouchPointX / 4;
            mBezierBEndY = mBezierBControlY + (mTouchPointY - mBezierBControlY) / 4;

            mBezierBMiddleX = (mBezierBStartX + mBezierBEndX) / 2;
            mBezierBMiddleY = (mBezierBStartY + mBezierBEndY) / 2;
            mBezierBVertexX = (mBezierBMiddleX + mBezierBControlX) / 2;
            mBezierBVertexY = (mBezierBMiddleY + mBezierBControlY) / 2;

            if (mBezierBControlY < 0) {

                MN = (mTouchPointX) * (-mBezierBControlY) / (mTouchPointY - mBezierBControlY);
                QN = (mBezierAControlX) * (-mBezierBControlY) / (mScreenHeight - mBezierBControlY);
                Mx = MN;
                My = 0;
                Qx = QN;
                Qy = 0;
            }


        } else if (mActionDownPoint.x > mHalfWidth && mActionDownPoint.y < mHalfHeight) {
            //右上
        } else if (mActionDownPoint.x < mHalfWidth && mActionDownPoint.y < mHalfHeight) {
            //左上
        }


    }

    @Override
    protected void drawFoldRegionShadow(Canvas canvas) {
        //1/4的宽度
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCahceAreaOfY = mScreenHeight / 30 * 29;
        mCacheAreaOfX = mScreenWidth / 5;
        mHalfHeight = h / 2;
        mHalfWidth = w / 2;

        initTouchValidRegion();
        int temp = 0;
        for (int i = 0; i <= SUB_HEIGHT; i++) {//y坐标
            for (int j = 0; j <= SUB_WIDTH; j++) {//x坐标
                mVerts[temp * 2] = (float) mScreenWidth / (float) SUB_WIDTH * j;
                mVerts[temp * 2 + 1] = (float) mScreenHeight / (float) SUB_HEIGHT * i;

                temp++;
            }
        }

    }

    private void initTouchValidRegion() {
        Path path = new Path();
        path.addCircle(0, mScreenHeight, mScreenWidth, Path.Direction.CCW);
        mTouchPointYValidRegion = computeRegion(path);

    }

    @Override
    protected void restoreAnim() {
        mScroller.startScroll((int) mTouchPointX, (int) mTouchPointY, (int) (mFooterX - mTouchPointX), (int) (mFooterY - mTouchPointY), 200);

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            mTouchPointX = mScroller.getCurrX();
            mTouchPointY = mScroller.getCurrY();

            invalidate();



        }
    }

    @Override
    protected void autoCompleteAnim() {

        if (mIsLeft) {
            mScroller.startScroll((int) mTouchPointX, (int) mTouchPointY, (int) (mScreenWidth * 2 - mTouchPointX), (int) (mScreenHeight - mTouchPointY), 600);
        } else {
            mScroller.startScroll((int) mTouchPointX, (int) mTouchPointY, (int) (-mScreenWidth - mTouchPointX), (int) (mScreenHeight - mTouchPointY), 600);

        }

    }

    @Override
    protected void abortAnim() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }

    }

    @Override
    protected boolean animFinished() {
        return mScroller.isFinished();
    }

    //校准Y
    @Override
    protected void revisePoint() {
        if (mTouchPointX < 0 || mTouchPointX > mScreenWidth)
            return;
        if (mTouchPointY > mCahceAreaOfY) {
            mTouchPointY = mCahceAreaOfY;
        }
        if (!mIsLeft && Math.pow(mScreenHeight - mTouchPointY, 2) + Math.pow(mTouchPointX-mCacheAreaOfX, 2) > Math.pow(mScreenWidth-mCacheAreaOfX, 2)) {
            mTouchPointY = mScreenHeight - (float) Math.sqrt(Math.pow(mScreenWidth-mCacheAreaOfX, 2) - Math.pow(mTouchPointX-mCacheAreaOfX, 2));
        } else if (mIsLeft && Math.pow(mScreenHeight - mTouchPointY, 2) + Math.pow(mScreenWidth - mCacheAreaOfX - mTouchPointX, 2) > Math.pow(mScreenWidth - mCacheAreaOfX, 2)) {
            mTouchPointY = mScreenHeight - (float) Math.sqrt(Math.pow(mScreenWidth-mCacheAreaOfX, 2) - Math.pow(mScreenWidth-mCacheAreaOfX - mTouchPointX, 2));
        }




    }

    @Override
    protected void drawCurrPage(Canvas canvas) {


        if (mBezierBControlY < 0) {
            mFlodAndNextPath.reset();
            mFlodAndNextPath.moveTo(mTouchPointX, mTouchPointY);
            mFlodAndNextPath.lineTo(mBezierAEndX, mBezierAEndY);
            mFlodAndNextPath.quadTo(mBezierAControlX, mBezierAControlY, mBezierAStartX, mBezierAStartY);
            mFlodAndNextPath.lineTo(mFooterX, mFooterY);
            mFlodAndNextPath.lineTo(mHeaderX, mHeaderY);
            mFlodAndNextPath.lineTo(Mx, My);
            mFlodAndNextPath.close();


        } else {

            mFlodAndNextPath.reset();
            mFlodAndNextPath.moveTo(mTouchPointX, mTouchPointY);
            mFlodAndNextPath.lineTo(mBezierAEndX, mBezierAEndY);
            mFlodAndNextPath.quadTo(mBezierAControlX, mBezierAControlY, mBezierAStartX, mBezierAStartY);
            mFlodAndNextPath.lineTo(mFooterX, mFooterY);
            mFlodAndNextPath.lineTo(mBezierBStartX, mBezierBStartY);
            mFlodAndNextPath.quadTo(mBezierBControlX, mBezierBControlY, mBezierBEndX, mBezierBEndY);
            mFlodAndNextPath.close();

        }


        canvas.save();
        canvas.clipPath(mFlodAndNextPath, Region.Op.DIFFERENCE);
        canvas.drawBitmap(mCurrPageBitmap, 0, 0, null);
        canvas.restore();

    }

    @Override
    protected void drawNextPage(Canvas canvas) {
        mNextPath.reset();
        if (mBezierBControlY < 0) {
            mNextPath.moveTo(mBezierAStartX, mBezierAStartY);
            mNextPath.lineTo(mBezierAVertexX, mBezierAVertexY);
            mNextPath.lineTo(Qx, Qy);
            mNextPath.lineTo(mHeaderX, mHeaderY);
            mNextPath.lineTo(mFooterX, mFooterY);
            mNextPath.close();


        } else {
            mNextPath.moveTo(mBezierAStartX, mBezierAStartY);
            mNextPath.lineTo(mBezierAVertexX, mBezierAVertexY);
            mNextPath.lineTo(mBezierBVertexX, mBezierBVertexY);
            mNextPath.lineTo(mBezierBStartX, mBezierBStartY);
            mNextPath.lineTo(mFooterX, mFooterY);
            mNextPath.close();
        }
        canvas.save();
        canvas.clipPath(mNextPath);
        canvas.drawBitmap(mNextPageBitmap, 0, 0, null);
        canvas.restore();
    }

    @Override
    protected void drawFoldRegion(Canvas canvas) {

        if (mBezierBControlY < 0) {

            mFlodPath.reset();
            mFlodPath.moveTo(mBezierAVertexX, mBezierAVertexY);
            mFlodPath.lineTo(mBezierAEndX, mBezierAEndY);
            mFlodPath.lineTo(mTouchPointX, mTouchPointY);
            mFlodPath.lineTo(Mx, My);
            mFlodPath.lineTo(Qx, Qy);
            mFlodPath.close();
        } else {
            mFlodPath.reset();
            mFlodPath.moveTo(mBezierAVertexX, mBezierAVertexY);
            mFlodPath.lineTo(mBezierAEndX, mBezierAEndY);
            mFlodPath.lineTo(mTouchPointX, mTouchPointY);
            mFlodPath.lineTo(mBezierBEndX, mBezierBEndY);
            mFlodPath.lineTo(mBezierBVertexX, mBezierBVertexY);
            mFlodPath.close();
        }


        canvas.save();
        canvas.clipPath(mFlodPath, Region.Op.INTERSECT);


        double degree = -1;
        if (mIsLeft) {
            canvas.scale(-1, 1);
            canvas.translate(-mTouchPointX, mTouchPointY);

            if (X > Y) {
                degree = 90 + Math.toDegrees(Math.acos((mScreenHeight - mTouchPointY) / X));

            } else if (X < Y) {
                degree = 90 - Math.toDegrees(Math.acos((mScreenHeight - mTouchPointY) / X));

            } else {
                degree = 90;

            }
            canvas.rotate((float) degree);
            canvas.translate(0, -mScreenHeight);


            //扭曲点
            int temp = 0;
            //倍率
            int powerX = 3;
            int powerY = 1;
            //初始偏移
            float initShift = 6.5f;

            int itemWidth = mScreenWidth / SUB_WIDTH;
            int itemHeight = mScreenHeight / SUB_HEIGHT;


            int bottomEnd = Math.round((X / 4 * 5) / itemWidth) + 1;
            int bottomStart = Math.round((X / 4 * 3) / itemWidth) - 1;
            int leftStart = Math.round((mScreenHeight - Y / 4 * 5) / itemHeight) - 1;
            int leftEnd = Math.round((mScreenHeight - Y / 4 * 3) / itemHeight) + 1;

            for (int i = 0; i <= SUB_HEIGHT; i++) {//i 为Y轴坐标
                for (int j = 0; j <= SUB_WIDTH; j++) {//j 为X轴坐标
                    mTempVerts[temp * 2] = mVerts[temp * 2];
                    mTempVerts[temp * 2 + 1] = mVerts[temp * 2 + 1];
                    if (j == 0) {//第一列的点
                        if (i >= leftStart && i <= leftEnd) {//最底下那几个点，即属于扭曲范围
                            mTempVerts[temp * 2] = mVerts[temp * 2] - initShift * powerX;
                            powerX /= 0.5;

                        }

                    }

                    if (i == SUB_HEIGHT) {//最后一行的点
                        if (j >= bottomStart && j <= bottomEnd) {//最左边那几个点，即属于扭曲范围
                            mTempVerts[temp * 2 + 1] = mVerts[temp * 2 + 1] + initShift * powerY;
                            powerY++;
                        }

                    }
                    temp++;
                }
            }


        } else {
            canvas.translate(mTouchPointX, mTouchPointY);
            if (X > Y) {
                degree = 180 - Math.toDegrees((Math.atan(Y / X))) * 2;
            } else if (X < Y) {
                degree = Math.toDegrees(Math.atan((mScreenHeight - mTouchPointY) / (mScreenWidth - mTouchPointX - X)));

            } else {
                degree = 90;
            }
            canvas.rotate((float) degree);
            canvas.translate(0, -mScreenHeight + 3);//+3弥补精度缺失
            canvas.scale(-1, 1);
            canvas.translate(-mScreenWidth, 0);


            //扭曲点

            int temp = 0;
            //倍率
            int powerX = 3;
            int powerY = 1;
            //初始偏移
            float initShift = 6.5f;

            int itemWidth = mScreenWidth / SUB_WIDTH;
            int itemHeight = mScreenHeight / SUB_HEIGHT;

            int bottomEnd = Math.round((mScreenWidth - X / 4 * 3) / itemWidth) + 1;
            int bottomStart = Math.round((mScreenWidth - X) / itemWidth) - 1;
            int leftStart = Math.round((mScreenHeight - Y) / itemHeight) - 1;
            int leftEnd = Math.round((mScreenHeight - Y / 4 * 3) / itemHeight);

            for (int i = 0; i <= SUB_HEIGHT; i++) {//i 为Y轴坐标
                for (int j = 0; j <= SUB_WIDTH; j++) {//j 为X轴坐标
                    mTempVerts[temp * 2] = mVerts[temp * 2];
                    mTempVerts[temp * 2 + 1] = mVerts[temp * 2 + 1];
                    if (j == SUB_WIDTH) {//第一列的点
                        if (i >= leftStart && i <= leftEnd) {//最底下那几个点，即属于扭曲范围
                            mTempVerts[temp * 2] = mVerts[temp * 2] + initShift * powerX;
                            powerX /= 0.5;

                        }

                    }

                    if (i == SUB_HEIGHT) {//最后一行的点
                        if (j >= bottomStart && j <= bottomEnd) {//最左边那几个点，即属于扭曲范围
                            mTempVerts[temp * 2 + 1] = mVerts[temp * 2 + 1] + initShift * powerY;
                            powerY++;
                        }

                    }
                    temp++;
                }
            }

        }


        Paint paint = new Paint();

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(new float[]{
                0.55f, 0, 0, 0,
                80.0f, 0, 0.55f,
                0, 0, 80.0f, 0,
                0, 0.55f, 0, 80.0f,
                0, 0, 0, 0.2f, 0
        });
        paint.setColorFilter(colorFilter);

        paint.setColorFilter(new ColorFilter());
        canvas.drawBitmapMesh(mCurrPageBitmap, SUB_WIDTH, SUB_HEIGHT, mTempVerts, 0, null, 0, paint);
        canvas.restore();


    }

    @Override
    protected void drawNextPageShadow(Canvas canvas) {
        if(mTouchPointY >= mScreenHeight-10){
            return;
        }

        //阴影宽度
        int distance = (int) Math.sqrt(Math.pow(mBezierAControlX - mBezierAMiddleX, 2) + Math.pow(mBezierAControlY - mBezierAMiddleY, 2));

        canvas.save();
        double height;
        double degree;

        canvas.translate(mBezierAStartX, mScreenHeight);
        if (mIsLeft) {
            degree = 90 + Math.toDegrees(Math.atan2(Y, X));
        } else {
            degree = Math.toDegrees(Math.atan((mScreenWidth - mBezierAStartX) / (mScreenHeight - mBezierBStartY)));
        }

        canvas.rotate((float) degree);
        canvas.scale(1, 1.3f);

        if (mBezierBControlY < 0) {
            height = Math.sqrt(Math.pow(mBezierAStartX - Qx, 2) + Math.pow(mBezierAStartY - Qy, 2));
        } else {
            height = Math.sqrt(Math.pow(mBezierAStartX - mBezierBStartX, 2) + Math.pow(mBezierAStartY - mBezierBStartY, 2));

        }
        if(!mIsLeft){
            canvas.translate(0, -(float) height);
        }

        int colors[] = {0xb0333333, 0x333333};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        gradientDrawable.setBounds(0, 0, distance + distance / 2, (int) height);
        gradientDrawable.draw(canvas);

        canvas.restore();

    }

    @Override
    protected void drawCurrPageShadow(Canvas canvas) {

        float topX;
        float topY;


        float K1 = (mBezierAControlY - mTouchPointY) / (mBezierAControlX - mTouchPointX);
        float K2 = (mBezierBControlY - mTouchPointY) / (mBezierBControlX - mTouchPointX);

        float h = SystemUtils.dp2px(mContext, 50);
        if (X > Y) {
            float b1 = mTouchPointY - K1 * mTouchPointX;
            float b2 = mTouchPointY - K2 * mTouchPointX;

            double b1New = b1 - h * Math.sqrt(Math.pow(K1, 2) + 1);
            double b2New = b2 - h * Math.sqrt(Math.pow(K2, 2) + 1);
            topX = (float) (b2New - b1New) / (K1 - K2);
            topY = (float) (K1 * topX + b1New);

        } else if (X < Y) {


            float b1 = mTouchPointY - K1 * mTouchPointX;
            float b2 = mTouchPointY - K2 * mTouchPointX;

            double b1New = h * Math.sqrt(Math.pow(K1, 2) + 1) + b1;
            double b2New = b2 - h * Math.sqrt(Math.pow(K2, 2) + 1);
            topX = (float) (b2New - b1New) / (K1 - K2);
            topY = (float) (K1 * topX + b1New);

        } else {

            if(mIsLeft){
                topX = (mTouchPointX + h);
                topY = (mTouchPointY + h);

            }else {
                topX = (mTouchPointX - h);
                topY = (mTouchPointY - h);
            }


        }

        Path path = new Path();

        path.moveTo(topX, topY);
        path.lineTo(mTouchPointX, mTouchPointY);
        path.lineTo(mBezierAEndX, mBezierAEndY);
        path.quadTo(mBezierAControlX, mBezierAControlY, mBezierAStartX, mBezierAStartY);
        path.close();

        canvas.save();
        canvas.clipPath(path);
        canvas.translate(mTouchPointX, mTouchPointY);
        double degree = Math.toDegrees(Math.asin((mScreenHeight - mTouchPointY) / X));

        if(mIsLeft){
            if(X < Y){
                canvas.rotate(-(float)degree);
            }else {
                canvas.rotate(-(float)(180 - degree));
            }
            canvas.translate(-mScreenWidth+h,0);

        }else{
            if (X < Y) {
                canvas.rotate((float) degree);
            } else {
                canvas.rotate((float) (180 - degree));
            }

            canvas.translate(-h, 0);

        }


        int colors[] = {0xb0333333, 0x333333};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        gradientDrawable.setBounds(0, 0, mScreenWidth, 30);
        gradientDrawable.draw(canvas);
        canvas.restore();

        //上边阴影
        path.reset();
        path.moveTo(topX, topY);
        path.lineTo(mTouchPointX, mTouchPointY);
        path.lineTo(mBezierBEndX, mBezierBEndY);
        path.quadTo(mBezierBControlX, mBezierBControlY, mBezierBStartX, mBezierBStartY);

        path.close();


        canvas.save();
        canvas.clipPath(path);
        canvas.translate(mTouchPointX, mTouchPointY);

        if(mIsLeft){
            if(Y > X){
                canvas.rotate(-(float) degree);

            }else {
                canvas.rotate(-(float) (180 - degree));

            }
            canvas.translate(0,-mScreenHeight+h);

        }else {
            if (Y > X) {
                canvas.rotate((float) (degree - 180));
            } else {
                canvas.rotate((float) -Math.toDegrees(Math.atan(Y / X)) * 2);

            }
            canvas.translate(0, -h);

        }
        canvas.scale(1, 1.3f);//补齐



        GradientDrawable gradientDrawableLR = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        gradientDrawableLR.setBounds(0, 0, 30, mScreenHeight);
        gradientDrawableLR.draw(canvas);
        canvas.restore();

    }
}
