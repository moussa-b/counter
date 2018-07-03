package com.mbo.counter.data.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Statistics
{
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(dataType = DataType.DATE_STRING, format = "yyyy-MM-dd")
    private Date date;

    @DatabaseField(index = true)
    private int counterId;

    @DatabaseField
    private int value;

    @DatabaseField
    private StatisticsType type;

    public static class Row
    {
        private String date;

        private int value;

        public Row()
        {
        }

        public Row(String date, int value)
        {
            this.date = date;
            this.value = value;
        }

        public String getDate()
        {
            return date;
        }

        public void setDate(String date)
        {
            this.date = date;
        }

        public int getValue()
        {
            return value;
        }

        public void setValue(int value)
        {
            this.value = value;
        }
    }

    public Statistics()
    {
    }

    public Statistics(Date date, int counterId)
    {
        this.date = date;
        this.counterId = counterId;
        this.value = 1;
    }

    public Statistics(Date date, int counterId, int value)
    {
        this.date = date;
        this.counterId = counterId;
        this.value = value;
    }

    public Statistics(Date date, int counterId, int value, StatisticsType type)
    {
        this.date = date;
        this.counterId = counterId;
        this.value = value;
        this.type = type;
    }

    public Date getDate()
    {
        return date;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public StatisticsType getType()
    {
        return type;
    }

    public void setType(StatisticsType type)
    {
        this.type = type;
    }
}
