package com.mbo.commons.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mbo.commons.R;

import java.util.ArrayList;

public class SegmentedButton extends RelativeLayout implements RadioCheckable
{
    // Constants
    public static final int DEFAULT_TEXT_COLOR = Color.TRANSPARENT;
    // https://crosp.net/blog/android/creating-custom-radio-groups-radio-buttons-android/
    // Views
    private TextView mTitleTextView, mSubtitleTextView;
    // Attribute Variables
    private String mTitle;
    private String mSubtitle;
    private boolean mIsSubtitleVisible;
    private int mTitleTextColor;
    private int mSubtitleTextColor;
    private int mPressedTextColor;

    // Variables
    private Drawable mInitialBackgroundDrawable;
    private OnClickListener mOnClickListener;
    private OnTouchListener mOnTouchListener;
    private boolean mChecked;
    private ArrayList<OnCheckedChangeListener> mOnCheckedChangeListeners = new ArrayList<>();


    //================================================================================
    // Constructors
    //================================================================================

    public SegmentedButton(Context context)
    {
        super(context);
        setupView();
    }

    public SegmentedButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        parseAttributes(attrs);
        setupView();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public SegmentedButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        parseAttributes(attrs);
        setupView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SegmentedButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttributes(attrs);
        setupView();
    }

    //================================================================================
    // Init & inflate methods
    //================================================================================

    private void parseAttributes(AttributeSet attrs)
    {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SegmentedButton, 0, 0);
        Resources resources = getContext().getResources();
        try
        {
            mTitle = a.getString(R.styleable.SegmentedButton_sb_title);
            mTitleTextColor = a.getColor(R.styleable.SegmentedButton_sb_title_text_color, resources.getColor(R.color.black));
            mPressedTextColor = a.getColor(R.styleable.SegmentedButton_sb_pressed_text_color, Color.WHITE);
            mIsSubtitleVisible = a.getBoolean(R.styleable.SegmentedButton_sb_subtitle_visible, false);
            mSubtitle = a.getString(R.styleable.SegmentedButton_sb_subtitle);
            mSubtitleTextColor = a.getColor(R.styleable.SegmentedButton_sb_subtitle_text_color, resources.getColor(R.color.gray));
        }
        finally
        {
            a.recycle();
        }
    }

    // Template method
    private void setupView()
    {
        inflateView();
        bindView();
        setCustomTouchListener();
    }

    protected void inflateView()
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.segmented_button, this, true);
        mTitleTextView = (TextView) findViewById(R.id.title_text_view);
        mSubtitleTextView = (TextView) findViewById(R.id.subtitle_text_view);
        mInitialBackgroundDrawable = getBackground();
    }

    protected void bindView()
    {
        if (mSubtitleTextColor != DEFAULT_TEXT_COLOR)
        {
            mSubtitleTextView.setTextColor(mSubtitleTextColor);
        }
        if (mTitleTextColor != DEFAULT_TEXT_COLOR)
        {
            mTitleTextView.setTextColor(mTitleTextColor);
        }
        mSubtitleTextView.setText(mSubtitle);
        mSubtitleTextView.setVisibility(mIsSubtitleVisible ? VISIBLE : GONE);
        mTitleTextView.setText(mTitle);
    }

    //================================================================================
    // Overriding default behavior
    //================================================================================

    @Override
    public void setOnClickListener(@Nullable OnClickListener l)
    {
        mOnClickListener = l;
    }

    protected void setCustomTouchListener()
    {
        super.setOnTouchListener(new TouchListener());
    }

    public OnTouchListener getOnTouchListener()
    {
        return mOnTouchListener;
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener)
    {
        mOnTouchListener = onTouchListener;
    }

    private void onTouchDown(MotionEvent motionEvent)
    {
        setChecked(true);
    }

    private void onTouchUp(MotionEvent motionEvent)
    {
        // Handle user defined click listeners
        if (mOnClickListener != null)
        {
            mOnClickListener.onClick(this);
        }
    }
    //================================================================================
    // Public methods
    //================================================================================

    public void setCheckedState()
    {
        setBackgroundResource(R.drawable.background_shape_segmented_button_pressed);
        mTitleTextView.setTextColor(mPressedTextColor);
        mSubtitleTextView.setTextColor(mPressedTextColor);
    }

    public void setNormalState()
    {
        setBackgroundDrawable(mInitialBackgroundDrawable);
        mTitleTextView.setTextColor(mTitleTextColor);
        mSubtitleTextView.setTextColor(mSubtitleTextColor);
    }

    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }

    public String getSubtitle()
    {
        return mSubtitle;
    }

    public void setSubtitle(String subtitle)
    {
        mSubtitle = subtitle;
    }

    //================================================================================
    // Checkable implementation
    //================================================================================

    @Override
    public boolean isChecked()
    {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked)
    {
        if (mChecked != checked)
        {
            mChecked = checked;
            if (!mOnCheckedChangeListeners.isEmpty())
            {
                for (int i = 0; i < mOnCheckedChangeListeners.size(); i++)
                {
                    mOnCheckedChangeListeners.get(i).onCheckedChanged(this, mChecked);
                }
            }
            if (mChecked)
            {
                setCheckedState();
            }
            else
            {
                setNormalState();
            }
        }
    }

    @Override
    public void toggle()
    {
        setChecked(!mChecked);
    }

    @Override
    public void addOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener)
    {
        mOnCheckedChangeListeners.add(onCheckedChangeListener);
    }

    @Override
    public void removeOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener)
    {
        mOnCheckedChangeListeners.remove(onCheckedChangeListener);
    }

    //================================================================================
    // Inner classes
    //================================================================================
    private final class TouchListener implements OnTouchListener
    {

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    onTouchDown(event);
                    break;
                case MotionEvent.ACTION_UP:
                    onTouchUp(event);
                    break;
            }
            if (mOnTouchListener != null)
            {
                mOnTouchListener.onTouch(v, event);
            }
            return true;
        }
    }
}
