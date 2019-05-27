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
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.sign_up), withText("No account? Register here for FREE"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        0),
                                3),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.SignUpFrgmnt_email_field),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("t"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.pass_field),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("12332"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.confirm_pass),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("12332"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.confirm_pass), withText("12332"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                4),
                        isDisplayed()));
        appCompatEditText4.perform(pressImeActionButton());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.SignUpFragmnt_sign_upButton), withText("Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                5),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.SignUpFrgmnt_email_field), withText("t"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText(""));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.SignUpFrgmnt_email_field),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.SignUpFrgmnt_email_field),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText7.perform(longClick());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.SignUpFrgmnt_email_field),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("tkaraushu@ymail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.SignUpFrgmnt_email_field), withText("tkaraushu@ymail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText9.perform(pressImeActionButton());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.pass_field), withText("12332"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText10.perform(pressImeActionButton());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.confirm_pass), withText("12332"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                4),
                        isDisplayed()));
        appCompatEditText11.perform(pressImeActionButton());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.SignUpFragmnt_sign_upButton), withText("Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                5),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.pass_field), withText("12332"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText12.perform(replaceText("123321"));

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.pass_field), withText("123321"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText13.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.confirm_pass), withText("12332"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                4),
                        isDisplayed()));
        appCompatEditText14.perform(replaceText("123321"));

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.confirm_pass), withText("123321"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                4),
                        isDisplayed()));
        appCompatEditText15.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText16 = onView(
                allOf(withId(R.id.confirm_pass), withText("123321"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                4),
                        isDisplayed()));
        appCompatEditText16.perform(pressImeActionButton());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.SignUpFragmnt_sign_upButton), withText("Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                5),
                        isDisplayed()));
        appCompatButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText17 = onView(
                allOf(withId(R.id.email_id_logIn),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText17.perform(longClick());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ViewInteraction appCompatEditText18 = onView(
                allOf(withId(R.id.email_id_logIn),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText18.perform(replaceText("tkaraushu@ymail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText19 = onView(
                allOf(withId(R.id.password_id_logIn),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText19.perform(replaceText("123321"), closeSoftKeyboard());

    //This is done before the SignIn account is verified
        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.button_id_log_in), withText("Sign in"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatButton4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //This is done after the SignIn account is verified
        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.button_id_log_in), withText("Sign in"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatButton5.perform(click());
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
