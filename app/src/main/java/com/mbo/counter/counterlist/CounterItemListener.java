package com.mbo.counter.counterlist;

import android.view.View;

import com.mbo.counter.data.model.Counter;

public interface CounterItemListener
{
    void onCounterDecrement(int position, int counterId, int limit);

    void onCounterIncrement(int position, int counterId, int limit);

    void onCounterShowMenu(View view, Counter clickedCounter, int position);
}
