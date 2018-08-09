package com.mbo.counter.countergrouplist;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;
import com.mbo.counter.data.model.CounterGroup;

import java.util.List;

public interface CounterGroupListContract
{
    interface View extends BaseView<CounterGroupListContract.Presenter>
    {
        void setCount(int groupPosition, int childPosition, int count);

        void showCounterGroups(List<CounterGroup> counters);

        void showCounterUi(int counterId);

        void showEditCounterUi(int counterId);

        void showNoCounterGroups();

        void showCounterStatisticsUi(int counterId);

        void renameCounterGroup(int groupPosition);
    }

    interface Presenter extends BasePresenter
    {
        void decrementCounter(final int groupPosition, final int childPosition, final int counterId);

        void deleteCounter(int counterId);

        void deleteCounterGroup(int counterGroupId);

        void deleteCountersInGroup(int counterGroupId);

        void duplicateCounter(int counterId);

        void duplicateCounterGroup(int counterGroupId);

        void incrementCounter(final int groupPosition, final int childPosition, final int counterId);

        void loadCounterGroups();

        void resetCounter(int groupPosition, int childPosition, int counterId);

        void resetCountersInGroup(int counterGroupId);

        void saveCounterGroup(CounterGroup counterGroup);
    }
}
