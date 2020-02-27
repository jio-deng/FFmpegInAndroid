package com.dzm.ffmpeg.wanandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.http.RetrofitFactory;
import com.dzm.ffmpeg.http.base.BaseResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description 查看某个公众号历史数据
 * @date 2020/2/25 11:42
 */
public class WxOffAccountDataActivity extends AppCompatActivity {
    private static final String TAG = "WxOffAccountDataActivit";

    private RecyclerView recyclerView;

    private int accountId;
    private int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wan_android);

        Intent intent = getIntent();
        accountId = intent.getIntExtra("accountId", 0);

        recyclerView = findViewById(R.id.rv_official_accounts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Disposable disposable = RetrofitFactory.getWanApiService().getWxOfficialAccountData(accountId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: " + throwable.getMessage());
                    }
                });
    }
}
