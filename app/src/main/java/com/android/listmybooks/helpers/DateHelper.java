package com.android.listmybooks.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    private static final DateFormat dateFormat;

    public static Date parseDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (java.text.ParseException var2) {
            return null;
        }
    }

    static {
        dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss ZZZZZ", Locale.US);
    }
}
