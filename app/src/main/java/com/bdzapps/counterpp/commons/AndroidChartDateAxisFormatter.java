package com.bdzapps.counterpp.commons;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;
import java.util.Locale;

public class AndroidChartDateAxisFormatter implements IAxisValueFormatter
{
    private Locale mLocale;
    private List<Long> mTimeStampGroups;

    public AndroidChartDateAxisFormatter(Locale locale, List<Long> timeStampGroups)
    {
        this.mLocale = locale;
        this.mTimeStampGroups = timeStampGroups;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis)
    {
        long timeStamp = mTimeStampGroups.get((int) value);
        return Utils.formatDateForGraphs(timeStamp, mLocale);
    }
}
