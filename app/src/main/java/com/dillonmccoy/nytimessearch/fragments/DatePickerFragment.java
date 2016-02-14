package com.dillonmccoy.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.dillonmccoy.nytimessearch.R;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DatePickerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DatePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatePickerFragment extends DialogFragment {
    private int year;
    private int month;
    private int day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker.
        Bundle args = this.getArguments();

        int year = args.getInt("year");
        int month = args.getInt("month");
        int day = args.getInt("day");

        if (year == 0) {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }


        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getActivity();

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}
