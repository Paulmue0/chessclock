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
                        long base = timeToMillis(baseHourPicker.getValue(), baseMinutePicker.getValue(), baseSecondPicker.getValue());
                        int bonus = (int) timeToMillis(bonusHourPicker.getValue(), bonusMinutePicker.getValue(), bonusSecondPicker.getValue());
                        listener.applyTexts(base, bonus);
                    }
                });
        baseSecondPicker = view.findViewById(R.id.baseSeconds);
        baseSecondPicker.setMinValue(0);
        baseSecondPicker.setMaxValue(59);
        baseSecondPicker.setValue(0);

        baseMinutePicker = view.findViewById(R.id.baseMinutes);
        baseMinutePicker.setMinValue(0);
        baseMinutePicker.setMaxValue(59);
        baseMinutePicker.setValue(0);

        baseHourPicker = view.findViewById(R.id.baseHours);
        baseHourPicker.setMinValue(0);
        baseHourPicker.setMaxValue(100);
        baseHourPicker.setValue(0);

        bonusSecondPicker = view.findViewById(R.id.bonusSeconds);
        bonusSecondPicker.setMinValue(0);
        bonusSecondPicker.setMaxValue(59);
        bonusSecondPicker.setValue(0);

        bonusMinutePicker = view.findViewById(R.id.bonusMinutes);
        bonusMinutePicker.setMinValue(0);
        bonusMinutePicker.setMaxValue(59);
        bonusMinutePicker.setValue(0);

        bonusHourPicker = view.findViewById(R.id.bonusHours);
        bonusHourPicker.setMinValue(0);
        bonusHourPicker.setMaxValue(100);
        bonusHourPicker.setValue(0);

        return builder.create();
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

    private long timeToMillis(int hour, int minutes, int seconds){
        long time = 0;
        time+= seconds*1000;
        time+= minutes*60*1000;
        time+= hour*60*60*1000;
        return time;
    }
}
