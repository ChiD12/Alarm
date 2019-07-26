package com.example.alarm;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.design.circularreveal.CircularRevealRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class Schedule extends AppCompatActivity {

    Calendar now;
    LinearLayout nowBar;
    View currentLineTimeView;
    View dayOfTheWeekBar;

    ArrayList<MyDate> weekEvents;

    RelativeLayout mon;
    RelativeLayout tues;
    RelativeLayout wed;
    RelativeLayout thurs;
    RelativeLayout fri;
    RelativeLayout sat;
    RelativeLayout sun;

    RelativeLayout monH;
    RelativeLayout tuesH;
    RelativeLayout wedH;
    RelativeLayout thursH;
    RelativeLayout friH;
    RelativeLayout satH;
    RelativeLayout sunH;

    TextView monT;
    TextView tuesT;
    TextView wedT;
    TextView thursT;
    TextView friT;
    TextView satT;
    TextView sunT;

    ScrollView top;

    private float x1, x2, y1, y2;

    static ArrayList<EventViews> myViews = new ArrayList<>(10);
    static int[] daysOfWeekA = new int[7];
    int dow;
    int dom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mon = (RelativeLayout) findViewById(R.id.mondayRelativeLayout);  //relative layout containers per day
        tues = (RelativeLayout) findViewById(R.id.tuesdayRelativeLayout);
        wed = (RelativeLayout) findViewById(R.id.wednesdayRelativeLayout);
        thurs = (RelativeLayout) findViewById(R.id.thursdayRelativeLayout);
        fri = (RelativeLayout) findViewById(R.id.fridayRelativeLayout);
        sat = (RelativeLayout) findViewById(R.id.saturdayRelativeLayout);
        sun = (RelativeLayout) findViewById(R.id.sundayRelativeLayout);

        monH = (RelativeLayout) findViewById(R.id.mondayHeaderRelativeLayout);  //the day headers
        tuesH = (RelativeLayout) findViewById(R.id.tuesdayHeaderRelativeLayout);
        wedH = (RelativeLayout) findViewById(R.id.wednesdayHeaderRelativeLayout);
        thursH = (RelativeLayout) findViewById(R.id.thursdayHeaderRelativeLayout);
        friH = (RelativeLayout) findViewById(R.id.fridayHeaderRelativeLayout);
        satH = (RelativeLayout) findViewById(R.id.saturdayHeaderRelativeLayout);
        sunH = (RelativeLayout) findViewById(R.id.sundayHeaderRelativeLayout);

        monT = findViewById(R.id.mondayDateTextView);  //date TextView
        tuesT = findViewById(R.id.tuesdayDateTextView);
        wedT = findViewById(R.id.wednesdayDateTextView);
        thursT = findViewById(R.id.thursdayDateTextView);
        friT = findViewById(R.id.fridayDateTextView);
        satT = findViewById(R.id.saturdayDateTextView);
        sunT = findViewById(R.id.sundayDateTextView);

        top = findViewById(R.id.calendarScrollView);

        top.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchEvent(event);
                return false;
            }
        });

        ReadCal cal = new ReadCal(getApplicationContext());
        weekEvents = cal.getEvents();

        now = Calendar.getInstance();


        nowBar = (LinearLayout) findViewById(R.id.currentTimeMarkerLinearLayout);
        currentLineTimeView = (View) findViewById(R.id.currentTimeLineView);
        dayOfTheWeekBar = findViewById(R.id.dayMarkerView);
        dow = now.get(Calendar.DAY_OF_WEEK); //today's weekday
        dom = now.get(Calendar.DAY_OF_MONTH); // today's day of month

        Calendar domLowerCal = (Calendar) now.clone(); //to get the date of previous days
        Calendar domHigherCal = (Calendar) now.clone(); //to get the date of the next couple days

        sunH.removeView(dayOfTheWeekBar);  //remove the dayOfTheWeekBar from sunday because its written in the sunday xml
        setDayOfWeek(dow);  //adds the dayOfTheWeekBar to the header relative layout

        for (int i = dow; i < 8; i++) {
            setDayOfMonth(i,domHigherCal.get(Calendar.DAY_OF_MONTH));
            domHigherCal.add(Calendar.DAY_OF_MONTH,1);
        }
        for (int i = dow-1; i >= 1; i--) {
            domLowerCal.add(Calendar.DAY_OF_MONTH,-1);
            setDayOfMonth(i,domLowerCal.get(Calendar.DAY_OF_MONTH));
        }

        int nowLine = timeToDP(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE)); //converts the hour and minutes of an event to its position in calendar in dp

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) nowBar.getLayoutParams();  //sets the bar for current time with some top margins to move it to the correct position
        lp.setMargins(0, (int) convertDpToPixel(nowLine, getApplicationContext()), 0, 0);

        nowBar.setLayoutParams(lp);


        new Thread(new Runnable() {
            public void run() {
                makeEvent();
            }
        }).start();
    }
    public boolean onTouchEvent(MotionEvent touchevent){
        switch (touchevent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                if (x1 < x2 &&  Math.abs(y2 - y1) <100){
                    finish();
                }
                break;
        }
        return false;
    }


    public void makeEvent(){   //loops through every event of the week and creates a clickable view for them at the correct positioning for its begining and end

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

            int dayOfWeek = i.getDayOfWeek();  //add EventViews to the Relative Layout of the day it takes place
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
            myViews.add(myView); // Stores all created EventViews objects
            final String currentString= i.getName();


            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int hourB = MainActivity.hoursBeforeEvent;
                    int minuteB = MainActivity.minutesBeforeEvent;
                    int secondHourB= MainActivity.hoursBeforeSecondEvent;
                    int secondMinuteB = MainActivity.minutesBeforeSecondEvent;
                    boolean secondAlarmPressed = MainActivity.secondAlarmPressed;


                    MyDate singleDate = search(currentString);

                    createAlarms(singleDate,hourB,minuteB);
                    if(secondAlarmPressed){
                        createAlarms(singleDate,secondHourB,secondMinuteB);
                    }

                }
            });
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
    public static MyDate search(String name){
        for(EventViews event: myViews){
            MyDate current =  event.getCurrentDate();
            if(current.getName().equals(name)){
                return current;
            }
        }
        return null;
    }

    public void createAlarms(MyDate singleDate, int hourB, int minuteB){

        MainActivity.calculateTimeBeforeEvent(singleDate,hourB);
        boolean daybefore = MainActivity.daybefore;

        ArrayList<Integer> alarmDays= new ArrayList<Integer>();
        alarmDays.add((daybefore)?singleDate.getDayOfWeek()-1:singleDate.getDayOfWeek());
        daybefore = false;
        int hour = singleDate.getHour();
        int minute= singleDate.getMinute();

        if(minuteB > singleDate.getMinute()){
            hour -= 1;
            minute = 60 - (minuteB - singleDate.getMinute());
        }
        else{
            minute -= minuteB;
        }
        hour -= hourB;


        Intent openClockIntent = new Intent(AlarmClock.ACTION_SET_ALARM);

        openClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        openClockIntent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        openClockIntent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        openClockIntent.putExtra(AlarmClock.EXTRA_DAYS,alarmDays);
        openClockIntent.putExtra(AlarmClock.EXTRA_MESSAGE,singleDate.getName());
        openClockIntent.putExtra(AlarmClock.EXTRA_IS_PM,singleDate.isPM());
        openClockIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        openClockIntent.putExtra(AlarmClock.VALUE_RINGTONE_SILENT,false);
        openClockIntent.putExtra(AlarmClock.EXTRA_VIBRATE,true);



        if(openClockIntent.resolveActivity(getPackageManager()) != null){
            startActivity(openClockIntent);
        }

    }
    private void setDayOfWeek(int dow){
        switch (dow){
            case Calendar.SUNDAY:
                sunH.addView(dayOfTheWeekBar);
                break;
            case Calendar.MONDAY:
                monH.addView(dayOfTheWeekBar);
                break;
            case Calendar.TUESDAY:
                tuesH.addView(dayOfTheWeekBar);
                break;
            case Calendar.WEDNESDAY:
                wedH.addView(dayOfTheWeekBar);
                break;
            case Calendar.THURSDAY:
                thursH.addView(dayOfTheWeekBar);
                break;
            case Calendar.FRIDAY:
                friH.addView(dayOfTheWeekBar);
                break;
            case Calendar.SATURDAY:
                satH.addView(dayOfTheWeekBar);
                break;
        }
    }
    private void setDayOfMonth(int dow, int dom){
        String domS = dom+"";
        switch (dow){
            case Calendar.SUNDAY:
                sunT.setText(domS);
                break;
            case Calendar.MONDAY:
                monT.setText(domS);
                break;
            case Calendar.TUESDAY:
                tuesT.setText(domS);
                break;
            case Calendar.WEDNESDAY:
                wedT.setText(domS);
                break;
            case Calendar.THURSDAY:
                thursT.setText(domS);
                break;
            case Calendar.FRIDAY:
                friT.setText(domS);
                break;
            case Calendar.SATURDAY:
                satT.setText(domS);
                break;
        }
    }
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}

