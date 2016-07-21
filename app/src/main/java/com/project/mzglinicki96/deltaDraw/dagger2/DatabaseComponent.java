package com.project.mzglinicki96.deltaDraw.dagger2;

import com.project.mzglinicki96.deltaDraw.activities.DatabaseActivity;
import com.project.mzglinicki96.deltaDraw.activities.DrawCreatingActivity;
import com.project.mzglinicki96.deltaDraw.activities.MainSettingsActivity;
import com.project.mzglinicki96.deltaDraw.fragments.DrawerFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mzglinicki.96 on 21.07.2016.
 */
@Singleton
@Component(modules = DatabaseModule.class)
public interface DatabaseComponent {

    void inject(DatabaseActivity databaseActivity);

    void inject(DrawCreatingActivity drawCreatingActivity);

    void inject(MainSettingsActivity settingsActivity);

    void inject(DrawerFragment drawerFragment);
}