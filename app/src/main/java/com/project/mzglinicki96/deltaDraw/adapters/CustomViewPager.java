package com.project.mzglinicki96.deltaDraw.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.project.mzglinicki96.deltaDraw.eventBus.GimBus;
import com.project.mzglinicki96.deltaDraw.fragments.CloseMenuEvent;

/**
 * Created by mzglinicki.96 on 24.03.2016.
 */
public class CustomViewPager extends ViewPager {

    private boolean swipeable;

    public CustomViewPager(final Context context) {
        super(context);
    }

    public CustomViewPager(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.swipeable = false;
        this.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                swipeable = ((PagerAdapter) getAdapter()).isSwipeable(position);
                if (swipeable) {
                    GimBus.getInstance().post(new CloseMenuEvent());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        return this.swipeable && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {
        return this.swipeable && super.onInterceptTouchEvent(event);
    }
}