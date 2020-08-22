package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static boolean isCounting = false;
    public static long initTime;
    long actualInitTime;
    Button startStopBut, pauseBut;
    double pauseOffset = 0;
    public static boolean pauseMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startStopBut = (Button)findViewById(R.id.startstopBut);
        final WatchView watchView = (WatchView)findViewById(R.id.watchView);

        pauseBut = (Button)findViewById(R.id.pauseBut);
        pauseBut.setVisibility(View.INVISIBLE);
        startStopBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTime = SystemClock.elapsedRealtime();
                actualInitTime = initTime;

                if(pauseMode){
                    isCounting = false;
                    pauseMode = false;
                    pauseBut.setText("PAUSE");
                }else{
                    isCounting = !isCounting;
                }
                if(isCounting) {
                    startStopBut.setText("STOP");
                    pauseBut.setVisibility(View.VISIBLE);


                }
                else {
                    initTime  = SystemClock.elapsedRealtime();
                    startStopBut.setText("START");
                    pauseBut.setVisibility(View.INVISIBLE);
                }
            }
        });

        pauseBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMode = !pauseMode;
                isCounting = !isCounting;
                if(pauseMode){
                    pauseBut.setText("RESUME");
                }else{
                    pauseBut.setText("PAUSE");
                }
                if(pauseMode){
                    pauseOffset = (double)(SystemClock.elapsedRealtime());
                }else{
                    initTime += SystemClock.elapsedRealtime() - pauseOffset;
                    pauseOffset = 0;
                }

            }
        });
    }
}