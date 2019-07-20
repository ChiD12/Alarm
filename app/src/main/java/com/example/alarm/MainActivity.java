package com.example.alarm;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.logging.Handler;

import me.everything.providers.android.calendar.CalendarProvider;


import static android.provider.AlarmClock.ALARM_SEARCH_MODE_ALL;
import static java.lang.Integer.parseInt;
import static java.util.Calendar.TUESDAY;

public class MainActivity extends AppCompatActivity {


    static Intent openClockIntent;
    static ArrayList<Integer> alarmDays;
    static int hoursBeforeEvent;
    static int minutesBeforeEvent;
    static int hoursBeforeSecondEvent;
    static int minutesBeforeSecondEvent;
    static int defWakeUp;
    static boolean daybefore = false;
    final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 5;
    private TextView text;
    private SeekBar seek;
    private TextView secondText;
    private SeekBar secondSeek;
    private Button newActivity;
    public static boolean secondAlarmPressed = false;

    static private int firstBefore =0;
    static private int secondBefore=0;
    static private int isTwoAlarms =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        Button BT = (Button) findViewById(R.id.BT);
        final Button openClock = (Button) findViewById(R.id.clkBTN);
        text = (TextView) findViewById(R.id.seekTextBT);
        seek = (SeekBar) findViewById(R.id.seekBT);
        secondText = (TextView) findViewById(R.id.seekTextBT2);
        secondSeek = (SeekBar) findViewById(R.id.seekBT2);
        newActivity = (Button) findViewById(R.id.newAct);

