package com.evan.pro.recycler;

import android.content.Context;
import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        MainActivity contextO = new MainActivity();
        Context contextT = InstrumentationRegistry.getInstrumentation().getTargetContext();
        View view = View.inflate(contextO, R.layout.activity_main, null);
    }

}