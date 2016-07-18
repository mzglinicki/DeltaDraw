package com.project.mzglinicki96.deltaDraw.fragments;

import android.graphics.Point;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.mzglinicki96.deltaDraw.Constants;
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

        setupEditCodeAcceptorListener();
    }

    @Override
    public boolean isSwipeable() {
        return true;
    }

    private void setupEditCodeAcceptorListener() {

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
                GimBus.getInstance().post(new OnCreatePictureEvent(coordinatesList));
                Toast.makeText(getContext(), R.string.saved_toast, Toast.LENGTH_SHORT).show();
            }
        });
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