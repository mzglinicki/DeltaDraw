package com.project.mzglinicki96.deltaDraw.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.project.mzglinicki96.deltaDraw.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mzglinicki.96 on 08.07.2016.
 */
public class SettingsViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.settingTitleText)
    protected TextView settingTitle;
    @Bind(R.id.settingCheckbox)
    protected CheckBox checkBox;

    public SettingsViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getSettingTitle() {
        return settingTitle;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}