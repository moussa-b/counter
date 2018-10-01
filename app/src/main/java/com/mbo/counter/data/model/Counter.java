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

    @DatabaseField
    private int limit;

    @DatabaseField
    private int count;

    @DatabaseField
    private String note;

    @DatabaseField
    private String color;

    @DatabaseField
    private String direction;

    @DatabaseField
    private double order;

    @DatabaseField
    private double orderInGroup;

    @DatabaseField(foreign = true)
    private Folder folder;

    @DatabaseField
    private int step;

    @DatabaseField
    private long creationTimeStamp;

    @DatabaseField
    private long lastModificationTimeStamp;

    public Counter()
    {
        this.step = 1;
    }

    public Counter(String name)
    {
        this.name = name;
        this.step = 1;
    }

    public Counter(int id, String name, int limit, String note, String direction, String color)
    {
        this(id, name, limit, note, direction, color, 1);
    }

    public Counter(int id, String name, int limit, String note, String direction, String color, int step)
    {
        this.id = id;
        this.name = name;
        this.limit = limit;
        this.note = note;
        this.direction = direction;
        this.color = color;
        this.step = step;
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

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
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

    public double getOrder()
    {
        return order;
    }

    public void setOrder(double order)
    {
        this.order = order;
    }

    public double getOrderInGroup()
    {
        return orderInGroup;
    }

    public void setOrderInGroup(double orderInGroup)
    {
        this.orderInGroup = orderInGroup;
    }

    public Folder getFolder()
    {
        return folder;
    }

    public void setFolder(Folder folder)
    {
        this.folder = folder;
    }

    public int getStep()
    {
        return step;
    }

    public void setStep(int step)
    {
        this.step = step;
    }

    public long getCreationTimeStamp()
    {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(long creationTimeStamp)
    {
        this.creationTimeStamp = creationTimeStamp;
    }

    public long getLastModificationTimeStamp()
    {
        return lastModificationTimeStamp;
    }

    public void setLastModificationTimeStamp(long lastModificationTimeStamp)
    {
        this.lastModificationTimeStamp = lastModificationTimeStamp;
    }
}
