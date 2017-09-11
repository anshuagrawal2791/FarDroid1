package com.computing.smartphone.fardroid1;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Timer;
import java.util.TimerTask;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class ExampleInstrumentedTest {
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.google.android.youtube";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String STRING_TO_BE_TYPED = "UiAutomator";
    private UiDevice mDevice;
    private String[] urls = new String[]{ "https://www.youtube.com/watch?v=ZmWBrN7QV6Y",
            "https://www.youtube.com/watch?v=evQsOFQju08",
            "https://www.youtube.com/watch?v=3pAnRKD4raY",
            "https://www.youtube.com/watch?v=Pn-1wEYol0Q",
            "https://www.youtube.com/watch?v=m0J-BwkQK4A"};
    int cur=0;
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        final Context context = InstrumentationRegistry.getTargetContext();

        assertEquals("com.computing.smartphone.fardroid1", context.getPackageName());
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());


        play(urls,context);


    }

    private void play(String urls[],Context context) throws RemoteException, UiObjectNotFoundException, InterruptedException {
        int cur=0;
        while(cur<urls.length) {
            mDevice.pressHome();
            mDevice.pressRecentApps();
            UiObject app = new UiObject(new UiSelector().resourceId("com.android.systemui:id/dismiss_task").descriptionContains("Dismiss YouTube"));
            if (app.waitForExists(2000))
                app.click();
            // Wait for launcher
            final String launcherPackage = mDevice.getLauncherPackageName();
            assertThat(launcherPackage, notNullValue());
            mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                    LAUNCH_TIMEOUT);

            // Launch the app
//        Context context = InstrumentationRegistry.getContext();
            final Intent intent = context.getPackageManager()
                    .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
            // Clear out any previous instances
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);

            // Wait for the app to appear
            mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                    LAUNCH_TIMEOUT);


            UiObject searchButton = new UiObject(new UiSelector()
                    .className("android.widget.ImageView")
                    .descriptionContains("Search"));
            searchButton.click();

            UiObject searchEdittext = new UiObject(new UiSelector().className("android.widget.EditText"));
//        searchEdittext.wa
            searchEdittext.clearTextField();
            searchEdittext.setText(urls[cur++]);
            mDevice.pressEnter();

//        Thread.sleep(5000);

            UiObject searchRes = new UiObject(new UiSelector().className("android.support.v7.widget.RecyclerView").instance(0).childSelector(new UiSelector().className("android.widget.ImageView")));
            searchRes.waitForExists(10000);
            playForTime(searchRes, 10);
//            cur++;
        }
        mDevice.pressHome();
        mDevice.pressRecentApps();
        UiObject app = new UiObject(new UiSelector().resourceId("com.android.systemui:id/dismiss_task").descriptionContains("Dismiss YouTube"));
        if (app != null)
            app.click();
        mDevice.pressHome();

    }

    private void playForTime(UiObject searchRes,long timeInSec) throws InterruptedException {
        try {
            Log.e("Unit Test","waiting for search results");

            searchRes.click();
            Log.e("Unit Test","Search results obtained");

            UiObject loadingBar = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/player_loading_view_thin"));
            UiObject triangles = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/fast_forward_rewind_triangles"));
            UiObject playPauseButton = new UiObject(new UiSelector().resourceId("com.google.android.youtube:id/player_control_play_pause_replay_button"));

            Log.e("Unit test","waiting for video to load...");
            playPauseButton.waitForExists(10000);
            Log.e("Unit Test","video has started to play");

            Thread.sleep(timeInSec*500);
            triangles.click();
            playPauseButton.click();
            Thread.sleep(3000);
            playPauseButton.click();
            Thread.sleep(timeInSec*500);


        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }
}
