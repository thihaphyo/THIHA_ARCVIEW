package com.pth.ptharcviewlib.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.pth.ptharcviewlib.R;

/**
 * Created by Phyo Thiha on 7/6/18.
 */
public class Config {

    public static final int CROP_INSIDE = 0;
    public static final int CROP_OUTSIDE = 1;
    public static final int BOTTOM = 0;
    public static final int TOP = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    private boolean isInside = true;
    private float height;
    private float elevation;
    private int position;

    public Config(Context context, AttributeSet attributeSet) {

        TypedArray styleAttributes = context.obtainStyledAttributes(attributeSet
                , R.styleable.ArcView
                , 0, 0);

        height = styleAttributes
                .getDimension(R.styleable.ArcView_height
                        , convertDpToPx(context, 10));

        final int direction = styleAttributes
                .getInt(R.styleable.ArcView_direction, CROP_INSIDE);

        isInside = direction == CROP_INSIDE;

        position = styleAttributes
                .getInt(R.styleable.ArcView_position, BOTTOM);

        styleAttributes.recycle();


    }

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public boolean isCropInside() {
        return isInside;
    }

    public float getHeight() {
        return height;
    }

    public int getPosition() {
        return position;
    }

    private static float convertDpToPx(Context context, int dp) {
        Resources resources = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                , dp, resources.getDisplayMetrics());
    }
}
