package com.dzm.ffmpeg.wanandroid;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.data.WanHomeData;
import com.dzm.ffmpeg.utils.ToastUtil;
import com.dzm.ffmpeg.wanandroid.adapters.WanHomeAdapter;
import com.dzm.ffmpeg.wanandroid.bean.WanHomeStatus;
import com.dzm.ffmpeg.wanandroid.vm.WanHomeViewModel;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description wan android home page
 * @date 2020/2/27 15:56
 */
public class WanHomeActivity extends AppCompatActivity {
    private WanHomeViewModel mViewModel;

    private RecyclerView recyclerView;
    private WanHomeAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wan_android_home);

        initViewModel();

        recyclerView = findViewById(R.id.rv_wan_android_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new WanHomeAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(WanHomeViewModel.class);
        mViewModel.getRepository().getPagedFeed().observe(this, new Observer<PagedList<WanHomeData.DatasBean>>() {
            @Override
            public void onChanged(PagedList<WanHomeData.DatasBean> wanHomeData) {
                adapter.submitList(wanHomeData);
            }
        });

        mViewModel.getRepository().getWanHomeStatus().observe(this, new Observer<WanHomeStatus>() {
            @Override
            public void onChanged(WanHomeStatus wanHomeStatus) {
                ToastUtil.makeText(WanHomeActivity.this, wanHomeStatus.msg);
            }
        });
    }
}
