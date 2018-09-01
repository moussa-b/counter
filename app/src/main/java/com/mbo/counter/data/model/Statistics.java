package com.mbo.counter.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Statistics
{
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private long dateTimeStamp;

    @DatabaseField(index = true)
    private int counterId;

    @DatabaseField
    private int value;

    @DatabaseField
    private StatisticsType type;

    public static class Row
    {
        private long dateTimeStamp;

        private int value;

        public Row()
        {
        }

        public Row(long dateTimeStamp, int value)
        {
            this.dateTimeStamp = dateTimeStamp;
            this.value = value;
        }

        public long getDateTimeStamp()
        {
            return dateTimeStamp;
        }

        public void setDateTimeStamp(long dateTimeStamp)
        {
            this.dateTimeStamp = dateTimeStamp;
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

    public Statistics(long dateTimeStamp, int counterId)
    {
        this.dateTimeStamp = dateTimeStamp;
        this.counterId = counterId;
        this.value = 1;
    }

    public Statistics(long dateTimeStamp, int counterId, int value)
    {
        this.dateTimeStamp = dateTimeStamp;
        this.counterId = counterId;
        this.value = value;
    }

    public Statistics(long dateTimeStamp, int counterId, int value, StatisticsType type)
    {
        this.dateTimeStamp = dateTimeStamp;
        this.counterId = counterId;
        this.value = value;
        this.type = type;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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

    public long getDateTimeStamp()
    {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(long dateTimeStamp)
    {
        this.dateTimeStamp = dateTimeStamp;
    }
}
