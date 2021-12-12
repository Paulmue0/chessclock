package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageButton;

public class MainActivity extends AppCompatActivity {
    private long START_TIME_IN_MILLIS = 600000;
    private short INCREMENT_TIME_IN_MILLIS = 0;

    private TextView mTextViewCountDownPlayer1;
    private TextView mTextViewCountDownPlayer2;
    private GifImageButton mButtonStartPausePlayer1;
    private GifImageButton mButtonStartPausePlayer2;
    private ImageButton mButtonReset;
    private ImageButton mButtonPlayPause;
    private NumberPicker mNumberPicker;


    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis_Player1 = START_TIME_IN_MILLIS;
    private long mTimeLeftInMillis_Player2 = START_TIME_IN_MILLIS;
    private long mEndTimePlayer1;
    private long mEndTimePlayer2;

    //if True == Player 1
    //If False == Player 2
    private boolean player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_main);
        // Set Starting Player
        player = true;
        mTextViewCountDownPlayer1 = findViewById(R.id.counttime_player1);
        mTextViewCountDownPlayer2 = findViewById(R.id.counttime_player2);

        mButtonStartPausePlayer1 = findViewById(R.id.startstop_player1);
        mButtonStartPausePlayer2 = findViewById(R.id.startstop_player2);
        mButtonReset = findViewById(R.id.reset);
        mButtonPlayPause = findViewById(R.id.playPause);

        mButtonStartPausePlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning && player) {
                    pauseTimer();
                    player = false;
                    startTimer();
                    //mButtonStartPausePlayer1.setScaleType(ImageView.ScaleType.CENTER);
                    mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_wasser);
                    //mButtonStartPausePlayer2.setScaleType(null);
                    mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_raw);
                } else if (player) {
                    startTimer();
                    player = false;
                    //mButtonStartPausePlayer1.setScaleType(ImageView.ScaleType.CENTER);
                    mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_wasser);
                    //mButtonStartPausePlayer2.setScaleType(null);
                    mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_raw);
                }
            }
        });
        mButtonStartPausePlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning && !player) {
                    pauseTimer();
                    player = true;
                    startTimer();
                    //mButtonStartPausePlayer1.setScaleType(null);
                    mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_raw);
                    //mButtonStartPausePlayer2.setScaleType(ImageView.ScaleType.CENTER);
                    mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_wasser);
                } else if (!player) {
                    startTimer();
                    player = false;
                    //mButtonStartPausePlayer1.setScaleType(null);
                    mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_raw);
                    //mButtonStartPausePlayer2.setScaleType(ImageView.ScaleType.CENTER);
                    mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_wasser);
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        mButtonPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { playOrPauseGame(); }
        });

        mNumberPicker = findViewById(R.id.picker);
        String[] arrayString= new String[]{"1 + 0","3 | 2","5 | 3","10 | 5","15 | 10"};
        mNumberPicker.setMinValue(0);
        mNumberPicker.setMaxValue(arrayString.length-1);
       // mNumberPicker.setValue(3);

        mNumberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                // TODO Auto-generated method stub
                return arrayString[value];
            }
        });
        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                if (newValue == 0){
                    START_TIME_IN_MILLIS = 60000;
                    INCREMENT_TIME_IN_MILLIS = 0;
                }
                else if(newValue == 1){
                    START_TIME_IN_MILLIS = 180000;
                    INCREMENT_TIME_IN_MILLIS = 3000;
                }
                else if(newValue == 2){
                    START_TIME_IN_MILLIS = 300000;
                    INCREMENT_TIME_IN_MILLIS = 5000;
                }
                else if(newValue == 3){
                    START_TIME_IN_MILLIS = 600000;
                    INCREMENT_TIME_IN_MILLIS = 5000;
                }
                else if(newValue == 4){
                    START_TIME_IN_MILLIS = 900000;
                    INCREMENT_TIME_IN_MILLIS = 15000;
                }
                resetTimer();
            }
        });
        mNumberPicker.setValue(3);

        updateCountDownText();
    }


    private void startTimer() {
        if (player)
            mEndTimePlayer1 = System.currentTimeMillis() + mTimeLeftInMillis_Player1;
        else mEndTimePlayer2 = System.currentTimeMillis() + mTimeLeftInMillis_Player2;

        mCountDownTimer = new CountDownTimer(getCurrentTimeLeft(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setCurrentTimeLeft(millisUntilFinished);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPausePlayer1.setVisibility(View.INVISIBLE);
                mButtonStartPausePlayer2.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();

        mTimerRunning = true;
    }

    private void setCurrentTimeLeft(long millisUntilFinished) {
        if (player) {
            mTimeLeftInMillis_Player1 = millisUntilFinished;
        } else {
            mTimeLeftInMillis_Player2 = millisUntilFinished;
        }
    }

    private long getCurrentTimeLeft() {
        if (player)
            return mTimeLeftInMillis_Player1;
        return mTimeLeftInMillis_Player2;
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonReset.setVisibility(View.VISIBLE);




    }

    private void resetTimer() {
        mTimeLeftInMillis_Player1 = START_TIME_IN_MILLIS;
        mTimeLeftInMillis_Player2 = START_TIME_IN_MILLIS;
        try {
            mCountDownTimer.cancel();
        } catch (Exception e) {

        }

        player = false;
        updateCountDownText();
        player = true;
        updateCountDownText();

        //mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPausePlayer1.setVisibility(View.VISIBLE);
        mButtonStartPausePlayer2.setVisibility(View.VISIBLE);

        mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_wasser);
        mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_wasser);
    }

    private void updateCountDownText() {
        if (player) {
            int minutes = (int) (mTimeLeftInMillis_Player1 / 1000) / 60;
            int seconds = (int) (mTimeLeftInMillis_Player1 / 1000) % 60;
            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            mTextViewCountDownPlayer1.setText(timeLeftFormatted);
        } else {
            int minutes = (int) (mTimeLeftInMillis_Player2 / 1000) / 60;
            int seconds = (int) (mTimeLeftInMillis_Player2 / 1000) % 60;
            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            mTextViewCountDownPlayer2.setText(timeLeftFormatted);
        }
    }

    private void updateButtons() {
       //TODO: Add visibility of buttons
    }

    private void playOrPauseGame() {
        if (mTimerRunning) {
            pauseTimer();
            if(player){
                try {
                    ((GifDrawable)mButtonStartPausePlayer1.getDrawable()).stop();

                }catch (Exception e) {
                    System.out.println("Fehler beim stoppen des Gifs");
                    System.out.println(e);
                }
            }
            else{
                try {
                    ((GifDrawable)mButtonStartPausePlayer2.getDrawable()).stop();

                }catch (Exception e) {
                    System.out.println("Fehler beim starten des Gifs");
                    System.out.println(e);
                }
            }

        } else {
            startTimer();
            if(player){
                try {
                    ((GifDrawable)mButtonStartPausePlayer1.getDrawable()).start();

                }catch (Exception e) {
                    System.out.println("Fehler beim starten des Gifs");
                    System.out.println(e);
                }
            }
            else{
                try {
                    ((GifDrawable)mButtonStartPausePlayer2.getDrawable()).start();

                }catch (Exception e) {
                    System.out.println("Fehler beim starten des Gifs");
                    System.out.println(e);
                }
            }

        }


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("millisLeftPlayer1", mTimeLeftInMillis_Player1);
        outState.putLong("millisLeftPlayer2", mTimeLeftInMillis_Player2);
        outState.putBoolean("timerRunning", mTimerRunning);
        outState.putBoolean("player", player);
        outState.putLong("endTimePlayer1", mEndTimePlayer1);
        outState.putLong("endTimePlayer2", mEndTimePlayer2);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updateCountDownText();
        updateButtons();

        if (mTimerRunning) {
            mEndTimePlayer1 = savedInstanceState.getLong("endTimePlayer1");
            mEndTimePlayer2 = savedInstanceState.getLong("endTimePlayer2");
            player = savedInstanceState.getBoolean("player");
            mTimeLeftInMillis_Player1 = mEndTimePlayer1 - System.currentTimeMillis();
            mTimeLeftInMillis_Player2 = mEndTimePlayer2 - System.currentTimeMillis();
            startTimer();
        }
    }
}