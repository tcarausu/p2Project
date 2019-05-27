package com.example.myapplication.login;


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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginForgetPasswordTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginForgetPasswordTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.textView_id_forgotPass_logIn), withText("Forgot your password ?"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction relativeLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.useThisFragmentID),
                                childAtPosition(
                                        withId(R.id.login_layout),
                                        0)),
                        1),
                        isDisplayed()));
        relativeLayout.perform(click());

        ViewInteraction relativeLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.useThisFragmentID),
                                childAtPosition(
                                        withId(R.id.login_layout),
                                        0)),
                        1),
                        isDisplayed()));
        relativeLayout2.perform(click());

        ViewInteraction relativeLayout3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.useThisFragmentID),
                                childAtPosition(
                                        withId(R.id.login_layout),
                                        0)),
                        1),
                        isDisplayed()));
        relativeLayout3.perform(click());

        ViewInteraction relativeLayout4 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.useThisFragmentID),
                                childAtPosition(
                                        withId(R.id.login_layout),
                                        0)),
                        1),
                        isDisplayed()));
        relativeLayout4.perform(click());

        ViewInteraction relativeLayout5 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.useThisFragmentID),
                                childAtPosition(
                                        withId(R.id.login_layout),
                                        0)),
                        1),
                        isDisplayed()));
        relativeLayout5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.ForgotPass_email_field),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("t@g.com"), closeSoftKeyboard());

        ViewInteraction relativeLayout6 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.useThisFragmentID),
                                childAtPosition(
                                        withId(R.id.login_layout),
                                        0)),
                        1),
                        isDisplayed()));
        relativeLayout6.perform(click());

        ViewInteraction relativeLayout7 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.useThisFragmentID),
                                childAtPosition(
                                        withId(R.id.login_layout),
                                        0)),
                        1),
                        isDisplayed()));
        relativeLayout7.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.ForgotPass_email_field), withText("t@g.com"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.Forgotpass_resetPass_button), withText("Send Mail"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());
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
