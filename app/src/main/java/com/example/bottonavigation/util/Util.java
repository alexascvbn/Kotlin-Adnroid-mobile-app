package com.example.bottonavigation.util;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.bottonavigation.BaseActivity;
import com.example.bottonavigation.model.UserModel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    private static DecimalFormat df = new DecimalFormat("0.00");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("zh"));
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", new Locale("zh"));


    public static UserModel getCurrentUser() {
        return new UserModel("001", "johnson@mail.com", "should not have password, this use for making chatbot only");
    }

    public static void refreshActionBarMenu(Activity activity)
    {
        activity.invalidateOptionsMenu();
    }

    public static String formatDate(Date date) {
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(date);
    }
    public static String formatTime(Date date) {
        return timeFormat.format(date);
    }

    public static String toTwoDeciPoint(Double num) {
        return df.format(num);
    }

    public static boolean isValidateEmailAddress(String emailAddress) {
        Pattern regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
        Matcher regMatcher = regexPattern.matcher(emailAddress);

        if (regMatcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
