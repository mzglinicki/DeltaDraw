package com.project.mzglinicki96.deltaDraw.fragments;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by mzglinicki.96 on 10.07.2016.
 */
public class PathModel {

    private final static int STROKE_WIDTH = 5;
    private Path path;
    private int color;

    public PathModel(Path path, int color) {
        this.path = path;
        this.color = color;
    }

    public Path getPath() {
        return path;
    }

    public Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(color);
        return paint;
    }
}