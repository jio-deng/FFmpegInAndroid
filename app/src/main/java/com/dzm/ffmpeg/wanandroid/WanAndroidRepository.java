package com.dzm.ffmpeg.wanandroid;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.dzm.ffmpeg.data.WanHomeData;
import com.dzm.ffmpeg.http.RequestUtil;
import com.dzm.ffmpeg.http.base.BaseResponse;
import com.dzm.ffmpeg.utils.LogUtils;
import com.dzm.ffmpeg.wanandroid.bean.WanHomeStatus;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description repository
 * @date 2020/3/6 11:03
 */
public class WanAndroidRepository {
    private LiveData<PagedList<WanHomeData.DatasBean>> pagedFeed;
    private MutableLiveData<WanHomeStatus> mWanHomeStatus;

    private int mPage = 0;

    public WanAndroidRepository() {
        mWanHomeStatus = new MutableLiveData<>();

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(50)
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(50)
                .setPrefetchDistance(10)
                .build();

        pagedFeed = new LivePagedListBuilder<>(new WanHomeDataSourceFactory(), config).build();
    }

    public LiveData<PagedList<WanHomeData.DatasBean>> getPagedFeed() {
        return pagedFeed;
    }

    public MutableLiveData<WanHomeStatus> getWanHomeStatus() {
        return mWanHomeStatus;
    }

    private WanHomeData loadFeedFromServer(int page) {
        BaseResponse<WanHomeData> feedBaseResponse = RequestUtil.getWanAndroidHomePageDataSync(page);
        if (feedBaseResponse.data == null) {
            mWanHomeStatus.setValue(new WanHomeStatus(feedBaseResponse));
        }

        return feedBaseResponse.data;
    }

    class WanHomeDataSourceFactory extends DataSource.Factory {

        @NonNull
        @Override
        public DataSource create() {
            return new WanHomePageDataSource();
        }
    }

    class WanHomePageDataSource extends PageKeyedDataSource<Integer, WanHomeData.DatasBean> {
        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, final @NonNull LoadInitialCallback<Integer, WanHomeData.DatasBean> callback) {
            LogUtils.d("loadInitial" + this);
//            mLoadState.postValue(Constants.LOAD_STATE_LOADING);
            mPage = 1;

            // get data
            WanHomeData wanHomeData = loadFeedFromServer(mPage);
            // mLoadActionType = Constants.LOAD_TYPE_DEFAULT;

            if (wanHomeData != null) {
                callback.onResult(wanHomeData.datas, 0, wanHomeData.datas.size(), mPage, mPage + 1);
            }

            // mLoadState.postValue(Constants.LOAD_STATE_IDLE);
        }


        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, WanHomeData.DatasBean> callback) {
            LogUtils.d("loadBefore" + this);
            // do nothing
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, WanHomeData.DatasBean> callback) {
            LogUtils.d("loadAfter" + this);
            // mLoadState.postValue(Constants.LOAD_STATE_LOADING);
            mPage = params.key;

            WanHomeData wanHomeData = loadFeedFromServer(mPage);

            if (wanHomeData != null && wanHomeData.datas != null && wanHomeData.datas.size() > 0) {
                callback.onResult(wanHomeData.datas, mPage + 1);
            }
        }
    }
}
