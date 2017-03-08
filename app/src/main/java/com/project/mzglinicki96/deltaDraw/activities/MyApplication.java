package com.project.mzglinicki96.deltaDraw.activities;

import android.app.Application;
import android.util.Log;

import com.project.mzglinicki96.deltaDraw.BuildConfig;
import com.project.mzglinicki96.deltaDraw.dagger2.DaggerStorageComponent;
import com.project.mzglinicki96.deltaDraw.dagger2.DatabaseModule;
import com.project.mzglinicki96.deltaDraw.dagger2.StorageComponent;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Created by mzglinicki.96 on 21.07.2016.
 */
public class MyApplication extends Application {

    private StorageComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerStorageComponent
                .builder()
                .databaseModule(new DatabaseModule(this))
                .build();
    }

    public StorageComponent getComponent() {
        return component;
    }

    //DEBUG
    private static final Stack<Long> timerStack = new Stack<>();

    public static void startTimer() {
        if (!BuildConfig.DEBUG) {
            return;
        }
        timerStack.push(System.currentTimeMillis());
    }

    public static void stopTimer(final String tag) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        try {
            long elapsedTime = System.currentTimeMillis() - timerStack.pop();
            Log.d(tag, elapsedTime + " ms");
        } catch (EmptyStackException e) {
            Log.d("Timer", "Timer Stack is Empty!");
            e.printStackTrace();
        }
    }
    //END DEBUG
}