package com.example.stopwatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class WatchView extends View {
    private int radius=0, height=0,width=0,padding=0,fontSize=0,numericalSpacing=0;
    private int handTruncation = 0,hourHandtruncation=0;
    private Paint paint;
    private boolean isInit;
    double pauseOffset = 0;
    private int[] numbers = {1,2,3,4,5,6,7,8,9,10,11,12};
    double curLockMin, curLockSec;
    private Rect rect = new Rect();
    private Canvas canvas;
    public WatchView(Context context) {
        super(context);
    }

    public WatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(){
         height = getHeight();
         width = getWidth();
         padding = numericalSpacing + 50;
         fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics());
         int min = Math.min(height,width);
         radius = min/2 - padding;
         handTruncation = min/20;
         paint = new Paint();
         isInit = true;



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDraw(Canvas canvas) {
        if(!isInit){
            init();
            isInit = true;
        }
        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        drawCircle(canvas, false);
        drawCircle(canvas, true);
        drawCenter(canvas);
        drawNumeral(canvas,true);
        drawNumeral(canvas, false);
        drawHands(canvas);

        postInvalidateDelayed(500);
        invalidate();
    }

    private void drawCircle(Canvas canvas, boolean isMinute) {
            int radius = isMinute?this.radius/2:this.radius;

            paint.reset();
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            canvas.drawCircle(width/2,height/2,radius + padding - 10 , paint );
    }
    private void drawCenter(Canvas canvas){
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(width/2,height/2, 12 ,paint);

    }
    private void drawNumeral(Canvas canvas, boolean isMin){
           paint.setTextSize(fontSize);
        paint.setColor(Color.BLACK);
        int radius = isMin?this.radius/2:this.radius;
           for(int n : numbers){

               String numString = ""+ 5*n;
               if(n==12){
                   numString = "0";
               }
               paint.getTextBounds(numString,0,numString.length(),rect);

               double angle = Math.PI/6 * (n -3);
               int x = (int)(width/2 + Math.cos(angle)* radius - rect.width()/2);
               int y = (int)(height/2 + Math.sin(angle)* radius + rect.height()/2);
               canvas.drawText(numString,x,y,paint);
           }
    }
    private void drawHand(Canvas canvas, double loc, boolean isMin){
            double angle  =Math.PI* loc/30 - Math.PI/2;
            int handRadius = isMin?(radius - handTruncation)/2:radius-handTruncation;
            canvas.drawLine(width/2, height /2, (float)(width/2 + Math.cos(angle)* handRadius),(float)(height/2 + Math.sin(angle)* handRadius),paint);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void drawHands(Canvas canvas){
        Calendar c = Calendar.getInstance();
        if(MainActivity.isCounting) {
            float hour = c.get(Calendar.HOUR_OF_DAY);
            hour = hour > 12 ? hour - 12 : hour;

            drawHand(canvas, (double)(SystemClock.elapsedRealtime()-MainActivity.initTime)/1000/60, true);
            drawHand(canvas, (double)(SystemClock.elapsedRealtime()-MainActivity.initTime)/1000, false);
            curLockMin = (double)(SystemClock.elapsedRealtime()-MainActivity.initTime)/1000/60;
            curLockSec = (double)(SystemClock.elapsedRealtime()-MainActivity.initTime)/1000;

        }
        if(MainActivity.pauseMode && !MainActivity.isCounting){
            drawHand(canvas, (double)curLockMin, true);
            drawHand(canvas, (double)(curLockSec), false);

        }
        else if(!MainActivity.isCounting){
            drawHand(canvas, 0  ,true);
            drawHand(canvas, 0 ,false);
        }



    }



}
