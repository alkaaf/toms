package com.spil.dev.tms.Activity.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by dalbo on 12/18/2017.
 */

public class DF {
    public static final String HOUR_MIN = "HH:mm";
    public static final String DEFAULT = "dd-MM-yyyy HH:mm";
    public static final String DATE_ONLY = "dd/MM/yyyy";
    public static final String DATE_MONT = "dd/MM";
    public static final String DATE_US = "yyyy-MM-dd";
    public static final String DATE_PRODUKSI = "EEEE, dd/MM HH:mm";
    public static final String MYSQL = "yyyy-MM-dd HH:mm:ss";

    public static Date parse(String date) {
        try {
            return new SimpleDateFormat(DEFAULT).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String format(long mills) {
        return new SimpleDateFormat(DEFAULT).format(new Date(mills));
    }

    public static String format(String format, long mills) {
        return new SimpleDateFormat(format).format(new Date(mills));
    }

    public static String format(String format, long mills, Locale locale) {
        return new SimpleDateFormat(format, locale).format(new Date(mills));
    }

    public static ArrayList<Integer> getYearFromNow() {
        ArrayList<Integer> years = new ArrayList<>();
        int yearNow = new GregorianCalendar().get(Calendar.YEAR);
        for (int i = 2017; i <= yearNow; i++) {
            years.add(i);
        }
        return years;
    }

    public static ArrayList<Integer> getDefaultMonth() {
        ArrayList<Integer> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }
        return months;
    }
}
