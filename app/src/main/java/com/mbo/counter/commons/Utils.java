package com.mbo.counter.commons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.lang.reflect.Field;
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

    // getDisplayWidth(context) => (display width in pixels)
    public static int getDisplayWidth(Context context)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    // getDisplayHeight(context) => (display height in pixels)
    public static int getDisplayHeight(Context context)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    // convertDpToPixel(25f, context) => (25dp converted to pixels)
    public static float convertDpToPixel(float dp, Context context)
    {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    // convertPixelsToDp(25f, context) => (25px converted to dp)
    public static float convertPixelsToDp(float px, Context context)
    {
        Resources r = context.getResources();
        DisplayMetrics metrics = r.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view)
    {
        //this will remove shift mode for bottom navigation view
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try
        {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++)
            {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }

        }
        catch (NoSuchFieldException e)
        {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        }
        catch (IllegalAccessException e)
        {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }
}
