package com.mbo.counter.commons;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Locale;

public class AndroidChartDateAxisFormatter implements IAxisValueFormatter
{
    private Locale mLocale;

    public AndroidChartDateAxisFormatter(Locale locale)
    {
        this.mLocale = locale;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis)
    {
        long timeStamp = (long) (value * 86400000);
        return Utils.formatDateForDisplay(timeStamp, mLocale);
    }
}
