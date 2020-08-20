package com.studies.flutters;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormer {

    private long TimeSec;
    public TimeFormer(long timeSec){
        this.TimeSec = timeSec;
    }

    public String JSONformTime(){
        Date date = new Date(TimeSec);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy - MM - dd hh:mm:ss");
        String time = simpleDateFormat.format(date);
        return time;
    }

}
