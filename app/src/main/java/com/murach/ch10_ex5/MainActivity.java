package com.murach.ch10_ex5;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView messageTextView;
    Timer timer;
    FileIO fileIO;
    int timesDownloaded = 0;

    boolean timerRunning = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileIO = new FileIO(getApplicationContext());
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        startTimer();
    }

    @Override
    protected void onPause() {
        stopTimer();
        super.onPause();
    }

    private void startTimer() {
        final long startMillis = System.currentTimeMillis();
        timer = new Timer(true);
        TimerTask task = new TimerTask() {
            
            @Override
            public void run() {
                long elapsedMillis = System.currentTimeMillis() - startMillis;
                fileIO.downloadFile();
                timesDownloaded++;
                updateView(elapsedMillis);
            }
        };
        timer.schedule(task, 0, 10000);
        timerRunning = true;
    }

    private void stopTimer ( ) {
        timerRunning = false;
        timer.cancel();
    }

    private void updateView(final long elapsedMillis) {
        // UI changes need to be run on the UI thread
        messageTextView.post(new Runnable() {

            int elapsedSeconds = (int) elapsedMillis/1000;

            @Override
            public void run() {
                messageTextView.setText(String.format("File downloaded %d time(s)", timesDownloaded));
            }
        });
    }

    public void toggleTimerClick(View view) {
        Button button = (Button) view;
        if (!timerRunning) {
            startTimer();
            button.setText("Stop");
        } else {
            stopTimer();
            button.setText("Start");
        }
    }
}