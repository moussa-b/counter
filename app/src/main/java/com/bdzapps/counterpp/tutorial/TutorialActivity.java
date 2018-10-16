package com.bdzapps.counterpp.tutorial;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdzapps.counterpp.commons.SharedPrefManager;
import com.mbo.counter.R;

public class TutorialActivity extends AppCompatActivity
{
    private ViewPager mViewPager;
    private LinearLayout mDotsLayout;
    private int[] layouts;
    private Button mbuttonSkip, mbuttonNext;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener()
    {

        @Override
        public void onPageSelected(int position)
        {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1)
            {
                // last page. make button text to GOT IT
                mbuttonNext.setText(getString(R.string.start));
                mbuttonSkip.setVisibility(View.GONE);
            }
            else
            {
                // still pages are left
                mbuttonNext.setText(getString(R.string.next));
                mbuttonSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {
        }

        @Override
        public void onPageScrollStateChanged(int arg0)
        {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        if (!SharedPrefManager.isFirstTimeLaunch(this))
            closeTutorial();

        // Making notification bar transparent
        // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.tutorial_activity);

        mViewPager = findViewById(R.id.view_pager);
        mDotsLayout = findViewById(R.id.layoutDots);
        mbuttonSkip = findViewById(R.id.btn_skip);
        mbuttonNext = findViewById(R.id.btn_next);

        // layouts of all welcome sliders
        layouts = new int[]{
                R.layout.tutorial_slide1,
                R.layout.tutorial_slide2,
                R.layout.tutorial_slide3,
                R.layout.tutorial_slide4};

        // adding bottom mDots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        TutorialActivity.TutorialPagerAdapter tutorialPagerAdapter = new TutorialPagerAdapter();
        mViewPager.setAdapter(tutorialPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        mbuttonSkip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                closeTutorial();
            }
        });

        mbuttonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int nextPage = mViewPager.getCurrentItem() + 1;
                if (nextPage < layouts.length)
                    mViewPager.setCurrentItem(nextPage);
                else
                    closeTutorial();

            }
        });
    }

    private void closeTutorial()
    {
        SharedPrefManager.setFirstTimeLaunch(this, false);
        finish();
    }

    private void addBottomDots(int currentPage)
    {
        TextView[] mDots = new TextView[layouts.length];

        mDotsLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.light_grey));
            mDotsLayout.addView(mDots[i]);
        }

        if (mDots.length > 0)
            mDots[currentPage].setTextColor(getResources().getColor(R.color.white));
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor()
    {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    public class TutorialPagerAdapter extends PagerAdapter
    {
        private LayoutInflater layoutInflater;

        private TutorialPagerAdapter()
        {
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position)
        {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert layoutInflater != null;
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount()
        {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj)
        {
            return view == obj;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)
        {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
