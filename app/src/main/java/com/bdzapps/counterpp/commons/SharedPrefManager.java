package com.bdzapps.counterpp.commons;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager
{
    private final static int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "counter";

    private static final String IS_FIRST_TIME_LAUNCH = "IS_FIRST_TIME_LAUNCH";

    public static void setFirstTimeLaunch(Context context, boolean isFirstTime)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public static boolean isFirstTimeLaunch(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
