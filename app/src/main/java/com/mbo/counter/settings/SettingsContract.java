package com.mbo.counter.settings;

import com.mbo.counter.BasePresenter;
import com.mbo.counter.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface SettingsContract
{
    interface View extends BaseView<Presenter>
    {
        void showColorPickerUi();
    }

    interface Presenter extends BasePresenter
    {

    }
}
