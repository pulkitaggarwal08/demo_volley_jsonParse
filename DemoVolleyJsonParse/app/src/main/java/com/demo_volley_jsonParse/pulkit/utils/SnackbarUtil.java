package com.demo_volley_jsonParse.pulkit.utils;

import android.app.Activity;
import android.support.design.widget.Snackbar;

/**
 * Created by Pulkit on 10-Jun-17.
 */

public class SnackbarUtil {
    public static void showShortSnackbar(Activity activity, String message){
        Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showLongSnackbar(Activity activity, String message) {
        Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
