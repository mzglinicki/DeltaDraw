package com.project.mzglinicki96.deltaDraw.activities;

import android.app.Application;

import com.project.mzglinicki96.deltaDraw.dagger2.DaggerStorageComponent;
import com.project.mzglinicki96.deltaDraw.dagger2.DatabaseModule;
import com.project.mzglinicki96.deltaDraw.dagger2.StorageComponent;

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
}