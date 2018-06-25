package com.mbo.counter.addeditcounter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mbo.counter.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditCounterFragment extends Fragment implements AddEditCounterContract.View
{
    public static final String ARGUMENT_EDIT_COUNTER_ID = "EDIT_COUNTER_ID";

    private TextInputEditText mName, mCount, mNote;

    private AddEditCounterContract.Presenter mPresenter;

    private Button mChangeColor;

    public AddEditCounterFragment()
    {
    }

    public static AddEditCounterFragment newInstance()
    {
        return new AddEditCounterFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_counter_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveCounter(mName.getText().toString(), Integer.parseInt(mCount.getText().toString()), null, null, null);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.add_edit_counter_fragment, container, false);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_counter_done);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mName = (TextInputEditText) root.findViewById(R.id.name_text_input);
        mCount = (TextInputEditText) root.findViewById(R.id.count_text_input);
        mNote = (TextInputEditText) root.findViewById(R.id.note_text_input);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull AddEditCounterContract.Presenter presenter)
    {
        mPresenter = presenter;
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
}
