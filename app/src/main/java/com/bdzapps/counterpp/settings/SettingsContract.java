package com.bdzapps.counterpp.settings;

import android.net.Uri;

import com.bdzapps.counterpp.BasePresenter;
import com.bdzapps.counterpp.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface SettingsContract
{
    interface View extends BaseView<Presenter>
    {
        void contactUs();

        void rateApplication();

        void selectFileForDataImport();

        void shareApplication();

        void showColorPickerUi();

        void showPrivacyPolicy();

        void showTutorial();
    }

    interface Presenter extends BasePresenter
    {
        void exportData();

        String getProperty(String property);

        void importData(Uri importDataUri);

        void resetData();
    }
}
