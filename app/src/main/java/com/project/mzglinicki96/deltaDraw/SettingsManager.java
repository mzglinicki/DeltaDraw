package com.project.mzglinicki96.deltaDraw;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mzglinicki.96 on 10.07.2016.
 */
public class SettingsManager {

    private static SettingsManager settingsManager = null;
    private final Context context;

    private SettingsManager(final Context context) {
        this.context = context;
    }

    public static SettingsManager getInstance(final Context context)  {

        if(settingsManager == null){
            settingsManager = new SettingsManager(context);
        }
        return settingsManager;
    }

    public List<SettingModel> getListOfSettings(){
        return createListOfSettings();
    }

    private List<SettingModel> createListOfSettings(){
        final SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        boolean isMenuColorVisible = sharedPreferences.getBoolean(Constants.COLOR_MENU_VISIBILITY, true);

        final List<SettingModel> settingModels = new ArrayList<>();
        settingModels.add(new SettingModel(context, SettingsHelper.DELETE_ALL.ordinal(), context.getString(R.string.settings_delete_all), false, false));
        settingModels.add(new SettingModel(context, SettingsHelper.COLORS_AMOUNT.ordinal(), context.getString(R.string.colors_amount), false, false));
        settingModels.add(new SettingModel(context, SettingsHelper.COLOR_MENU_VISIBILITY.ordinal(), context.getString(R.string.menu_color_visibility), true, isMenuColorVisible));
        settingModels.add(new SettingModel(context, SettingsHelper.DEFAULT_SETTINGS.ordinal(), context.getString(R.string.default_settings), false, false));
        return settingModels;
    }
}