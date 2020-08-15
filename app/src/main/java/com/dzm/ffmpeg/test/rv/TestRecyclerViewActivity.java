package com.dzm.ffmpeg.test.rv;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dzm.ffmpeg.BaseActivity;
import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.databinding.ActivityTestRvBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description test rv
 * @date 2020/8/15 9:42
 */
public class TestRecyclerViewActivity extends BaseActivity {
    private ActivityTestRvBinding mActivityTestRvBinding;
    private List<RvBean> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityTestRvBinding = DataBindingUtil.setContentView(this, R.layout.activity_test_rv);

        // init data
        mData = new ArrayList<>();
        for (int i = 0; i < 50; i ++) {
            RvBean bean = new RvBean();
            if (i % 10 == 0) {
                bean.type = 1;
                bean.text = "Title " + (i / 10 + 1);
            } else {
                bean.type = 2;
                bean.text = "Context " + (i % 10);
            }
            mData.add(bean);
        }

        MyAdapter adapter = new MyAdapter(mData);
        mActivityTestRvBinding.recyclerView.setAdapter(adapter);
        mActivityTestRvBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mActivityTestRvBinding.recyclerView.addItemDecoration(new MyItemDecoration(mData));
    }


}
