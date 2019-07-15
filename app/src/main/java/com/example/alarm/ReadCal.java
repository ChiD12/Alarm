package com.example.alarm;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.CalendarContract;
import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Integer.parseInt;


public class ReadCal<cursor> {

    Cursor cursor;
    ContentResolver contentResolver;
    Calendar today;
    Calendar thisEvent = Calendar.getInstance();
    MyDate todayDate;

    public ReadCal(Context ctx){
        contentResolver = ctx.getContentResolver();
        today = Calendar.getInstance();
        todayDate = createDateObj(today, "Today");
        Log.i(todayDate.toString(),"hi");
    }

    public ArrayList<MyDate> getEvents(){


        Calendar copy = (Calendar) today.clone();
        copy.add(Calendar.DAY_OF_MONTH,6); //stop getting events 6 full days from now
        copy.set(Calendar.HOUR_OF_DAY,23);
        copy.set(Calendar.MINUTE,59);
        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
                .buildUpon();
        ContentUris.appendId(eventsUriBuilder,today.getTimeInMillis());
        ContentUris.appendId(eventsUriBuilder, copy.getTimeInMillis());
        Uri eventsUri = eventsUriBuilder.build();
        Cursor cursor = null;
        cursor = contentResolver.query(eventsUri, null,CalendarContract.Events.ALL_DAY + "=? ",new String[]{"0"},CalendarContract.Instances.BEGIN);

        //cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI,null,CalendarContract.Events.ALL_DAY + "=? ",new String[]{"0"},CalendarContract.Events.DTSTART);

        ArrayList<MyDate>  dateAList= new ArrayList<MyDate>(10);

        while(cursor.moveToNext()){
            if(cursor != null){

                int id_1 = cursor.getColumnIndex(CalendarContract.Events.ALL_DAY);
                int id_2 = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                int id_3 = cursor.getColumnIndex(CalendarContract.Events.DURATION);
                int id_4 = cursor.getColumnIndex(CalendarContract.Instances.BEGIN);
                int id_5 = cursor.getColumnIndex(CalendarContract.Events.EXRULE);
                int id_6 = cursor.getColumnIndex(CalendarContract.Events.EXDATE);
                int id_7 = cursor.getColumnIndex(CalendarContract.Events.RDATE);
                int id_8 = cursor.getColumnIndex(CalendarContract.Events.RRULE);




                String idValue = cursor.getString(id_1);
                String titleValue = cursor.getString(id_2);
                String repdate = cursor.getString(id_3);
                String begValue = cursor.getString(id_4);
                String exrule = cursor.getString(id_5);
                String exdate = cursor.getString(id_6);
                String rdate = cursor.getString(id_7);
                String rrule = cursor.getString(id_8);




                Log.e("gdr","name: " +titleValue+ " exdate: " + exdate + " exrule: "  + exrule + " rdate: " + rdate + " rrule: " + rrule + " duration: " + repdate);


                Long beglong = Long.parseLong(begValue); //Change start time of event to Long

                thisEvent.setTimeInMillis(beglong);  //set Calendar object to the time of begining of event
                thisEvent.get(Calendar.MONTH);
                Date calendarDate = thisEvent.getTime(); //Changes Calendar to Date object for output

                SimpleDateFormat dateformat = new SimpleDateFormat("kk:mm, MM,dd,yyyy"); //Date formating and toString



                MyDate current = createDateObj(thisEvent,titleValue);

                Log.e(current.toString(),"hello");

                 if(thisEvent.after(today)) //to avoid adding an event that has already started, only add if this starting time of event is after now
                    dateAList.add(current);
            }
            else{

            }
        }cursor.close();

        return dateAList;
    }
    public MyDate createDateObj(Calendar cal, String name){

        Date d = cal.getTime();
        SimpleDateFormat datey = new SimpleDateFormat("yyyy");
        String Year = datey.format(d);
        int inty = parseInt(Year);

        SimpleDateFormat dateM = new SimpleDateFormat("MM");
        String Month = dateM.format(d);
        int intM = parseInt(Month);

        SimpleDateFormat dated = new SimpleDateFormat("dd");
        String day = dated.format(d);
        int intd = parseInt(day);

        SimpleDateFormat dateh = new SimpleDateFormat("kk");
        String hour = dateh.format(d);
        int inth = parseInt(hour);

        SimpleDateFormat datem = new SimpleDateFormat("mm");
        String minute = datem.format(d);
        int intm = parseInt(minute);

        SimpleDateFormat ampm = new SimpleDateFormat("aa");
        String ampmS = ampm.format(d);

        long timeinmil = cal.getTimeInMillis();
        int dow = cal.get(Calendar.DAY_OF_WEEK);

        return new MyDate(name,inty,intM,intd,inth,intm,dow, timeinmil,(ampmS.equals("PM")));
    }

}
