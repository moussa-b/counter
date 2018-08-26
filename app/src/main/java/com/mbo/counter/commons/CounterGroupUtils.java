package com.mbo.counter.commons;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.mbo.counter.R;

import static com.mbo.counter.commons.Utils.convertDpToPixel;

public class CounterGroupUtils
{
    public static void showAddCounterGroup(Context context, String counterGroupName, final CallBack callBack)
    {
        if (context != null)
        {
            final EditText addCounterGroupEditText = new EditText(context);
            if (counterGroupName != null)
                addCounterGroupEditText.setText(counterGroupName);
            addCounterGroupEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            addCounterGroupEditText.setSingleLine();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = (int) convertDpToPixel(10, context);
            params.leftMargin = margin;
            params.rightMargin = margin;
            addCounterGroupEditText.setLayoutParams(params);
            FrameLayout container = new FrameLayout(context);
            container.addView(addCounterGroupEditText);
            String positiveButtonLabel = counterGroupName == null ? context.getString(R.string.add) : context.getString(R.string.validate);

            final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.add_counter_group_dialog_title))
                    .setMessage(context.getString(R.string.add_counter_group_dialog_message))
                    .setView(container)
                    .setPositiveButton(positiveButtonLabel, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            if (callBack != null)
                                callBack.execute(addCounterGroupEditText.getText().toString());
                        }
                    })
                    .setNegativeButton(context.getString(R.string.cancel), null)
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

    public static void showAddCounterGroup(Context context, final CallBack callBack)
    {
        showAddCounterGroup(context, null, callBack);
    }
}
