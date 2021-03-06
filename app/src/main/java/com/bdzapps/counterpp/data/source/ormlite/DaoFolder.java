package com.bdzapps.counterpp.data.source.ormlite;

import com.bdzapps.counterpp.data.model.Folder;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class DaoFolder extends BaseDaoImpl<Folder, Long>
{
    protected DaoFolder(ConnectionSource connectionSource, Class<Folder> dataClass) throws SQLException
    {
        super(connectionSource, dataClass);
    }
}
