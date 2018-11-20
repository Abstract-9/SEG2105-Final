package com.codeflo.seg2105_final.models;

public class Availability {
    String day, time1, time2;

    public Availability(String day, String time1, String time2){
        this.day = day;
        this.time1 = time1;
        this.time2 = time2;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }
}
