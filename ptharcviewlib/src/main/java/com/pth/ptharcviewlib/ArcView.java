package com.pth.ptharcviewlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import com.pth.ptharcviewlib.util.Config;

/**
 * Created by Phyo Thiha on 7/6/18.
 */
public class ArcView extends FrameLayout {

    private Config config;

    private int height = 0;

    private int width = 0;

    private Path clipPath;

    public ArcView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ArcView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attributeSet) {
        config = new Config(context, attributeSet);
        config.setElevation(ViewCompat.getElevation(this));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private Path createArc() {
        final Path path = new Path();
        float arcHeight = config.getHeight();

        switch (config.getPosition()) {

            case Config.BOTTOM: {

                if (config.isCropInside()) {
                    path.moveTo(0, 0);
                    path.lineTo(0, height);
                    path.quadTo(width / 2, height - 2 * arcHeight, width, height);
                    path.lineTo(width, 0);
                    path.close();
                } else {
                    path.moveTo(0, 0);
                    path.lineTo(0, height - arcHeight);
                    path.quadTo(width / 2, height + arcHeight, width, height - arcHeight);
                    path.lineTo(width, 0);
                    path.close();
                }

                break;

            }
            case Config.TOP: {

                if (config.isCropInside()) {
                    path.moveTo(0, height);
                    path.lineTo(0, 0);
                    path.quadTo(width / 2, 2 * arcHeight, width, 0);
                    path.lineTo(width, height);
                    path.close();
                } else {
                    path.moveTo(0, arcHeight);
                    path.quadTo(width / 2, -arcHeight, width, arcHeight);
                    path.lineTo(width, height);
                    path.lineTo(0, height);
                    path.close();
                }

                break;
            }
            case Config.LEFT: {

                if (config.isCropInside()) {
                    path.moveTo(width, 0);
                    path.lineTo(0, 0);
                    path.quadTo(arcHeight * 2, height / 2, 0, height);
                    path.lineTo(width, height);
                    path.close();
                } else {
                    path.moveTo(width, 0);
                    path.lineTo(arcHeight, 0);
                    path.quadTo(-arcHeight, height / 2, arcHeight, height);
                    path.lineTo(width, height);
                    path.close();
                }

                break;

            }
            case Config.RIGHT: {

                if (config.isCropInside()) {
                    path.moveTo(0, 0);
                    path.lineTo(width, 0);
                    path.quadTo(width - arcHeight * 2, height / 2, width, height);
                    path.lineTo(0, height);
                    path.close();
                } else {
                    path.moveTo(0, 0);
                    path.lineTo(width - arcHeight, 0);
                    path.quadTo(width + arcHeight, height / 2, width - arcHeight, height);
                    path.lineTo(0, height);
                    path.close();
                }

                break;

            }
        }
        return path;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            calculateLayout();
        }
    }

    private void calculateLayout() {
        if (config == null) {
            return;
        }
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        if (width > 0 && height > 0) {

            clipPath = createArc();
            ViewCompat.setElevation(this, config.getElevation());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !config.isCropInside()) {
                ViewCompat.setElevation(this, config.getElevation());
                setOutlineProvider(new ViewOutlineProvider() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setConvexPath(clipPath);
                    }
                });
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        canvas.drawPath(clipPath, paint);
        canvas.restoreToCount(saveCount);
        paint.setXfermode(null);
    }
}
