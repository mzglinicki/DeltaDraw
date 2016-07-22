package com.project.mzglinicki96.deltaDraw.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.mzglinicki96.deltaDraw.fragments.FragmentParent;

/**
 * Created by mzglinicki.96 on 21.03.2016.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private final FragmentParent[] fragments;
    private final Context context;

    public PagerAdapter(final Context context, final FragmentManager fm, final FragmentParent[] fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public Fragment getItem(final int position) {
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return context.getResources().getString(fragments[position].getTitleId());
    }

    public boolean isSwipeable(final int position) {
        return fragments[position].isSwipeable();
    }
}