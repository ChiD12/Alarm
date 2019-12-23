package com.event.alarm;

import android.util.Log;

public class MyDate {
    private String name;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int dayOfWeek;
    private long timeinMil;
    private boolean isPM;
    private int endHour;
    private int endMinute;
    private int color;

    public MyDate(String name, int y, int M, int d,int h, int m, int dow, long mil, boolean pm,int eH, int eM, int color){
        setName(name);
        setYear(y);
        setMonth(M);
        setDay(d);
        setHour(h);
        setMinute(m);
        setDayOfWeek(dow);
        setTimeinMil(mil);
        setPM(pm);
        setEndHour(eH);
        setEndMinute(eM);
        setColor(color);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        if(month > 0 && month < 13)
            this.month = month;
        else
            Log.e("Not valid month",name);
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        if(day > 0 && day < 32)
            this.day = day;
        else
            Log.e("Not valid day",name);
    }
    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        if(hour > 0 && hour < 25)
            this.hour = hour;
        else
            Log.e("Not valid hour",name);
    }
    public int getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        if(minute < 60 && minute > -1)
            this.minute = minute;
        else
            Log.e("Not valid minute",name);
    }
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    public long getTimeinMil() {
        return timeinMil;
    }
    public void setTimeinMil(long timeinMil) {
        this.timeinMil = timeinMil;
    }
    public boolean isPM() {
        return isPM;
    }
    public void setPM(boolean PM) {
        isPM = PM;
    }
    public int getEndHour() {
        return endHour;
    }
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
    public int getEndMinute() {
        return endMinute;
    }
    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }
    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }

    public String toString(){
        return "name: " + name + "  " +hour + ":" + minute + isPM+", " + year + "-" + month + "-" + day;
    }

    public static boolean within7Days(MyDate today, MyDate eventD){

        if(today.getYear() != eventD.getYear() && (today.getMonth() != 12 || today.getDay() < 24))
            return false;

        if(today.getMonth()+1 == eventD.getMonth() && today.getDay() >= 24){
            switch (today.getMonth()){
                case 1: //january march may july august october december 31 day months
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    if(eventD.getDay() <= today.getDay()-24)
                        return true;
                    break;
                case 4:  //april june september november 30 day months
                case 6:
                case 9:
                case 11:
                    if(eventD.getDay() <= today.getDay()-23)
                        return true;
                    break;
                case 2: //february
                    if(eventD.getDay() <= today.getDay()-22)
                        return true;
                    break;
            }

        }
        if(today.getMonth() == eventD.getMonth() && eventD.getDay() - today.getDay() >= 0 && eventD.getDay() - today.getDay() < 8)
            return true;
        return false;
    }

}
