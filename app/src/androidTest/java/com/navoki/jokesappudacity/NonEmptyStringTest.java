package com.navoki.jokesappudacity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.navoki.jokesappudacity.activities.MainActivity;
import com.navoki.jokesappudacity.interfaces.OnAsynResponse;
import com.navoki.jokesappudacity.utils.JokesAsyncTask;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class NonEmptyStringTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    // Opening VideoPage with some dummy data
    @Before
    public void intentWithStubbedNoteId() {

        Intent startIntent = new Intent();
        mActivityTestRule.launchActivity(startIntent);

    }

    @Test
    public void checkResponse() {
        try {
            Thread.sleep(5000);

            JokesAsyncTask asyncTask = new JokesAsyncTask(new OnAsynResponse() {
                @Override
                public void onResponse(String response) {
                    assertNotNull(response);
                }
            });
            asyncTask.execute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
