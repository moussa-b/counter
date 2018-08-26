package com.mbo.counter.data.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class Folder
{
    @DatabaseField(generatedId = true)
    private int id;

    @ForeignCollectionField(eager = true)
    private ForeignCollection<Counter> counters;

    @DatabaseField
    private String name;

    @DatabaseField
    private int order;

    @DatabaseField(dataType = DataType.DATE_STRING, format = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @DatabaseField(dataType = DataType.DATE_STRING, format = "yyyy-MM-dd HH:mm:ss")
    private Date lastModificationDate;

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

    public Date getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    public Date getLastModificationDate()
    {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate)
    {
        this.lastModificationDate = lastModificationDate;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }
}
