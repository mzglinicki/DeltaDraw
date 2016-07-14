package com.project.mzglinicki96.deltaDraw.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.project.mzglinicki96.deltaDraw.R;

/**
 * Created by mzglinicki.96 on 08.07.2016.
 */
public class SettingsViewHolder extends RecyclerView.ViewHolder {

    private final TextView settingTitle;
    private final CheckBox checkBox;

    public SettingsViewHolder(final View itemView) {
        super(itemView);
        this.settingTitle = (TextView) itemView.findViewById(R.id.settingTitleText);
        this.checkBox = (CheckBox) itemView.findViewById(R.id.settingCheckbox);
    }

    public TextView getSettingTitle() {
        return settingTitle;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}