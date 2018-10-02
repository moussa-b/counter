package com.bdzapps.counterpp.counterlist;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class CounterListItemTouchHelperCallback extends ItemTouchHelper.Callback
{
    //https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf
    private final CounterListAdapter mAdapter;

    CounterListItemTouchHelperCallback(CounterListAdapter adapter)
    {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {

    }
}
