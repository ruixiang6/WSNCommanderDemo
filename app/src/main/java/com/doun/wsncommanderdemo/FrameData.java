package com.doun.wsncommanderdemo;

/**
 * Created by power on 2017/2/23,023.
 */

public class FrameData {

    private String time;
    private String data;
    private String length;

    private int imageId;

    public FrameData(String time, String data, String length, int imageId) {
        this.time = time;
        this.data = data;
        this.length = length;
        this.imageId = imageId;
    }

    public String getLength() {
        return length;
    }

    public String getTime() {
        return time;
    }

    public String getData() {
        return data;
    }
    public int getImageId() {
        return imageId;
    }

}
