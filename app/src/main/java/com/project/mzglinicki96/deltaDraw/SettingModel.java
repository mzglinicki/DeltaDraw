package com.project.mzglinicki96.deltaDraw;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mzglinicki.96 on 08.07.2016.
 */
public class SettingModel {

    private final Context context;
    private final String settingTitle;
    private final int settingId;
    private boolean checkBoxVisibility;
    private boolean mark;

    public SettingModel(final Context context, final int settingId, final String settingTitle, final boolean isCheckBoxVisible, final boolean mark) {
        this.context = context;
        this.settingId = settingId;
        this.settingTitle = settingTitle;
        this.checkBoxVisibility = isCheckBoxVisible;
        this.mark = mark;
    }

    public int getSettingId() {
        return settingId;
    }

    public String getSettingTitle() {
        return settingTitle;
    }

    public boolean isCheckBoxEnable() {
        return checkBoxVisibility;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean defaultValue){
        mark = defaultValue;
    }

    public boolean toggleMark() {

        mark = !mark;

        final SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.COLOR_MENU_VISIBILITY,mark);
        editor.apply();

        return mark;
    }
}