package com.mbo.counter.addeditcounter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.mbo.counter.R;
import com.mbo.counter.data.model.CounterGroup;
import com.mbo.counter.utils.CallBack;
import com.mbo.counter.utils.CounterGroupUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditCounterFragment extends Fragment implements AddEditCounterContract.View
{
    public static final String ARGUMENT_EDIT_COUNTER_ID = "EDIT_COUNTER_ID";

    private TextInputEditText mName, mCount, mNote;

    private AutoCompleteTextView mGroup;

    private AddEditCounterContract.Presenter mPresenter;

    private Button mChangeColor;

    private ArrayAdapter<String> mAutoCompleteAdapter;

    private ArrayList<String> mCounterGroupsName = new ArrayList<>();

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
                    mPresenter.saveCounter();
                    Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.add_edit_counter_fragment, container, false);
        mName = root.findViewById(R.id.name_text_input);
        mCount = root.findViewById(R.id.count_text_input);
        mNote = root.findViewById(R.id.note_text_input);
        mGroup = root.findViewById(R.id.group_auto_complete_text_view);
        mCounterGroupsName.add("+ " + getString(R.string.add_counter_group));
        if (getActivity() != null)
        {
            mAutoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, mCounterGroupsName);
            mGroup.setAdapter(mAutoCompleteAdapter);
        }

        mGroup.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                if (hasFocus)
                {
                    mGroup.showDropDown();
                }
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
                            mCounterGroupsName.add(counterGroup.getName());
                            mAutoCompleteAdapter.notifyDataSetChanged();
                            mGroup.setSelection(mCounterGroupsName.size() - 1);
                        }
                    });
                }
                Toast.makeText(getContext(), " selected : " + pos + ", id : " + id, Toast.LENGTH_LONG).show();
            }
        });
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
                mCounterGroupsName.add(counterGroup.getName());

            mAutoCompleteAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setCount(int count)
    {
        mCount.setText(String.valueOf(count));
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
