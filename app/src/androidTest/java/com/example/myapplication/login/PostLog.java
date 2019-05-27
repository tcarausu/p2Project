package com.example.myapplication.login;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PostLog {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.CAMERA",
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Test
    public void postLog() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(200);
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
                allOf(withId(R.id.ic_like_posts),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationBar),
                                        0),
                                3),
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

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.onMoreDotsImage),
                        childAtPosition(
                                allOf(withId(R.id.cardview_background),
                                        childAtPosition(
                                                withClassName(is("android.support.v7.widget.CardView")),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.alertButtonDelete), withText("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.alert_dialog_layout),
                                        1),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.imageView_gallery),
                        childAtPosition(
                                withParent(withId(R.id.container)),
                                2),
                        isDisplayed()));
        appCompatImageView2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.imageView_gallery),
                        childAtPosition(
                                withParent(withId(R.id.container)),
                                2),
                        isDisplayed()));
        appCompatImageView3.perform(click());

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                0)))
                .atPosition(1);
        appCompatTextView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageView4 = onView(
                allOf(withId(R.id.imageView_gallery),
                        childAtPosition(
                                withParent(withId(R.id.container)),
                                2),
                        isDisplayed()));
        appCompatImageView4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DataInteraction appCompatTextView2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                0)))
                .atPosition(1);
        appCompatTextView2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.textview_next), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                2),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.image_desc_edittext),
                        childAtPosition(
                                allOf(withId(R.id.relLayout2),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                1)));
        appCompatEditText.perform(scrollTo(), replaceText("g"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.image_ingredients_edittext),
                        childAtPosition(
                                allOf(withId(R.id.relLayout3),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                0)));
        appCompatEditText2.perform(scrollTo(), click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.image_ingredients_edittext),
                        childAtPosition(
                                allOf(withId(R.id.relLayout3),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                0)));
        appCompatEditText3.perform(scrollTo(), replaceText("h"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.image_recipe_edittext),
                        childAtPosition(
                                allOf(withId(R.id.relLayout4),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                3)),
                                0)));
        appCompatEditText4.perform(scrollTo(), replaceText("h"), closeSoftKeyboard());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.textview_share), withText("Upload"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                1),
                        isDisplayed()));
        appCompatTextView4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.ic_like_posts),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationBar),
                                        0),
                                3),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageView5 = onView(
                allOf(withId(R.id.onMoreDotsImage),
                        childAtPosition(
                                allOf(withId(R.id.cardview_background),
                                        childAtPosition(
                                                withClassName(is("android.support.v7.widget.CardView")),
                                                0)),
                                5),
                        isDisplayed()));
        appCompatImageView5.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.alertButtonDelete), withText("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.alert_dialog_layout),
                                        1),
                                1),
                        isDisplayed()));
        appCompatButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageView6 = onView(
                allOf(withId(R.id.close_share),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageView6.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageView7 = onView(
                allOf(withId(R.id.close_share),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageView7.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageView8 = onView(
                allOf(withId(R.id.imageView_gallery),
                        childAtPosition(
                                withParent(withId(R.id.container)),
                                2),
                        isDisplayed()));
        appCompatImageView8.perform(click());

        DataInteraction appCompatTextView5 = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                0)))
                .atPosition(1);
        appCompatTextView5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView6 = onView(
                allOf(withId(R.id.textview_next), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                2),
                        isDisplayed()));
        appCompatTextView6.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.image_desc_edittext),
                        childAtPosition(
                                allOf(withId(R.id.relLayout2),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                1)));
        appCompatEditText5.perform(scrollTo(), replaceText("g"), closeSoftKeyboard());

        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(R.id.textview_share), withText("Upload"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.profileToolBar),
                                        0),
                                1),
                        isDisplayed()));
        appCompatTextView7.perform(click());

        DataInteraction appCompatTextView8 = onData(anything())
                .inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(
                                withId(R.id.contentPanel),
                                0)))
                .atPosition(0);
        appCompatTextView8.perform(click());
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
