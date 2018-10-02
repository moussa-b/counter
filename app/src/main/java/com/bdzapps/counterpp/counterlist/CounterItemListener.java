package com.bdzapps.counterpp.counterlist;

import android.view.View;

import com.bdzapps.counterpp.data.model.Counter;

public interface CounterItemListener
{
    void onCounterDecrement(int position, int counterId, int limit);

    void onCounterIncrement(int position, int counterId, int limit);

    void onCounterShowMenu(View view, Counter clickedCounter, int position);

    void onItemMove(Counter counter);
}
