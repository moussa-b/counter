package com.mbo.counter.data.source.ormlite;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.mbo.counter.data.model.Counter;

import java.sql.SQLException;

public class CounterDao extends BaseDaoImpl<Counter, Long>
{
    protected CounterDao(ConnectionSource connectionSource, Class<Counter> dataClass) throws SQLException
    {
        super(connectionSource, dataClass);
    }
}
