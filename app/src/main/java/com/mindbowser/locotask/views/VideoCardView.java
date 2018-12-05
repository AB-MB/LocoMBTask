package com.mindbowser.locotask.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.mindbowser.locotask.R;


/**
 * Custom view, which masks the smaller video in circular. Has a card attached which can change it's length.
 */
public class VideoCardView extends View {

    private final static String TAG = "VideoCardView";

    private int cardTopMargin = 0;
    private int cardWidth = 0;
    private int cardHeight = 0;
    private int cardRadiusInner = 0;
    private int cardRadiusOuter = 0;
    private int stroke = 0;
    private int transparentHeight = 0;
    private float centerX = 0;
    private float centerY = 0;
    private int mainWidth = 0;
    private int mainHeight = 0;

    //Flag for checking whether view is drawn or not.
    private boolean isDrawn = false;

    private int videoWidth = 0;
    private int videoHeight = 0;
    private int videoMargin = 0;

    private OnLayoutListener layoutListener;

    public VideoCardView(Context context) {
        super(context);
    }

    public VideoCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public VideoCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public VideoCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VideoCardView);
        cardTopMargin = a.getInt(R.styleable.VideoCardView_cardTopMargin, cardTopMargin);
        cardWidth = a.getInt(R.styleable.VideoCardView_cardWidth, cardWidth);
        cardHeight = a.getInt(R.styleable.VideoCardView_cardHeight, cardHeight);
        cardRadiusInner = a.getInt(R.styleable.VideoCardView_cardRadiusInner, cardRadiusInner);
        cardRadiusOuter = a.getInt(R.styleable.VideoCardView_cardRadiusOuter, cardRadiusOuter);
        a.recycle();
    }

    /**
     * Calculates required parameters for VideoCardView creation
     */
    private void defaultAttributes() {
        mainWidth = getWidth();
        mainHeight = getHeight();
        cardTopMargin = mainHeight / 10;
        cardWidth = mainWidth - (mainWidth / 5);
        cardHeight = mainHeight / 2;
        cardRadiusInner = cardWidth / 6;
        cardRadiusOuter = cardRadiusInner + (cardRadiusInner / 10);
        stroke = (cardRadiusInner / 3);
        transparentHeight = cardRadiusOuter;
        centerX = cardWidth / 2;
        centerY = transparentHeight + (cardRadiusOuter / 6);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.v(TAG, "onDraw : getWidth: " + getWidth() + ", getHeight: " + getHeight());
        if (!isDrawn)
            defaultAttributes();
        isDrawn = true;
        Bitmap bitmap = bitmapDraw();
        if (bitmap != null)
            canvas.drawBitmap(bitmap, getWidth() / 2 - cardWidth / 2, cardTopMargin, null);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout");
        defaultAttributes();

        if (this.layoutListener != null && !isDrawn)
            this.layoutListener.onLayout();

        isDrawn = true;
    }

    /**
     * Creates a bitmap with transparent circle & a card with dynamic height.
     *
     * @return
     */
    private Bitmap bitmapDraw() {
        Bitmap bitmap = Bitmap.createBitmap(cardWidth, cardHeight, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);
        //Fill it with color
        Canvas canvasBitmap = new Canvas(bitmap);
        canvasBitmap.drawColor(getResources().getColor(R.color.colorPrimary));
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(0xFFFFFFFF);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //Erase color
        canvasBitmap.drawCircle(centerX, centerY, cardRadiusInner, paint);

        //Draws a transparent rectangle
        RectF outerRectangle = new RectF(0, 0, cardWidth, transparentHeight);
        canvasBitmap.drawRect(outerRectangle, paint);

        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvasBitmap.drawCircle(centerX, centerY, cardRadiusOuter, paint);
        return bitmap;
    }

    public int getCardTopMargin() {
        return cardTopMargin;
    }

    public void setCardTopMargin(int cardTopMargin) {
        this.cardTopMargin = cardTopMargin;
    }

    public int getCardWidth() {
        return cardWidth;
    }

    public void setCardWidth(int cardWidth) {
        this.cardWidth = cardWidth;
    }

    public int getCardHeight() {
        return cardHeight;
    }

    public void setCardHeight(int cardHeight) {
        this.cardHeight = cardHeight;
        invalidate();
    }

    public int getCardRadiusInner() {
        return cardRadiusInner;
    }

    public void setCardRadiusInner(int cardRadiusInner) {
        this.cardRadiusInner = cardRadiusInner;
    }

    public int getCardRadiusOuter() {
        return cardRadiusOuter;
    }

    public void setCardRadiusOuter(int cardRadiusOuter) {
        this.cardRadiusOuter = cardRadiusOuter;
    }

    public int getStroke() {
        return stroke;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    public int getTransparentHeight() {
        return transparentHeight;
    }

    public void setTransparentHeight(int transparentHeight) {
        this.transparentHeight = transparentHeight;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public int getMainWidth() {
        return mainWidth;
    }

    public void setMainWidth(int mainWidth) {
        this.mainWidth = mainWidth;
    }

    public int getMainHeight() {
        return mainHeight;
    }

    public void setMainHeight(int mainHeight) {
        this.mainHeight = mainHeight;
    }

    public int getVideoWidth() {
        videoWidth = getCardWidth() / 2 + getStroke();
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        videoHeight = getCardWidth();
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getVideoMargin() {
        videoMargin = getCardTopMargin() + getStroke();
        return videoMargin;
    }

    public void setVideoMargin(int videoMargin) {
        this.videoMargin = videoMargin;
    }

    public boolean isDrawn() {
        return isDrawn;
    }

    public void setDrawn(boolean drawn) {
        isDrawn = drawn;
    }

    public void setOnLayoutListener(OnLayoutListener layoutListener) {
        this.layoutListener = layoutListener;
    }

    /**
     * Listener for notifying view layout is done.
     */
    public interface OnLayoutListener {
        void onLayout();
    }
}
