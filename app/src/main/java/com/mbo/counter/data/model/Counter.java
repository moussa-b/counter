package com.mbo.counter.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Counter extends RealmObject
{
    @Required
    @PrimaryKey
    private String id;

    @Required
    private String name;

    private int count;

    private int currentCount;

    private String note;

    private String color;

    private String direction;

    private int type;

    public Counter()
    {
    }

    public Counter(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
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

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
