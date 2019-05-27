package com.example.myapplication.login;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.myapplication.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchActivity {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void searchActivity() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction ky = onView(
                allOf(withText("Sign In"),
                        childAtPosition(
                                allOf(withId(R.id.googleSignInButton),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        ky.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.ic_search),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationBar),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_bar_id),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("g"), closeSoftKeyboard());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.search_button_id),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.search_bar_id), withText("g"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("t"));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.search_bar_id), withText("t"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(closeSoftKeyboard());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.search_button_id),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.search_bar_id), withText("t"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("to"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.search_bar_id), withText("to"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(closeSoftKeyboard());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.search_button_id),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.search_bar_id), withText("to"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("toa"));

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.search_bar_id), withText("toa"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText7.perform(closeSoftKeyboard());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.search_button_id),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction circleImageView = onView(
                allOf(withId(R.id.user_profile_Image_item_id),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_recycler_view_id),
                                        0),
                                0),
                        isDisplayed()));
        circleImageView.perform(click());

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                0)))
                .atPosition(0);
        appCompatTextView.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
