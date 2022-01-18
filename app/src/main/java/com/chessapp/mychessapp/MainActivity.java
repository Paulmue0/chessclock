package com.chessapp.mychessapp;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.mychessapp.R;

import java.util.Locale;
import java.util.Objects;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageButton;

@Keep
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
    private boolean mIsPaused;
    boolean isFinished;

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
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

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
        mButtonStartPausePlayer1.setOnClickListener(v -> {
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
        });

        mButtonStartPausePlayer2 = findViewById(R.id.startstop_player2);
        mButtonStartPausePlayer2.setOnClickListener(v -> {
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
        });

        mButtonReset.setOnClickListener(v -> {
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

        });

        mButtonPlayPause.setOnClickListener(view -> playOrPauseGame());

        mNumberPicker = findViewById(R.id.picker);
        String[] arrayString= new String[]{"1 + 0","3 | 2","5 | 3","10 | 5","15 | 10"};
        mNumberPicker.setMinValue(0);
        mNumberPicker.setMaxValue(arrayString.length-1);
        mNumberPicker.setDisplayedValues(arrayString);
        mNumberPicker.setValue(3);


        mNumberPicker.setFormatter(value -> arrayString[value]);
        mNumberPicker.setOnValueChangedListener((numberPicker, oldValue, newValue) -> {
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
        });
        updateCountDownText();

        if(savedInstanceState != null){
            player = savedInstanceState.getBoolean("player");
            isFinished = savedInstanceState.getBoolean("isFinished");
            mTimerRunning = savedInstanceState.getBoolean("timerRunning");
            mIsPaused = savedInstanceState.getBoolean("isPaused");
            mEndTimePlayer1 = savedInstanceState.getLong("endTimePlayer1");
            mEndTimePlayer2 = savedInstanceState.getLong("endTimePlayer2");
            mTimeLeftInMillis_Player1 = savedInstanceState.getLong("millisLeftPlayer1");
            mTimeLeftInMillis_Player2 = savedInstanceState.getLong("millisLeftPlayer2");
            turnCnt = savedInstanceState.getShort("mTurnCnt");
            INCREMENT_TIME_IN_MILLIS = savedInstanceState.getShort("incrementTime");
            START_TIME_IN_MILLIS = savedInstanceState.getLong("startingTime");
            player = !player;
            updateCountDownText();
            player = !player;
            if (mTimerRunning | mIsPaused) {
                mButtonPlayPause.setVisibility(View.VISIBLE);
                mButtonReset.setVisibility(View.VISIBLE);
                mNumberPicker.setVisibility(View.INVISIBLE);
                mTextViewCountDownPlayer1.setVisibility(View.VISIBLE);
                mTextViewCountDownPlayer2.setVisibility(View.VISIBLE);
                if (player){
                    if (!mIsPaused) mTimeLeftInMillis_Player1 = mEndTimePlayer1 - System.currentTimeMillis();
                    mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_flieg_aseprite);
                    mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_thinking);
                }
                else{
                   if (!mIsPaused) mTimeLeftInMillis_Player2 = mEndTimePlayer2 - System.currentTimeMillis();
                    mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_thinking);
                    mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_flieg_aseprite);
                }


                updateCountDownText();
                if (!mIsPaused){
                    mButtonPlayPause.setImageResource(R.drawable.pause_button);
                    startTimer();
                }
                else{
                    mButtonPlayPause.setImageResource(R.drawable.play_button);
                    mButtonStartPausePlayer1.setClickable(false);
                    mButtonStartPausePlayer2.setClickable(false);

                    if(player){
                        try {
                            ((GifDrawable)mButtonStartPausePlayer1.getDrawable()).stop();

                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        try {
                            ((GifDrawable)mButtonStartPausePlayer2.getDrawable()).stop();

                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }

            }
            else if(isFinished){
                resetTimer();
            }
            else{
                mNumberPicker.setVisibility(View.VISIBLE);
                mButtonReset.setVisibility(View.INVISIBLE);
                mButtonPlayPause.setVisibility(View.INVISIBLE);
                System.out.println(mTimeLeftInMillis_Player1);
                System.out.println(mTimeLeftInMillis_Player2);

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

    @SuppressLint("SetTextI18n")
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
        else {
            mEndTimePlayer2 = System.currentTimeMillis() + mTimeLeftInMillis_Player2;
            if (mEndTimePlayer1 == 0) {
                mEndTimePlayer1 = System.currentTimeMillis() + mTimeLeftInMillis_Player1;
            }
        }


        mCountDownTimer = new CountDownTimer(getCurrentTimeLeft(), 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                setCurrentTimeLeft(millisUntilFinished);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                isFinished = true;
                mButtonStartPausePlayer1.setClickable(false);
                mButtonStartPausePlayer2.setClickable(false);
                mButtonPlayPause.setClickable(false);
                mButtonReset.setVisibility(View.VISIBLE);
                if(player){
                    mButtonStartPausePlayer2.setImageResource(R.drawable.pelikan_flieg_aseprite_winning);
                }
                else{
                    mButtonStartPausePlayer1.setImageResource(R.drawable.pelikan_flieg_aseprite_winning);
                }
                player = false;
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

        mEndTimePlayer1 = 0;
        mEndTimePlayer2 = 0;

        mTextViewCountDownPlayer1.setTextColor(Color.GRAY);
        mTextViewCountDownPlayer2.setTextColor(Color.GRAY);

        isFinished = false;
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
        mButtonStartPausePlayer2.setClickable(true);
        mButtonStartPausePlayer2.setVisibility(View.VISIBLE);
        mButtonStartPausePlayer2.setClickable(true);

        mButtonPlayPause.setClickable(true);

        mButtonStartPausePlayer1.setImageResource(R.drawable.player1_loading_screen);
        mButtonStartPausePlayer2.setImageResource(R.drawable.player2_loading_screen);


    }

    private void updateCountDownText() {
        if (player) {
            int minutes = (int) (mTimeLeftInMillis_Player1 / 1000) / 60;
            int seconds = (int) (mTimeLeftInMillis_Player1 / 1000) % 60;
            int millis = (int) (((mTimeLeftInMillis_Player1) %1000)/100);

            String timeLeftFormatted;
            if(minutes < 1){
                timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, millis);
                mTextViewCountDownPlayer1.setTextColor(0xffe42f03);
            }
            else{
                timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            }


            mTextViewCountDownPlayer1.setText(timeLeftFormatted);
        } else {
            int minutes = (int) (mTimeLeftInMillis_Player2 / 1000) / 60;
            int seconds = (int) (mTimeLeftInMillis_Player2 / 1000) % 60;
            int millis = (int) (((mTimeLeftInMillis_Player2) %1000)/100);

            String timeLeftFormatted;
            if(minutes < 1){
                timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, millis);
                mTextViewCountDownPlayer2.setTextColor(0xffe42f03);
            }
            else{
                timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            }
            mTextViewCountDownPlayer2.setText(timeLeftFormatted);
        }
    }

    private void playOrPauseGame() {
        if (mTimerRunning) {
            mButtonPlayPause.setImageResource(R.drawable.play_button);
            mButtonStartPausePlayer1.setClickable(false);
            mButtonStartPausePlayer2.setClickable(false);
            pauseTimer();
            mIsPaused = true;
            if(player){
                try {
                    ((GifDrawable)mButtonStartPausePlayer1.getDrawable()).stop();

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    ((GifDrawable)mButtonStartPausePlayer2.getDrawable()).stop();

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            mButtonPlayPause.setImageResource(R.drawable.pause_button);
            mButtonStartPausePlayer1.setClickable(true);
            mButtonStartPausePlayer2.setClickable(true);
            startTimer();
            mIsPaused = false;
            if(player){
                try {
                    ((GifDrawable)mButtonStartPausePlayer1.getDrawable()).start();

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    ((GifDrawable)mButtonStartPausePlayer2.getDrawable()).start();

                }catch (Exception e) {
                    e.printStackTrace();
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
        outState.putBoolean("isFinished", isFinished);
        outState.putBoolean("isPaused", mIsPaused);
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