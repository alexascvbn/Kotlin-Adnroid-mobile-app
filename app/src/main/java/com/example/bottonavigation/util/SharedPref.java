package com.example.bottonavigation.util;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SharedPref
{
    private final static String PREF_PROFILE = "PREF_PROFILE";
    public static final String USERNAME = "USERNAME";
    public static final String ADDRESS = "ADDRESS";
    public static final String DELIVERY_ADDRESS = "DELIVERY_ADDRESS";
    public static final String EMAIL = "EMAIL";
    public static final String COMPANY = "COMPANY";
    public static final String AVATAR = "AVATAR";
    public static final String SENDNOTE = "SENDNOTE";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String NAV_IS_RIGHT = "IS_RIGHT";
//    public static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static void setSharedPreferenceString(Context context, String key, String value){
        SharedPreferences settings = context.getSharedPreferences(PREF_PROFILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setSharedPreferenceInt(Context context, String key, int value){
        SharedPreferences settings = context.getSharedPreferences(PREF_PROFILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setSharedPreferenceBoolean(Context context, String key, boolean value){
        SharedPreferences settings = context.getSharedPreferences(PREF_PROFILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static String getSharedPreferenceString(Context context, String key, String defValue){
        SharedPreferences settings = context.getSharedPreferences(PREF_PROFILE, 0);
        return settings.getString(key, defValue);
    }
    public static int getSharedPreferenceInt(Context context, String key, int defValue){
        SharedPreferences settings = context.getSharedPreferences(PREF_PROFILE, 0);
        return settings.getInt(key, defValue);
    }
    public static boolean getSharedPreferenceBoolean(Context context, String key, boolean defValue){
        SharedPreferences settings = context.getSharedPreferences(PREF_PROFILE, 0);
        return settings.getBoolean(key, defValue);
    }

}

