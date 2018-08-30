package com.mbo.counter.data.source.ormlite;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.mbo.counter.data.model.Folder;

import java.sql.SQLException;

public class DaoFolder extends BaseDaoImpl<Folder, Long>
{
    protected DaoFolder(ConnectionSource connectionSource, Class<Folder> dataClass) throws SQLException
    {
        super(connectionSource, dataClass);
    }
}
