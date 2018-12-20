package com.bdzapps.counterpp.counterlist;

import android.view.View;

import com.bdzapps.counterpp.data.model.Counter;

public interface CounterItemListener
{
    void onCounterDecrement(int position, Counter counter);

    void onCounterIncrement(int position, Counter counter);

    void onCounterShowFullScreen(int counterId);

    void onCounterShowMenu(View view, Counter clickedCounter, int position);

    void onItemMove(Counter counter);
}
