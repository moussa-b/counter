package com.bdzapps.counterpp.commons;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class AndroidChartValueFormatter implements IValueFormatter
{
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
    {
        return value != 0 ? String.valueOf((int) value) : "";
    }
}
