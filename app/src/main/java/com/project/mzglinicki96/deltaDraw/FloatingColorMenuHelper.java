package com.project.mzglinicki96.deltaDraw;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

/**
 * Created by mzglinicki.96 on 09.07.2016.
 */
public enum FloatingColorMenuHelper {

    BLACK(Constants.BLACK_COLOR_KEY, Constants.BLACK_COLOR_INDICATOR, R.string.black) {
        @Override
        public int getColor() {
            return Color.BLACK;
        }

        @Override
        public ImageView createImageView(Context context) {
            return createImageView(context, R.drawable.fab_menu_black);
        }
    },
    GREEN(Constants.GREEN_COLOR_KEY, Constants.GREEN_COLOR_INDICATOR, R.string.green) {
        @Override
        public int getColor() {
            return Color.GREEN;
        }

        @Override
        public ImageView createImageView(Context context) {
            return createImageView(context, R.drawable.fab_menu_green);
        }
    },
    BLUE(Constants.BLUE_COLOR_KEY, Constants.BLUE_COLOR_INDICATOR, R.string.blue) {
        @Override
        public int getColor() {
            return Color.BLUE;
        }

        @Override
        public ImageView createImageView(Context context) {
            return createImageView(context, R.drawable.fab_menu_blue);
        }
    },
    RED(Constants.RED_COLOR_KEY, Constants.RED_COLOR_INDICATOR, R.string.red) {
        @Override
        public int getColor() {
            return Color.RED;
        }

        @Override
        public ImageView createImageView(Context context) {
            return createImageView(context, R.drawable.fab_menu_red);
        }
    },
    ORANGE(Constants.ORANGE_COLOR_KEY, Constants.ORANGE_COLOR_INDICATOR, R.string.orange) {
        @Override
        public int getColor() {
            return Color.MAGENTA;
        }

        @Override
        public ImageView createImageView(Context context) {
            return createImageView(context, R.drawable.fab_menu_orange);
        }
    },
    YELLOW(Constants.YELLOW_COLOR_KEY, Constants.YELLOW_COLOR_INDICATOR, R.string.yellow) {
        @Override
        public int getColor() {
            return Color.YELLOW;
        }

        @Override
        public ImageView createImageView(Context context) {
            return createImageView(context, R.drawable.fab_menu_yellow);
        }
    };

    public abstract int getColor();
    public abstract ImageView createImageView(final Context context);

    private final String colorKey;
    private final int colorIndicator;
    private final int colorId;

    FloatingColorMenuHelper(final String colorKey, final int colorIndicator, final int colorId) {
        this.colorKey = colorKey;
        this.colorIndicator = colorIndicator;
        this.colorId = colorId;
    }

    public int getColorId() {
        return colorId;
    }

    public int getColorIndicator() {
        return colorIndicator;
    }

    public String getColorKey() {
        return colorKey;
    }

    public ImageView createImageView(final Context context, final int drawableId) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(drawableId);
        return imageView;
    }
}