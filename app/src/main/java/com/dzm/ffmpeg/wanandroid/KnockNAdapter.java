package com.dzm.ffmpeg.wanandroid;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.databinding.ItemKnockNBinding;
import com.dzm.ffmpeg.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description adapter for knock n
 * @date 2020/7/12 15:40
 */
public class KnockNAdapter extends RecyclerView.Adapter<KnockNAdapter.MyViewHolder> {
    private Set<Integer> nums = new HashSet<>();
    private List<Integer> data = new ArrayList<>();

    public KnockNAdapter() {
        for (int i = 0; i < 150; i ++) {
            data.add(i+1);
        }
    }

    public void setNums(Set<Integer> nums) {
        this.nums.clear();
        this.nums.addAll(nums);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemKnockNBinding itemKnockNBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_knock_n, parent, false);
        return new MyViewHolder(itemKnockNBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ItemKnockNBinding mDataBinding;

        public MyViewHolder(@NonNull ItemKnockNBinding itemKnockNBinding) {
            super(itemKnockNBinding.getRoot());
            this.mDataBinding = itemKnockNBinding;
        }

        public void bind(int target) {
            boolean isBlack = true;
            int tmp = target;
            while (tmp > 0) {
                if (nums.contains(tmp % 10)) {
                    isBlack = false;
                    break;
                }

                tmp /= 10;
            }

            for (int i : nums) {
                if (target % i == 0) {
                    isBlack = false;
                    break;
                }
            }

            mDataBinding.tvNum.setText(String.valueOf(target));
            if (isBlack) {
                mDataBinding.tvNum.setTextColor(Utils.getColor(R.color.knock_n_read));
            } else {
                mDataBinding.tvNum.setTextColor(Utils.getColor(R.color.knock_n_knock));
            }
        }
    }
}
