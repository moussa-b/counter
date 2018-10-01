package com.mbo.counter.settings;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.mbo.counter.R;
import com.mbo.counter.colorpicker.ColorPickerFragment;
import com.mbo.counter.colorpicker.ColorPickerListener;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SettingsContract.View, ColorPickerListener
{
    private static final int REQUEST_SELECT_FILE = 100;
    private SettingsContract.Presenter mPresenter;

    public SettingsFragment()
    {
    }

    public static SettingsFragment newInstance()
    {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        addPreferencesFromResource(R.xml.pref_main);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_SELECT_FILE && resultCode == RESULT_OK)
        {
            Uri selectedFile = data.getData();
            if (selectedFile != null)
                mPresenter.importData(selectedFile);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference)
    {
        String key = preference.getKey();

        if (key != null && getActivity() != null)
        {
            Resources resources = getActivity().getResources();
            if (key.equals(resources.getString(R.string.key_theme_and_color)))
                showColorPickerUi();
            else if (key.equals(resources.getString(R.string.key_export_data)))
                mPresenter.exportData();
            else if (key.equals(resources.getString(R.string.key_import_data)))
                selectFileForDataImport();
            else if (key.equals(resources.getString(R.string.key_reset_data)))
                mPresenter.resetData();
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter)
    {
        this.mPresenter = presenter;
    }

    @Override
    public void selectFileForDataImport()
    {
        Intent intent = new Intent()
                .setType("*/*")
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setAction(Intent.ACTION_OPEN_DOCUMENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), REQUEST_SELECT_FILE);
    }

    @Override
    public void showColorPickerUi()
    {
        FragmentManager fm = getFragmentManager();
        ColorPickerFragment colorPickerFragment = ColorPickerFragment.newInstance();
        colorPickerFragment.setTargetFragment(this, ColorPickerFragment.REQUEST_COLOR_PICKER);
        if (fm != null)
            colorPickerFragment.show(fm, ColorPickerFragment.TAG_COLOR_PICKER);
    }

    @Override
    public void onSelectColor(int selectedColor)
    {
        Log.e("MBO", String.valueOf(selectedColor));
    }
}
