package com.bdzapps.counterpp.colorpicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;

import com.bdzapps.counterpp.commons.Utils;
import com.mbo.counter.R;

import static com.bdzapps.counterpp.commons.Utils.convertDpToPixel;

public class ColorPickerFragment extends DialogFragment
{
    public static final int REQUEST_COLOR_PICKER = 100;
    public static final String TAG_COLOR_PICKER = "fragment_color_picker";
    public static final String ARGUMENT_COLOR = "ARGUMENT_COLOR";
    private static final String ARGUMENT_TITLE = "ARGUMENT_TITLE";
    private static final String ARGUMENT_MESSAGE = "ARGUMENT_MESSAGE";
    private int mSelectedPosition = -1;
    private ColorPickerAdapter mColorPickerAdapter;
    private String[] mAndroidColors;

    public ColorPickerFragment()
    {
    }

    public static ColorPickerFragment newInstance()
    {
        return new ColorPickerFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Activity activity = getActivity();

        if (activity != null)
        {
            String title = getArguments() != null ? getArguments().getString(ARGUMENT_TITLE) :
                    activity.getResources().getString(R.string.color_picker_title);
            String message = getArguments() != null ? getArguments().getString(ARGUMENT_MESSAGE) :
                    activity.getResources().getString(R.string.color_picker_message);
            String initialColor = getArguments() != null ? getArguments().getString(ARGUMENT_COLOR) : null;

            mAndroidColors = getResources().getStringArray(R.array.android_colors);
            mColorPickerAdapter = new ColorPickerAdapter(mAndroidColors, initialColor);
            final Fragment parentFragment = getTargetFragment();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    if (mSelectedPosition >= 0 && mAndroidColors != null && mAndroidColors.length > 0)
                    {
                        if (activity instanceof ColorPickerListener)
                            ((ColorPickerListener) activity).onSelectColor(mAndroidColors[mSelectedPosition]);

                        if (parentFragment instanceof ColorPickerListener)
                            ((ColorPickerListener) parentFragment).onSelectColor(mAndroidColors[mSelectedPosition]);
                    }
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    if (dialog != null)
                    {
                        dialog.dismiss();
                    }
                }
            });

            // Prepare grid view
            GridView gridView = new GridView(activity);
            gridView.setAdapter(mColorPickerAdapter);
            gridView.setNumColumns(5);
            gridView.setVerticalSpacing((int) convertDpToPixel(12, activity));
            gridView.setHorizontalSpacing((int) convertDpToPixel(12, activity));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    mSelectedPosition = position;
                    mColorPickerAdapter.setSelectedColor(position);
                }
            });

            int padding = (int) convertDpToPixel(10, activity);
            final ScrollView sv = new ScrollView(activity);
            sv.addView(gridView);
            sv.setPadding(padding, padding, padding, padding);
            sv.setFillViewport(true);
            sv.setMinimumHeight((int) (0.6 * Utils.getDisplayWidth(activity)));
            alertDialogBuilder.setView(sv);

            return alertDialogBuilder.create();
        }
        else
            return super.onCreateDialog(savedInstanceState);
    }


}
