package com.dzm.ffmpeg.test.rv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dzm.ffmpeg.R;

import java.util.List;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description test
 * @date 2020/8/15 9:49
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public static final int TYPE_TITLE = 1;
    public static final int TYPE_CONTENT = 2;

    private List<RvBean> mData;

    public MyAdapter(List<RvBean> data) {
        mData = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mTv.setText(mData.get(position).text);
    }

//    @Override
//    public int getItemViewType(int position) {
//        return mData.get(position).type;
//    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTv = itemView.findViewById(R.id.tv);
        }
    }
}
