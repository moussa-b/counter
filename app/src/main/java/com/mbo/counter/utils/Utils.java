package com.mbo.counter.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.support.v4.util.Preconditions.checkNotNull;

public class Utils
{
    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId)
    {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }


    public static String formatDateForDisplay(String inputDate, Locale locale)
    {
        // inputDate format must be 2000-07-20
        DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputDateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy", locale);
        String outputDate = inputDate;
        try
        {
            outputDate = outputDateFormat.format(inputDateFormat.parse(inputDate));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return outputDate;
    }
}
