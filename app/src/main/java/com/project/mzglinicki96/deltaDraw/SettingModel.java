package com.project.mzglinicki96.deltaDraw;

/**
 * Created by mzglinicki.96 on 08.07.2016.
 */
public class SettingModel {

    private final int settingTitleResId;
    private final int settingId;
    private final boolean checkBoxVisibility;
    private boolean mark;

    public SettingModel(final int settingId, final int settingTitle, final boolean isCheckBoxVisible, final boolean mark) {
        this.settingId = settingId;
        this.settingTitleResId = settingTitle;
        this.checkBoxVisibility = isCheckBoxVisible;
        this.mark = mark;
    }

    public int getSettingId() {
        return settingId;
    }

    public int getSettingTitleResId() {
        return settingTitleResId;
    }

    public boolean isCheckBoxEnable() {
        return checkBoxVisibility;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMarked() {
        mark = true;
    }

    public boolean toggleMark() {
        mark = !mark;
        return mark;
    }
}