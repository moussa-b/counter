package com.bdzapps.counterpp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.bdzapps.counterpp.commons.SharedPrefManager;
import com.bdzapps.counterpp.commons.Utils;
import com.bdzapps.counterpp.counter.CounterFragment;
import com.bdzapps.counterpp.counter.CounterPresenter;
import com.bdzapps.counterpp.counterlist.CounterListFragment;
import com.bdzapps.counterpp.counterlist.CounterListPresenter;
import com.bdzapps.counterpp.data.source.ormlite.OrmLiteDataSource;
import com.bdzapps.counterpp.folderlist.FolderListFragment;
import com.bdzapps.counterpp.folderlist.FolderListPresenter;
import com.bdzapps.counterpp.settings.SettingsFragment;
import com.bdzapps.counterpp.settings.SettingsPresenter;
import com.bdzapps.counterpp.tutorial.TutorialActivity;
import com.mbo.counter.R;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{

    // https://medium.com/mindorks/essential-guide-for-designing-your-android-app-architecture-mvp-part-1-74efaf1cda40
    CounterListFragment mCounterListFragment;
    FolderListFragment mFolderListFragment;
    CounterFragment mCounterFragment;
    SettingsFragment mSettingsFragment;
    BottomNavigationView mBottomNavigationView;
    boolean mHideMenu = true;
    private SharedPreferences.OnSharedPreferenceChangeListener mListener; // Required to be member and not local variable

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        boolean showTutorial = SharedPrefManager.isFirstTimeLaunch(this);
        if (showTutorial)
            startActivity(new Intent(this, TutorialActivity.class));

        setContentView(R.layout.home_activity);
        mBottomNavigationView = findViewById(R.id.navigation);
        Utils.removeShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        Fragment contentFrame = getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (contentFrame instanceof CounterFragment)
            mCounterFragment = (CounterFragment) contentFrame;
        else
        {
            // Create the fragment
            mCounterFragment = CounterFragment.newInstance();
            Utils.addFragmentToActivity(getSupportFragmentManager(), mCounterFragment, R.id.contentFrame);
        }

        new CounterPresenter(1, OrmLiteDataSource.getInstance(), mCounterFragment);

        mCounterListFragment = CounterListFragment.newInstance();
        new CounterListPresenter(OrmLiteDataSource.getInstance(), mCounterListFragment);

        mFolderListFragment = FolderListFragment.newInstance();
        new FolderListPresenter(OrmLiteDataSource.getInstance(), mFolderListFragment);

        mSettingsFragment = SettingsFragment.newInstance();
        new SettingsPresenter(OrmLiteDataSource.getInstance(), mSettingsFragment);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean isScreenAlwaysOnEnabled = sharedPreferences.getBoolean(getString(R.string.key_screen_always_on), false);
        if (isScreenAlwaysOnEnabled)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mListener = new SharedPreferences.OnSharedPreferenceChangeListener()
        {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
            {
                if (key.equals(getString(R.string.key_screen_always_on)))
                {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                    boolean isScreenAlwaysOnEnabled = sharedPreferences.getBoolean(getString(R.string.key_screen_always_on), false);
                    if (isScreenAlwaysOnEnabled)
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(mListener);

        setTitle(getString(R.string.counter));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        if (mHideMenu)
        {
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        loadFragment(item.getItemId());
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if (mCounterFragment.isHidden())
        {
            mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
            loadFragment(R.id.navigation_home);
        }
        else
            finish();
    }

    private void loadFragment(int itemId)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (itemId)
        {
            case R.id.navigation_home:
                setTitle(getString(R.string.counter));
                mHideMenu = true;
                if (mCounterFragment.isAdded())
                    transaction.show(mCounterFragment);
                else
                    Utils.addFragmentToActivity(getSupportFragmentManager(), mCounterFragment, R.id.contentFrame);

                if (mCounterListFragment.isAdded())
                    transaction.hide(mCounterListFragment);

                if (mFolderListFragment.isAdded())
                    transaction.hide(mFolderListFragment);

                if (mSettingsFragment.isAdded())
                    transaction.hide(mSettingsFragment);

                break;
            case R.id.navigation_counters:
                setTitle(getString(R.string.counter_list));
                mHideMenu = false;
                if (mCounterListFragment.isAdded())
                    transaction.show(mCounterListFragment);
                else
                    Utils.addFragmentToActivity(getSupportFragmentManager(), mCounterListFragment, R.id.contentFrame);

                if (mCounterFragment.isAdded())
                    transaction.hide(mCounterFragment);

                if (mFolderListFragment.isAdded())
                    transaction.hide(mFolderListFragment);

                if (mSettingsFragment.isAdded())
                    transaction.hide(mSettingsFragment);

                break;
            case R.id.navigation_folders:
                setTitle(getString(R.string.folder_list));
                mHideMenu = false;
                if (mFolderListFragment.isAdded())
                    transaction.show(mFolderListFragment);
                else
                    Utils.addFragmentToActivity(getSupportFragmentManager(), mFolderListFragment, R.id.contentFrame);

                if (mCounterFragment.isAdded())
                    transaction.hide(mCounterFragment);

                if (mCounterListFragment.isAdded())
                    transaction.hide(mCounterListFragment);

                if (mSettingsFragment.isAdded())
                    transaction.hide(mSettingsFragment);

                break;
            case R.id.navigation_settings:
                setTitle(getString(R.string.settings));
                mHideMenu = true;
                if (mSettingsFragment.isAdded())
                    transaction.show(mSettingsFragment);
                else
                    Utils.addFragmentToActivity(getSupportFragmentManager(), mSettingsFragment, R.id.contentFrame);

                if (mCounterFragment.isAdded())
                    transaction.hide(mCounterFragment);

                if (mCounterListFragment.isAdded())
                    transaction.hide(mCounterListFragment);

                if (mFolderListFragment.isAdded())
                    transaction.hide(mFolderListFragment);

                break;
        }

        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isVolumeButtonEnabled = sharedPreferences.getBoolean(getString(R.string.key_count_volume_buttons), false);
        if (isVolumeButtonEnabled && mCounterFragment.isVisible())
        {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
            {
                mCounterFragment.decrementCounter();
                return true;
            }
            else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
            {
                mCounterFragment.incrementCounter();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
