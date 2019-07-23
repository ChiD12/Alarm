package com.example.alarm;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class Schedule extends AppCompatActivity {

    Calendar now;
    LinearLayout nowBar;
    View bar;

    ArrayList<MyDate> weekEvents;

    RelativeLayout mon;
    RelativeLayout tues;
    RelativeLayout wed;
    RelativeLayout thurs;
    RelativeLayout fri;
    RelativeLayout sat;
    RelativeLayout sun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mon = (RelativeLayout) findViewById(R.id.mondayRelativeLayout);
        tues = (RelativeLayout) findViewById(R.id.tuesdayRelativeLayout);
        wed = (RelativeLayout) findViewById(R.id.wednesdayRelativeLayout);
        thurs = (RelativeLayout) findViewById(R.id.thursdayRelativeLayout);
        fri = (RelativeLayout) findViewById(R.id.fridayRelativeLayout);
        sat = (RelativeLayout) findViewById(R.id.saturdayRelativeLayout);
        sun = (RelativeLayout) findViewById(R.id.sundayRelativeLayout);


        ReadCal cal = new ReadCal(getApplicationContext());
        weekEvents =  cal.getEvents();

        now = Calendar.getInstance();
        nowBar = (LinearLayout) findViewById(R.id.currentTimeMarkerLinearLayout);
        bar = (View) findViewById(R.id.currentTimeLineView);

        float x = bar.getX();
        float y = bar.getY();
        Log.e("x: " + x + " y: " + y,"x and y");


        int nowLine = timeToDP(now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE));
        Log.e(""+now.get(Calendar.HOUR_OF_DAY),"x and y");

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) nowBar.getLayoutParams();
        lp.setMargins(0,(int)convertDpToPixel(nowLine,getApplicationContext()),0,0);

        nowBar.setLayoutParams(lp);



        makeEvent(13,14,"Name Event");

    }

    public void makeEvent(int start, int end, String name){

        for(MyDate i: weekEvents) {

            int math = timeToDP(i.getEndHour(), i.getEndMinute()) - timeToDP(i.getHour(), i.getMinute());
            int mathInPixel = (int) convertDpToPixel(math, getApplicationContext());

            int startTime = timeToDP(i.getHour(), i.getMinute());
            int startTimePixel = (int) convertDpToPixel(startTime, getApplicationContext());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, mathInPixel);  //height is endtime - starttime to pixels
            layoutParams.setMargins(5, startTimePixel, 0, 0);  //to set how far down the view will be or start time

            //View child = getLayoutInflater().inflate(R.layout.sample_event_view, null);  to create the view using XML


            EventViews myView = new EventViews(Schedule.this, i);

            int dayOfWeek = i.getDayOfWeek();
            int currentDOW;
            switch (dayOfWeek){
                case Calendar.MONDAY:
                    mon.addView(myView, layoutParams);
                    break;
                case Calendar.TUESDAY:
                    tues.addView(myView, layoutParams);
                    break;
                case Calendar.WEDNESDAY:
                    wed.addView(myView, layoutParams);
                    break;
                case Calendar.THURSDAY:
                    thurs.addView(myView, layoutParams);
                    break;
                case Calendar.FRIDAY:
                    fri.addView(myView, layoutParams);
                    break;
                case Calendar.SATURDAY:
                    sat.addView(myView, layoutParams);
                    break;
                case Calendar.SUNDAY:
                    sun.addView(myView, layoutParams);
                    break;

            }


        }
    }

    static public int timeToDP(int hour, int minute){
        return  (60 * hour + minute);
    }
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}

