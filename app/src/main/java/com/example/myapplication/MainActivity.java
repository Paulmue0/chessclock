package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 600000;

    private TextView mTextViewCountDownPlayer1;
    private TextView mTextViewCountDownPlayer2;
    private Button mButtonStartPausePlayer1;
    private Button mButtonStartPausePlayer2;
    private Button mButtonReset;
    private Button mButtonPlayPause;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis_Player1 = START_TIME_IN_MILLIS;
    private long mTimeLeftInMillis_Player2 = START_TIME_IN_MILLIS;

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
                } else if (player) {
                    startTimer();
                    player = false;
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
                } else if (!player) {
                    startTimer();
                    player = true;
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

        updateCountDownText();
    }


    private void startTimer() {
        mCountDownTimer = new CountDownTimer(getCurrentTimeLeft(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setCurrentTimeLeft(millisUntilFinished);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPausePlayer1.setText("Zeit abgelaufen");
                mButtonStartPausePlayer2.setText("Zeit abgelaufen");
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

    private void playOrPauseGame() {
        if (mTimerRunning) {
            pauseTimer();
        } else {
            startTimer();
        }

    }
}