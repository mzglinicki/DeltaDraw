package com.project.mzglinicki96.deltaDraw;

import android.content.SharedPreferences;

import com.project.mzglinicki96.deltaDraw.activities.MyApplication;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindString;

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
        settingModels.add(new SettingModel(SettingsHelper.DELETE_ALL.ordinal(), getSettingTitle(R.string.settings_delete_all), false, false));
        settingModels.add(new SettingModel(SettingsHelper.COLORS_AMOUNT.ordinal(), getSettingTitle(R.string.setting_select_color_title), false, false));
        settingModels.add(new SettingModel(SettingsHelper.COLOR_MENU_VISIBILITY.ordinal(), getSettingTitle(R.string.menu_color_visibility), true, isMenuColorVisible));
        settingModels.add(new SettingModel(SettingsHelper.DEFAULT_SETTINGS.ordinal(), getSettingTitle(R.string.default_settings), false, false));
        return settingModels;
    }

    public void toggleCheckBoxMarked(SettingModel settingModel) {
        boolean mark = settingModels.get(settingModel.getSettingId()).toggleMark();
        sharedPreferences.edit().putBoolean(Constants.COLOR_MENU_VISIBILITY, mark).apply();
    }

    private String getSettingTitle(int stringId) {
        return MyApplication.getContext().getResources().getString(stringId);
    }
}