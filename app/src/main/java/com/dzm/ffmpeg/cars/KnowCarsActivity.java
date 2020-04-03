package com.dzm.ffmpeg.cars;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.data.Car;
import com.dzm.ffmpeg.data.KnowCars;
import com.dzm.ffmpeg.databinding.ActivityKnowCarsBinding;
import com.dzm.ffmpeg.utils.AssertsUtil;
import com.dzm.ffmpeg.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description 了解车
 * @date 2020/4/3 11:37
 */
public class KnowCarsActivity extends AppCompatActivity {
    private static final String TAG = "KnowCarsActivity";

    private ActivityKnowCarsBinding mDataBinding;
    private KnowCarsAdapter adapter;
    private String carsData = "";
    private Gson gson;
    private KnowCars knowCars;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gson = new Gson();
        String s = getIntent().getStringExtra("data");
        if (!TextUtils.isEmpty(s)) {
            LogUtils.d(s);
            knowCars = new KnowCars();
            knowCars.cars = gson.fromJson(s, new TypeToken<List<Car>>(){}.getType());
        }
        initViews();
    }

    private void initViews() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_know_cars);
        mDataBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        if (knowCars == null) {
            try {
                carsData = AssertsUtil.readFileFromAssets(this, getString(R.string.file_path_cars));
                LogUtils.d(carsData);
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e(e);
            }

            knowCars = gson.fromJson(carsData, KnowCars.class);
        }

        adapter = new KnowCarsAdapter(knowCars);
        mDataBinding.recyclerView.setAdapter(adapter);
    }
}
