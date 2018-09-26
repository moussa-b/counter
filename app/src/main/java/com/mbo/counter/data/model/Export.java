package com.mbo.counter.data.model;

import java.util.List;

public class Export
{
    int versionCode;
    String versionName;
    int databaseVersion;
    List<Counter> counters;
    List<Folder> folders;
    List<Statistics> statistics;

    public Export()
    {
    }

    public Export(List<Counter> counters, List<Folder> folders, List<Statistics> statistics)
    {
        this.counters = counters;
        this.folders = folders;
        this.statistics = statistics;
    }

    public int getVersionCode()
    {
        return versionCode;
    }

    public void setVersionCode(int versionCode)
    {
        this.versionCode = versionCode;
    }

    public String getVersionName()
    {
        return versionName;
    }

    public void setVersionName(String versionName)
    {
        this.versionName = versionName;
    }

    public int getDatabaseVersion()
    {
        return databaseVersion;
    }

    public void setDatabaseVersion(int databaseVersion)
    {
        this.databaseVersion = databaseVersion;
    }

    public List<Counter> getCounters()
    {
        return counters;
    }

    public void setCounters(List<Counter> counters)
    {
        this.counters = counters;
    }

    public List<Folder> getFolders()
    {
        return folders;
    }

    public void setFolders(List<Folder> folders)
    {
        this.folders = folders;
    }

    public List<Statistics> getStatistics()
    {
        return statistics;
    }

    public void setStatistics(List<Statistics> statistics)
    {
        this.statistics = statistics;
    }
}
