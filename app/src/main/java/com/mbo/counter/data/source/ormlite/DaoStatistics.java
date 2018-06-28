package com.mbo.counter.data.source.ormlite;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.mbo.counter.data.model.Statistics;

import java.sql.SQLException;

public class DaoStatistics extends BaseDaoImpl<Statistics, Long>
{
    protected DaoStatistics(ConnectionSource connectionSource, Class<Statistics> dataClass) throws SQLException
    {
        super(connectionSource, dataClass);
    }
}
