package com.mbo.counter.counter;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface CounterContract
{

    interface View extends BaseView<Presenter>
    {

    }

    interface Presenter extends BasePresenter
    {

    }
}
