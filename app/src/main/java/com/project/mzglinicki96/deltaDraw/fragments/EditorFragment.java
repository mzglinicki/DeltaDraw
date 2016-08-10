package com.project.mzglinicki96.deltaDraw.fragments;

import android.graphics.Point;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pgssoft.gimbus.Subscribe;
import com.project.mzglinicki96.deltaDraw.Constants;
import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.eventBus.GimBus;
import com.project.mzglinicki96.deltaDraw.eventBus.OnCreatePictureEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mzglinicki.96 on 22.03.2016.
 */
public class EditorFragment extends FragmentParent {

    @Bind(R.id.editTextListOfPoints)
    EditText pointsHolder;
    @Bind(R.id.editCodeAcceptor)
    Button editCodeAcceptor;

    private static final int NUM_OF_POINTS_INCREMENTATOR = 10;
    private boolean errorInList = false;

    public EditorFragment() {
        titleId = R.string.edit_tab_title;
        layoutId = R.layout.fragment_edit;
        GimBus.getInstance().register(this);
    }

    @Override
    protected void init(final View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public boolean isSwipeable() {
        return true;
    }

    @OnClick(R.id.editCodeAcceptor)
    public void onEditCodeAcceptorClick() {

        coordinatesList.clear();
        int x;
        int y;

        final Pattern pattern = Pattern.compile("(\\s\\d+)|(\\-\\d)");
        final Matcher matcher = pattern.matcher(pointsHolder.getText().toString());
        while (matcher.find()) {
            try {
                x = Integer.parseInt(matcher.group().trim());
                matcher.find();
                y = Integer.parseInt(matcher.group().trim());
            } catch (IllegalStateException exception) {
                errorInList = true;
                continue;
            }
            coordinatesList.add(new Point(x, y));
        }
        if (errorInList) {
            Toast.makeText(getContext(), R.string.errorsInList, Toast.LENGTH_SHORT).show();
            errorInList = false;
        }else {
            GimBus.getInstance().post(new OnCreatePictureEvent(coordinatesList));
            Toast.makeText(getContext(), R.string.saved_toast, Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void writeCode(final OnCreatePictureEvent event) {

        int numOfPoint = NUM_OF_POINTS_INCREMENTATOR;

        StringBuffer coordinatesListToPrint = new StringBuffer();
        final List<Point> pointList = event.getCoordinatesList();

        if (pointList.isEmpty()) {
            pointsHolder.setText(null);
        } else {
            for (final Point point : pointList) {
                if (point.x < 0 && point.x == Constants.END_SHAPE_SIGN) {
                    coordinatesListToPrint.append(point.x).append(", ").append(point.y).append("\n");
                    continue;
                } else if (point.x < 0) {
                    coordinatesListToPrint.append(point.x).append(", ").append(point.y).append("\n");
                    continue;
                }
                coordinatesListToPrint.append(Constants.POINT_SIGN).append(numOfPoint).append(" ").append(point.x).append(", ").append(point.y).append("\n");
                numOfPoint += NUM_OF_POINTS_INCREMENTATOR;
            }
            pointsHolder.setText(coordinatesListToPrint);
        }
        coordinatesList = event.getCoordinatesList();
    }
}