package com.project.mzglinicki96.deltaDraw;

import android.content.SharedPreferences;

import com.project.mzglinicki96.deltaDraw.activities.MyApplication;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindString;

import static com.project.mzglinicki96.deltaDraw.SettingsHelper.*;

/**
 * Created by mzglinicki.96 on 10.07.2016.
 */
public class SettingsManager {

    private SharedPreferences sharedPreferences;
    private List<SettingModel> settingModels;

    @Inject
    public SettingsManager(final SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public List<SettingModel> getListOfSettings() {
        return createListOfSettings();
    }

    private List<SettingModel> createListOfSettings() {
        boolean isMenuColorVisible = sharedPreferences.getBoolean(Constants.COLOR_MENU_VISIBILITY, true);

        settingModels = new ArrayList<>();
        settingModels.add(new SettingModel(DELETE_ALL.ordinal(), R.string.settings_delete_all, false, false));
        settingModels.add(new SettingModel(COLORS_AMOUNT.ordinal(), R.string.setting_select_color_title, false, false));
        settingModels.add(new SettingModel(COLOR_MENU_VISIBILITY.ordinal(), R.string.menu_color_visibility, true, isMenuColorVisible));
        settingModels.add(new SettingModel(DEFAULT_SETTINGS.ordinal(), R.string.default_settings, false, false));
        return settingModels;
    }

    public void toggleCheckBoxMarked(final SettingModel settingModel) {
        boolean mark = settingModels.get(settingModel.getSettingId()).toggleMark();
        sharedPreferences.edit()
                .putBoolean(Constants.COLOR_MENU_VISIBILITY, mark)
                .apply();
    }
}