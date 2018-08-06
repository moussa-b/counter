package com.mbo.counter.counterlist;

import android.view.View;

import com.mbo.counter.data.model.Counter;

public interface CounterItemListener
{
    void onCounterDecrement(int position, int counterId);

    void onCounterIncrement(int position, int counterId);

    void onCounterShowMenu(View view, Counter clickedCounter, int position);
}
