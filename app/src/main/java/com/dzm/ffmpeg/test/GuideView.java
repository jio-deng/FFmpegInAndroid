package com.dzm.ffmpeg.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.dzm.ffmpeg.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description 引导页
 * @date 2020/8/16 8:38
 */
public class GuideView extends View {
    private Activity activity;
    private List<View> mToBeCut;
    private Paint mPaint;

    public GuideView(Context context) {
        super(context);
        init(context);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.activity = (Activity) context;
        mToBeCut = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        setOnClickListener( v -> ((ViewGroup)activity.findViewById(android.R.id.content)).removeView(this));
    }

    public GuideView add(View view) {
        mToBeCut.add(view);
        return this;
    }

    public void show() {
        ((ViewGroup)activity.findViewById(android.R.id.content)).addView(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint);
        drawBackground(canvas);
        drawHollow(canvas);
        canvas.restoreToCount(layerId);
    }

    private void drawBackground(Canvas canvas) {
        mPaint.setXfermode(null);
        mPaint.setColor(0x60000000);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }

    private void drawHollow(Canvas canvas) {
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mPaint.setColor(Color.WHITE);

        LogUtils.d("getWidth:" + getWidth() + ", getHeight:" + getHeight());
        for (View view : mToBeCut) {
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            int dx = view.getLeft();
            int dy = view.getTop();

            LogUtils.d("left : " + dx + ", top : " + dy + ", width : " + width + ", height : " + height);

            canvas.drawRect(dx, dy, dx + width, dy + height, mPaint);
        }
    }
}
