package com.example.alarm;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class Schedule extends AppCompatActivity {

    Calendar now;
    LinearLayout nowBar;
    View bar;

    ArrayList<MyDate> weekEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

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
    public void makeEvent(){
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(30, 20, 30, 0);

        Button okButton=new Button(this);
        okButton.setText("some text");
        ll.addView(okButton, layoutParams);
    }

}

