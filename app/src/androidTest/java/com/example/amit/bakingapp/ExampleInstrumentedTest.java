package com.example.amit.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
import android.support.test.rule.ActivityTestRule;
import android.support.test.InstrumentationRegistry;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private final int recipe_id = 2;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.amit.bakingapp", appContext.getPackageName());

        SystemClock.sleep(1000);

        // Check the initial quantity variable is zero
        onView(withId(R.id.rv_numbers)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_detail_rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.step_description)).check(matches(isDisplayed()));
        onView(withId(R.id.fragmentContainer)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()));
    }

    @Test
    public void RecipeDetail() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.amit.bakingapp", appContext.getPackageName());

        SystemClock.sleep(1000);

        // Check the initial quantity variable is zero
        onView(withId(R.id.rv_numbers)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_detail_rv_list)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        // verify fragments got created and displayed correctly
        onView(withId(R.id.step_description)).check(matches(isDisplayed()));
        onView(withId(R.id.fragmentContainer)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()));

        // mak sure image is disabled in recipe detail
        onView(withId(R.id.recipe_step_image_view)).check(matches(isDisplayed()));

    }

}