        new Thread(new Runnable() {
            public void run() {


                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    requestStoragePermission();
                }



                try{readCustom();
                }catch (Exception e){
                    Log.getStackTraceString(e);
                    Log.e("cant find file","intent");
                }


            }
        }).start();





        //testchange
        hoursBeforeEvent = 1;
        defWakeUp = 11;

        BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(new Runnable() {
                    public void run() {
                        // a potentially time consuming task

                        String filepath = "config.txt";
                        File test = new File(filepath);
                        Log.e(""+test.exists(),"test io");
                        ReadCal cal = new ReadCal(getApplicationContext());
                        ArrayList<MyDate> dateA =  cal.getEvents();

                        int firstHourofDay= Integer.MAX_VALUE;
                        int firstMinuteofDay = Integer.MAX_VALUE;


                        createAlarms(dateA,hoursBeforeEvent,minutesBeforeEvent);
                        if(secondAlarmPressed){
                            createAlarms(dateA,hoursBeforeSecondEvent,minutesBeforeSecondEvent);
                        }


                    }
                }).start();
            }
        });

        openClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                try{
                    intent = getPackageManager().getLaunchIntentForPackage("com.google.android.deskclock");
                    startActivity(intent);
                }catch (Exception e) { }

                try{
                    intent = getPackageManager().getLaunchIntentForPackage("com.oneplus.deskclock");
                    startActivity(intent);
                }catch (Exception e){}

                try{
                    intent = getPackageManager().getLaunchIntentForPackage("com.sec.android.app.clockpackage");
                    startActivity(intent);
                }catch (Exception e){}

                /*if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Clock app not added",Toast.LENGTH_SHORT).show();
                }*/

            }
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //first seek bar
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress){
                    case 0: text.setText("Time before event: 30 Minutes");
                            hoursBeforeEvent =0;
                            minutesBeforeEvent = 30;
                            writeCustom("first", 0);
                            break;
                    case 1: text.setText("Time before event: 1 Hour");
                            hoursBeforeEvent =1;
                            minutesBeforeEvent = 0;
                            writeCustom("first", 1);
                            break;
                    case 2: text.setText("Time before event: 1 Hour and 30 Minutes");
                            hoursBeforeEvent =1;
                            minutesBeforeEvent = 30;
                            writeCustom("first", 2);
                            break;
                    case 3: text.setText("Time before event: 2 Hours");
                            hoursBeforeEvent =2;
                            minutesBeforeEvent = 0;
                            writeCustom("first", 3);
                            break;
                    case 4: text.setText("Time before event: 2 Hours and 30 Minutes");
                            hoursBeforeEvent =2;
                            minutesBeforeEvent = 30;
                            writeCustom("first", 4);
                            break;
                    case 5: text.setText("Time before event: 3 Hours");
                            hoursBeforeEvent =3;
                            minutesBeforeEvent = 0;
                            writeCustom("first", 5);
                            break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        secondSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //second seek bar
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress){
                    case 0: secondText.setText("Time before event: 30 Minutes");
                            hoursBeforeSecondEvent =0;
                            minutesBeforeSecondEvent = 30;
                            writeCustom("second", 0);
                            break;
                    case 1: secondText.setText("Time before event: 1 Hour");
                            hoursBeforeSecondEvent =1;
                            minutesBeforeSecondEvent = 0;
                            writeCustom("second", 1);
                            break;
                    case 2: secondText.setText("Time before event: 1 Hour and 30 Minutes");
                            hoursBeforeSecondEvent =1;
                            minutesBeforeSecondEvent = 30;
                            writeCustom("second", 2);
                            break;
                    case 3: secondText.setText("Time before event: 2 Hours");
                            hoursBeforeSecondEvent =2;
                            minutesBeforeSecondEvent = 0;
                            writeCustom("second", 3);
                            break;
                    case 4: secondText.setText("Time before event: 2 Hours and 30 Minutes");
                            hoursBeforeSecondEvent =2;
                            minutesBeforeSecondEvent = 30;
                            writeCustom("second", 4);
                            break;
                    case 5: secondText.setText("Time before event: 3 Hours");
                            hoursBeforeSecondEvent =3;
                            minutesBeforeSecondEvent = 0;
                            writeCustom("second", 5);
                            break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




            newActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newactint = new Intent(MainActivity.this, Schedule.class);
                    startActivity(newactint);
                }
            });
    }



    public void createAlarms(ArrayList<MyDate> dateA, int hourB, int minuteB){
        int dateCreated=0;

        for (int i = 0; i < dateA.size(); i++) {
            if(dateA.get(i) != null){
                Log.i(dateA.get(i).toString(),"s");

                if(dateA.get(i).getDay() != dateCreated){

                    Log.e("set alarm for", dateA.get(i).toString());
                    calculateTimeBeforeEvent(dateA.get(i),hourB);


                    alarmDays= new ArrayList<Integer>();
                    alarmDays.add((daybefore)?dateA.get(i).getDayOfWeek()-1:dateA.get(i).getDayOfWeek());
                    daybefore = false;
                    int hour = dateA.get(i).getHour();
                    int minute= dateA.get(i).getMinute();

                    if(minuteB == 30 && dateA.get(i).getMinute() == 0){
                        hour -= 1;
                        minute = 30;
                    }
                    hour -= hourB;

                    openClockIntent = new Intent(AlarmClock.ACTION_SET_ALARM);

                    openClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    openClockIntent.putExtra(AlarmClock.EXTRA_HOUR, hour);
                    openClockIntent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
                    openClockIntent.putExtra(AlarmClock.EXTRA_DAYS,alarmDays);
                    openClockIntent.putExtra(AlarmClock.EXTRA_MESSAGE,dateA.get(i).getName());
                    openClockIntent.putExtra(AlarmClock.EXTRA_IS_PM,dateA.get(i).isPM());
                    openClockIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                    openClockIntent.putExtra(AlarmClock.VALUE_RINGTONE_SILENT,false);
                    openClockIntent.putExtra(AlarmClock.EXTRA_VIBRATE,true);



                    if(openClockIntent.resolveActivity(getPackageManager()) != null){
                        startActivity(openClockIntent);
                    }

                    if(getDeviceName().substring(0,7).equals("OnePlus")){    //if device is a oneplus
                        try{Thread.sleep(1000);}catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                    }


                    dateCreated = dateA.get(i).getDay();
                }

            }
            else
                break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(secondAlarmPressed){
            secondAlarmPressed = false;
            secondSeek.setVisibility(View.INVISIBLE);
            secondText.setVisibility(View.INVISIBLE);
            writeCustom(true);

        }else{
            secondAlarmPressed = true;
            secondSeek.setVisibility(View.VISIBLE);
            secondText.setVisibility(View.VISIBLE);
            writeCustom(false);
        }

        return super.onOptionsItemSelected(item);
    }

    public static void calculateTimeBeforeEvent(MyDate change, int hourB){
        int currentDay = change.getDay();
        if(change.getHour() - hourB < 0 || change.getHour() == 24){
            daybefore = true;
            currentDay = change.getDay() -1;
            if(currentDay<1){
                Calendar mycal = new GregorianCalendar(change.getYear(), change.getMonth()-1, change.getDay());
                //change.setDay(mycal.getActualMaximum(Calendar.DAY_OF_MONTH));
                if(change.getMonth() < 1){//january
                    change.setMonth(12);//december
                    change.setYear(change.getYear()-1);
                }
                else{
                    change.setMonth(change.getMonth()-1);
                }
            }
            //change.setDay(change.getDay()-1);
        }
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CALENDAR)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_CALENDAR}, MY_PERMISSIONS_REQUEST_READ_CALENDAR);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_CALENDAR}, MY_PERMISSIONS_REQUEST_READ_CALENDAR);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CALENDAR)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void writeCustom(String type, int value){ //specifies what to write to file

        switch (type){
            case "first":
                firstBefore = value;
                break;
            case "second":
                secondBefore = value;
                break;
        }
        String save;
        save = firstBefore + " " + secondBefore + " " + isTwoAlarms;
        writeToFile(save,MainActivity.this);
    }
    public void writeCustom(int value, int value2){

        firstBefore = value;
        secondBefore = value2;

        String save;
        save = firstBefore + " " + secondBefore + " " + isTwoAlarms;
        writeToFile(save,MainActivity.this);
    }
    public void writeCustom(boolean two){


        if(two){
            isTwoAlarms = 1;
        }else{
            isTwoAlarms = 0;
        }

        String save;
        save = firstBefore + " " + secondBefore + " " + isTwoAlarms;
        writeToFile(save,MainActivity.this);
    }

    public void readCustom(){  //specifies what to read from file
        String current = readFromFile(MainActivity.this);
        String[] split = current.split(" ");
        seek.setProgress(Integer.parseInt(split[0]));
        secondSeek.setProgress(Integer.parseInt(split[1]));

        if(split[2].equals("1")){
            secondAlarmPressed = false;
            secondSeek.setVisibility(View.INVISIBLE);
            secondText.setVisibility(View.INVISIBLE);

        }else{
            secondAlarmPressed = true;
            secondSeek.setVisibility(View.VISIBLE);
            secondText.setVisibility(View.VISIBLE);
        }

    }









    public void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            Toast.makeText(MainActivity.this,"saved to " + getFilesDir() + "/" + "config.txt",Toast.LENGTH_LONG).show();
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
    public static String getDeviceName() {             //to check if device is OnePlus
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {   //support method for getDeviceName
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e("in intent","intent");
        super.onNewIntent(intent);
        if(intent.getStringExtra("methodName") != null) {
            if (intent.getStringExtra("methodName").equals("myMethod")) {
                Log.e("in if statement","intent");
                Log.e("intent entered", intent.toString());
                ReadCal cal = new ReadCal(getApplicationContext());
                ArrayList<MyDate> dateA = cal.getEvents();


                createAlarms(dateA, hoursBeforeEvent, minutesBeforeEvent);
                if (secondAlarmPressed) {
                    createAlarms(dateA, hoursBeforeSecondEvent, minutesBeforeSecondEvent);
                }
                finish();
                System.exit(0);
            }
        }
    }
}
