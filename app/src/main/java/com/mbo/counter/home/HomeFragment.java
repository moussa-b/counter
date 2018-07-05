package com.mbo.counter.home;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mbo.counter.R;
import com.mbo.counter.addeditcounter.AddEditCounterActivity;
import com.mbo.counter.counter.CounterActivity;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.CounterGroup;

import java.util.ArrayList;
import java.util.List;

import static com.mbo.commons.utils.Utils.convertDpToPixel;
import static com.mbo.counter.counter.CounterFragment.ARGUMENT_COUNTER_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment implements HomeContract.View
{
    private static final int REQUEST_COUNTER = 100;
    private HomeContract.Presenter mPresenter;
    private CountersAdapter mListAdapter;
    private TextView mNoCounterTextView;
    private ListView mCountListView;
    private Menu menu;
    private boolean isFabOpen = false;
    FloatingActionButton mFabBase, mFabAddCounter, mFabAddCounterGroup;
    LinearLayout mAddCounterLayout, mAddCounterGroupLayout;

    private CounterItemListener mCounterListener = new CounterItemListener()
    {
        @Override
        public void onCounterClick(Counter clickedCounter)
        {
            mPresenter.openCounter(clickedCounter);
        }

        @Override
        public void delete(Counter clickedCounter)
        {
            mPresenter.deleteCounter(clickedCounter.getId());
        }
    };

    public HomeFragment()
    {
    }

    public static HomeFragment newInstance()
    {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    { // Lifecycle : called first, for doing any non-graphical initialisations
        super.onCreate(savedInstanceState);
        mListAdapter = new CountersAdapter(new ArrayList<Counter>(0), mCounterListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    { // Lifecycle : called after onCreate, for doing any graphical initialisations
        View root = inflater.inflate(R.layout.home_fragment, container, false);

        // Set up tasks view
        mCountListView = (ListView) root.findViewById(R.id.counter_list_view);
        mCountListView.setAdapter(mListAdapter);

        // Set up  no tasks view
        mNoCounterTextView = (TextView) root.findViewById(R.id.no_counter_text_view);
        mNoCounterTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showAddCounter();
            }
        });

        // Set up floating action button
        mFabBase = (FloatingActionButton) getActivity().findViewById(R.id.fab_base);
        mFabAddCounter = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_counter);
        mFabAddCounterGroup = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_counter_group);

        mAddCounterLayout = (LinearLayout) getActivity().findViewById(R.id.add_counter_layout);
        mAddCounterGroupLayout = (LinearLayout) getActivity().findViewById(R.id.add_counter_group_layout);

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

        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        closeFabMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_edit:
                editCounters(true);
                break;
        }
        return true;
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
    public void showCounters(List<Counter> counters)
    {
        mListAdapter.replaceData(counters);

        mCountListView.setVisibility(View.VISIBLE);
        mNoCounterTextView.setVisibility(View.GONE);
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
    public void showCounterUi(int counterId)
    {
        Intent intent = new Intent(getContext(), CounterActivity.class);
        intent.putExtra(ARGUMENT_COUNTER_ID, counterId);
        startActivityForResult(intent, REQUEST_COUNTER);
    }

    @Override
    public void editCounters(boolean activeEdition)
    {
        mListAdapter.toggleEditMode();

        if (mListAdapter.isActiveEditMode())
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_done));
        else
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_edit));

        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean isFabMenuOpen()
    {
        return isFabOpen;
    }

    @Override
    public void showNoCounters()
    {
        mCountListView.setVisibility(View.GONE);
        mNoCounterTextView.setVisibility(View.VISIBLE);
    }

    public interface CounterItemListener
    {
        void onCounterClick(Counter clickedCounter);

        void delete(Counter clickedCounter);
    }

    private static class CountersAdapter extends BaseAdapter
    {
        private List<Counter> mCounters;

        private CounterItemListener mCounterListener;

        private boolean activeEditMode = false;

        public CountersAdapter(List<Counter> counters, CounterItemListener itemListener)
        {
            this.mCounters = counters;
            this.mCounterListener = itemListener;
        }

        @Override
        public int getCount()
        {
            return mCounters.size();
        }

        @Override
        public Counter getItem(int i)
        {
            return mCounters.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup)
        {
            View rowView = view;

            if (rowView == null)
            {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.counter_item, viewGroup, false);
            }

            final Counter counter = getItem(i);

            rowView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (!activeEditMode)
                        mCounterListener.onCounterClick(counter);
                }
            });

            TextView countTextView = (TextView) rowView.findViewById(R.id.count_text_view);
            countTextView.setText(String.valueOf(counter.getCount()));

            TextView nameTextView = (TextView) rowView.findViewById(R.id.name_text_view);
            nameTextView.setText(counter.getName());

            TextView limitTextView = (TextView) rowView.findViewById(R.id.limit_text_view);
            limitTextView.setText("(" + counter.getLimit() + ")");

            TextView progressionTextView = (TextView) rowView.findViewById(R.id.progression_text_view);
            ProgressBar counterItemProgressBar = (ProgressBar) rowView.findViewById(R.id.counter_item_progress_bar);
            ImageButton deleteImageButton = (ImageButton) rowView.findViewById(R.id.delete_image_button);

            if (!activeEditMode)
            {
                progressionTextView.setVisibility(View.VISIBLE);
                counterItemProgressBar.setVisibility(View.VISIBLE);
                deleteImageButton.setVisibility(View.GONE);

                int progression = (int) (100 * (float) counter.getCount() / ((float) counter.getLimit()));
                progressionTextView.setText(String.valueOf(progression) + "%");

                counterItemProgressBar.setProgress(progression);
            }
            else
            {
                progressionTextView.setVisibility(View.GONE);
                counterItemProgressBar.setVisibility(View.GONE);
                deleteImageButton.setVisibility(View.VISIBLE);
            }

            deleteImageButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (activeEditMode)
                    {
                        mCounterListener.delete(counter);
                        mCounters.remove(i);
                        notifyDataSetChanged();
                    }
                }
            });

            return rowView;
        }

        private void replaceData(List<Counter> counters)
        {
            this.mCounters = counters;
            notifyDataSetChanged();
        }

        private void toggleEditMode()
        {
            activeEditMode = !activeEditMode;
        }

        private boolean isActiveEditMode()
        {
            return activeEditMode;
        }
    }
}
