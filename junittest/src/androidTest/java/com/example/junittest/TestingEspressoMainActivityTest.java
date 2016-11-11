package com.example.junittest;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by chenmin on 2016/11/11.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestingEspressoMainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new
            ActivityTestRule<>(MainActivity.class);
    @Test
    public void testHelloWorldIsShown() {
        onView(withText("Hello world!")).check
                (matches(isDisplayed()));
    }
}
