package com.mbo.counter.countergrouplist;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;
import com.mbo.counter.data.model.CounterGroup;

import java.util.List;

public interface CounterGroupListContract
{
    interface View extends BaseView<CounterGroupListContract.Presenter>
    {
        void showCounterGroups(List<CounterGroup> counters);

        void showNoCounterGroups();
    }

    interface Presenter extends BasePresenter
    {
        void loadCounterGroups();
    }
}
