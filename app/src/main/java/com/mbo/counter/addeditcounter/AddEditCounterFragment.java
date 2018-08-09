package com.mbo.counter.addeditcounter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mbo.counter.R;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.CounterGroup;
import com.mbo.counter.utils.CallBack;
import com.mbo.counter.utils.CounterGroupUtils;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditCounterFragment extends Fragment implements AddEditCounterContract.View
{
    public static final String ARGUMENT_EDIT_COUNTER_ID = "EDIT_COUNTER_ID";

    private TextInputEditText mName, mLimit, mNote;

    private AutoCompleteTextView mGroup;

    private Button mChangeColor;

    private ImageButton mDecrease, mIncrease;

    private AddEditCounterContract.Presenter mPresenter;

    private ArrayAdapter<String> mAutoCompleteAdapter;

    private List<String> mCounterGroupsName = new ArrayList<>();
    private List<CounterGroup> mCounterGroups = new ArrayList<>();

    public AddEditCounterFragment()
    {
    }

    public static AddEditCounterFragment newInstance()
    {
        return new AddEditCounterFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null)
        {
            FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_counter_done);
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    saveCounter();
                    // Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.add_edit_counter_fragment, container, false);
        mName = root.findViewById(R.id.name_text_input);
        mLimit = root.findViewById(R.id.limit_text_input);
        mNote = root.findViewById(R.id.note_text_input);
        mGroup = root.findViewById(R.id.group_auto_complete_text_view);
        mChangeColor = root.findViewById(R.id.change_color_button);
        mDecrease = root.findViewById(R.id.decrease_image_view);
        mIncrease = root.findViewById(R.id.increase_image_view);
        mCounterGroupsName.add("+ " + getString(R.string.add_counter_group));
        mCounterGroups.add(new CounterGroup("+ " + getString(R.string.add_counter_group)));
        if (getActivity() != null)
        {
            mAutoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, mCounterGroupsName);
            mGroup.setAdapter(mAutoCompleteAdapter);
        }

        setListeners();

        return root;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull AddEditCounterContract.Presenter presenter)
    {
        mPresenter = presenter;
    }

    @Override
    public void processCounterGroups(List<CounterGroup> counterGroups)
    {
        if (getActivity() != null && counterGroups != null && counterGroups.size() > 0)
        {
            for (CounterGroup counterGroup : counterGroups)
            {
                // Add data in mCounterGroups and mCounterGroupsName to be sure that order is exactly the same
                mCounterGroups.add(counterGroup);
                mCounterGroupsName.add(counterGroup.getName());
            }

            mAutoCompleteAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void saveCounter()
    {
        Counter counter = mPresenter.getCounter();
        if (isEmpty(mName.getText().toString()))
            Toast.makeText(getContext(), R.string.name_required_warning, Toast.LENGTH_LONG).show();
        else
        {
            counter.setName(mName.getText().toString());
            try
            {
                int limit = Integer.parseInt(mLimit.getText().toString());
                counter.setLimit(limit);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
            counter.setNote(mNote.getText() != null ? mNote.getText().toString() : null);
            mPresenter.saveCounter();
        }
    }

    @Override
    public void setColor(String color)
    {

    }

    @Override
    public void setDirection(String direction)
    {

    }

    @Override
    public void setLimit(int limit)
    {
        mLimit.setText(String.valueOf(limit));
    }

    @Override
    public void setListeners()
    {
        mGroup.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                if (hasFocus)
                    mGroup.showDropDown();
            }
        });

        mGroup.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id)
            {
                if (pos == 0)
                {
                    CounterGroupUtils.showAddCounterGroup(getContext(), new CallBack()
                    {
                        @Override
                        public void execute(Object data)
                        {
                            CounterGroup counterGroup = new CounterGroup((String) data);
                            mPresenter.saveCounterGroup(counterGroup);
                            mPresenter.getCounter().setCounterGroup(counterGroup);
                            mCounterGroupsName.add(counterGroup.getName());
                            mCounterGroups.add(counterGroup);
                            mAutoCompleteAdapter.notifyDataSetChanged();
                            mGroup.setSelection(mCounterGroupsName.size() - 1);
                        }
                    });
                }
                else
                {
                    CounterGroup counterGroup = mCounterGroups.get(pos);
                    mPresenter.getCounter().setCounterGroup(counterGroup);
                }
            }
        });

        mDecrease.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPresenter.getCounter().setDirection("DESC");
            }
        });

        mIncrease.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPresenter.getCounter().setDirection("ASC");
            }
        });
    }

    @Override
    public void setNote(String note)
    {
        mNote.setText(note);
    }

    @Override
    public void setName(String name)
    {
        mName.setText(name);
    }

    @Override
    public void showCountersList()
    {
        Activity activity = getActivity();
        if (activity != null)
        {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }
}
