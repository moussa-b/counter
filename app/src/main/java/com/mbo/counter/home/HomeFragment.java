package com.mbo.counter.home;

import android.animation.Animator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mbo.commons.utils.Utils;
import com.mbo.commons.widgets.SegmentedButton;
import com.mbo.commons.widgets.SegmentedButtonGroup;
import com.mbo.counter.R;
import com.mbo.counter.about.AboutActivity;
import com.mbo.counter.addeditcounter.AddEditCounterActivity;
import com.mbo.counter.counter.CounterFragment;
import com.mbo.counter.counter.CounterPresenter;
import com.mbo.counter.countergrouplist.CounterGroupListFragment;
import com.mbo.counter.countergrouplist.CounterGroupListPresenter;
import com.mbo.counter.counterlist.CounterListFragment;
import com.mbo.counter.counterlist.CounterListPresenter;
import com.mbo.counter.data.model.CounterGroup;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;
import com.mbo.counter.settings.SettingsActivity;
import com.mbo.counter.utils.CallBack;
import com.mbo.counter.utils.CounterGroupUtils;
import com.mbo.counter.utils.PropertiesReader;

import java.util.Properties;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment implements HomeContract.View
{
    private HomeContract.Presenter mPresenter;
    private boolean isFabOpen = false;
    FloatingActionButton mFabBase, mFabAddCounter, mFabAddCounterGroup;
    LinearLayout mAddCounterLayout, mAddCounterGroupLayout;
    SegmentedButtonGroup mSegmentedButtonGroup;
    CounterFragment mCounterFragment;
    CounterListFragment mCounterListFragment;
    CounterGroupListFragment mCounterGroupListFragment;

    public HomeFragment()
    {
    }

    public static HomeFragment newInstance()
    {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    { // Lifecycle : called after onCreate, for doing any graphical initialisations
        View root = inflater.inflate(R.layout.home_fragment, container, false);

        // Set up floating action button
        Activity activity = getActivity();
        if (activity != null)
        {
            mFabBase = activity.findViewById(R.id.fab_base);
            mFabAddCounter = activity.findViewById(R.id.fab_add_counter);
            mFabAddCounterGroup = activity.findViewById(R.id.fab_add_counter_group);

            mAddCounterLayout = activity.findViewById(R.id.add_counter_layout);
            mAddCounterGroupLayout = activity.findViewById(R.id.add_counter_group_layout);

            mFabBase.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (isFabOpen)
                        closeFabMenu();
                    else
                        showFabMenu();
                }
            });

            mAddCounterLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    showAddCounter();
                }
            });

            mAddCounterGroupLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    showAddCounterGroup();
                }
            });
        }

        setHasOptionsMenu(true);

        // Set up segmented button group
        mSegmentedButtonGroup = root.findViewById(R.id.counter_segmented_button_group);
        mSegmentedButtonGroup.setOnCheckedChangeListener(new SegmentedButtonGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(View radioGroup, View radioButton, boolean isChecked, int checkedId)
            {
                prepareViewByCheckedId(checkedId);
            }
        });

        mCounterFragment = CounterFragment.newInstance();
        new CounterPresenter(1, OrmLiteDataSource.getInstance(), mCounterFragment);
        Utils.addFragmentToActivity(getChildFragmentManager(), mCounterFragment, R.id.contentFrame);

        mCounterListFragment = CounterListFragment.newInstance();
        new CounterListPresenter(OrmLiteDataSource.getInstance(), mCounterListFragment);

        mCounterGroupListFragment = CounterGroupListFragment.newInstance();
        new CounterGroupListPresenter(OrmLiteDataSource.getInstance(), mCounterGroupListFragment);

        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.start();
        prepareViewByCheckedId(getCheckedSegmentedButton());
    }

    @Override
    public void onPause()
    {
        super.onPause();
        closeFabMenu();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    @Override
    public void showFabMenu()
    {
        isFabOpen = true;
        mAddCounterLayout.setVisibility(View.VISIBLE);
        mAddCounterGroupLayout.setVisibility(View.VISIBLE);
        mFabBase.animate().rotation(135);
        mAddCounterLayout.animate().translationY(-getResources().getDimension(R.dimen.fab_first_menu_item));
        mAddCounterGroupLayout.animate().translationY(-getResources().getDimension(R.dimen.fab_second_menu_item));
    }

    @Override
    public void showSettingsUi()
    {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void closeFabMenu()
    {
        isFabOpen = false;
        mFabBase.animate().rotation(0);
        mAddCounterLayout.animate().translationY(0);
        mAddCounterGroupLayout.animate().translationY(0).setListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                if (!isFabOpen)
                {
                    mAddCounterLayout.setVisibility(View.GONE);
                    mAddCounterGroupLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        });
    }

    @Override
    public void contactUs()
    {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

        PropertiesReader propertiesReader = new PropertiesReader(getContext());
        Properties properties = propertiesReader.getProperties("configuration.properties");
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
    public int getCheckedSegmentedButton()
    {
        SegmentedButton segmentedButton = mSegmentedButtonGroup.findViewById(R.id.simple_counter_button);
        if (segmentedButton.isChecked())
            return R.id.simple_counter_button;

        segmentedButton = mSegmentedButtonGroup.findViewById(R.id.counter_groups_button);
        if (segmentedButton.isChecked())
            return R.id.counter_groups_button;

        segmentedButton = mSegmentedButtonGroup.findViewById(R.id.all_counters_button);
        if (segmentedButton.isChecked())
            return R.id.all_counters_button;

        return View.NO_ID;
    }

    @Override
    public void toggleFabVisibility(boolean visible)
    {
        if (visible)
            mFabBase.setVisibility(View.VISIBLE);
        else
        {
            if (isFabOpen)
                closeFabMenu();
            mFabBase.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAddCounter()
    {
        Intent intent = new Intent(getContext(), AddEditCounterActivity.class);
        startActivityForResult(intent, AddEditCounterActivity.REQUEST_ADD_COUNTER);
    }

    @Override
    public void showAddCounterGroup()
    {
        closeFabMenu();
        Context context = getContext();
        if (context != null)
        {
            CounterGroupUtils.showAddCounterGroup(context, new CallBack()
            {
                @Override
                public void execute(Object data)
                {
                    mPresenter.saveCounterGroup(new CounterGroup((String) data));
                }
            });
        }
    }

    @Override
    public void showEvaluateUi()
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
    public boolean isFabMenuOpen()
    {
        return isFabOpen;
    }

    @Override
    public void prepareViewByCheckedId(int id)
    {
        toggleFabVisibility(id != R.id.simple_counter_button);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (id == R.id.all_counters_button)
        {
            if (mCounterListFragment.isAdded())
                transaction.show(mCounterListFragment);
            else
                Utils.addFragmentToActivity(getChildFragmentManager(), mCounterListFragment, R.id.contentFrame);

            if (mCounterFragment.isAdded())
                transaction.hide(mCounterFragment);

            if (mCounterGroupListFragment.isAdded())
                transaction.hide(mCounterGroupListFragment);
        }
        else if (id == R.id.counter_groups_button)
        {
            if (mCounterGroupListFragment.isAdded())
                transaction.show(mCounterGroupListFragment);
            else
                Utils.addFragmentToActivity(getChildFragmentManager(), mCounterGroupListFragment, R.id.contentFrame);

            if (mCounterFragment.isAdded())
                transaction.hide(mCounterFragment);

            if (mCounterListFragment.isAdded())
                transaction.hide(mCounterListFragment);
        }
        else
        {
            if (mCounterFragment.isAdded())
                transaction.show(mCounterFragment);
            else
                Utils.addFragmentToActivity(getChildFragmentManager(), mCounterFragment, R.id.contentFrame);

            if (mCounterListFragment.isAdded())
                transaction.hide(mCounterListFragment);

            if (mCounterGroupListFragment.isAdded())
                transaction.hide(mCounterGroupListFragment);
        }
        transaction.commit();
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

    @Override
    public void showAboutUi()
    {
        Intent intent = new Intent(getContext(), AboutActivity.class);
        startActivity(intent);
    }
}
