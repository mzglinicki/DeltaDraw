package com.project.mzglinicki96.deltaDraw.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzglinicki.96 on 22.03.2016.
 */
public abstract class FragmentParent extends Fragment {

    protected List<Point> coordinatesList = new ArrayList<>();
    protected int titleId;
    protected int layoutId;
    private View view;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(layoutId, container, false);
        init(view);
        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public int getTitleId() {
        return titleId;
    }

    public abstract boolean isSwipeable();

    protected abstract void init(final View view);
}