package com.project.mzglinicki96.deltaDraw.fragments;

import android.graphics.Point;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.mzglinicki96.deltaDraw.Constants;
import com.project.mzglinicki96.deltaDraw.FloatingColorMenuHelper;
import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.eventBus.GimBus;
import com.project.mzglinicki96.deltaDraw.eventBus.OnCreatePictureEvent;
import com.pgssoft.gimbus.EventBus;
import com.pgssoft.gimbus.Subscribe;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mzglinicki.96 on 22.03.2016.
 */
public class EditorFragment extends FragmentParent {

    private static final int NUM_OF_POINTS_INCREMENTATOR = 10;

    private EditText pointsHolder;
    private Button editCodeAcceptor;

    public EditorFragment() {
        titleId = R.string.edit_tab_title;
        layoutId = R.layout.fragment_edit;
        final EventBus gimBus = GimBus.getInstance();
        gimBus.register(this);
    }

    @Override
    protected void init(final View view) {
        pointsHolder = (EditText) view.findViewById(R.id.editTextListOfPoints);
        editCodeAcceptor = (Button) view.findViewById(R.id.editCodeAcceptor);
        editTextListener();
    }

    @Override
    public boolean isSwipeable() {
        return true;
    }

    private void editTextListener() {

        editCodeAcceptor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        continue;
                    }
                    coordinatesList.add(new Point(x, y));
                }
                if (!coordinatesList.isEmpty()) {
                    coordinatesList.add(new Point(Constants.END_SHAPE_SIGN, Constants.END_SHAPE_SIGN));
                }
                GimBus.getInstance().post(new OnCreatePictureEvent(coordinatesList));
                Toast.makeText(getContext(), R.string.saved_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private StringBuffer checkColorChanging(StringBuffer coordinatesListToPrint, Point point) {
        for (FloatingColorMenuHelper floatingColorMenuHelper : FloatingColorMenuHelper.values()) {
            if (floatingColorMenuHelper.getColorIndicator() == point.x) {
                String color = getContext().getString(floatingColorMenuHelper.getColorId());
                coordinatesListToPrint.append(color).append("\n");
            }
        }
        return coordinatesListToPrint;
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
                if (point.x < 0) {
                    if (point.x == Constants.END_SHAPE_SIGN) {
                        coordinatesListToPrint.append(getContext().getString(R.string.end_of_shape)).append("\n");
                        continue;
                    }
                    coordinatesListToPrint = checkColorChanging(coordinatesListToPrint, point);
                    continue;
                } else if (numOfPoint == NUM_OF_POINTS_INCREMENTATOR && point == pointList.get(0)) {
                    coordinatesListToPrint.append(getContext().getString(R.string.black)).append("\n");
                }
                coordinatesListToPrint.append(Constants.POINT_SIGN).append(numOfPoint).append(" ").append(point.x).append(", ").append(point.y).append("\n");
                numOfPoint += NUM_OF_POINTS_INCREMENTATOR;
            }
            pointsHolder.setText(coordinatesListToPrint);
        }
        coordinatesList = event.getCoordinatesList();
    }
}