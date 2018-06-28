package com.mbo.counter.data.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Counter
{
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private int total;

    @DatabaseField
    private int count;

    @DatabaseField
    private String note;

    @DatabaseField
    private String color;

    @DatabaseField
    private String direction;

    @DatabaseField
    private int order;

    @DatabaseField(foreign = true)
    private CounterGroup counterGroup;

    public Counter()
    {
    }

    public Counter(String name)
    {
        this.name = name;
    }

    public Counter(int id, String name, int total, String note, String direction, String color)
    {
        this.id = id;
        this.name = name;
        this.total = total;
        this.note = note;
        this.direction = direction;
        this.color = color;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public CounterGroup getCounterGroup()
    {
        return counterGroup;
    }

    public void setCounterGroup(CounterGroup counterGroup)
    {
        this.counterGroup = counterGroup;
    }
}
