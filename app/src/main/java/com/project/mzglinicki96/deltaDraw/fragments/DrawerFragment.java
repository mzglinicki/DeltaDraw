package com.project.mzglinicki96.deltaDraw.fragments;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.ClipData;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.pgssoft.gimbus.Subscribe;
import com.project.mzglinicki96.deltaDraw.Constants;
import com.project.mzglinicki96.deltaDraw.FloatingColorMenuHelper;
import com.project.mzglinicki96.deltaDraw.activities.MyApplication;
import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.eventBus.GimBus;
import com.project.mzglinicki96.deltaDraw.eventBus.OnCreatePictureEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mzglinicki.96 on 21.03.2016.
 */
public class DrawerFragment extends FragmentParent implements View.OnDragListener {

    @Inject
    SharedPreferences sharedPreferences;
    @Bind(R.id.canvas)
    DrawerOnScreen drawer;
    @Bind(R.id.fabMenuContainer)
    RelativeLayout container;

    private final static int FLOATING_COLOR_MENU_RADIUS = 200;
    private final static int MENU_INITIAL_ROTATION = 0;
    private final static int ON_MENU_OPEN_ROTATION = 45;
    private final static int ZERO_DEGREE_ANGLE = 0;
    private final static int RIGHT_ANGLE = 90;
    private final static int STRAIGHT_ANGLE = 180;
    private final static int THREE_QUARTERS_ANGLE = 270;
    private final static int FULL_ANGLE = 360;

    private FloatingActionMenu actionMenu;
    private List<ImageView> actionImages;
    private FloatingActionButton floatingActionButton;

    public DrawerFragment() {

        titleId = R.string.draw_tab_title;
        layoutId = R.layout.fragment_drawer;

        GimBus.getInstance().register(this);
    }

    @Override
    protected void init(final View view) {
        view.setOnDragListener(this);
        ButterKnife.bind(this, view);
        ((MyApplication) getActivity().getApplication()).getComponent().inject(this);

        if(sharedPreferences.getBoolean(Constants.COLOR_MENU_VISIBILITY, true)){
            createFloatingColorChangeMenu(STRAIGHT_ANGLE, THREE_QUARTERS_ANGLE);
        }
    }

    @Override
    public boolean isSwipeable() {
        return false;
    }

    @Override
    public boolean onDrag(final View v, final DragEvent event) {

        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        final View menuButton = (View) event.getLocalState();
        final ViewGroup owner = (ViewGroup) menuButton.getParent();
        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) owner.getLayoutParams();

        int viewHeight = v.getHeight();
        int viewWidth = v.getWidth();

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                closeMenu(null);
                break;
            case DragEvent.ACTION_DROP:

                int xCord = (int) event.getX();
                int yCord = (int) event.getY();
                int bottomMargin = viewHeight - yCord - (owner.getHeight() / 2);
                int rightMargin = viewWidth - xCord - (owner.getWidth() / 2);

                layoutParams.setMargins(0, 0, rightMargin, bottomMargin);
                owner.setLayoutParams(layoutParams);
                menuButton.setVisibility(View.VISIBLE);

