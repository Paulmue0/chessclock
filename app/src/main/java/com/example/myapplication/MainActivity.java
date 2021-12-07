package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

import pl.droidsonroids.gif.GifImageButton;

public class MainActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 600000;

    private TextView mTextViewCountDownPlayer1;
    private TextView mTextViewCountDownPlayer2;
    private GifImageButton mButtonStartPausePlayer1;
    private GifImageButton mButtonStartPausePlayer2;
    private Button mButtonReset;
    private Button mButtonPlayPause;
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

        mNumberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                // TODO Auto-generated method stub
                return arrayString[value];
            }
        });

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
        mCountDownTimer.cancel();
        player = false;
        updateCountDownText();
        player = true;
        updateCountDownText();

        //mButtonReset.setVisibility(View.INVISIBLE);
        mButtonStartPausePlayer1.setVisibility(View.VISIBLE);
        mButtonStartPausePlayer2.setVisibility(View.VISIBLE);
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
        if (mTimerRunning) {
            mButtonPlayPause.setText("Pause");
        } else {
            mButtonPlayPause.setText("Start");
        }
    }

    private void playOrPauseGame() {
        if (mTimerRunning) {
            pauseTimer();
        } else {
            startTimer();
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