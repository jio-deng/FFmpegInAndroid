package com.dzm.ffmpeg.wanandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.data.WanHomeData;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description wan android home page adapter
 * @date 2020/2/27 16:41
 */
public class WanHomeAdapter extends RecyclerView.Adapter<WanHomeAdapter.WanHomeViewHolder> {
    private WanHomeData data;

    public void setData(WanHomeData data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WanHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WanHomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wan_android_home_page, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WanHomeViewHolder holder, int position) {
        WanHomeData.DatasBean bean = data.datas.get(position);
        holder.mName.setText(bean.title);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.datas.size();
    }

    class WanHomeViewHolder extends RecyclerView.ViewHolder {
        TextView mName;

        public WanHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
        }
    }
}
