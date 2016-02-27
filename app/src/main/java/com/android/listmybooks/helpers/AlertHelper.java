package com.android.listmybooks.helpers;

import android.support.design.widget.Snackbar;
import android.view.View;

public class AlertHelper {

    public static void showAlertShort(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .show();
    }

    public static void showAlertLong(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .show();
    }

    public static void showAlertIndefinite(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .show();
    }
}
