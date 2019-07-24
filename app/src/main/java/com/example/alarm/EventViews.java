package com.example.alarm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class EventViews extends View {

    private Paint _paintDoodle = new Paint();
    private MyDate currentDate;

    private String mExampleString;
    private  String mExampleString2;
    private int mExampleColor = Color.RED;
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
    }

    public EventViews(Context context, AttributeSet attrs,MyDate date) {
        super(context, attrs);
        currentDate = date;
        init(attrs,0);
    }

    public EventViews(Context context, AttributeSet attrs, int defStyleAttr, MyDate date) {
        super(context, attrs, defStyleAttr);
        currentDate = date;
        init(attrs,defStyleAttr);
    }


    private void init (AttributeSet attrs, int defstyle){
        _paintDoodle.setColor(Color.DKGRAY);

        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        mExampleString = currentDate.getName();
        mExampleString2 = currentDate.getHour() +":"+currentDate.getMinute() + " - " + currentDate.getEndHour()+":"+currentDate.getEndMinute();

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(25);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);
        mTextWidth2 = mTextPaint.measureText(mExampleString2);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawRect(0,0,getWidth(),getHeight(),_paintDoodle);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        canvas.drawText(mExampleString,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

        canvas.drawText(mExampleString2,
                paddingLeft + (contentWidth - mTextWidth2) / 2,
                paddingTop+30 + (contentHeight + mTextHeight) / 2,
                mTextPaint);
    }

    public MyDate getCurrentDate() {
        return currentDate;
    }
}
