package com.bdzapps.counterpp.data.source.ormlite;

import com.bdzapps.counterpp.data.model.Statistics;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class DaoStatistics extends BaseDaoImpl<Statistics, Long>
{
    protected DaoStatistics(ConnectionSource connectionSource, Class<Statistics> dataClass) throws SQLException
    {
        super(connectionSource, dataClass);
    }
}
