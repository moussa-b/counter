package com.bdzapps.counterpp.addeditcounter;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bdzapps.counterpp.colorpicker.ColorPickerFragment;
import com.bdzapps.counterpp.colorpicker.ColorPickerListener;
import com.bdzapps.counterpp.commons.CallBack;
import com.bdzapps.counterpp.commons.FolderUtils;
import com.bdzapps.counterpp.commons.Utils;
import com.bdzapps.counterpp.data.model.Counter;
import com.bdzapps.counterpp.data.model.Folder;
import com.mbo.counter.R;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditCounterFragment extends Fragment implements AddEditCounterContract.View, ColorPickerListener
{
    public static final String ARGUMENT_EDIT_COUNTER_ID = "EDIT_COUNTER_ID";

    private TextInputEditText mName, mLimit, mNote;

    private Spinner mGroup;

    private Button mChangeColor;

    private AddEditCounterContract.Presenter mPresenter;

    private ArrayAdapter<String> mSpinnerAdapter;

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
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.add_edit_counter_fragment, container, false);
        mName = root.findViewById(R.id.name_text_input);
        mLimit = root.findViewById(R.id.limit_text_input);
        mNote = root.findViewById(R.id.note_text_input);
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
            mSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, mFolderNames)
            {
                @Override
                public boolean isEnabled(int position)
                {
                    return position != 0;
                }

                @Override
                public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
                {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    tv.setTextColor(container.getContext().getResources().getColor(position == 0 ? R.color.silver : R.color.black));
                    return view;
                }
            };
            mGroup.setAdapter(mSpinnerAdapter);
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

                                mSpinnerAdapter.notifyDataSetChanged();
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
            mSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
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

            mSpinnerAdapter.notifyDataSetChanged();
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
        if (color != null)
            mChangeColor.setBackgroundColor(Color.parseColor(color));
        else
        {
            String randomColor = Utils.getRandomColor(getContext());
            mChangeColor.setBackgroundColor(Color.parseColor(randomColor));
            mPresenter.getCounter().setColor(randomColor);
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
    public void showColorPickerUi()
    {
        Bundle bundle = new Bundle();
        bundle.putString(ColorPickerFragment.ARGUMENT_COLOR, mPresenter.getCounter().getColor());

        FragmentManager fm = getFragmentManager();
        ColorPickerFragment colorPickerFragment = ColorPickerFragment.newInstance();
        colorPickerFragment.setArguments(bundle); // Set initial color
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
    public void onSelectColor(String selectedColor)
    {
        mChangeColor.setBackgroundColor(Color.parseColor(selectedColor));
        mPresenter.getCounter().setColor(selectedColor);
    }
}
