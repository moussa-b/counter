package com.mbo.counter.addeditcounter;

import android.support.annotation.NonNull;

import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Folder;
import com.mbo.counter.data.source.CounterDataSource;

import java.util.List;

public class AddEditCounterPresenter implements AddEditCounterContract.Presenter
{
    @NonNull
    private final CounterDataSource mCounterDataSource;

    @NonNull
    private final AddEditCounterContract.View mAddCounterView;

    private int mCounterId;

    private Counter mCounter;

    public AddEditCounterPresenter(int counterId, @NonNull CounterDataSource counterDataSource,
                                   @NonNull AddEditCounterContract.View addCounterView)
    {
        this.mCounterId = counterId;
        this.mCounterDataSource = counterDataSource;
        this.mAddCounterView = addCounterView;

        mAddCounterView.setPresenter(this);
    }

    @Override
    public void start()
    {
        loadFolders();
        populateCounter();
    }

    @Override
    public Counter getCounter()
    {
        return mCounter;
    }

    @Override
    public void loadFolders()
    {
        mCounterDataSource.getFolders(new CounterDataSource.LoadFoldersCallback()
        {
            @Override
            public void onFoldersLoaded(List<Folder> folders)
            {
                mAddCounterView.processFolders(folders);
            }

            @Override
            public void onDataNotAvailable()
            {

            }
        });
    }

    @Override
    public void saveCounter()
    {
        mCounterDataSource.saveCounter(mCounter);
        mAddCounterView.showCountersList();
    }

    @Override
    public void saveFolder(Folder folder)
    {
        mCounterDataSource.saveFolder(folder);
    }

    @Override
    public void populateCounter()
    {
        if (mCounterId != 0)
        {
            mCounterDataSource.getCounter(mCounterId, new CounterDataSource.GetCounterCallback()
            {
                @Override
                public void onCounterLoaded(Counter counter)
                {
                    mCounter = counter;
                    mAddCounterView.setName(mCounter.getName());
                    mAddCounterView.setColor(mCounter.getColor());
                    mAddCounterView.setGroup(mCounter.getFolder());
                    mAddCounterView.setLimit(mCounter.getLimit());
                    mAddCounterView.setStep(mCounter.getStep());
                    mAddCounterView.setNote(mCounter.getNote());
                }

                @Override
                public void onDataNotAvailable()
                {
                }
            });
        }
        else
        {
            mCounter = new Counter();
            mAddCounterView.setStep(mCounter.getStep());
            mAddCounterView.setColor(null); // will add a random color to counter
        }
    }
}
