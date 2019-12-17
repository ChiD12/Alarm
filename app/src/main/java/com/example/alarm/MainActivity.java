package com.example.alarm;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    static Intent openClockIntent;
    static ArrayList<Integer> alarmDays;
    static int hoursBeforeEvent;
    static int minutesBeforeEvent;
    static int hoursBeforeSecondEvent;
    static int minutesBeforeSecondEvent;
    static int defWakeUp;
    static boolean daybefore = false;
    final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 5;
    public TextView text;
    public SeekBar seek;
    public TextView secondText;
    public SeekBar secondSeek;
    public static boolean secondAlarmPressed;
    public static boolean swticherBool = false;
    Intent newactint;
    Activity scheduleActivity;
    private LocationManager locationManager;




    static public int firstBefore;
    static public int secondBefore;
    static public int isTwoAlarms;

    private float x1, x2, y1, y2;

    private NavigationView navigationView;
    public MenuItem twoAlarms;
    public Menu menu;
    public MenuItem item;

    DrawerLayout DL;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DL = findViewById(R.id.drawer_layout);



        if(!DL.isDrawerOpen(GravityCompat.START)){
            DL.setOnTouchListener(new View.OnTouchListener() { //TODO doesnt happen when sidebar open
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    onTouchEvent(motionEvent);
                    return false;
                }
            });
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,DL, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        DL.addDrawerListener(toggle);
        toggle.syncState();
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        Button BT = (Button) findViewById(R.id.BT);
        final Button openClock = (Button) findViewById(R.id.clkBTN);
        text = (TextView) findViewById(R.id.seekTextBT);
        seek = (SeekBar) findViewById(R.id.seekBT);
        secondText = (TextView) findViewById(R.id.seekTextBT2);
        secondSeek = (SeekBar) findViewById(R.id.seekBT2);
        newactint =new Intent(MainActivity.this, Schedule.class);

        new Thread(new Runnable() {
            public void run() {

                Calendar now = Calendar.getInstance();

                Intent myIntent = new Intent(getApplicationContext(),
                        MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), 1, myIntent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                alarmManager.set(AlarmManager.RTC, now.getTimeInMillis(),
                        pendingIntent);

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




        menu = navigationView.getMenu(); // set text color for navigation menu

        MenuItem tools= menu.findItem(R.id.navigation_divider);
        MenuItem options= menu.findItem(R.id.divider);
        item = menu.findItem(R.id.nav_twoalarms);

        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);

        SpannableString n = new SpannableString(options.getTitle());
        n.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, n.length(), 0);
        options.setTitle(n);

        navigationView.setNavigationItemSelectedListener(this);

        /*Bundle extras = getIntent().getExtras(); //to receive intent from schedule

        SharedPreferences bb = getSharedPreferences("my_prefs", 0);
        String value = bb.getString("CHANGE_UI", "");
        Toast.makeText(this,"value",Toast.LENGTH_SHORT).show();
        if (value != null) {
            //String value = extras.getString("CHANGE_UI");
            if(value.equals("false")){
                Toast.makeText(this,"got in false " + value,Toast.LENGTH_SHORT).show();
                changeSecondSeekFalse();
            }
            if(value.equals("true")){
                Toast.makeText(this,"got in true + value",Toast.LENGTH_SHORT).show();
                changeSecondSeekTrue();
            }
        }*/

        //testchange
        hoursBeforeEvent = 1;
        defWakeUp = 11;

        BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                new Thread(new Runnable() {
                    public void run() {

                        String filepath = "config.txt";
                        File test = new File(filepath);
                        Log.e(""+test.exists(),"test io");
                        ReadCal cal = new ReadCal(getApplicationContext());
                        ArrayList<MyDate> dateA =  cal.getEvents();




                        createAlarms(dateA,hoursBeforeEvent,minutesBeforeEvent,false);
                        if(secondAlarmPressed){
                            createAlarms(dateA,hoursBeforeSecondEvent,minutesBeforeSecondEvent,false);
                        }
                    }
                }).start();
            }
        });

        openClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                new Thread(new Runnable() {
                    public void run() {
                        // a potentially time consuming task

                        String filepath = "config.txt";
                        File test = new File(filepath);
                        Log.e(""+test.exists(),"test io");
                        ReadCal cal = new ReadCal(getApplicationContext());
                        ArrayList<MyDate> dateA =  cal.getEvents();




                        createAlarms(dateA,hoursBeforeEvent,minutesBeforeEvent,true);
                        if(secondAlarmPressed){
                            createAlarms(dateA,hoursBeforeSecondEvent,minutesBeforeSecondEvent,true);
                        }
                    }
                }).start();

            }
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //first seek currentLineTimeView
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress){
                    case 0: text.setText("30 Minutes");
                            hoursBeforeEvent =0;
                            minutesBeforeEvent = 30;
                            writeCustom("first", 0);
                            seek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            break;
                    case 1: text.setText("1 Hour");
                            hoursBeforeEvent =1;
                            minutesBeforeEvent = 0;
                            writeCustom("first", 1);
                            seek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            break;
                    case 2: text.setText("1 Hour and 30 Minutes");
                            hoursBeforeEvent =1;
                            minutesBeforeEvent = 30;
                            writeCustom("first", 2);
                            seek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            break;
                    case 3: text.setText("2 Hours");
                            hoursBeforeEvent =2;
                            minutesBeforeEvent = 0;
                            writeCustom("first", 3);
                            seek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            break;
                    case 4: text.setText("2 Hours and 30 Minutes");
                            hoursBeforeEvent =2;
                            minutesBeforeEvent = 30;
                            writeCustom("first", 4);
                            seek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            break;
                    case 5: text.setText("3 Hours");
                            hoursBeforeEvent =3;
                            minutesBeforeEvent = 0;
                            writeCustom("first", 5);
                            seek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
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
        secondSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //second seek currentLineTimeView
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress){
                    case 0: secondText.setText("30 Minutes");
                            hoursBeforeSecondEvent =0;
                            minutesBeforeSecondEvent = 30;
                            writeCustom("second", 0);
                            secondSeek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            break;
                    case 1: secondText.setText("1 Hour");
                            hoursBeforeSecondEvent =1;
                            minutesBeforeSecondEvent = 0;
                            writeCustom("second", 1);
                            secondSeek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            break;
                    case 2: secondText.setText("1 Hour and 30 Minutes");
                            hoursBeforeSecondEvent =1;
                            minutesBeforeSecondEvent = 30;
                            writeCustom("second", 2);
                            secondSeek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            break;
                    case 3: secondText.setText("2 Hours");
                            hoursBeforeSecondEvent =2;
                            minutesBeforeSecondEvent = 0;
                            writeCustom("second", 3);
                            secondSeek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            break;
                    case 4: secondText.setText("2 Hours and 30 Minutes");
                            hoursBeforeSecondEvent =2;
                            minutesBeforeSecondEvent = 30;
                            writeCustom("second", 4);
                            secondSeek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            break;
                    case 5: secondText.setText("3 Hours");
                            hoursBeforeSecondEvent =3;
                            minutesBeforeSecondEvent = 0;
                            writeCustom("second", 5);
                            secondSeek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
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




            /*newActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(newactint);
                }
            });*/



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //getMenuInflater().inflate(R.menu.menu, menu);
        // Create your menu...

        this.menu = menu;

        return true;
    }


    public boolean onTouchEvent(MotionEvent touchevent){
        if(!DL.isDrawerOpen(GravityCompat.START)){
            switch (touchevent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    x1 = touchevent.getX();
                    y1 = touchevent.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    x2 = touchevent.getX();
                    y2 = touchevent.getY();
                    if (x1 > x2){
                        startActivity(newactint);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }
                    break;
            }
        }
        return false;
    }



    public void createAlarms(ArrayList<MyDate> dateA, int hourB, int minuteB, boolean all){
        int dateCreated=0;

        for (int i = 0; i < dateA.size(); i++) {
            if(dateA.get(i) != null){
                Log.i(dateA.get(i).toString(),"s");

                if(dateA.get(i).getDay() != dateCreated || all){ //only creates for the first event of the day or if all button is pressed

                    Log.e("set alarm for", dateA.get(i).toString());
                    calculateTimeBeforeEvent(dateA.get(i),hourB);


                    alarmDays= new ArrayList<Integer>();
                    alarmDays.add((daybefore)?dateA.get(i).getDayOfWeek()-1:dateA.get(i).getDayOfWeek());
                    daybefore = false;
                    int hour = dateA.get(i).getHour();
                    int minute= dateA.get(i).getMinute();

                    if(minuteB > dateA.get(i).getMinute()){
                        hour -= 1;
                        minute = 60 - (minuteB - dateA.get(i).getMinute());
                    }
                    else{
                        minute -= minuteB;
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





                        if (openClockIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(openClockIntent);
                        }


                    if(getDeviceName().substring(0,7).equals("OnePlus")){    //if device is a oneplus
                        try{Thread.sleep(1500);}catch (InterruptedException e) {

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
        Log.e("second","in writecustom "+ save);
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


        if(two) {
            isTwoAlarms = 1;
        }else{
            isTwoAlarms = 0;
        }

        String save;
        save = firstBefore + " " + secondBefore + " " + isTwoAlarms;
        Log.e("second","in writecustom "+ save);
        writeToFile(save,MainActivity.this);
    }

    public void readCustom(){  //specifies what to read from file
        String current = readFromFile(MainActivity.this);
        String[] split = current.split(" ");

        if(split[2].equals("0")){
            Log.e("second","turned false from read");
            swticherBool = false;
            secondAlarmPressed = false;
            isTwoAlarms = 0;
            item.setTitle("Two Alarms");
            secondSeek.setVisibility(View.INVISIBLE);
            secondText.setVisibility(View.INVISIBLE);
            //menu.findItem(R.id.nav_twoalarms).setChecked(false);
            //menu.findItem(R.id.nav_twoalarms).setTitle("One Alarm");

        }else if (split[2].equals("1")){
            Log.e("second","turned true from read");
            swticherBool = true;
            secondAlarmPressed = true;
            isTwoAlarms = 1;
            item.setTitle("One Alarm");
            Log.i("second","gothere");
            secondSeek.setVisibility(View.VISIBLE);
            secondText.setVisibility(View.VISIBLE);
            //menu.findItem(R.id.nav_twoalarms).setChecked(true);
            //menu.findItem(R.id.nav_twoalarms).setChecked(true);
        }else{
            Log.e("second", "neither 1 or 0");
        }

        seek.setProgress(Integer.parseInt(split[0]));
        firstBefore = Integer.parseInt(split[0]);
        secondSeek.setProgress(Integer.parseInt(split[1]));
        secondBefore = Integer.parseInt(split[1]);

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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_gotocal:
                seek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                startActivity(newactint);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.nav_gotoclock:
                Intent intent;
                seek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
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
                break;
            case R.id.nav_twoalarms:
                seek.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                if(swticherBool){
                    menuItem.setTitle("Two Alarms");
                    Log.e("second","turned false from switch");
                    secondAlarmPressed = false;
                    swticherBool = false;
                    secondSeek.setVisibility(View.INVISIBLE);
                    secondText.setVisibility(View.INVISIBLE);
                    writeCustom(false);

                }else{
                    menuItem.setTitle("One Alarm");
                    Log.e("second","turned true from switch");
                    secondAlarmPressed = true;
                    swticherBool = true;
                    secondSeek.setVisibility(View.VISIBLE);
                    secondText.setVisibility(View.VISIBLE);
                    writeCustom(true);
                }
                break;
        }
        DL.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed(){   //overides the back button pressed to close drawer if open
        if(DL.isDrawerOpen(GravityCompat.START)){
            DL.closeDrawer(GravityCompat.START);
        }else{
        super.onBackPressed();
        }
    }
    public void changeSecondSeekFalse(){
        secondSeek.setVisibility(View.INVISIBLE);
        secondText.setVisibility(View.INVISIBLE);
        writeCustom(false);
    }
    public void changeSecondSeekTrue(){
        secondSeek.setVisibility(View.VISIBLE);
        secondText.setVisibility(View.VISIBLE);
        writeCustom(true);
    }

}
