package com.mbo.counter.data.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Folder
{
    @DatabaseField(generatedId = true)
    private int id;

    @ForeignCollectionField(eager = true)
    private transient ForeignCollection<Counter> counters; // Transient because GSON doesn't know how to serialize

    @DatabaseField
    private String name;

    @DatabaseField
    private int order;

    @DatabaseField
    private long creationTimeStamp;

    @DatabaseField
    private long lastModificationTimeStamp;

    public Folder()
    {
    }

    public Folder(String name)
    {
        this(name, null);
    }

    public Folder(String name, ForeignCollection<Counter> counters)
    {
        this.name = name;
        this.counters = counters;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public ForeignCollection<Counter> getCounters()
    {
        return counters;
    }

    public void setCounters(ForeignCollection<Counter> counters)
    {
        this.counters = counters;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
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
