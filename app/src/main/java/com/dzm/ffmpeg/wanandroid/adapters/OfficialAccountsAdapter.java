package com.dzm.ffmpeg.wanandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dzm.ffmpeg.R;
import com.dzm.ffmpeg.data.WxOfficialAccount;
import com.dzm.ffmpeg.wanandroid.WxOffAccountDataActivity;

import java.util.List;

/**
 * @author Johnny Deng
 * @version 1.0
 * @description Adapter for Official Accounts
 * @date 2020/2/25 11:11
 */
public class OfficialAccountsAdapter extends RecyclerView.Adapter<OfficialAccountsAdapter.OffiAccountsViewHolder> {

    private List<WxOfficialAccount> datas;

    public OfficialAccountsAdapter(List<WxOfficialAccount> datas) {
        this.datas = datas;
    }

    public void setData(List<WxOfficialAccount> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OffiAccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OffiAccountsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_official_account, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OffiAccountsViewHolder holder, int position) {
        WxOfficialAccount data = datas.get(position);
        holder.mName.setText(data.name);
        holder.mOtherData.setText("courseId=" + data.courseId + ",parentChapterId=" + data.parentChapterId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), WxOffAccountDataActivity.class);
                intent.putExtra("accountId", data.courseId);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class OffiAccountsViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mOtherData;

        OffiAccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mOtherData = itemView.findViewById(R.id.other_data);
        }
    }
}
