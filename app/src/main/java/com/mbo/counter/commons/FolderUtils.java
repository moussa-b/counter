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

public class FolderUtils
{
    public static void showAddFolder(Context context, String folderName, final CallBack callBack)
    {
        if (context != null)
        {
            final EditText addFolderEditText = new EditText(context);
            if (folderName != null)
                addFolderEditText.setText(folderName);
            addFolderEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            addFolderEditText.setSingleLine();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margin = (int) convertDpToPixel(10, context);
            params.leftMargin = margin;
            params.rightMargin = margin;
            addFolderEditText.setLayoutParams(params);
            FrameLayout container = new FrameLayout(context);
            container.addView(addFolderEditText);
            String positiveButtonLabel = folderName == null ? context.getString(R.string.add) : context.getString(R.string.validate);

            final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.add_folder_dialog_title))
                    .setMessage(context.getString(R.string.add_folder_dialog_message))
                    .setView(container)
                    .setPositiveButton(positiveButtonLabel, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            if (callBack != null)
                                callBack.execute(addFolderEditText.getText().toString());
                        }
                    })
                    .setNegativeButton(context.getString(R.string.cancel), null)
                    .create();

            dialog.show();

            final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            if (positiveButton != null)
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

            addFolderEditText.addTextChangedListener(new TextWatcher()
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

    public static void showAddFolder(Context context, final CallBack callBack)
    {
        showAddFolder(context, null, callBack);
    }
}
