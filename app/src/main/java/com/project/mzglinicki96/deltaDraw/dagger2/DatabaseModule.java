package com.project.mzglinicki96.deltaDraw.dagger2;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.project.mzglinicki96.deltaDraw.activities.MyApplication;
import com.project.mzglinicki96.deltaDraw.database.DatabaseHelper;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * Created by mzglinicki.96 on 21.07.2016.
 */
@Module
public class DatabaseModule {

    private final MyApplication application;

    public DatabaseModule(MyApplication application) {
        this.application = application;
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Singleton
    @Provides
    DatabaseHelper databaseHelper(){
        return new DatabaseHelper(application.getApplicationContext());
    }
}