                setMenuOrientation(xCord, yCord, viewWidth, viewHeight);
                break;
            default:
                break;
        }
        return true;
    }

    private void createFloatingColorChangeMenu(int startAngle, int endAngle) {

        if (floatingActionButton == null) {
            floatingActionButton = createMenuMainButton();
            setOnFABLongClickListener(floatingActionButton);
        }
        actionMenu = createFloatingActionMenu(floatingActionButton, startAngle, endAngle);
        setupOnMenuClickAnimation(floatingActionButton);
        setupOnMenuClickListener();
    }

    private FloatingActionButton createMenuMainButton() {

        final ImageView fabIcon = new ImageView(getContext());
        final VectorDrawableCompat drawableCompat = VectorDrawableCompat.create(getResources(), R.drawable.ic_brush_24dp, getContext().getTheme());
        fabIcon.setImageDrawable(drawableCompat);
        drawer.setFABIcon(fabIcon);

        final FloatingActionButton floatingActionButton = new FloatingActionButton.Builder(getActivity())
                .setContentView(fabIcon)
                .setTheme(FloatingActionButton.THEME_DARK)
                .build();

        setupFABOnlyOnCurrentView(floatingActionButton);
        return floatingActionButton;
    }

    private void setupFABOnlyOnCurrentView(final FloatingActionButton floatingActionButton) {
        final ViewGroup owner = (ViewGroup) floatingActionButton.getParent();
        owner.removeView(floatingActionButton);

        container.addView(floatingActionButton);
    }

    private void setupOnMenuClickListener() {

        for (int i = 0; i < actionImages.size(); i++) {
            setupOnMenuItemClickListener(actionImages.get(i), i);
        }
    }

    private FloatingActionMenu createFloatingActionMenu(final FloatingActionButton floatingActionButton, final int startAngle, final int endAngle) {

        final FloatingActionMenu.Builder menuBuilder = new FloatingActionMenu.Builder(getActivity());
        for (final SubActionButton subActionButton : checkColorAmountSettings()) {
            menuBuilder.addSubActionView(subActionButton);
        }

        return menuBuilder.setRadius(FLOATING_COLOR_MENU_RADIUS)
                .setStartAngle(startAngle)
                .setEndAngle(endAngle)
                .attachTo(floatingActionButton)
                .build();
    }

    private List<SubActionButton> getColors() {

        final List<SubActionButton> actionButtonList = new ArrayList<>();
        actionImages = new ArrayList<>();

        for (final FloatingColorMenuHelper floatingColorMenuHelper : FloatingColorMenuHelper.values()) {
            final SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());

            final ImageView imageView = floatingColorMenuHelper.createImageView(getContext());
            actionImages.add(imageView);

            final SubActionButton menuItem = itemBuilder.setContentView(imageView).build();
            actionButtonList.add(menuItem);
        }
        return actionButtonList;
    }

    private List<SubActionButton> checkColorAmountSettings() {

        final List<SubActionButton> enableButtons = getColors();
        final List<Boolean> listOfEnableColors = new ArrayList<>();

        for (FloatingColorMenuHelper floatingColorMenuHelper : FloatingColorMenuHelper.values()) {
            listOfEnableColors.add(getSharedPreferences(floatingColorMenuHelper.getColorKey()));
        }

        for (int i = listOfEnableColors.size() - 1; i >= 0; i--) {
            if (!listOfEnableColors.get(i)) {
                enableButtons.remove(i);
            }
        }
        return enableButtons;
    }

    private boolean getSharedPreferences(String color){
        return sharedPreferences.getBoolean(color, true);
    }

    private void setupOnMenuItemClickListener(final ImageView menuItem, final int colorByOrdinal) {
        menuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.setStrokeColorIndicator(colorByOrdinal);
                drawer.setBrushColor(FloatingColorMenuHelper.values()[colorByOrdinal].getColor());
                closeMenu(null);
            }
        });
    }

    private void setupOnMenuClickAnimation(final FloatingActionButton rightLowerButton) {

        actionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                animateMenuTouch(rightLowerButton, MENU_INITIAL_ROTATION, ON_MENU_OPEN_ROTATION);
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                animateMenuTouch(rightLowerButton, ON_MENU_OPEN_ROTATION, MENU_INITIAL_ROTATION);
            }
        });
    }

    private void animateMenuTouch(final FloatingActionButton fAB, final int initialRotation, final int endRotation) {
        fAB.setRotation(initialRotation);
        final PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat(View.ROTATION, endRotation);
        final ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fAB, valuesHolder);
        animation.start();
    }

    private void setOnFABLongClickListener(final FloatingActionButton floatingActionButton) {
        floatingActionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        v);
                v.startDrag(data, shadowBuilder, v, 0);
                floatingActionButton.setVisibility(View.INVISIBLE);
                return true;
            }
        });
    }

    private void setMenuOrientation(final int xCord, final int yCord, final int viewWidth, final int viewHeight) {

        int startAngle = STRAIGHT_ANGLE;
        int endAngle = THREE_QUARTERS_ANGLE;
        int halfOfScreenHeight = viewHeight / 2;
        int halfOfScreenWidth = viewWidth / 2;

        if (xCord < halfOfScreenWidth && yCord < halfOfScreenHeight) {
            startAngle = ZERO_DEGREE_ANGLE;
            endAngle = RIGHT_ANGLE;
        } else if (xCord > halfOfScreenWidth && yCord < halfOfScreenHeight) {
            startAngle = RIGHT_ANGLE;
            endAngle = STRAIGHT_ANGLE;
        } else if (xCord > halfOfScreenWidth && yCord > halfOfScreenHeight) {
            startAngle = STRAIGHT_ANGLE;
            endAngle = THREE_QUARTERS_ANGLE;
        } else if (xCord < halfOfScreenWidth && yCord > halfOfScreenHeight) {
            startAngle = THREE_QUARTERS_ANGLE;
            endAngle = FULL_ANGLE;
        }
        createFloatingColorChangeMenu(startAngle, endAngle);
    }

    @Subscribe
    public void closeMenu(@Nullable CloseMenuEvent event) {
        actionMenu.close(true);
    }

    @Subscribe
    public void redrawCanvas(final OnCreatePictureEvent listOfPoints) {
        if (getView() == null) {
            return;
        }
        drawer = (DrawerOnScreen) getView().findViewById(R.id.canvas);
        drawer.redrawCanvas(listOfPoints);
    }
}