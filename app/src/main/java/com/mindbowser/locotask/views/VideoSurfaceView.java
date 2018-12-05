package com.mindbowser.locotask.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

/**
 * SurfaceView for video player. Can crop the view in circular shape.
 */
public class VideoSurfaceView extends SurfaceView {

    private final static String TAG = "VideoSurfaceView";
    private Path clipPath;
    private boolean isCircular;

    public VideoSurfaceView(Context context) {
        super(context);
        init();
    }

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        clipPath = new Path();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (this.isCircular)
            canvas.clipPath(clipPath);
        super.dispatchDraw(canvas);
    }

    /**
     * Crops the view in circular shape
     * @param centerX
     * @param centerY
     * @param radius
     */
    public void cropCircle(float centerX, float centerY, int radius) {
        Log.i(TAG, "cropCircle: x=" + centerX + " ,y= " + centerY + ", radius=" + radius);
        clipPath.addCircle(centerX, centerY, radius, Path.Direction.CW);
    }

    /**
     * Sets the flag for cropping the view in circular shape
     * @param isCircular
     */
    public void setCircular(boolean isCircular) {
        this.isCircular = isCircular;
        invalidate();
    }
}