package com.mbo.counter.data.model;

import com.j256.ormlite.field.DatabaseField;

public class Counter
{
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private int count;

    @DatabaseField
    private int currentCount;

    @DatabaseField
    private String note;

    @DatabaseField
    private String color;

    @DatabaseField
    private String direction;

    public Counter()
    {
    }

    public Counter(String name)
    {
        this.name = name;
    }

    public Counter(long id, String name, int count, String note, String direction, String color)
    {
        this.id = id;
        this.name = name;
        this.count = count;
        this.note = note;
        this.direction = direction;
        this.color = color;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public int getCurrentCount()
    {
        return currentCount;
    }

    public void setCurrentCount(int currentCount)
    {
        this.currentCount = currentCount;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getDirection()
    {
        return direction;
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }
}
