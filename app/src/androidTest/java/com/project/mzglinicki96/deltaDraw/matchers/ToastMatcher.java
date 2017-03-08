package com.project.mzglinicki96.deltaDraw.matchers;

import android.os.IBinder;
import android.support.test.espresso.Root;
import android.view.WindowManager;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * Created by mazg on 08.03.2017.
 */

public class ToastMatcher extends TypeSafeDiagnosingMatcher<Root> {

    @Override
    protected boolean matchesSafely(final Root root, final Description mismatchDescription) {
        int type = root.getWindowLayoutParams().get().type;

        if ((type != WindowManager.LayoutParams.TYPE_TOAST)) {
            return false;
        }
        final IBinder windowToken = root.getDecorView().getWindowToken();
        final IBinder appToken = root.getDecorView().getApplicationWindowToken();
        if (windowToken == appToken) {
            return true;
        }
        return false;
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("is toast");
    }
}
