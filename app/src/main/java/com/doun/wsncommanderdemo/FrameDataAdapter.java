package com.doun.wsncommanderdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

/**
 * Created by power on 2017/2/23,023.
 */

public class FrameDataAdapter extends RecyclerView.Adapter<FrameDataAdapter.ViewHolder>{

    private List<FrameData> mFrameDataList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View framedataView;
        ImageView framedataImage;
        TextView framedataTime;
        TextView framedataData;
        TextView framedataLength;

        public ViewHolder(View view) {
            super(view);
            framedataView = view;
            framedataImage = (ImageView) view.findViewById(R.id.framedata_image);
            framedataTime = (TextView) view.findViewById(R.id.framedata_time);
            framedataData = (TextView) view.findViewById(R.id.framedata_data);
            framedataLength = (TextView) view.findViewById(R.id.framedata_length);
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
                Toast.makeText(v.getContext(), byte2hex(FrameData.getData()), Toast.LENGTH_LONG).show();
            }
        });
        holder.framedataImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                FrameData FrameData = mFrameDataList.get(position);
                Toast.makeText(v.getContext(), "you clicked image " + byte2hex(FrameData.getData()), Toast.LENGTH_LONG).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FrameData frameData = mFrameDataList.get(position);
        holder.framedataImage.setImageResource(frameData.getImageId());
        holder.framedataTime.setText(frameData.getTime());
        holder.framedataData.setText(byte2hex(frameData.getData()));
//        holder.framedataData.setText(Arrays.toString(frameData.getData()));
        holder.framedataLength.setText(frameData.getLength());
    }

    @Override
    public int getItemCount() {
        return mFrameDataList.size();
    }


    private static String byte2hex(byte [] buffer){
        String h = "";

        for(int i = 0; i < buffer.length; i++){
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if(temp.length() == 1){
                temp = "0" + temp;
            }
            h = h + " "+ temp;
        }

        return h;

    }

}
