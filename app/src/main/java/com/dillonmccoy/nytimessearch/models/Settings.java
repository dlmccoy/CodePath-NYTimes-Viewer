package com.dillonmccoy.nytimessearch.models;

import org.parceler.Parcel;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Parcel
public class Settings {
    public static final int SORT_RELEVANCE = 0;
    public static final int SORT_NEWEST = 1;
    public static final int SORT_OLDEST = 2;
    public int sortOrder;

    public Date beginDate;

    public boolean showArt = false;
    public boolean showFashion = false;
    public boolean showSports = false;

    public Settings() {}
}
