package com.mbo.counter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mbo.counter.commons.Utils;
import com.mbo.counter.counter.CounterFragment;
import com.mbo.counter.counter.CounterPresenter;
import com.mbo.counter.counterlist.CounterListFragment;
import com.mbo.counter.counterlist.CounterListPresenter;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;
import com.mbo.counter.folderlist.FolderListFragment;
import com.mbo.counter.folderlist.FolderListPresenter;
import com.mbo.counter.settings.SettingsFragment;
import com.mbo.counter.settings.SettingsPresenter;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{

    // https://medium.com/mindorks/essential-guide-for-designing-your-android-app-architecture-mvp-part-1-74efaf1cda40
    CounterListFragment mCounterListFragment;
    FolderListFragment mFolderListFragment;
    CounterFragment mCounterFragment;
    SettingsFragment mSettingsFragment;
    BottomNavigationView mBottomNavigationView;
    boolean mHideMenu = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        mBottomNavigationView = findViewById(R.id.navigation);
        Utils.removeShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        mCounterFragment = (CounterFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mCounterFragment == null)
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
        new SettingsPresenter(mSettingsFragment);
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
}