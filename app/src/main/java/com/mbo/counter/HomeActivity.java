package com.mbo.counter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mbo.counter.addeditcounter.AddEditCounterActivity;
import com.mbo.counter.counter.CounterFragment;
import com.mbo.counter.counter.CounterPresenter;
import com.mbo.counter.counterlist.CounterListFragment;
import com.mbo.counter.counterlist.CounterListPresenter;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;
import com.mbo.counter.folderlist.CounterGroupListFragment;
import com.mbo.counter.folderlist.CounterGroupListPresenter;
import com.mbo.counter.settings.SettingsFragment;
import com.mbo.counter.settings.SettingsPresenter;
import com.mbo.counter.utils.Utils;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{

    // https://medium.com/mindorks/essential-guide-for-designing-your-android-app-architecture-mvp-part-1-74efaf1cda40
    CounterListFragment mCounterListFragment;
    CounterGroupListFragment mCounterGroupListFragment;
    CounterFragment mCounterFragment;
    SettingsFragment mSettingsFragment;
    boolean mHideMenu = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        Utils.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(this);

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

        mCounterGroupListFragment = CounterGroupListFragment.newInstance();
        new CounterGroupListPresenter(OrmLiteDataSource.getInstance(), mCounterGroupListFragment);

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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (item.getItemId())
        {
            case R.id.navigation_home:
                mHideMenu = true;
                if (mCounterFragment.isAdded())
                    transaction.show(mCounterFragment);
                else
                    Utils.addFragmentToActivity(getSupportFragmentManager(), mCounterFragment, R.id.contentFrame);

                if (mCounterListFragment.isAdded())
                    transaction.hide(mCounterListFragment);

                if (mCounterGroupListFragment.isAdded())
                    transaction.hide(mCounterGroupListFragment);

                if (mSettingsFragment.isAdded())
                    transaction.hide(mSettingsFragment);

                break;
            case R.id.navigation_counters:
                mHideMenu = false;
                if (mCounterListFragment.isAdded())
                    transaction.show(mCounterListFragment);
                else
                    Utils.addFragmentToActivity(getSupportFragmentManager(), mCounterListFragment, R.id.contentFrame);

                if (mCounterFragment.isAdded())
                    transaction.hide(mCounterFragment);

                if (mCounterGroupListFragment.isAdded())
                    transaction.hide(mCounterGroupListFragment);

                if (mSettingsFragment.isAdded())
                    transaction.hide(mSettingsFragment);

                break;
            case R.id.navigation_folders:
                mHideMenu = false;
                if (mCounterGroupListFragment.isAdded())
                    transaction.show(mCounterGroupListFragment);
                else
                    Utils.addFragmentToActivity(getSupportFragmentManager(), mCounterGroupListFragment, R.id.contentFrame);

                if (mCounterFragment.isAdded())
                    transaction.hide(mCounterFragment);

                if (mCounterListFragment.isAdded())
                    transaction.hide(mCounterListFragment);

                if (mSettingsFragment.isAdded())
                    transaction.hide(mSettingsFragment);

                break;
            case R.id.navigation_settings:
                mHideMenu = true;
                if (mSettingsFragment.isAdded())
                    transaction.show(mSettingsFragment);
                else
                    Utils.addFragmentToActivity(getSupportFragmentManager(), mSettingsFragment, R.id.contentFrame);

                if (mCounterFragment.isAdded())
                    transaction.hide(mCounterFragment);

                if (mCounterListFragment.isAdded())
                    transaction.hide(mCounterListFragment);

                if (mCounterGroupListFragment.isAdded())
                    transaction.hide(mCounterGroupListFragment);

                break;
        }

        transaction.commit();
        invalidateOptionsMenu();
        return true;
    }

    private void showAddCounter()
    {
        Intent intent = new Intent(this, AddEditCounterActivity.class);
        startActivityForResult(intent, AddEditCounterActivity.REQUEST_ADD_COUNTER);
    }
}
