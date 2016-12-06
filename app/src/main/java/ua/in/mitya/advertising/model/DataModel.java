package ua.in.mitya.advertising.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitya on 05.12.2016.
 */

public class DataModel {

    @SerializedName("video_source")
    private String videoSource;
    @SerializedName("click_url")
    private String clickUrl;

    public DataModel(String videoSource, String clickUrl) {
        this.videoSource = videoSource;
        this.clickUrl = clickUrl;
    }

    public String getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(String videoSource) {
        this.videoSource = videoSource;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }
}
