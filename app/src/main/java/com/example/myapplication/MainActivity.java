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
    private TextView mTurnCounter1;
    private TextView mTurnCounter2;
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
    private short turnCnt;

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

        mTextViewCountDownPlayer1 = findViewById(R.id.counttime_player1);
        mTextViewCountDownPlayer2 = findViewById(R.id.counttime_player2);


        mTurnCounter1 = findViewById(R.id.turnCounter);
        mTurnCounter1.setVisibility(View.INVISIBLE);

        mTurnCounter2 = findViewById(R.id.turnCounter2);
        mTurnCounter2.setVisibility(View.INVISIBLE);

        mButtonReset = findViewById(R.id.reset);
        mButtonReset.setVisibility(View.INVISIBLE);

        mButtonPlayPause = findViewById(R.id.playPause);
        mButtonPlayPause.setVisibility(View.INVISIBLE);

        mButtonStartPausePlayer1 = findViewById(R.id.startstop_player1);
        mButtonStartPausePlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning && player) {
                    pauseTimer();
                    addIncrement();
                    player = false;
                    startTimer();
                    //mButtonStartPausePlayer1.setScaleType(ImageView.ScaleType.CENTER);
                    mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_thinking);
                    //mButtonStartPausePlayer2.setScaleType(null);
                    mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_flieg);
                    turnCnt++;
                    updateCounter();
                } else if (player) {
                    startTimer();
                    player = false;
                    //mButtonStartPausePlayer1.setScaleType(ImageView.ScaleType.CENTER);
                    mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_thinking);
                    //mButtonStartPausePlayer2.setScaleType(null);
                    mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_flieg);

                    mButtonReset.setVisibility(View.VISIBLE);
                    mButtonPlayPause.setVisibility(View.VISIBLE);
                    mNumberPicker.setVisibility(View.INVISIBLE);
                }
            }
        });

        mButtonStartPausePlayer2 = findViewById(R.id.startstop_player2);
        mButtonStartPausePlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning && !player) {
                    pauseTimer();
                    addIncrement();
                    player = true;
                    startTimer();
                    //mButtonStartPausePlayer1.setScaleType(null);
                    mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_flieg);
                    //mButtonStartPausePlayer2.setScaleType(ImageView.ScaleType.CENTER);
                    mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_thinking);
                } else if (!player) {
                    startTimer();
                    player = true;
                    //mButtonStartPausePlayer1.setScaleType(null);
                    mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_flieg);
                    //mButtonStartPausePlayer2.setScaleType(ImageView.ScaleType.CENTER);
                    mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_thinking);

                    mTurnCounter1.setVisibility(View.VISIBLE);
                    mTurnCounter2.setVisibility(View.VISIBLE);
                    mButtonReset.setVisibility(View.VISIBLE);
                    mButtonPlayPause.setVisibility(View.VISIBLE);
                    mNumberPicker.setVisibility(View.INVISIBLE);
                    updateCounter();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTurnCounter1.setVisibility(View.INVISIBLE);
                mTurnCounter2.setVisibility(View.INVISIBLE);
                mButtonReset.setVisibility(View.INVISIBLE);
                mButtonPlayPause.setVisibility(View.INVISIBLE);
                mButtonPlayPause.setImageResource(R.drawable.pause_button);
                mNumberPicker.setVisibility(View.VISIBLE);

                if(!mTimerRunning)
                    playOrPauseGame();

                turnCnt = 0;
                updateCounter();
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
        mNumberPicker.setValue(3);

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
                    INCREMENT_TIME_IN_MILLIS = 2000;
                }
                else if(newValue == 2){
                    START_TIME_IN_MILLIS = 300000;
                    INCREMENT_TIME_IN_MILLIS = 3000;
                }
                else if(newValue == 3){
                    START_TIME_IN_MILLIS = 600000;
                    INCREMENT_TIME_IN_MILLIS = 5000;
                }
                else if(newValue == 4){
                    START_TIME_IN_MILLIS = 900000;
                    INCREMENT_TIME_IN_MILLIS = 10000;
                }
                resetTimer();
            }
        });
        mNumberPicker.setValue(3);

        if(savedInstanceState != null){
            player = savedInstanceState.getBoolean("player");
            mTimerRunning = savedInstanceState.getBoolean("timerRunning");
            mEndTimePlayer1 = savedInstanceState.getLong("endTimePlayer1");
            mEndTimePlayer2 = savedInstanceState.getLong("endTimePlayer2");
            mTimeLeftInMillis_Player1 = savedInstanceState.getLong("millisLeftPlayer1");
            mTimeLeftInMillis_Player2 = savedInstanceState.getLong("millisLeftPlayer2");
            turnCnt = savedInstanceState.getShort("mTurnCnt");
            INCREMENT_TIME_IN_MILLIS = savedInstanceState.getShort("incrementTime");
            START_TIME_IN_MILLIS = savedInstanceState.getLong("startingTime");

            if (mTimerRunning) {
                mTimeLeftInMillis_Player1 = mEndTimePlayer1 - System.currentTimeMillis();
                mTimeLeftInMillis_Player2 = mEndTimePlayer2 - System.currentTimeMillis();
                mButtonPlayPause.setVisibility(View.VISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
                mNumberPicker.setVisibility(View.INVISIBLE);
                if (player){
                    mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_flieg);
                    mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_thinking);
                }
                else{
                    mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_thinking);
                    mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_flieg);
                }


                updateCountDownText();
                startTimer();
            }
            else{
                mNumberPicker.setVisibility(View.VISIBLE);
                mButtonReset.setVisibility(View.INVISIBLE);
                mButtonPlayPause.setVisibility(View.INVISIBLE);

                updateCountDownText();
            }
        }
        else {
            // Set Starting Player
            player = false;
            turnCnt = 0;
        }

        updateCountDownText();
    }

    private void updateCounter() {
        mTurnCounter1.setText("Turns: " + turnCnt);
        mTurnCounter2.setText("Turns: " + turnCnt);
    }

    private void addIncrement() {
        if (player){
            mTimeLeftInMillis_Player1 += INCREMENT_TIME_IN_MILLIS;
        }
        else
            mTimeLeftInMillis_Player2 += INCREMENT_TIME_IN_MILLIS;
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
            e.printStackTrace();
        }
        mTimerRunning = false;
        player = true;
        updateCountDownText();

        player = false;
        updateCountDownText();


        //mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPausePlayer1.setVisibility(View.VISIBLE);
        mButtonStartPausePlayer2.setVisibility(View.VISIBLE);

        mButtonStartPausePlayer1.setImageResource(R.drawable.player1_loading_screen);
        mButtonStartPausePlayer2.setImageResource(R.drawable.player2_loading_screen);


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
            mButtonPlayPause.setImageResource(R.drawable.play_button);
            mButtonStartPausePlayer1.setClickable(false);
            mButtonStartPausePlayer2.setClickable(false);
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
            mButtonPlayPause.setImageResource(R.drawable.pause_button);
            mButtonStartPausePlayer1.setClickable(true);
            mButtonStartPausePlayer2.setClickable(true);
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
        outState.putShort("incrementTime", INCREMENT_TIME_IN_MILLIS);
        outState.putLong("startingTime", START_TIME_IN_MILLIS);
        outState.putShort("mTurnCnt", turnCnt);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}