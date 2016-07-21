package com.project.mzglinicki96.deltaDraw.activities;

import android.app.Application;

import com.project.mzglinicki96.deltaDraw.dagger2.DaggerDatabaseComponent;
import com.project.mzglinicki96.deltaDraw.dagger2.DatabaseComponent;
import com.project.mzglinicki96.deltaDraw.dagger2.DatabaseModule;

/**
 * Created by mzglinicki.96 on 21.07.2016.
 */
public class MyApplication extends Application {

    DatabaseComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerDatabaseComponent
                .builder()
                .databaseModule(new DatabaseModule(this))
                .build();
    }

    public DatabaseComponent getComponent() {
        return component;
    }
}