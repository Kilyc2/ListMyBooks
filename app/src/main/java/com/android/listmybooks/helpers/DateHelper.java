package com.android.listmybooks.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    private static final DateFormat dateFormat;
    private static final DateFormat dateFormatString;

    public static Date parseDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (java.text.ParseException var2) {
            return null;
        }
    }

    public static String getDateFormated(String date) {
        return dateFormatString.format(parseDate(date)).toString();
    }

    static {
        dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy kk:mm:ss ZZZZZ", Locale.US);
        dateFormatString = new SimpleDateFormat("yyyyMMddkkmmss", Locale.US);
    }
}
