package com.mbo.counter.commons;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Locale;

public class AndroidChartDateAxisFormatter implements IAxisValueFormatter
{
    private Locale mLocale;

    private static final long ONE_DAY_MILLIS = 86400000L;

    public AndroidChartDateAxisFormatter(Locale locale)
    {
        this.mLocale = locale;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis)
    {
        long timeStamp = (long) (value * ONE_DAY_MILLIS);
        return Utils.formatDateForDisplay(timeStamp, mLocale);
    }
}
