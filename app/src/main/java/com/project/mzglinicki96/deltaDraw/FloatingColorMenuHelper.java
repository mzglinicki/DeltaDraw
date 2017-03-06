package com.project.mzglinicki96.deltaDraw;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

/**
 * Created by mzglinicki.96 on 09.07.2016.
 */
public enum FloatingColorMenuHelper {

    BLACK(Constants.BLACK_COLOR_KEY, Constants.BLACK_COLOR_INDICATOR, Color.BLACK) {
        @Override
        public ImageView createImageView(Context context) {
            return createImageView(context, R.drawable.fab_menu_black);
        }
    },
    GREEN(Constants.GREEN_COLOR_KEY, Constants.GREEN_COLOR_INDICATOR, Color.GREEN) {
        @Override
        public ImageView createImageView(Context context) {
            return createImageView(context, R.drawable.fab_menu_green);
        }
    },
    BLUE(Constants.BLUE_COLOR_KEY, Constants.BLUE_COLOR_INDICATOR, Color.BLUE) {
        @Override
        public ImageView createImageView(Context context) {
            return createImageView(context, R.drawable.fab_menu_blue);
        }
    },
    RED(Constants.RED_COLOR_KEY, Constants.RED_COLOR_INDICATOR, Color.RED) {
        @Override
        public ImageView createImageView(Context context) {
            return createImageView(context, R.drawable.fab_menu_red);
        }
    },
    ORANGE(Constants.ORANGE_COLOR_KEY, Constants.ORANGE_COLOR_INDICATOR, Color.argb(255, 255, 165, 0)) {
        @Override
        public ImageView createImageView(Context context) {
            return createImageView(context, R.drawable.fab_menu_orange);
        }
    },
    YELLOW(Constants.YELLOW_COLOR_KEY, Constants.YELLOW_COLOR_INDICATOR, Color.YELLOW) {
        @Override
        public ImageView createImageView(Context context) {
            return createImageView(context, R.drawable.fab_menu_yellow);
        }
    };

    public abstract ImageView createImageView(final Context context);

    private final String colorKey;
    private final int colorIndicator;
    private final int color;

    FloatingColorMenuHelper(final String colorKey, final int colorIndicator, final int color) {
        this.colorKey = colorKey;
        this.colorIndicator = colorIndicator;
        this.color = color;
    }

    public int getColorIndicator() {
        return colorIndicator;
    }

    public String getColorKey() {
        return colorKey;
    }

    public int getColor() {
        return color;
    }

    public ImageView createImageView(final Context context, final int drawableId) {
        final ImageView imageView = new ImageView(context);
        imageView.setImageResource(drawableId);
        return imageView;
    }
}