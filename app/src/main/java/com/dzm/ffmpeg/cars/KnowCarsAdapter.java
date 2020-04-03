package com.dzm.ffmpeg.cars;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.data.Car;
import com.dzm.ffmpeg.data.KnowCars;
import com.dzm.ffmpeg.databinding.ItemKnowCarsBinding;
import com.dzm.ffmpeg.utils.LogUtils;
import com.google.gson.Gson;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description cars adapter
 * @date 2020/4/3 11:43
 */
public class KnowCarsAdapter extends RecyclerView.Adapter<KnowCarsAdapter.KnowCarsViewHolder> {
    private KnowCars data;
    private Gson gson;

    public KnowCarsAdapter(KnowCars cars) {
        data = cars;
        gson = new Gson();
    }

    @NonNull
    @Override
    public KnowCarsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemKnowCarsBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_know_cars, parent, false);
        return new KnowCarsViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull KnowCarsViewHolder holder, int position) {
        holder.bind(data.cars.get(position));
    }

    @Override
    public int getItemCount() {
        return data.cars.size();
    }

    class KnowCarsViewHolder extends RecyclerView.ViewHolder {
        ItemKnowCarsBinding dataBinding;

        public KnowCarsViewHolder(ItemKnowCarsBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
        }

        public void bind(Car car) {
            dataBinding.setCar(car);
            Glide.with(dataBinding.getRoot()).load(car.image).into(dataBinding.image);

            dataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.d("click on car");
                    if (car.cars != null && car.cars.size() > 0) {
                        Intent intent = new Intent(dataBinding.getRoot().getContext(), KnowCarsActivity.class);
                        intent.putExtra("data", gson.toJson(car.cars));
                        dataBinding.getRoot().getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
