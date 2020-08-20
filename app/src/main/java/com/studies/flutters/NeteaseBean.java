package com.studies.flutters;

import android.graphics.Bitmap;

public class NeteaseBean {
    private String creator;
    private String detil;
    private String coverName;
    private String user;
    private String listImageURL;
    private String isPribate;
    private long musicId;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDetil() {
        return detil;
    }

    public void setDetil(String detil) {
        this.detil = detil;
    }

    public String getCoverName() {
        return coverName;
    }

    public void setCoverName(String coverName) {
        this.coverName = coverName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getListImageURL() {
        return listImageURL;
    }

    public void setListImageURL(String listImageURL) {
        this.listImageURL = listImageURL;
    }

    public String getIsPribate() {
        return isPribate;
    }

    public void setIsPribate(String isPribate) {
        this.isPribate = isPribate;
    }

    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }

    public long getMusicId() {
        return musicId;
    }



}
