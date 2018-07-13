package com.mbo.counter.home;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mbo.commons.utils.Utils;
import com.mbo.commons.widgets.SegmentedButton;
import com.mbo.commons.widgets.SegmentedButtonGroup;
import com.mbo.counter.R;
import com.mbo.counter.addeditcounter.AddEditCounterActivity;
import com.mbo.counter.counter.CounterFragment;
import com.mbo.counter.counter.CounterPresenter;
import com.mbo.counter.counterlist.CounterListFragment;
import com.mbo.counter.counterlist.CounterListPresenter;
import com.mbo.counter.data.model.CounterGroup;
import com.mbo.counter.data.source.ormlite.OrmLiteDataSource;

import static com.mbo.commons.utils.Utils.convertDpToPixel;

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
        mFabBase = getActivity().findViewById(R.id.fab_base);
        mFabAddCounter = getActivity().findViewById(R.id.fab_add_counter);
        mFabAddCounterGroup = getActivity().findViewById(R.id.fab_add_counter_group);

        mAddCounterLayout = getActivity().findViewById(R.id.add_counter_layout);
        mAddCounterGroupLayout = getActivity().findViewById(R.id.add_counter_group_layout);

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
            final EditText addCounterGroupEditText = new EditText(context);
            addCounterGroupEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            addCounterGroupEditText.setSingleLine();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = (int) convertDpToPixel(10, context);
            params.leftMargin = margin;
            params.rightMargin = margin;
            addCounterGroupEditText.setLayoutParams(params);
            FrameLayout container = new FrameLayout(context);
            container.addView(addCounterGroupEditText);

            final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle(getString(R.string.add_counter_group_dialog_title))
                    .setMessage(getString(R.string.add_counter_group_dialog_message))
                    .setView(container)
                    .setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            mPresenter.saveCounterGroup(new CounterGroup(addCounterGroupEditText.getText().toString()));
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .create();

            dialog.show();

            final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (positiveButton != null)
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

            addCounterGroupEditText.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    if (positiveButton != null)
                    {
                        if (TextUtils.isEmpty(s) || s.length() < 2)
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        else
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                }
            });
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
        if (id == R.id.simple_counter_button)
        {
            if (mCounterFragment.isAdded())
                transaction.show(mCounterFragment);
            else
                Utils.addFragmentToActivity(getChildFragmentManager(), mCounterFragment, R.id.contentFrame);

            if (mCounterListFragment.isAdded())
                transaction.hide(mCounterListFragment);
        }
        else
        {
            if (mCounterListFragment.isAdded())
                transaction.show(mCounterListFragment);
            else
                Utils.addFragmentToActivity(getChildFragmentManager(), mCounterListFragment, R.id.contentFrame);

            if (mCounterFragment.isAdded())
                transaction.hide(mCounterFragment);
        }
        transaction.commit();
    }
}
