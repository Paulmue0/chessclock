package com.chessapp.mychessapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mychessapp.R;

public class PickTimeDialog extends AppCompatDialogFragment {
    // Number Picker for base Time
    private NumberPicker baseSecondPicker;
    private NumberPicker baseMinutePicker;
    private NumberPicker baseHourPicker;
    // Number Picker for bonus Time
    private NumberPicker bonusSecondPicker;
    private NumberPicker bonusMinutePicker;
    private NumberPicker bonusHourPicker;

    private String[] baseMinutes;
    private String[] bonusSeconds;

    private PickTimeDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_picktime_dialog, null);

        builder.setView(view)
                .setTitle("Pick Your Own Time!")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        long base = timeToMillis(Integer.parseInt(baseMinutes[baseMinutePicker.getValue()]), baseSecondPicker.getValue());
                        int bonus = (int) timeToMillis(Integer.parseInt(bonusSeconds[bonusSecondPicker.getValue()]));
                        listener.applyTexts(base, bonus);
                    }
                });

        initBaseTime();
        initBonusTime();
        baseSecondPicker = view.findViewById(R.id.baseSeconds);
        baseSecondPicker.setMinValue(0);
        baseSecondPicker.setMaxValue(59);
        baseSecondPicker.setValue(0);

        baseMinutePicker = view.findViewById(R.id.baseMinutes);
        baseMinutePicker.setMinValue(0);
        baseMinutePicker.setMaxValue(baseMinutes.length - 1);
        baseMinutePicker.setDisplayedValues(baseMinutes);
        baseMinutePicker.setValue(0);

        bonusSecondPicker = view.findViewById(R.id.bonusSeconds);
        bonusSecondPicker.setMinValue(0);
        bonusSecondPicker.setMaxValue(bonusSeconds.length - 1);
        bonusSecondPicker.setDisplayedValues(bonusSeconds);
        bonusSecondPicker.setValue(0);

        return builder.create();
    }

    private void initBonusTime() {
        String[] tmp_bonusTime = new String[31];
        int y = 0;
        for (int i = 0; i<tmp_bonusTime.length; i++){
            tmp_bonusTime[i] = Integer.toString(y);
            if (y<20)
                y++;
            else if (y<45)
                y += 5;
            else if (y<60)
                y += 15;
            else if (y<180)
                y += 30;
        }
        bonusSeconds = tmp_bonusTime;
    }

    private void initBaseTime() {
        String[] tmp_baseTime = new String[35];
        int time = 0;
        for (int i = 0; i<tmp_baseTime.length; i++){
            tmp_baseTime[i] = Integer.toString(time);
            if (time<20)
                time++;
            else if (time<45)
                time += 5;
            else if (time<180)
                time += 15;
        }
        baseMinutes = tmp_baseTime;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (PickTimeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement PickTimeDialog.");
        }
    }

    public interface PickTimeDialogListener{
        void applyTexts(long baseTime, int bonusTime);
    }

    private long timeToMillis(int minutes, int seconds){
        long time = 0;
        time+= seconds*1000;
        time+= minutes*60*1000;
        return time;
    }

    private long timeToMillis(int seconds){
        long time = 0;
        time+= seconds*1000;
        return time;
    }

}
