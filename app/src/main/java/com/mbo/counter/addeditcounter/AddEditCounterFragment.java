package com.mbo.counter.addeditcounter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.mbo.counter.R;
import com.mbo.counter.colorpicker.ColorPickerFragment;
import com.mbo.counter.colorpicker.ColorPickerListener;
import com.mbo.counter.commons.CallBack;
import com.mbo.counter.commons.FolderUtils;
import com.mbo.counter.commons.Utils;
import com.mbo.counter.data.model.Counter;
import com.mbo.counter.data.model.Folder;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditCounterFragment extends Fragment implements AddEditCounterContract.View, ColorPickerListener
{
    public static final String ARGUMENT_EDIT_COUNTER_ID = "EDIT_COUNTER_ID";

    private TextInputEditText mName, mLimit, mNote, mStep;

    private Spinner mGroup;

    private Button mChangeColor;

    private AddEditCounterContract.Presenter mPresenter;

    private ArrayAdapter<String> mAutoCompleteAdapter;

    private List<String> mFolderNames = new ArrayList<>();
    private List<Folder> mFolders = new ArrayList<>();

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
        mStep = root.findViewById(R.id.step_text_input);
        mGroup = root.findViewById(R.id.group_spinner);
        mChangeColor = root.findViewById(R.id.change_color_button);
        mFolderNames.add(getString(R.string.select_folder));
        mFolderNames.add(getString(R.string.add_folder) + " +");
        mChangeColor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showColorPickerUi();
            }
        });
        if (getActivity() != null)
        {
            mAutoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, mFolderNames);
            mGroup.setAdapter(mAutoCompleteAdapter);
            mGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    if (position == 1)
                    {
                        FolderUtils.showAddFolder(getContext(), new CallBack()
                        {
                            @Override
                            public void execute(Object data)
                            {
                                Folder folder = new Folder((String) data);
                                mPresenter.saveFolder(folder);

                                mFolderNames.add(folder.getName());
                                mFolders.add(folder);
                                mPresenter.getCounter().setFolder(folder);

                                mAutoCompleteAdapter.notifyDataSetChanged();
                                mGroup.setSelection(mFolderNames.size() - 1);
                            }
                        });
                    }
                    else if (position != 0)
                    {
                        Folder folder = mFolders.get(position - 2);
                        mPresenter.getCounter().setFolder(folder);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });
        }

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
    public void processFolders(List<Folder> folders)
    {
        if (getActivity() != null && folders != null && folders.size() > 0)
        {
            for (Folder folder : folders)
            {
                // Add data in mFolders and mFolderNames to be sure that order is exactly the same
                mFolders.add(folder);
                mFolderNames.add(folder.getName());
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
                int step = Integer.parseInt(mStep.getText().toString());
                counter.setStep(step);
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
        if (color != null)
            mChangeColor.setBackgroundColor(Color.parseColor(color));
        else
        {
            int randomColor = Utils.getRandomColor(getContext());
            String hexRandomColor = "#" + Integer.toHexString(randomColor & 0x00ffffff); // 0x00ffffff required to remove transparency
            mChangeColor.setBackgroundColor(randomColor);
            mPresenter.getCounter().setColor(hexRandomColor);
        }
    }

    @Override
    public void setGroup(Folder folder)
    {
        if (folder != null)
        {
            for (int i = 0; i < mFolders.size(); i++)
            {
                if (mFolders.get(i).getId() == folder.getId())
                    mGroup.setSelection(i + 2); // + 2 because spinner has 2 extra entries "Select Group" and "Create Groupe"
            }
        }

    }

    @Override
    public void setLimit(int limit)
    {
        mLimit.setText(String.valueOf(limit));
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
    public void setStep(int step)
    {
        mStep.setText(String.valueOf(step));
    }

    @Override
    public void showColorPickerUi()
    {
        FragmentManager fm = getFragmentManager();
        ColorPickerFragment colorPickerFragment = ColorPickerFragment.newInstance();
        colorPickerFragment.setTargetFragment(this, ColorPickerFragment.REQUEST_COLOR_PICKER);
        if (fm != null)
            colorPickerFragment.show(fm, ColorPickerFragment.TAG_COLOR_PICKER);
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

    @Override
    public void onSelectColor(int selectedColor)
    {
        mChangeColor.setBackgroundColor(selectedColor);
        String hexColor = "#" + Integer.toHexString(selectedColor & 0x00ffffff); // 0x00ffffff required to remove transparency
        mPresenter.getCounter().setColor(hexColor);
    }
}
