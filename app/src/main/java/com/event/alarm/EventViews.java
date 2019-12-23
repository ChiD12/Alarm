package com.event.alarm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class EventViews extends View {

    private Paint _paintDoodle = new Paint();

    private MyDate currentDate;

    private String mExampleString;
    private  String mExampleString2;
    private  String mExampleString3;
    private int StringName;
    private final int def = Color.DKGRAY; //null
    Context context = getContext();
    private final int LAVENDAR = ContextCompat.getColor(context, R.color.Lavendar); //1
    private final int TURQUOISE  = ContextCompat.getColor(context, R.color.Turquoise); //2
    private final int LIGHTPURPLE  = ContextCompat.getColor(context, R.color.LightPurple); //3
    private final int LIGHTRED  = ContextCompat.getColor(context, R.color.LightRed); //4
    private final int YELLOW  = ContextCompat.getColor(context, R.color.Yellow); //5
    private final int ORANGE  = ContextCompat.getColor(context, R.color.Orange); //6
    private final int LIGHTBLUE  = ContextCompat.getColor(context, R.color.LightBlue); //7
    private final int GRAY  = ContextCompat.getColor(context, R.color.Gray); //8
    private final int DARKBLUE  = ContextCompat.getColor(context, R.color.DarkBlue); //9
    private final int GREEN  = ContextCompat.getColor(context, R.color.Green); //10
    private final int RED  = ContextCompat.getColor(context, R.color.Red); //11

    StringBuilder sb;



    private float mExampleDimension = 0;
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextWidth2;
    private float mTextHeight;

    public EventViews(Context context, MyDate date){
        super(context);
        currentDate = date;
        init(null,0);
        sb = new StringBuilder();
    }

    public EventViews(Context context, AttributeSet attrs,MyDate date) {
        super(context, attrs);
        currentDate = date;
        init(attrs,0);
        sb = new StringBuilder();
    }

    public EventViews(Context context, AttributeSet attrs, int defStyleAttr, MyDate date) {
        super(context, attrs, defStyleAttr);
        currentDate = date;
        init(attrs,defStyleAttr);
        sb = new StringBuilder();
    }


    private void init (AttributeSet attrs, int defstyle){
        int color = currentDate.getColor();
        switch (color){
            case 0:
                _paintDoodle.setColor(def);
                break;
            case 1:
                _paintDoodle.setColor(LAVENDAR);
                break;
            case 2:
                _paintDoodle.setColor(TURQUOISE);
                break;
            case 3:
                _paintDoodle.setColor(LIGHTPURPLE);
                break;
            case 4:
                _paintDoodle.setColor(LIGHTRED);
                break;
            case 5:
                _paintDoodle.setColor(YELLOW);
                break;
            case 6:
                _paintDoodle.setColor(ORANGE);
                break;
            case 7:
                _paintDoodle.setColor(LIGHTBLUE);
                break;
            case 8:
                _paintDoodle.setColor(GRAY);
                break;
            case 9:
                _paintDoodle.setColor(DARKBLUE);
                break;
            case 10:
                _paintDoodle.setColor(GREEN);
                break;
            case 11:
                _paintDoodle.setColor(RED);
                break;

        }
        if(color == 0)
            StringName = Color.WHITE;
        else
            StringName = Color.BLACK;

        //_paintDoodle.setColor(Color.DKGRAY);

        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        mExampleString = currentDate.getName();
        if(currentDate.getHour()>12){
            mExampleString2 = currentDate.getHour()-12 +":"+currentDate.getMinute();
        }else{
            mExampleString2 = currentDate.getHour() +":"+currentDate.getMinute();
        }
        if(currentDate.getEndHour() > 12){
            mExampleString3 = currentDate.getEndHour()-12+":"+currentDate.getEndMinute();
        }else{
            mExampleString3 = currentDate.getEndHour()+":"+currentDate.getEndMinute();
        }




        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(25);
        mTextPaint.setColor(StringName);
        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        mTextWidth = mTextPaint.measureText(mExampleString);
        mTextWidth2 = mTextPaint.measureText(mExampleString2);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //_paintDoodle.setStrokeWidth(1);
        //_paintDoodle.setStyle(Paint.Style.STROKE);
        //canvas.drawRect(0,0,getWidth(),getHeight(),_paintDoodle);
        canvas.drawRoundRect(5,10,getWidth()-5,getHeight()-5,20,20,_paintDoodle);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        if(contentWidth <  mTextWidth){
            String[] sArray = mExampleString.split(" ");
            double half= Math.ceil((double)sArray.length/2);
            sb.delete(0,sb.length());
            for (int i = 0; i < half; i++) {
                sb.append(sArray[i]);
                if(i != half-1){
                    sb.append(" ");
                }
            }

            float firstLineWidth = mTextPaint.measureText(sb.toString());
            canvas.drawText(sb.toString(),
                    paddingLeft + (contentWidth - firstLineWidth) / 2,
                    paddingTop -30 + (contentHeight + mTextHeight) / 2,
                    mTextPaint);
            sb.delete(0,sb.length());
            for (int i = (int)half; i < sArray.length; i++) {
                sb.append(sArray[i]);
                if(i != half-1) {
                    sb.append(" ");
                }
            }
            float secondLineWidth = mTextPaint.measureText(sb.toString());
            canvas.drawText(sb.toString(),
                    paddingLeft + (contentWidth - secondLineWidth) / 2,
                    paddingTop + (contentHeight + mTextHeight) / 2,
                    mTextPaint);


        }else{
            canvas.drawText(mExampleString,
                    paddingLeft + (contentWidth - mTextWidth) / 2,
                    paddingTop + (contentHeight + mTextHeight) / 2,
                    mTextPaint);

        }



        canvas.drawText(mExampleString2,
                paddingLeft + (contentWidth - mTextWidth2) / 2,
                paddingTop+30 + (contentHeight + mTextHeight) / 2,
                mTextPaint);

        float mTextWidth3 = mTextPaint.measureText(mExampleString3);
        canvas.drawText(mExampleString3,
                paddingLeft + (contentWidth - mTextWidth3) / 2,
                paddingTop+60 + (contentHeight + mTextHeight) / 2,
                mTextPaint);

    }

    public MyDate getCurrentDate() {
        return currentDate;
    }
}
