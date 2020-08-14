package com.dzm.ffmpeg.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.dzm.ffmpeg.R;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description draw bitmap and shadow
 * @date 2020/8/14 13:06
 */
public class BitmapShadowView extends View {
    private Paint mPaint;
    private Bitmap mBmp,mShadowBmp;
    private int mDx = 10,mDy = 10;
    private int mShadowColor;

    private BlurMaskFilter mBlurMaskFilter;

    public BitmapShadowView(Context context, @Nullable AttributeSet attrs) throws Exception {
        super(context, attrs);
        init(context, attrs);
    }

    public BitmapShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) throws Exception {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) throws Exception {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BitmapShadowView);
        int BitmapID = typedArray.getResourceId(R.styleable.BitmapShadowView_src,-1);
        if (BitmapID == -1){
            throw new Exception("BitmapShadowView 需要定义Src属性,而且必须是图像");
        }
        mBmp = BitmapFactory.decodeResource(getResources(), BitmapID);
        mDx = typedArray.getInt(R.styleable.BitmapShadowView_shadowDx,0);
        mDy = typedArray.getInt(R.styleable.BitmapShadowView_shadowDy,0);
        float mRadius = typedArray.getFloat(R.styleable.BitmapShadowView_shadowRadius, 0);
        mShadowColor = typedArray.getColor(R.styleable.BitmapShadowView_shadowColor, Color.BLACK);

        typedArray.recycle();

        mPaint = new Paint();
        mShadowBmp = mBmp.extractAlpha();

        mBlurMaskFilter = new BlurMaskFilter(mRadius, BlurMaskFilter.Blur.NORMAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = mBmp.getWidth();
        int height = mBmp.getHeight();
        setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY) ? measureWidth: width,
                (measureHeightMode == MeasureSpec.EXACTLY) ? measureHeight: height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth() - mDx;
        int height = width * mBmp.getHeight() / mBmp.getWidth();

        //绘制阴影
        mPaint.setColor(mShadowColor);
        mPaint.setMaskFilter(mBlurMaskFilter);
        canvas.drawBitmap(mShadowBmp, null, new Rect(mDx, mDy, width + mDx, height + mDy), mPaint);

        //绘制原图像
        mPaint.setMaskFilter(null);
        canvas.drawBitmap(mBmp, null, new Rect(0,0, width, height), mPaint);
    }
}
