package com.dzm.ffmpeg.test.rv;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description test
 * @date 2020/8/15 9:47
 */
public class MyItemDecoration extends RecyclerView.ItemDecoration {
    private static final int LENGTH = 50;

    private List<RvBean> mData;
    private Paint mPaint;

    public MyItemDecoration(List<RvBean> data) {
        mData = data;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i ++) {
            View child = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(child);

            int type = mData.get(index).type;

            // TODO
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int childCount = parent.getChildCount();
        int radius = 25;
        for (int i = 0; i < childCount; i ++) {
            View child = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(child);

            int type = mData.get(index).type;
            int cx = (int) ((((float) type) - 0.5) * LENGTH);
            int cy = (child.getTop() + child.getBottom()) / 2;
            c.drawCircle(cx, cy, radius, mPaint);
        }

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int type = mData.get(parent.getChildAdapterPosition(view)).type;
        outRect.right = LENGTH;
        outRect.left = type * LENGTH;
    }
}
