package com.mindbowser.locotask;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mindbowser.locotask.util.Utility;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    @Test
    public void useAppContext() {
        //Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.mindbowser.locotask", appContext.getPackageName());
    }

    @Test
    public void getVideoDimensionsWidth_isCorrectUnitTest() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MediaPlayer player = MediaPlayer.create(appContext, R.raw.videoplayback);
        assertEquals(179, Utility.getVideoDimensions(player, 320, 320)[0]);
    }

    @Test
    public void getVideoDimensionsHeight_isCorrectUnitTest() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MediaPlayer player = MediaPlayer.create(appContext, R.raw.videoplayback);
        assertEquals(320, Utility.getVideoDimensions(player, 320, 320)[1]);
    }

}
