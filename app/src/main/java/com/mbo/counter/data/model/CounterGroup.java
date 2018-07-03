package com.mbo.counter.data.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class CounterGroup
{
    @DatabaseField(generatedId = true)
    private int id;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<Counter> counters;

    @DatabaseField
    private String name;

    public CounterGroup()
    {
    }

    public CounterGroup(String name)
    {
        this(name, null);
    }

    public CounterGroup(String name, ForeignCollection<Counter> counters)
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
}
