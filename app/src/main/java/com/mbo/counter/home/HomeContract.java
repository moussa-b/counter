package com.mbo.counter.home;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface HomeContract {

    interface View extends BaseView<Presenter> {
        void showAddCounter();
    }

    interface Presenter extends BasePresenter {

    }
}
