package com.project.mzglinicki96.deltaDraw;

import android.graphics.Point;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mzglinicki.96 on 18.07.2016.
 */
public class PictureLoaderSupport extends AsyncTask<String, Void, List<Point>> {

    private final OnCompleteListener listener;

    public interface OnCompleteListener {
        void onLoadListComplete(final List<Point> coordinates);
    }

    public PictureLoaderSupport(final OnCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Point> doInBackground(final String... list) {

        final List<Point> coordinatesList = new ArrayList<>();
        String listFromDb = list[0];

        if (listFromDb != null) {
            final Pattern pattern = Pattern.compile("(\\d+)|(\\-\\d)");
            final Matcher matcher = pattern.matcher(listFromDb);
            while (matcher.find()) {
                int x = Integer.parseInt(matcher.group().trim());
                matcher.find();
                int y = Integer.parseInt(matcher.group().trim());
                coordinatesList.add(new Point(x, y));
            }
        }
        return coordinatesList;
    }

    @Override
    protected void onPostExecute(final List<Point> points) {
        super.onPostExecute(points);
        listener.onLoadListComplete(points);
    }
}