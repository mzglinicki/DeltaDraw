package com.project.mzglinicki96.deltaDraw;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzglinicki.96 on 11.07.2016.
 */
public class PointListHolder {

    private List<Point> coordinatesList = new ArrayList<>();

    private static PointListHolder list = null;

    private PointListHolder() {}

    public static PointListHolder getInstance()  {

        if(list == null){
            list = new PointListHolder();
        }
        return list;
    }

    public List<Point> getCoordinatesList() {
        return coordinatesList;
    }

    public void setCoordinatesList(final List<Point> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }
}