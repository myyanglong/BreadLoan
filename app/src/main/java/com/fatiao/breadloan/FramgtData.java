package com.fatiao.breadloan;

public class FramgtData {
    private String Id;
    private String Name;
    private String ImageviewUrl;
    private String Label;
    private String MinLimit;
    private String MaxLimit;
    private String PassRate;
    private String Rate;
    private String Url;
    private String PicturePath;

    public String getMaxLimit() {
        return MaxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        MaxLimit = maxLimit;
    }

    public String getMinLimit() {
        return MinLimit;
    }

    public void setMinLimit(String minLimit) {
        MinLimit = minLimit;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String getImageviewUrl() {
        return ImageviewUrl;
    }

    public void setImageviewUrl(String imageviewUrl) {
        ImageviewUrl = imageviewUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getPicturePath() {
        return PicturePath;
    }

    public void setPicturePath(String picturePath) {
        PicturePath = picturePath;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPassRate() {
        return PassRate;
    }

    public void setPassRate(String passRate) {
        PassRate = passRate;
    }
}
