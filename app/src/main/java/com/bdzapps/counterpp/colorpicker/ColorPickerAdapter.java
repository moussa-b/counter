package com.bdzapps.counterpp.colorpicker;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.mbo.counter.R;

public class ColorPickerAdapter extends BaseAdapter
{
    private final String[] mColors;
    private int mSelectedColor = -1;

    public ColorPickerAdapter(String[] mColors)
    {
        this.mColors = mColors;
    }

    @Override
    public int getCount()
    {
        return mColors.length;
    }

    @Override
    public Object getItem(int position)
    {
        return mColors[position];
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        GridView grid = (GridView) parent;
        int size = grid.getColumnWidth();
        View rowView = convertView;
        if (rowView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(R.layout.color_picker_item, parent, false);
        }

        ImageView colorPickerImageView = rowView.findViewById(R.id.color_picker_image_button);
        ImageView selectedColorImageView = rowView.findViewById(R.id.selected_color_image_view);

        colorPickerImageView.setLayoutParams(new ConstraintLayout.LayoutParams(size, size));
        colorPickerImageView.setPadding(8, 8, 8, 8);
        colorPickerImageView.setBackgroundColor(Color.parseColor(mColors[position]));
        selectedColorImageView.setVisibility(position == mSelectedColor ? View.VISIBLE : View.GONE);

        return rowView;
    }

    public void setSelectedColor(int position)
    {
        mSelectedColor = position;
        notifyDataSetChanged();
    }
}
