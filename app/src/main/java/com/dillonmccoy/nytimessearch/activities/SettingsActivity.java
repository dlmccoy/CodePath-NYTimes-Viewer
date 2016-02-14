package com.dillonmccoy.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.dillonmccoy.nytimessearch.R;
import com.dillonmccoy.nytimessearch.fragments.DatePickerFragment;
import com.dillonmccoy.nytimessearch.models.Settings;

import org.parceler.Parcels;

import java.util.Date;

public class SettingsActivity extends AppCompatActivity implements OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private Settings settings;
    private static final int YEAR_DIFF = 1900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = (Settings) Parcels.unwrap(getIntent().getParcelableExtra(SearchActivity.SETTINGS_EXTRA));

        setupSpinner();
    }

    private void setupSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.sSortOrder);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Set the initial value.
        spinner.setSelection(settings.sortOrder);

        // Attach the onClick listener of the activity.
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        settings.sortOrder = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void onSettingsSave(View view) {
        Intent i = new Intent();
        i.putExtra(SearchActivity.SETTINGS_EXTRA, Parcels.wrap(settings));
        setResult(SearchActivity.SAVE_SETTINGS_RESULT, i);
        this.finish();
    }

    // Attach to an onclick handler to show the date picker.
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        Bundle dateArgs = new Bundle();

        // If the settings already contain a beginDate, prepopulate the date picker with it.
        if (settings.beginDate != null) {
            dateArgs.putInt("year", settings.beginDate.getYear() + YEAR_DIFF);
            dateArgs.putInt("month", settings.beginDate.getMonth());
            dateArgs.putInt("day", settings.beginDate.getDate());
        }

        newFragment.setArguments(dateArgs);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Handle the date when it is selected.
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        settings.beginDate = new Date(year - YEAR_DIFF, monthOfYear, dayOfMonth);
    }
}
