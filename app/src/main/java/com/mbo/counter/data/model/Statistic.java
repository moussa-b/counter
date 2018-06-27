package com.mbo.counter.data.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Statistic
{
    @DatabaseField(dataType = DataType.DATE_STRING, format = "yyyy-MM-dd")
    private Date date;

    @DatabaseField
    private int counterId;

    public Statistic()
    {
    }

    public Statistic(Date date, int counterId)
    {
        this.date = date;
        this.counterId = counterId;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public int getCounterId()
    {
        return counterId;
    }

    public void setCounterId(int counterId)
    {
        this.counterId = counterId;
    }
}
