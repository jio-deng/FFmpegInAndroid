package com.dzm.ffmpeg.wanandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.data.WxOfficialAccount;
import com.dzm.ffmpeg.http.RetrofitFactory;
import com.dzm.ffmpeg.http.base.BaseResponse;
import com.dzm.ffmpeg.wanandroid.adapters.OfficialAccountsAdapter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description wan android activity
 * @date 2020/2/25 10:50
 */
public class WanAndroidActivity extends AppCompatActivity {
    private static final String TAG = "WanAndroidActivity";

    private RecyclerView recyclerView;
    private OfficialAccountsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wan_android);

        // btn_get_official_accounts
        findViewById(R.id.btn_get_official_accounts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disposable disposable = RetrofitFactory.getWanApiService().getWxOfficialAccounts()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BaseResponse<List<WxOfficialAccount>>>() {
                    @Override
                    public void accept(BaseResponse<List<WxOfficialAccount>> wxOfficialAccounts) throws Exception {
                        if (adapter == null) {
                            adapter = new OfficialAccountsAdapter(WanAndroidActivity.this, wxOfficialAccounts.data);
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.setData(wxOfficialAccounts.data);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: " + throwable.getMessage());
                    }
                });
            }
        });

        recyclerView = findViewById(R.id.rv_official_accounts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // jump to home page
        findViewById(R.id.btn_wan_android_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WanAndroidActivity.this, WanHomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
