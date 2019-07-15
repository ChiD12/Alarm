package com.example.alarm;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class CalendarContentResolver {
    public static final String[] FIELDS = {
            CalendarContract.Calendars.NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.CALENDAR_COLOR,
            CalendarContract.Calendars.VISIBLE,
            Events.DTSTART
    };

    public static final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/calendars");

    ContentResolver contentResolver;
    Set<String> calendars = new HashSet<String>();

    public  CalendarContentResolver(Context ctx) {
        contentResolver = ctx.getContentResolver();
    }

    public Set<String> getCalendars() {
        // Fetch a list of all calendars sync'd with the device and their display names
        Cursor cursor = contentResolver.query(CALENDAR_URI, FIELDS, null, null, null);

        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(0);
                    String displayName = cursor.getString(1);
                    // This is actually a better pattern:
                    String color = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR));
                    Boolean selected = !cursor.getString(3).equals("0");
                    calendars.add(displayName);
                    Log.i(name,name);
                    Log.i(displayName,displayName);
                }
            }
        } catch (AssertionError ex) { /*TODO: log exception and bail*/ }

        return calendars;
    }
}