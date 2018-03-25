package com.mbo.counter.addeditcounter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mbo.counter.R;
import com.mbo.counter.utils.Utils;

public class AddEditCounterActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_COUNTER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_counter_activity);

        AddEditCounterFragment addEditFragment = (AddEditCounterFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (addEditFragment == null) {
            // Create the fragment
            addEditFragment = AddEditCounterFragment.newInstance();
            Utils.addFragmentToActivity(getSupportFragmentManager(), addEditFragment, R.id.contentFrame);
        }
    }
}
