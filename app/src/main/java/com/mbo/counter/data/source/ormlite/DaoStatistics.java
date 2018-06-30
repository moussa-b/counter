package com.mbo.counter.data.source.ormlite;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.support.ConnectionSource;
import com.mbo.counter.data.model.Statistics;

import java.sql.SQLException;
import java.util.List;

public class DaoStatistics extends BaseDaoImpl<Statistics, Long>
{
    protected DaoStatistics(ConnectionSource connectionSource, Class<Statistics> dataClass) throws SQLException
    {
        super(connectionSource, dataClass);
    }

    public List<Statistics.Row> getCounterStatisticsById(int counterId) throws SQLException
    {
        return queryRaw(
                "select s.date, count (s.counterId) as stats, sum (value) as val" +
                        " from statistics s" +
                        " where counterId = " + counterId +
                        " group by s.date" +
                        " order by count (s.counterId) desc",
                new RawRowMapper<Statistics.Row>()
                {
                    public Statistics.Row mapRow(String[] columnNames, String[] resultColumns)
                    {
                        return new Statistics.Row(resultColumns[0], Integer.parseInt(resultColumns[1]));
                    }
                }).getResults();
    }
}
