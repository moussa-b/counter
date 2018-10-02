package com.bdzapps.counterpp.data.source.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bdzapps.counterpp.data.model.Counter;
import com.bdzapps.counterpp.data.model.Folder;
import com.bdzapps.counterpp.data.model.Statistics;
import com.bdzapps.counterpp.data.source.CounterDataSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class OrmLiteHelper extends OrmLiteSqliteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "counter.db";

    public OrmLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
    {
        try
        {
            TableUtils.createTable(connectionSource, Counter.class);
            TableUtils.createTable(connectionSource, Folder.class);
            TableUtils.createTable(connectionSource, Statistics.class);

            CounterDataSource dataSource = OrmLiteDataSource.getInstance();
            Folder defaultFolder = new Folder("");
            Counter defaultCounter = new Counter("");
            dataSource.saveFolder(defaultFolder);
            defaultCounter.setFolder(defaultFolder);
            dataSource.saveCounter(defaultCounter);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {

    }
}
