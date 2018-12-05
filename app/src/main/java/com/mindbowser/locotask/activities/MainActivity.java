package com.mindbowser.locotask.activities;

import android.animation.Animator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;

import com.mindbowser.locotask.R;
import com.mindbowser.locotask.util.Utility;
import com.mindbowser.locotask.views.VideoCardView;
import com.mindbowser.locotask.views.VideoSurfaceView;

import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, VideoCardView.OnLayoutListener, SurfaceHolder.Callback {
    private final static String TAG = "MainActivity";

    @BindView(R.id.activity_main_video_surface_view)
    protected VideoSurfaceView videoView;

    @BindView(R.id.activity_main_video_card_view)
    protected VideoCardView cardView;

    @BindView(R.id.activity_main_relative_layout)
    protected RelativeLayout relativeLayout;

    private Handler handler;
    private boolean inCircleMode;

    private final static int CIRCULAR_INTERVAL = 5000;
    private final static int MINIMUM_CARD_HEIGHT = 300;
    private final static int MAXIMUM_CARD_HEIGHT = 500;

    //Parameters for video view.
    private int cropCenterX;
    private int cropCenterY;
    private int cropRadius;
    private int croppedLayoutWidth;
    private int croppedLayoutHeight;
    private int fullLayoutWidth;
    private int fullLayoutHeight;

    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initParameters();
    }

    /**
     * Initialise the parameters used.
     */
    private void initParameters() {
        handler = new Handler();
        cardView.setVisibility(View.INVISIBLE);
        cardView.setOnLayoutListener(this);
        SurfaceHolder holder = videoView.getHolder();
        holder.addCallback(this);

        player = MediaPlayer.create(this, R.raw.bird_s);
        player.setOnCompletionListener(this);

        //Setting the video with proper aspect ratio.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int dimenFull[] = Utility.getVideoDimensions(player, width, height);
        fullLayoutWidth = dimenFull[0];
        fullLayoutHeight = dimenFull[1];

        setVideoLayout();
    }

    //Runnable for switching the views from circular video to full screen video.
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            inCircleMode = !inCircleMode;
            cardView.setVisibility(inCircleMode ? View.VISIBLE : View.INVISIBLE);

            animateView(relativeLayout);
            setVideoLayout();

            //Getting the random height value from specified range.
            int cardHeight = ThreadLocalRandom.current().nextInt(MINIMUM_CARD_HEIGHT, MAXIMUM_CARD_HEIGHT);
            int updatedHeight = Utility.getPixelFromDP(MainActivity.this, cardHeight);
            cardView.setCardHeight(updatedHeight);

            Log.i(TAG, "cardHeight: " + cardHeight);
            Log.i(TAG, "updatedHeight: " + updatedHeight);

            handler.postDelayed(runnable, CIRCULAR_INTERVAL);
        }
    };

    /**
     * Calculates the dimensions required for cropped video view.
     */
    private void calculateCroppedParams() {
        int dimen[] = Utility.getVideoDimensions(player, cardView.getVideoWidth(), cardView.getVideoWidth());
        croppedLayoutWidth = dimen[0];
        croppedLayoutHeight = dimen[1];

        cropRadius = croppedLayoutWidth / 2;
        cropCenterX = cropRadius;
        cropCenterY = cropRadius;

        Log.i(TAG, "croppedLayoutHeight: " + croppedLayoutHeight);
        Log.i(TAG, "croppedLayoutWidth: " + croppedLayoutWidth);
        Log.i(TAG, "cropRadius: " + cropRadius);
    }

    /**
     * Change the layout dimensions for video view.
     */
    private void setVideoLayout() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) videoView.getLayoutParams();

        //Changing the margin, width & height of videoView.
        layoutParams.setMargins(0, inCircleMode ? cardView.getVideoMargin() : 0, 0, 0);
        layoutParams.width = inCircleMode ? croppedLayoutWidth : fullLayoutWidth;
        layoutParams.height = inCircleMode ? croppedLayoutHeight : fullLayoutHeight;
        layoutParams.addRule(inCircleMode ? RelativeLayout.CENTER_HORIZONTAL : RelativeLayout.CENTER_IN_PARENT);

        videoView.cropCircle(cropCenterX, cropCenterY, cropRadius);
        videoView.setCircular(inCircleMode);
        videoView.setLayoutParams(layoutParams);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onLayout() {
        Log.i(TAG, "onLayout: starting runnable");
        calculateCroppedParams();
        player.start();
        handler.postDelayed(runnable, CIRCULAR_INTERVAL);
    }

    /**
     * Give circular ripple effects to view
     *
     * @param view
     */
    private void animateView(View view) {

        //Get the center for the clipping circle
        int cx = inCircleMode ? cardView.getMainWidth() / 2 : (int) (cardView.getCenterX() + (2 * cardView.getStroke()));
        int cy = inCircleMode ? cardView.getMainHeight() / 2 : (int) (cardView.getCenterY() + (2 * cardView.getStroke()));

        //Get the final radius for the clipping circle
        int rippleRadius = Math.max(view.getWidth(), view.getHeight()) / 2;

        //Create the animator for this view
        Animator animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, rippleRadius);
        animator.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        player.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
        if (player != null && player.isPlaying())
            handler.postDelayed(runnable, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null && player.isPlaying())
            player.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null && !player.isPlaying())
            player.start();
    }
}
