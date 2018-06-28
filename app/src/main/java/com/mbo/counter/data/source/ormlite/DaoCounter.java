package com.mbo.counter.data.source.ormlite;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.mbo.counter.data.model.Counter;

import java.sql.SQLException;

public class DaoCounter extends BaseDaoImpl<Counter, Long>
{
    protected DaoCounter(ConnectionSource connectionSource, Class<Counter> dataClass) throws SQLException
    {
        super(connectionSource, dataClass);
    }
}
