package com.project.mzglinicki96.deltaDraw.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.project.mzglinicki96.deltaDraw.Constants;
import com.project.mzglinicki96.deltaDraw.FloatingColorMenuHelper;
import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.eventBus.GimBus;
import com.project.mzglinicki96.deltaDraw.eventBus.OnCreatePictureEvent;
import com.pgssoft.gimbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzglinicki.96 on 20.03.2016.
 */
public class DrawerOnScreen extends View {

    private final OnCreatePictureEvent createPicture = new OnCreatePictureEvent();
    private final ArrayList<PathModel> listOfPaths = new ArrayList<>();
    private final EventBus gimBus = GimBus.getInstance();

    private List<Point> coordinatesList = createPicture.getCoordinatesList();
    private Path defaultPath = new Path();
    private ImageView fabIcon;

    private int defaultColor;
    private int defaultColorIndicator;
    private int colorIndicator;

    public DrawerOnScreen(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawerOnScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setDefaultColorIndicator(FloatingColorMenuHelper.BLACK.getColorIndicator());
        defaultColor = getDefaultColor(defaultColorIndicator);
        gimBus.register(this);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        for (final PathModel pathModel : listOfPaths) {
            canvas.drawPath(pathModel.getPath(), pathModel.getPaint());
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {

        final OnCreatePictureEvent createPicture = new OnCreatePictureEvent();

        int xPos = (int) event.getX();
        int yPos = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                coordinatesList.add(new Point(xPos, yPos));
                break;
            case MotionEvent.ACTION_UP:
                coordinatesList.add(new Point(xPos, yPos));
                coordinatesList.add(new Point(Constants.END_SHAPE_SIGN, Constants.END_SHAPE_SIGN));
                coordinatesList.addAll(createPicture.getCoordinatesList());
                gimBus.post(new OnCreatePictureEvent(coordinatesList));
                break;
            default:
                return false;
        }
        redrawCanvas();
        return true;
    }

    public void setStrokeColorIndicator(final int color) {
        colorIndicator = FloatingColorMenuHelper.values()[color].getColorIndicator();
        coordinatesList.add(new Point(colorIndicator, colorIndicator));
    }

    public void setFABIcon(final ImageView fabIcon) {
        this.fabIcon = fabIcon;
    }

    public void setDefaultColorIndicator(final int colorIndicator) {
        defaultColorIndicator = colorIndicator;
    }

    public int getDefaultColorIndicator() {
        return defaultColorIndicator;
    }

    public int getDefaultColor(final int colorIndicator) {
        return getColorOrdinal(colorIndicator);
    }

    private int getColorOrdinal(final int colorIndicator) {

        int color = 0;

        for (final FloatingColorMenuHelper floatingColorMenuHelper : FloatingColorMenuHelper.values()) {
            if (floatingColorMenuHelper.getColorIndicator() == colorIndicator) {
                color = floatingColorMenuHelper.getColor();
                if (fabIcon != null) {
                    setBrushColor(color);
                }
                break;
            }
        }
        return color;
    }

    private boolean isStartShapeColorDefault(final List<Point> points) {

        int startListXCoordinate = points.get(0).x;

        if (startListXCoordinate < 0) {
            return false;
        } else {
            colorIndicator = getDefaultColorIndicator();
            defaultPath.moveTo(startListXCoordinate, points.get(0).y);
        }
        return true;
    }

    private void drawShapes(final List<Point> points, boolean isStartShapeColorDefault) {
        for (final Point point : points) {

            if (!isStartShapeColorDefault && point.x == point.y) {
                colorIndicator = point.x;
                colorToggler(colorIndicator);
                continue;
            }

            if (!isStartShapeColorDefault) {
                defaultPath.moveTo(point.x, point.y);
                isStartShapeColorDefault = true;
            }

            if (point.x >= 0) {
                defaultPath.lineTo(point.x, point.y);
            } else if (point.x == Constants.END_SHAPE_SIGN) {
                isStartShapeColorDefault = false;
            }
        }
    }

    private void redrawCanvas() {
        redrawCanvas(null);
    }

    public void redrawCanvas(final OnCreatePictureEvent event) {
        if (event != null && !event.getCoordinatesList().isEmpty()) {
            coordinatesList = event.getCoordinatesList();
        }
        listOfPaths.clear();
        defaultPath.reset();
        listOfPaths.add(new PathModel(defaultPath, defaultColor));
        drawShapes(coordinatesList, isStartShapeColorDefault(coordinatesList));
        invalidate();
    }

    private void colorToggler(final int colorIndicator) {
        createNewPathModel(getColorOrdinal(colorIndicator));
    }

    private void createNewPathModel(final int color) {
        defaultPath = new Path();
        listOfPaths.add(new PathModel(defaultPath, color));
    }

    public void clearAll() {

        defaultPath.reset();
        coordinatesList.clear();
        listOfPaths.clear();
        colorIndicator = getDefaultColorIndicator();
        setBrushColor(Color.WHITE);
        invalidate();
    }

    public void clearLastShape() {
        if (!coordinatesList.isEmpty()) {
            defaultPath.reset();

            for (int i = coordinatesList.size() - 1; i >= 0; i--) {
                coordinatesList.remove(i);
                try {
                    if (coordinatesList.get(i - 1).x == Constants.END_SHAPE_SIGN) {
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException onLastShape) {
                    setBrushColor(Color.WHITE);
                    break;
                }
            }
            changeBrushColor();
        }
        invalidate();
    }

    private void changeBrushColor() {
        for (int i = coordinatesList.size() - 1; i >= 0; i--) {
            try {
                if (coordinatesList.get(i - 1).x <= 0 && coordinatesList.get(i - 1).x != Constants.END_SHAPE_SIGN) {
                    colorIndicator = coordinatesList.get(i - 1).x;
                    setBrushColor(colorIndicator);
                    break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                setBrushColor(defaultColor);
                break;
            }
        }
    }

    public void setBrushColor(int color) {
        final VectorDrawableCompat drawableCompat = VectorDrawableCompat.create(getResources(), R.drawable.ic_brush_24dp, getContext().getTheme());
        assert drawableCompat != null;
        drawableCompat.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        fabIcon.setImageDrawable(drawableCompat);
    }
}