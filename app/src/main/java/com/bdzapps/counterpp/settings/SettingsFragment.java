package com.bdzapps.counterpp.settings;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;

import com.bdzapps.counterpp.colorpicker.ColorPickerFragment;
import com.bdzapps.counterpp.colorpicker.ColorPickerListener;
import com.bdzapps.counterpp.commons.PropertiesReader;
import com.bdzapps.counterpp.webview.WebViewActivity;
import com.mbo.counter.R;

import java.util.Properties;

import static android.app.Activity.RESULT_OK;
import static com.bdzapps.counterpp.webview.WebViewFragment.ARGUMENT_LOAD_URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SettingsContract.View, ColorPickerListener
{
    private static final int REQUEST_SELECT_FILE = 100;
    private SettingsContract.Presenter mPresenter;
    private Properties properties;

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
        if (getContext() != null)
        {
            PropertiesReader propertiesReader = new PropertiesReader(getContext());
            properties = propertiesReader.getProperties("configuration.properties");
        }
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
            else if (key.equals(resources.getString(R.string.key_contact_us)))
                contactUs();
            else if (key.equals(resources.getString(R.string.key_share_app)))
                shareApplication();
            else if (key.equals(resources.getString(R.string.key_rate_app)))
                rateApplication();
            else if (key.equals(resources.getString(R.string.key_privacy_policy)))
                showPrivacyPolicy();
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
    public void showPrivacyPolicy()
    {
        if (properties != null && getContext() != null)
        {
            String url = properties.getProperty("privacy_policy_url");
            if (url != null)
            {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra(ARGUMENT_LOAD_URL, url);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onSelectColor(int selectedColor)
    {
        Log.e("MBO", String.valueOf(selectedColor));
    }

    @Override
    public void contactUs()
    {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        if (properties != null)
        {
            String mailTo = "mailto:" + properties.getProperty("contact_email") +
                    "?subject=" + Uri.encode(getString(R.string.app_name)) +
                    "&body=" + Uri.encode("Hello");

            emailIntent.setData(Uri.parse(mailTo));

            try
            {
                startActivity(emailIntent);
            }
            catch (ActivityNotFoundException e)
            {
                Toast.makeText(getContext(), getString(R.string.email_warning), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void rateApplication()
    {
        if (getContext() != null)
        {
            Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try
            {
                startActivity(goToMarket);
            }
            catch (ActivityNotFoundException e)
            {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
            }
        }
    }

    @Override
    public void shareApplication()
    {
        Activity activity = getActivity();
        if (activity != null)
        {
            ShareCompat.IntentBuilder.from(activity)
                    .setType("text/plain")
                    .setChooserTitle(getString(R.string.share) + " " + getString(R.string.app_name))
                    .setText("http://play.google.com/store/apps/details?id=" + activity.getPackageName())
                    .startChooser();
        }
    }
}
