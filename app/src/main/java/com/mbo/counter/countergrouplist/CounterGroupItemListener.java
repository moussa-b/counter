package com.mbo.counter.countergrouplist;

import android.view.View;

import com.mbo.counter.data.model.Counter;

public interface CounterGroupItemListener
{
    void onCounterDecrement(int groupPosition, int childPosition, int counterId);

    void onCounterIncrement(int groupPosition, int childPosition, int counterId);

    void onCounterShowMenu(View view, Counter clickedCounter, int groupPosition, int childPosition);
}
