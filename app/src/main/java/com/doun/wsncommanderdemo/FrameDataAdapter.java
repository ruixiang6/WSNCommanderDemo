package com.doun.wsncommanderdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by power on 2017/2/23,023.
 */

public class FrameDataAdapter extends RecyclerView.Adapter<FrameDataAdapter.ViewHolder>{

    private List<FrameData> mFrameDataList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View framedataView;
        ImageView framedataImage;
        TextView framedataName;

        public ViewHolder(View view) {
            super(view);
            framedataView = view;
            framedataImage = (ImageView) view.findViewById(R.id.framedata_image);
            framedataName = (TextView) view.findViewById(R.id.framedata_name);
        }
    }

    public FrameDataAdapter(List<FrameData> framedataList) {
        mFrameDataList = framedataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.framedata_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.framedataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                FrameData FrameData = mFrameDataList.get(position);
                Toast.makeText(v.getContext(), "you clicked view " + FrameData.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.framedataImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                FrameData FrameData = mFrameDataList.get(position);
                Toast.makeText(v.getContext(), "you clicked image " + FrameData.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FrameData FrameData = mFrameDataList.get(position);
        holder.framedataImage.setImageResource(FrameData.getImageId());
        holder.framedataName.setText(FrameData.getName());
    }

    @Override
    public int getItemCount() {
        return mFrameDataList.size();
    }

    
}
