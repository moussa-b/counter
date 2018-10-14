package com.bdzapps.counterpp.commons;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.mbo.counter.R;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.support.v4.util.Preconditions.checkNotNull;

public class Utils
{
    public static final int NO_ICON = 0;

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

    public static String getCurrentDateTimeForFileName()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();
        return dateFormat.format(date);
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

    public static String formatDateForDisplay(long dateTimeStamp, Locale locale)
    {
        Date date = new Date(dateTimeStamp);
        DateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yy", locale);
        return outputDateFormat.format(date);
    }

    public static String formatDateForGraphs(long dateTimeStamp, Locale locale)
    {
        Date date = new Date(dateTimeStamp);
        DateFormat outputDateFormat = new SimpleDateFormat("dd/MM", locale);
        return outputDateFormat.format(date);
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

    public static MediaPlayer getIncreaseMediaPlayer(Context context)
    {
        return MediaPlayer.create(context, R.raw.increase);
    }

    public static MediaPlayer getDecreaseMediaPlayer(Context context)
    {
        return MediaPlayer.create(context, R.raw.decrease);
    }

    public static void showWarningDialog(final Context context, boolean isCancellable, String title, String message, final CallBack confirmCallBack, final CallBack cancelCallBack, int icon)
    {
        if (context != null)
        {
            final AlertDialog.Builder materialDialog = new AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setMessage(message)
                    .setTitle(title);

            materialDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    if (confirmCallBack != null)
                        confirmCallBack.execute(null);
                    dialog.dismiss();
                }
            });

            if (isCancellable)
            {
                materialDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (cancelCallBack != null)
                            cancelCallBack.execute(null);
                        dialog.dismiss();
                    }
                });
            }
            else
                materialDialog.setCancelable(false);

            if (icon != NO_ICON)
            {
                materialDialog.setIcon(icon);
            }

            materialDialog.show();
        }
    }

    public static void showWarningDialog(final Context context, boolean isCancellable, int title, int message, final CallBack confirmCallBack, final CallBack cancelCallBack, int icon)
    {
        if (context != null)
        {
            final AlertDialog.Builder materialDialog = new AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setMessage(message)
                    .setTitle(title);

            materialDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    if (confirmCallBack != null)
                        confirmCallBack.execute(null);
                    dialog.dismiss();
                }
            });

            if (isCancellable)
            {
                materialDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (cancelCallBack != null)
                            cancelCallBack.execute(null);
                        dialog.dismiss();
                    }
                });
            }
            else
                materialDialog.setCancelable(false);

            if (icon != NO_ICON)
            {
                materialDialog.setIcon(icon);
            }

            materialDialog.show();
        }
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

    public static long atEndOfDay(Calendar calendar)
    {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    public static long atStartOfDay(Calendar calendar)
    {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static String getRandomColor(Context context)
    {
        if (context != null)
        {
            String[] androidColors = context.getResources().getStringArray(R.array.android_colors);
            return androidColors[new Random().nextInt(androidColors.length)];
        }
        else
            return "#000000";
    }

    public static ProgressDialog createProgressDialog(Context context, String message)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context, int stringId)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(stringId));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    public static boolean isConnectedToNetwork(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        else
            return false;
    }
}
