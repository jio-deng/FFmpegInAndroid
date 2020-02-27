package com.dzm.ffmpeg.utils;

import android.view.View;

public abstract class NoDoubleClickListener implements View.OnClickListener {
    private static final int INTERNAL = 1000;

    private long lastClickTime = 0;

    public abstract void noDoubleClick(View view);

    @Override
    public void onClick(View v) {
        long curTime = System.currentTimeMillis();
        if (curTime - lastClickTime > INTERNAL) {
            noDoubleClick(v);
        }
        lastClickTime = curTime;
    }
}
