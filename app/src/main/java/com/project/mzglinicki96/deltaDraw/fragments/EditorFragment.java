package com.project.mzglinicki96.deltaDraw.fragments;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pgssoft.gimbus.Subscribe;
import com.project.mzglinicki96.deltaDraw.Constants;
import com.project.mzglinicki96.deltaDraw.FloatingColorMenuHelper;
import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.eventBus.GimBus;
import com.project.mzglinicki96.deltaDraw.eventBus.OnCreatePictureEvent;

import java.util.ArrayList;
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
    @Bind(R.id.errorCardView)
    CardView errorCardView;
    @Bind(R.id.errorTextView)
    TextView errorTextView;

    private static final int NUM_OF_POINTS_INCREMENTATOR = 10;
    private List<String> linesWithErrors;
    private InputMethodManager inputManager;

    public EditorFragment() {
        titleId = R.string.edit_tab_title;
        layoutId = R.layout.fragment_edit;
        GimBus.getInstance().register(this);
    }

    @Override
    protected void init(final View view) {
        ButterKnife.bind(this, view);
        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @OnClick(R.id.editTextListOfPoints)
    public void onCodeEditTextClick() {
        if (inputManager.isActive()) {
            errorCardView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isSwipeable() {
        return true;
    }

    @OnClick(R.id.editCodeAcceptor)
    public void onEditCodeAcceptorClick() {

        inputManager.hideSoftInputFromWindow(pointsHolder.getWindowToken(), 0);

        if (!isInputDataCorrect()) {
            showErrorView();
            return;
        }
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

    private void showErrorView() {
        errorCardView.setVisibility(View.VISIBLE);
        final StringBuffer errorMassage = new StringBuffer();
        if (linesWithErrors.size() == 1){
            errorMassage.append(getResources().getString(R.string.error));
        }else {
            errorMassage.append(getResources().getString(R.string.errors));
        }
        for (final String lineWithError : linesWithErrors) {
            errorMassage.append(lineWithError).append("\n");
        }
        errorTextView.setText(errorMassage);
    }

    private boolean isInputDataCorrect() {

        linesWithErrors = new ArrayList<>();
        final String codeByLines[] = pointsHolder.getText().toString().split("[\n]");
        final Pattern linePattern = Pattern.compile("(\\D\\d+\\s\\d+\\D\\s\\d+)|(\\-\\d\\D\\s\\-\\d)");

        for (int i = 0; i < codeByLines.length - 1; i++) {
            final String line = codeByLines[i];
            final Matcher lineMatcher = linePattern.matcher(line);
            if (!lineMatcher.matches()) {
                linesWithErrors.add(line);
            }
        }
        return linesWithErrors.isEmpty();
    }

    private void clearErrors() {
        if (linesWithErrors != null && !linesWithErrors.isEmpty()) {
            linesWithErrors.clear();
        }
        errorCardView.setVisibility(View.GONE);
    }

    @Subscribe
    public void writeCode(final OnCreatePictureEvent event) {

        clearErrors();
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