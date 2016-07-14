package com.project.mzglinicki96.deltaDraw.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.mzglinicki96.deltaDraw.Constants;
import com.project.mzglinicki96.deltaDraw.eventBus.GimBus;
import com.project.mzglinicki96.deltaDraw.eventBus.OnCreatePictureEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        checkIfFromLib(getArguments());
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

    public void getPicture(final String list) {

        if (list != null) {
            final Pattern pattern = Pattern.compile("(\\d+)|(\\-\\d)");
            final Matcher matcher = pattern.matcher(list);
            while (matcher.find()) {
                int x = Integer.parseInt(matcher.group().trim());
                matcher.find();
                int y = Integer.parseInt(matcher.group().trim());
                coordinatesList.add(new Point(x, y));
            }
            GimBus.getInstance().post(new OnCreatePictureEvent(coordinatesList));
        }
    }

    private boolean checkIfFromLib(final Bundle bundle) {
        try {
            getPicture(new JSONObject(bundle.getString(Constants.POINTS_IN_JASON)).getString(Constants.KEY_LIST));
        } catch (NullPointerException emptyJson) {
            return true;
        } catch (JSONException e) {
            return false;
        }
        return false;
    }

    public abstract boolean isSwipeable();

    protected abstract void init(final View view);
}