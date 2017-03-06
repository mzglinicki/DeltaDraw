package com.project.mzglinicki96.deltaDraw.eventBus;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzglinicki.96 on 25.03.2016.
 */
public class OnCreatePictureEvent {

    private List<Point> coordinatesList = new ArrayList<>();

    public OnCreatePictureEvent() {
    }

    public OnCreatePictureEvent(final List<Point> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

    public List<Point> getCoordinatesList() {
        return coordinatesList;
    }
}