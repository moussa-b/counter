package com.bdzapps.counterpp.data.source.ormlite;

import com.bdzapps.counterpp.data.model.Counter;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class DaoCounter extends BaseDaoImpl<Counter, Long>
{
    protected DaoCounter(ConnectionSource connectionSource, Class<Counter> dataClass) throws SQLException
    {
        super(connectionSource, dataClass);
    }
}
