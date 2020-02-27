package com.dzm.ffmpeg.utils;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 懒加载Fragment
 */
public abstract class BaseFragment extends Fragment {
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;

    //Fragment对用户可见的标记
    private boolean isUIVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        isUIVisible = isVisibleToUser;
        if (isUIVisible) {
            checkLoad();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        checkLoad();
    }

    private void checkLoad() {
        if (isViewCreated && isUIVisible) {
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;

            lazyLoadData();
        }
    }

    /**
     *  -------------- abstract -------------
     */

    /**
     * 加载数据
     */
    protected abstract void lazyLoadData();

    /**
     * 同步数据
     */
    public abstract void syncData();


}
